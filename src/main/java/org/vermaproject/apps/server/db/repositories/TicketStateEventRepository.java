package org.vermaproject.apps.server.db.repositories;

import org.vermaproject.apps.server.db.entities.TicketStateEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface TicketStateEventRepository extends JpaRepository<TicketStateEvent, UUID> {
}
