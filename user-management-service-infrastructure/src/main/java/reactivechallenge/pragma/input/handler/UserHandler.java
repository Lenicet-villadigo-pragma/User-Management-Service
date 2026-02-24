package reactivechallenge.pragma.input.handler;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactivechallenge.pragma.api.ISubscriptionServicePort;
import reactivechallenge.pragma.input.dto.SubscribeRequestDto;
import reactivechallenge.pragma.input.dto.SubscribeResponseDto;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class UserHandler {
    private final ISubscriptionServicePort subscriptionServicePort;

    public Mono<ServerResponse> subscribeToBootcamp(ServerRequest request) {
        return request.bodyToMono(SubscribeRequestDto.class)
                .map(subscribeRequestDto ->  subscriptionServicePort
                        .subscribeUserToBootcamps(subscribeRequestDto.userId(), subscribeRequestDto.bootcampIds())
                        .map(SubscribeResponseDto::fromModel)
                        .collectList()
                )
                .flatMap(subscriptionResponseDtoList -> ServerResponse.ok()
                        .bodyValue(subscriptionResponseDtoList));
    }
}
