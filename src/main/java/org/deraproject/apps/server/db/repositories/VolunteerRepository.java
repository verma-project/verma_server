package org.deraproject.apps.server.db.repositories;

import org.deraproject.apps.server.entities.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VolunteerRepository extends JpaRepository<Volunteer, UUID> {
}
