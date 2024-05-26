package org.vermaproject.apps.server.db.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.vermaproject.apps.server.utils.converters.StringTrimConverter;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Table(name = "visitors")
@Entity(name = "Visitor")
@ToString
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class Visitor extends BaseEntity implements Serializable {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(nullable = false)
    @NotEmpty
    @Convert(converter = StringTrimConverter.class)
    private String firstName;

    @Column(nullable = false)
    @NotEmpty
    @Convert(converter = StringTrimConverter.class)
    private String lastName;

    @Column(nullable = false, unique = true)
    @NotEmpty
    @Email
    @Convert(converter = StringTrimConverter.class)
    private String emailAddr;

    // Null value allowed, only populate for take-home repairs.
    @Convert(converter = StringTrimConverter.class)
    private String residentialAddress;

    /// Null value allowed, only populate for take-home repairs.
    @Convert(converter = StringTrimConverter.class)
    private String phoneNumber;

    @OneToMany(mappedBy = "visitor",
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Ticket> tickets;

    @Column(nullable = false)
    private boolean banned;

    public boolean isNotBanned() {
        return !isBanned();
    }
}
