// SPDX-FileCopyrightText: 2023-2025 Dom Rodriguez (shymega) <shymega@shymega.org.uk>
//
// SPDX-License-Identifier: AGPL-3.0-only

package org.vermaproject.apps.server.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vermaproject.apps.server.db.entities.Visitor;
import org.vermaproject.apps.server.db.repositories.VisitorRepository;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class VisitorService {
    @Autowired
    private VisitorRepository repository;

    public Visitor create(final Visitor o) {
        return repository.saveAndFlush(o);
    }

    public Collection<Visitor> getAllBannedVisitors() {
        return repository.findAll()
            .stream()
            .filter(Objects::nonNull)
            .filter(Visitor::isBanned)
            .toList();
    }

    public Collection<Visitor> getAllUnbannedVisitors() {
        return repository.findAll()
            .stream()
            .filter(Objects::nonNull)
            .filter(Visitor::isNotBanned)
            .toList();
    }

    public Collection<Visitor> getAllVisitorsBySurname(final String surname) {
        return repository.findAll()
            .stream()
            .filter(Objects::nonNull)
            .filter(v -> Objects.equals(v.getLastName(), surname))
            .toList();
    }

    public void ban(final UUID id) {
        modifyBanned(id, true);
    }

    public void unban(final UUID id) {
        modifyBanned(id, false);
    }

    private void modifyBanned(final UUID id, final boolean banState) throws NoSuchElementException, IllegalArgumentException {
        if (id == null) throw new IllegalArgumentException("No ID provided.");
        Visitor o;

        try {
            o = repository.findById(id)
                .orElseThrow();
        } catch (final NoSuchElementException e) {
            log.error("Unable to find Visitor with id: " + id);
            throw new NoSuchElementException("Visitor with ID: " + id + " - not found.", e);
        }

        o.setBanned(banState);

        repository.saveAndFlush(o);
    }
}
