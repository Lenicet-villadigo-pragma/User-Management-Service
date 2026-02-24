package reactivechallenge.pragma.out.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("person")
public record UserEntity(
    @Id @Column("id") Long id,
    @Column("name") String name
) {}
