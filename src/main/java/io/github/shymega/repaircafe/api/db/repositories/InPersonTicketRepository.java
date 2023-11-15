package io.github.shymega.repaircafe.api.db.repositories;

import io.github.shymega.repaircafe.api.models.InPersonTicket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InPersonTicketRepository extends JpaRepository<InPersonTicket, UUID> {
}
