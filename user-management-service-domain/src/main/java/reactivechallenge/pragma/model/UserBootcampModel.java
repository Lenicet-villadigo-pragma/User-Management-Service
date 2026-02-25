package reactivechallenge.pragma.model;

import java.time.LocalDateTime;

public record UserBootcampModel(
        Long id,
        Long userId,
        Long bootcampId,
        Integer statusSubscription,
        LocalDateTime subscribedOn,
        LocalDateTime unsubscribedOn
) {}
