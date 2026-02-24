package reactivechallenge.pragma.input.dto;

import java.util.List;

public record SubscribeRequestDto(
        Long userId,
        List<Long> bootcampIds
) {}