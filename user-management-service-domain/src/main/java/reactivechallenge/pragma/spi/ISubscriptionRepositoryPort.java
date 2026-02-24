package reactivechallenge.pragma.spi;

import reactivechallenge.pragma.model.UserBootcampModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ISubscriptionRepositoryPort {
    Mono<UserBootcampModel> subscribeUserToBootcamps(UserBootcampModel userBootcampModel);
    Flux<Long> getSubscribedBootcampsByUserId(Long userId);
}
