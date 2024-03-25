package org.vermaproject.apps.server.services;

import lombok.extern.slf4j.Slf4j;
import org.vermaproject.apps.server.db.entities.Repair;
import org.vermaproject.apps.server.db.entities.RepairEvent;
import org.vermaproject.apps.server.db.entities.Ticket;
import org.vermaproject.apps.server.db.entities.TicketEvent;
import org.vermaproject.apps.server.db.repositories.RepairEventRepository;
import org.vermaproject.apps.server.db.repositories.RepairRepository;
import org.vermaproject.apps.server.db.repositories.TicketEventRepository;
import org.vermaproject.apps.server.db.repositories.TicketRepository;
import org.vermaproject.apps.server.enums.RepairEventEnum;
import org.vermaproject.apps.server.enums.RepairTypeEnum;
import org.vermaproject.apps.server.enums.TicketEventEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RepairService {
    @Autowired
    private RepairRepository repairRepository;

    @Autowired
    private RepairEventRepository repairEventRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketEventRepository ticketEventRepository;

    public List<Repair> getRepairsByType(final RepairTypeEnum repairType) {
        return repairRepository.findRepairsByRepairType(repairType)
            .stream()
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    public void createAndAssociateEntities(final Set<Repair> newRepairEntities, final Ticket ticket) {
        newRepairEntities.forEach(newRepair -> {
            ticketEventRepository.save(createTicketEvent(ticket));

            repairEventRepository.save(createRepairEvent(newRepair));
        });
    }

    private TicketEvent createTicketEvent(final Ticket savedTicket) {
        TicketEvent newTicketEvent = new TicketEvent();
        newTicketEvent.setTicketEvent(TicketEventEnum.REGISTERED);
        newTicketEvent.setTicket(savedTicket);
        return newTicketEvent;
    }

    private RepairEvent createRepairEvent(final Repair savedRepair) {
        RepairEvent newRepairEvent = new RepairEvent();
        newRepairEvent.setRepairEvent(RepairEventEnum.REGISTERED);
        newRepairEvent.setRepair(savedRepair);
        return newRepairEvent;
    }
}
