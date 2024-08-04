package org.vermaproject.apps.server.db.entities;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Table(name = "cafes")
@Entity(name = "Cafe")
@ToString
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CafeBlueprint extends BaseEntity implements Serializable {
}
