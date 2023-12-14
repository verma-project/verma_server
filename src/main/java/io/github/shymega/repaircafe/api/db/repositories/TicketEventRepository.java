package io.github.shymega.repaircafe.api.db.repositories;

import io.github.shymega.repaircafe.api.entities.TicketEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TicketEventRepository extends JpaRepository<TicketEvent, UUID> {
}
