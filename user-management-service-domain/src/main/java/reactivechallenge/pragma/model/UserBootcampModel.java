package reactivechallenge.pragma.model;

public record UserBootcampModel(
        Long userId,
        Long bootcampId,
        Integer statusSubscription
) {}
