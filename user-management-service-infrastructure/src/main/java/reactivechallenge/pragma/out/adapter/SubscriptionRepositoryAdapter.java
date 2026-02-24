package reactivechallenge.pragma.out.adapter;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactivechallenge.pragma.exception.DatabaseError;
import reactivechallenge.pragma.mapper.IGenericMapper;
import reactivechallenge.pragma.model.UserBootcampModel;
import reactivechallenge.pragma.out.entity.UserBootcampEntity;
import reactivechallenge.pragma.out.repository.ISubscriptionRepository;
import reactivechallenge.pragma.spi.ISubscriptionRepositoryPort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Component
public class SubscriptionRepositoryAdapter implements ISubscriptionRepositoryPort {

    private final ISubscriptionRepository subscriptionRepository;
    private final IGenericMapper<UserBootcampModel, UserBootcampEntity> userBootcampMapper;
    private static final Integer STATUS_SUBSCRIPTION = 1;

    @Override


    public Mono<UserBootcampModel> subscribeUserToBootcamps(UserBootcampModel userBootcampModel) {
        return subscriptionRepository.save(userBootcampMapper.fromModel(userBootcampModel))
                .map(userBootcampMapper::toModel)
                .onErrorMap(throwable -> new DatabaseError("Error subscribing user to bootcamps"+throwable.getMessage()));

    }

    @Override
    public Flux<Long> getSubscribedBootcampsByUserId(Long userId) {
        return subscriptionRepository.findAllByUserIdAndStatusSubscription(userId, STATUS_SUBSCRIPTION)
                .map(UserBootcampEntity::bootcampId)
                .onErrorMap(throwable -> new DatabaseError("Error retrieving subscribed bootcamps for user"+throwable.getMessage()));
    }
}
