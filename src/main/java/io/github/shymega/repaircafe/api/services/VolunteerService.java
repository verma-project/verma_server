package io.github.shymega.repaircafe.api.services;

import io.github.shymega.repaircafe.api.db.repositories.VolunteerRepository;
import io.github.shymega.repaircafe.api.models.Volunteer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class VolunteerService {
    @Autowired
    private VolunteerRepository repository;

    public Volunteer create(Volunteer o) {
        return repository.saveAndFlush(o);
    }

    public void delete(UUID id) {
        disable(id);
    }

    public void disable(UUID id) {
        modifyActive(id, false);
    }

    public void enable(UUID id) {
        modifyActive(id, true);
    }

    private void modifyActive(UUID id, boolean state) {
        if (id == null) throw new IllegalArgumentException("No ID provided.");

        Volunteer o = repository.findById(id)
            .orElseThrow();
        o.setActive(state);
        repository.saveAndFlush(o);
    }
}
