package org.deraproject.apps.server.services;

import org.deraproject.apps.server.db.repositories.CafeRepository;
import org.deraproject.apps.server.db.repositories.VolunteerRepository;
import org.deraproject.apps.server.enums.SkillsEnum;
import org.deraproject.apps.server.entities.Volunteer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class VolunteerService {
    @Autowired
    private VolunteerRepository repository;

    public Volunteer create(Volunteer o) {
        return repository.saveAndFlush(o);
    }

    public Collection<Volunteer> findAllRepairersBySkills(List<SkillsEnum> skillsEnumList) {
        return this.repository.findAll()
            .stream()
            .filter(Objects::nonNull)
            .filter(obj -> skillsEnumList.stream()
                .anyMatch(obj.getSkills()::contains))
            .collect(Collectors.toList());

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
