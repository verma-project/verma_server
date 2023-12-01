package io.github.shymega.repaircafe.api.services;

import io.github.shymega.repaircafe.api.db.repositories.VisitorRepository;
import io.github.shymega.repaircafe.api.models.Visitor;
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
