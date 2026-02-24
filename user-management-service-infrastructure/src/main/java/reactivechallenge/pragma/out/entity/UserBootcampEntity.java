package reactivechallenge.pragma.out.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("person_bootcamp")
public record UserBootcampEntity(
        @Id @Column("person_id") Long userId,
        @Column("bootcamp_id") Long bootcampId,
        @Column("status_subscription") Integer statusSubscription
) {
}
