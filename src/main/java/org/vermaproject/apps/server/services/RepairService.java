package org.vermaproject.apps.server.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vermaproject.apps.server.db.entities.Repair;
import org.vermaproject.apps.server.db.entities.RepairStateEvent;
import org.vermaproject.apps.server.db.entities.Ticket;
import org.vermaproject.apps.server.db.entities.TicketStateEvent;
import org.vermaproject.apps.server.db.repositories.RepairRepository;
import org.vermaproject.apps.server.db.repositories.RepairStateEventRepository;
import org.vermaproject.apps.server.db.repositories.TicketRepository;
import org.vermaproject.apps.server.db.repositories.TicketStateEventRepository;
import org.vermaproject.apps.server.enums.RepairStateEventEnum;
import org.vermaproject.apps.server.enums.RepairTypeEnum;
import org.vermaproject.apps.server.enums.TicketStateEventEnum;

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
    private RepairStateEventRepository repairStateEventRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketStateEventRepository ticketStateEventRepository;

    public List<Repair> getRepairsByType(final RepairTypeEnum repairType) {
        return repairRepository.findAllRepairsByRepairType(repairType)
            .stream()
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    public Repair create(final Repair repair) {
        // UUID PK somewhat guaranteed a unique Repair.
        // TODO: Check ticket exists before creating repair?
        return repairRepository.save(repair);
    }

    public void initTicketRepairEvents(final Set<Repair> newRepairEntities, final Ticket ticket) {
        newRepairEntities.forEach(newRepair -> {
            ticketStateEventRepository.save(createTicketEvent(ticket, TicketStateEventEnum.REGISTERED));
            repairStateEventRepository.save(createRepairEvent(newRepair, RepairStateEventEnum.REGISTERED));
        });
    }

    private TicketStateEvent createTicketEvent(final Ticket savedTicket, final TicketStateEventEnum evt) {
        if (evt == null) throw new IllegalArgumentException("Invalid TicketStateEventEnum");
        TicketStateEvent newTicketStateEvent = new TicketStateEvent();
        newTicketStateEvent.setTicketEvent(TicketStateEventEnum.REGISTERED);
        newTicketStateEvent.setTicket(savedTicket);
        return newTicketStateEvent;
    }

    private RepairStateEvent createRepairEvent(final Repair savedRepair, final RepairStateEventEnum evt) {
        if (evt == null) throw new IllegalArgumentException("Invalid RepairStateEventEnum");
        RepairStateEvent newRepairStateEvent = new RepairStateEvent();
        newRepairStateEvent.setRepairEvent(RepairStateEventEnum.REGISTERED);
        newRepairStateEvent.setRepair(savedRepair);
        return newRepairStateEvent;
    }
}
