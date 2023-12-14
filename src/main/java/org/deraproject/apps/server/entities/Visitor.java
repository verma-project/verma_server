package org.deraproject.apps.server.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

@Table(name = "visitors")
@Entity(name = "Visitor")
@ToString
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Visitor implements Serializable {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(nullable = false)
    @NotEmpty
    private String firstName;

    @Column(nullable = false)
    @NotEmpty
    private String lastName;

    @Column(nullable = false, unique = true)
    @NotEmpty
    @Email
    private String emailAddr;

    // Null value allowed, only populate for take-home repairs.
    private String residentialAddress;

    /// Null value allowed, only populate for take-home repairs.
    private String phoneNumber;

    @OneToMany(mappedBy = "visitor",
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Ticket> tickets;

    @Column(nullable = false)
    private boolean banned;
}
