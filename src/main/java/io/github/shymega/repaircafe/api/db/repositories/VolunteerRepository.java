package io.github.shymega.repaircafe.api.db.repositories;

import io.github.shymega.repaircafe.api.models.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VolunteerRepository extends JpaRepository<Volunteer, UUID> {
}
