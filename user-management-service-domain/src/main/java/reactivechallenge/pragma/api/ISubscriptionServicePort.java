package reactivechallenge.pragma.api;

import reactivechallenge.pragma.model.SubscriptionStatus;
import reactor.core.publisher.Flux;

import java.util.List;

public interface ISubscriptionServicePort {
        Flux<SubscriptionStatus> subscribeUserToBootcamps(Long userId, List<Long> bootcampIds);
}
