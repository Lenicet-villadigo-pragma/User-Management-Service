package reactivechallenge.pragma.out.adapter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactivechallenge.pragma.model.BootcampExternalModel;
import reactivechallenge.pragma.spi.IBootcampServicePort;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class BootcampServiceAdapter implements IBootcampServicePort {

    private final WebClient webClient;

    public BootcampServiceAdapter(WebClient.Builder webClientBuilder
            , @Value("${parameterized.services.skill.base-url}") String baseUrl
            , @Value("${parameterized.flag.webclient-builder-debug}") Boolean webClientBuilderDebug) {
        this.webClient = webClientBuilder.baseUrl(baseUrl)
                .codecs(configurer -> configurer.defaultCodecs().enableLoggingRequestDetails(webClientBuilderDebug))
                .build();
    }

    @Override
    public Flux<BootcampExternalModel> getBootcampsByIds(List<Long> bootcampIds) {
        String bootcampsIdsAsString = bootcampIds.stream().map(String::valueOf)
                .collect(Collectors.joining(", "));

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/getByIds")
                        .queryParam("bootcampsIds", bootcampsIdsAsString)
                        .build())
                .retrieve()
                .bodyToFlux(BootcampExternalModel.class)
                .onErrorResume(e -> {
                    log.error("Error al obtener la lista de los bootcamps con ids {}: {}. retornando flujo vacío"
                            , bootcampsIdsAsString, e.getMessage());

                    return  Flux.empty();
                });
    }
}
