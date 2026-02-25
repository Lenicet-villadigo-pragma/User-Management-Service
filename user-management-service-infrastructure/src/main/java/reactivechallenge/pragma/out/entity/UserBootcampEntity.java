package reactivechallenge.pragma.out.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("person_bootcamp")
@NoArgsConstructor
public class UserBootcampEntity implements Persistable<Long> {

    public UserBootcampEntity(Long userId, Long bootcampId, Integer statusSubscription) {
        this.userId = userId;
        this.bootcampId = bootcampId;
        this.statusSubscription = statusSubscription;
    }

    @Id
    @Column("person_id")
    @Getter
    @Setter
    Long userId;

    @Column("bootcamp_id")
    @Getter
    @Setter
    Long bootcampId;

    @Column("status_subscription")
    @Getter
    @Setter
    Integer statusSubscription;

    @Transient
    private boolean isNew = true;

    @Override
    public Long getId() {
        return userId;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    public UserBootcampEntity setAsNew() {
        this.isNew = true;
        return this;
    }
}
