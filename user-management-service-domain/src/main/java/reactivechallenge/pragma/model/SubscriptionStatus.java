package reactivechallenge.pragma.model;

public record SubscriptionStatus(
        Long bootcampId,
        String bootcampName,
        boolean subscribed,
        String message
) {}
