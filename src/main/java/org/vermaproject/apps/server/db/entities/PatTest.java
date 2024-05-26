package org.vermaproject.apps.server.db.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Table(name = "pat_tests")
@Entity(name = "PatTest")
@ToString
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PatTest extends BaseEntity implements Serializable {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.NONE)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NonNull
    @NotEmpty
    private Repair repair;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private PatTestStateEvent patTestStateEvent;

    @NonNull
    @NotEmpty
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Volunteer testedBy;
}
