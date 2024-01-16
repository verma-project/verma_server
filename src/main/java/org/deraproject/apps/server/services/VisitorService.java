package org.deraproject.apps.server.services;

import lombok.extern.slf4j.Slf4j;
import org.deraproject.apps.server.db.repositories.VisitorRepository;
import org.deraproject.apps.server.db.entities.Visitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class VisitorService {
    @Autowired
    private VisitorRepository repository;

    public Visitor create(Visitor o) {
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

    public Collection<Visitor> getAllVisitorsBySurname(String surname) {
        return repository.findAll()
            .stream()
            .filter(Objects::nonNull)
            .filter(v -> Objects.equals(v.getLastName(), surname))
            .toList();
    }

    public void ban(UUID id) {
        modifyBanned(id, true);
    }

    public void unban(UUID id) {
        modifyBanned(id, false);
    }

    private void modifyBanned(UUID id, boolean banState) throws NoSuchElementException {
        if (id == null) throw new IllegalArgumentException("No ID provided.");
        Visitor o = null;

        try {
            o = repository.findById(id)
                .orElseThrow();
        } catch (NoSuchElementException e) {
            log.error("Unable to find Visitor with id: " + id);
            throw new NoSuchElementException("Visitor with ID: " + id + " - not found.", e);
        }

        o.setBanned(banState);

        repository.saveAndFlush(o);
    }
}
