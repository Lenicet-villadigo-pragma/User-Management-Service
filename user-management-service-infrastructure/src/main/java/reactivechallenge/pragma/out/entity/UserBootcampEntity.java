package reactivechallenge.pragma.out.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("person_bootcamp")
public record UserBootcampEntity(

        @Id
        Long id,
        @Column("person_id")
        Long userId,
        Long bootcampId,
        Integer statusSubscription,
        LocalDateTime subscribedOn,
        LocalDateTime unsubscribedOn

) {}
