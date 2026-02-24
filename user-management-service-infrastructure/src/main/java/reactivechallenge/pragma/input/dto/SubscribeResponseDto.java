package reactivechallenge.pragma.input.dto;

import reactivechallenge.pragma.model.SubscriptionStatus;

public record SubscribeResponseDto (
        Long bootcampId,
        String bootcampName,
        boolean subscribed,
        String message
){
    public static SubscribeResponseDto fromModel(SubscriptionStatus model) {
        return new SubscribeResponseDto(model.bootcampId(), model.bootcampName(), model.subscribed(), model.message());
    }
}
