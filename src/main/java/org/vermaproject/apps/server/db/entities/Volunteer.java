package org.vermaproject.apps.server.db.entities;

/* Backed by Postgres native */

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.vermaproject.apps.server.enums.SkillsEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.vermaproject.apps.server.enums.VolunteerTypeEnum;
import org.vermaproject.apps.server.utils.converters.StringTrimConverter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Table(name = "volunteers")
@Entity(name = "Volunteer")
@ToString
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class Volunteer implements Serializable {
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
    private String emailAddress;

    @Convert(converter = StringTrimConverter.class)
    private String initials;

    @PrePersist
    @JsonIgnore
    private void genInitials() {
        if (initials != null) return;

        this.initials = String.format("%s%s",
            this.firstName.charAt(0),
            this.lastName.charAt(0));
    }

    @Column(nullable = false)
    private boolean active;

    public boolean isNotActive() {
        return !isActive();
    }

    @ElementCollection(targetClass = SkillsEnum.class)
    @JoinTable(name = "Volunteer_Skills", joinColumns = @JoinColumn(name = "volunteer_id"))
    @Enumerated(EnumType.STRING)
    private List<SkillsEnum> skills;

    @ElementCollection(targetClass = VolunteerTypeEnum.class)
    @JoinTable(name = "Volunteer_Type", joinColumns = @JoinColumn(name = "volunteer_id"))
    @Enumerated(EnumType.STRING)
    private List<VolunteerTypeEnum> volunteerType;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Repair> repairs;
}
