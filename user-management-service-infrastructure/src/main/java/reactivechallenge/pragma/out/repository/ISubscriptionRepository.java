package reactivechallenge.pragma.out.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactivechallenge.pragma.out.entity.UserBootcampEntity;
import reactor.core.publisher.Flux;

public interface ISubscriptionRepository extends R2dbcRepository<UserBootcampEntity, Long> {
    Flux<UserBootcampEntity> findAllByUserIdAndStatusSubscription(Long userId, Integer statusSubscription);
}
