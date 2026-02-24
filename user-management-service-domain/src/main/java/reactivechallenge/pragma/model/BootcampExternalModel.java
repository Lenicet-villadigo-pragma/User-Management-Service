package reactivechallenge.pragma.model;

import java.time.Duration;
import java.time.LocalDateTime;

public record BootcampExternalModel(
        Long id,
        String name,
        String description,
        LocalDateTime startDate,
        Duration estimatedTime
) {}
