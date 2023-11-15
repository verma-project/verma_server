package io.github.shymega.repaircafe.api.db.repositories;

import io.github.shymega.repaircafe.api.models.OnlineTicket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OnlineTicketRepository extends JpaRepository<OnlineTicket, UUID> {
}
