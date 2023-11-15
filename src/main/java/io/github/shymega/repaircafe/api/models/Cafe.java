package io.github.shymega.repaircafe.api.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

@Table(name = "cafes")
@Entity
@ToString
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Cafe implements Serializable {
    @Id
    @Column(nullable = false)
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(nullable = false)
    private String cafeShortId;

    @Column(nullable = false)
    private String cafeName;

    @Column(nullable = false)
    private String cafeWebsite;

    @Column(nullable = false)
    private String cafeContactEmail;

    @Column(nullable = false)
    @OneToMany(mappedBy = "cafe_id")
    private Set<CafeEvent> cafeEvents;
}
