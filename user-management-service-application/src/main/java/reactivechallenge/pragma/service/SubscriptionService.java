package reactivechallenge.pragma.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactivechallenge.pragma.api.ISubscriptionServicePort;
import reactivechallenge.pragma.exception.BusinessDomainException;
import reactivechallenge.pragma.model.BootcampExternalModel;
import reactivechallenge.pragma.model.SubscriptionStatus;
import reactivechallenge.pragma.model.UserBootcampModel;
import reactivechallenge.pragma.spi.IBootcampServicePort;
import reactivechallenge.pragma.spi.ISubscriptionRepositoryPort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;

@AllArgsConstructor
@Slf4j
public class SubscriptionService implements ISubscriptionServicePort {
    private final IBootcampServicePort bootcampService;
    private final ISubscriptionRepositoryPort subscriptionRepositoryPort;
    private static final Integer FLAG_SUBSCRIBED = 1;
    private static final Integer MAX_ACTIVE_BOOTCAMPS = 5;

    @Override
    public Flux<SubscriptionStatus> subscribeUserToBootcamps(Long userId, List<Long> bootcampIds) {
        if(bootcampIds==null || bootcampIds.isEmpty()){
            return Flux.just( new SubscriptionStatus(0L, "", false
                    , "No se han enviado ids de bootcamps para suscribirse"));
        }

        return indicateStatusForIncomingBootcamps(userId, bootcampIds)
                .flatMap(subscriptionStatus -> {
                    if(!subscriptionStatus.subscribed()){
                        return Mono.just(subscriptionStatus);
                    }
                    return saveSubscription(userId, subscriptionStatus);
                });
    }

    private Mono<SubscriptionStatus> saveSubscription(Long userId, SubscriptionStatus subscriptionStatus){
        return subscriptionRepositoryPort
                .subscribeUserToBootcamps(new UserBootcampModel(null,userId, subscriptionStatus.bootcampId()
                        , FLAG_SUBSCRIBED, LocalDateTime.now(), null))
                .thenReturn(subscriptionStatus)
                .doOnError(e -> log.error("Error al suscribir al bootcamp con id {} para el usuario con id {}. Error: {}"
                        , subscriptionStatus.bootcampId(), userId, e.getMessage()))
                .onErrorResume(e -> Mono.just(new SubscriptionStatus(subscriptionStatus.bootcampId()
                        , subscriptionStatus.bootcampName(), false
                        , "Error al suscribir al bootcamp")));
    }

    private Flux<SubscriptionStatus> indicateStatusForIncomingBootcamps(Long userId, List<Long> bootcampIds){
        return getBootcampModels(bootcampIds)
                .zipWith(getActivatedSubscribedBootcampsByUser(userId))
                .flatMapMany(tuple -> {

                    List<BootcampExternalModel> incomingBootcamps = new ArrayList<>(tuple.getT1());
                    List<BootcampExternalModel> previousBootcamps = tuple.getT2();
                    incomingBootcamps.removeAll(previousBootcamps);
                    List<BootcampExternalModel> incomingBootcampsPlusPrevious = Stream.concat(previousBootcamps.stream()
                            , incomingBootcamps.stream()).toList();

                    return verifyLimitToSubscribeBootcamps(tuple.getT1().size(), tuple.getT2().size())
                            .thenMany(
                                    Flux.fromIterable(incomingBootcamps)
                                            .flatMap(modelForCompare -> Flux
                                                    .fromIterable(incomingBootcampsPlusPrevious)
                                                    .filter(modelToCompare ->
                                                            !Objects.equals(modelForCompare.id(), modelToCompare.id()))
                                                    .map(modelToCompare ->
                                                           verifyDates(modelForCompare, modelToCompare))
                                            ).distinct()
                                            .mergeWith(getStatusForBootcampsAlreadySubscribed(previousBootcamps))
                                            .mergeWith(getStatusForBootcampsNotFound(bootcampIds, tuple.getT1())                                            )

                            );
                });
    }

    private Flux<SubscriptionStatus> getStatusForBootcampsNotFound(List<Long> bootcampIds,
                                                                   List<BootcampExternalModel> bootcampsFound){
        SequencedSet<Long> bootcampsIdsSequence = new LinkedHashSet<>(bootcampIds);
        bootcampsFound.stream().map(BootcampExternalModel::id).toList().forEach(bootcampsIdsSequence::remove);
        List<Long> bootcampIdsNotFound = new ArrayList<>(bootcampsIdsSequence);

        return Flux.fromIterable(
                bootcampIdsNotFound.stream().map(bootcampIdLeft ->
                        new SubscriptionStatus(bootcampIdLeft, "",
                                false,
                                "No se encontró un bootcamp con este id para suscribirse")
                ).toList()
        );
    }

    private Flux<SubscriptionStatus> getStatusForBootcampsAlreadySubscribed(List<BootcampExternalModel> bootcampsSubscribed){
        return Flux.fromIterable(
                bootcampsSubscribed.stream().map(bootcampExternalModel ->
                        new SubscriptionStatus(bootcampExternalModel.id(), bootcampExternalModel.name(), false,
                                "Ya estás suscrito a este bootcamp")
                ).toList()
        );
    }

    private Mono<List<BootcampExternalModel>> getBootcampModels(List<Long> bootcampIds) {
        if(bootcampIds==null || bootcampIds.isEmpty()){
            return Mono.just(List.of());
        }

        return bootcampService.getBootcampsByIds(bootcampIds)
                .collectSortedList(Comparator.comparing(BootcampExternalModel::startDate))
                .onErrorComplete();
    }

    private Mono<List<BootcampExternalModel>> getActivatedSubscribedBootcampsByUser(Long userId) {
        return subscriptionRepositoryPort.getSubscribedBootcampsByUserId(userId)
                .collectList()
                .flatMap(this::getBootcampModels);
    }

    private Mono<Void> verifyLimitToSubscribeBootcamps(int bootcampsToSubscribe, int bootcampsSubscribed){
        if (bootcampsToSubscribe + bootcampsSubscribed > MAX_ACTIVE_BOOTCAMPS) {
            return Mono.error(new BusinessDomainException(
                    String.format("No puede inscribirse en más de %d bootcamps al tiempo, actualmente hay %d activo(s)"
                            , MAX_ACTIVE_BOOTCAMPS, bootcampsSubscribed))
            );
        }

        return Mono.empty();
    }


    private SubscriptionStatus verifyDates(BootcampExternalModel model1, BootcampExternalModel model2) {
        DateTimeFormatter isoFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        SubscriptionStatus subscriptionStatus;
        LocalDateTime endDateModel1 = model1.startDate().plusHours(model1.estimatedTime().toHours());
        LocalDateTime endDateModel2 = model2.startDate().plusHours(model2.estimatedTime().toHours());

        if(model1.startDate().toLocalDate().isBefore(LocalDate.now())){
            subscriptionStatus =  new SubscriptionStatus(model1.id(), model1.name(), false
                    , String.format("No se puede subscribir,la fecha de inicio ya ha pasado %s"
                    , model1.startDate().format(isoFormat))
            );
        }else if(model1.startDate().toLocalDate().isEqual(model2.startDate().toLocalDate())) {
            subscriptionStatus = new SubscriptionStatus(model1.id(), model1.name(), false
                    , String.format("No se puede subscribir, empieza en la misma fecha que el bootcamp %s", model2.name())
            );
        }else if(model1.startDate().isBefore(endDateModel2)){
            subscriptionStatus = new SubscriptionStatus(model1.id(), model1.name(), false
                    , String.format("Necesitas terminar primero el bootcamp %s que termina el %s para poder suscribirte a este bootcamp que inicia el %s"
                    , model2.name(), endDateModel2.format(isoFormat), model1.startDate().format(isoFormat))
            );
        } else {
            subscriptionStatus = new SubscriptionStatus(model1.id(), model1.name(), true
                    , String.format("Sin conflictos con la fecha de inicio %s y fin %s"
                    , model1.startDate().format(isoFormat), endDateModel1.format(isoFormat))
            );
        }

        return subscriptionStatus;
    }

}
