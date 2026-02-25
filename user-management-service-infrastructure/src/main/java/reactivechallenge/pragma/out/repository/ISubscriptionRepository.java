package reactivechallenge.pragma.out.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactivechallenge.pragma.out.entity.UserBootcampEntity;
import reactor.core.publisher.Flux;

public interface ISubscriptionRepository extends R2dbcRepository<UserBootcampEntity, Long> {
    @Query("SELECT * FROM person_bootcamp WHERE person_id = :userId AND status_subscription = :statusSubscription")
    Flux<UserBootcampEntity> findAllByUserIdAndStatusSubscription(@Param("userId") Long userId
            , @Param("statusSubscription") Integer statusSubscription);
}
