package org.vermaproject.apps.server.db.entities;

/* Backed by Postgres native */

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.vermaproject.apps.server.enums.SkillsEnum;
import org.vermaproject.apps.server.enums.VolunteerTypeEnum;
import org.vermaproject.apps.server.utils.converters.StringTrimConverter;

import java.io.Serializable;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Table(name = "volunteers")
@Entity(name = "Volunteer")
@ToString
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class Volunteer extends BaseEntity implements Serializable {
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

    @Convert(converter = StringTrimConverter.class)
    private String shortCode;
    @Column(nullable = false)
    private boolean active;
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

    @PrePersist
    @JsonIgnore
    private void genShortcode() {
        if (shortCode != null)
            return;
        Random rnd = new Random(); // Generate random integer for short code.

        this.shortCode = String.format("%s%s-%d",
            this.firstName.charAt(0),
            this.lastName.charAt(0),
            rnd.nextInt(1));
    }

    public boolean isNotActive() {
        return !isActive();
    }
}
