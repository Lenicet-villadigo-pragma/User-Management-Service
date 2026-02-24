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
import java.util.Comparator;
import java.util.List;
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
            return Flux.empty();
        }

        return indicateStatusForIncomingBootcamps(userId, bootcampIds)
                .flatMap(subscriptionStatus -> {
                    if(!subscriptionStatus.subscribed()){
                        return Mono.just(subscriptionStatus);
                    }

                    return subscriptionRepositoryPort
                        .subscribeUserToBootcamps(new UserBootcampModel(userId, subscriptionStatus.bootcampId()
                                , FLAG_SUBSCRIBED))
                        .thenReturn(subscriptionStatus)
                        .onErrorResume(e -> Mono.just(new SubscriptionStatus(subscriptionStatus.bootcampId()
                                , subscriptionStatus.bootcampName(), false
                                , String.format("Error al suscribir al bootcamp: %s", e.getMessage()))));
                });
    }

    private Flux<SubscriptionStatus> indicateStatusForIncomingBootcamps(Long userId, List<Long> bootcampIds){
        return getBootcampModels(bootcampIds)
                .zipWith(getActivatedSubscribedBootcampsByUser(userId))
                .flatMapMany(tuple -> {

                    List<BootcampExternalModel> incomingBootcamps = tuple.getT1();
                    List<BootcampExternalModel> incomingBootcampsPlusPrevious = Stream.concat(tuple.getT2().stream()
                            , incomingBootcamps.stream()).toList();

                    return verifyLimitToSubscribeBootcamps(tuple.getT1().size(), tuple.getT2().size())
                            .thenMany(
                                    Flux.fromIterable(incomingBootcamps)
                                            .flatMap(modelForCompare -> Flux
                                                    .fromIterable(incomingBootcampsPlusPrevious)
                                                    .map(modelToCompare ->
                                                            verifyDates(modelForCompare, modelToCompare))
                                            )
                            );
                });
    }

    private Mono<List<BootcampExternalModel>> getBootcampModels(List<Long> bootcampIds) {
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

        if(model1.startDate().toLocalDate().isBefore(LocalDate.now())){
            subscriptionStatus =  new SubscriptionStatus(model1.id(), model1.name(), false
                    , String.format("No se puede subscribir,la fecha de inicio ya ha pasado %s"
                    , model1.startDate().format(isoFormat))
            );
        }else if(model1.startDate().toLocalDate().isEqual(model2.startDate().toLocalDate())) {
            subscriptionStatus = new SubscriptionStatus(model1.id(), model1.name(), false
                    , String.format("No se puede subscribir, empieza en la misma fecha que el bootcamp %s", model2.name())
            );
        }else if(endDateModel1.isAfter(model2.startDate())){
            subscriptionStatus = new SubscriptionStatus(model1.id(), model1.name(), false
                    , String.format("No se puede subscribir, la fecha de finalización %s tiene conflicto con el bootcamp %s"
                    , endDateModel1.format(isoFormat), model2.name())
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
