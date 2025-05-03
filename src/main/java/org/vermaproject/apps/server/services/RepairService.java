// SPDX-FileCopyrightText: 2023-2025 Dom Rodriguez (shymega) <shymega@shymega.org.uk>
//
// SPDX-License-Identifier: AGPL-3.0-only

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
import org.vermaproject.apps.server.enums.RepairState;
import org.vermaproject.apps.server.enums.RepairType;
import org.vermaproject.apps.server.enums.TicketState;

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

    public List<Repair> getRepairsByType(final RepairType repairType) {
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
            ticketStateEventRepository.save(createTicketEvent(ticket, TicketState.REGISTERED));
            repairStateEventRepository.save(createRepairEvent(newRepair, RepairState.REGISTERED));
        });
    }

    private TicketStateEvent createTicketEvent(final Ticket savedTicket, final TicketState evt) {
        if (evt == null) throw new IllegalArgumentException("Invalid TicketState");
        TicketStateEvent newTicketStateEvent = new TicketStateEvent();
        newTicketStateEvent.setTicketEvent(TicketState.REGISTERED);
        newTicketStateEvent.setTicket(savedTicket);
        return newTicketStateEvent;
    }

    private RepairStateEvent createRepairEvent(final Repair savedRepair, final RepairState evt) {
        if (evt == null) throw new IllegalArgumentException("Invalid RepairState");
        RepairStateEvent newRepairStateEvent = new RepairStateEvent();
        newRepairStateEvent.setRepairEvent(RepairState.REGISTERED);
        newRepairStateEvent.setRepair(savedRepair);
        return newRepairStateEvent;
    }
}
