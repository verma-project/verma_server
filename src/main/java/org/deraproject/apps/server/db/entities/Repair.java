package org.deraproject.apps.server.db.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.deraproject.apps.server.enums.RepairTypeEnum;
import org.deraproject.apps.server.utils.converters.RepairTypeConverter;
import org.deraproject.apps.server.utils.converters.StringListConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.deraproject.apps.server.utils.converters.StringTrimConverter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/* Backed by Postgres native */

@Table(name = "repairs")
@Entity(name = "Repair")
@ToString
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class Repair implements Serializable {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(nullable = false)
    @NotEmpty
    @Convert(converter = StringListConverter.class)
    private List<String> details;

    @Column(nullable = false)
    @NotEmpty
    @Convert(converter = StringTrimConverter.class)
    private String itemValue;

    @Column(nullable = false)
    @NotEmpty
    @Convert(converter = StringTrimConverter.class)
    private String itemAge;

    @Column(nullable = false)
    @Convert(converter = RepairTypeConverter.class)
    private RepairTypeEnum repairType;

    @Column(nullable = false)
    @NotEmpty
    @Convert(converter = StringListConverter.class)
    private String repairTypeCustom;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "repairs")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Volunteer> volunteers;

    @OneToMany(mappedBy = "repair", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<RepairEvent> repairEvents;

    @ManyToOne
    @JoinColumn(name = "ticket_id", insertable = false, updatable = false)
    private Ticket ticket;

    private boolean isCustomRepairType() {
        return repairType.equals(RepairTypeEnum.OTHER) && (repairTypeCustom != null && !repairTypeCustom.isEmpty());
    }
}
