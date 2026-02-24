package reactivechallenge.pragma.spi;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ISubscriptionRepositoryPort {
    Mono<Void> subscribeUserToBootcamps(Long userId, Long bootcampId);
    Flux<Long> getSubscribedBootcampsByUserId(Long userId);
}
