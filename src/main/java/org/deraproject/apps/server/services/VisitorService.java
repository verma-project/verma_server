package org.deraproject.apps.server.services;

import org.deraproject.apps.server.db.repositories.VisitorRepository;
import org.deraproject.apps.server.entities.Visitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

@Service
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
            .filter(v -> !v.isBanned())
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

    private void modifyBanned(UUID id, boolean state) {
        if (id == null) throw new IllegalArgumentException("No ID provided.");

        Visitor o = repository.findById(id)
            .orElseThrow();
        o.setBanned(state);
        repository.saveAndFlush(o);

    }
}
