package org.deraproject.apps.server.services;

import lombok.extern.slf4j.Slf4j;
import org.deraproject.apps.server.db.repositories.VolunteerRepository;
import org.deraproject.apps.server.enums.SkillsEnum;
import org.deraproject.apps.server.db.entities.Volunteer;
import org.deraproject.apps.server.enums.VolunteerTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class VolunteerService {
    @Autowired
    private VolunteerRepository repository;

    private final List<VolunteerTypeEnum> repairVolunteerType = List.of(VolunteerTypeEnum.REPAIRER);
    private final List<VolunteerTypeEnum> frontOfHouseVolunteerType = List.of(VolunteerTypeEnum.FRONT_OF_HOUSE);
    private final List<VolunteerTypeEnum> administratorVolunteerType = List.of(VolunteerTypeEnum.ADMINISTRATOR);

    public Volunteer create(final Volunteer o) {
        return repository.saveAndFlush(o);
    }

    public Collection<Volunteer> findAllRepairersBySkills(final List<SkillsEnum> skillsEnumList) {
        return repository.findAll()
            .stream()
            .filter(Objects::nonNull)
            .filter(Volunteer::isActive)
            .filter(obj -> skillsEnumList.stream()
                .anyMatch(obj.getSkills()::contains))
            .filter(obj -> repairVolunteerType.stream()
                .anyMatch(obj.getVolunteerType()::contains))
            .collect(Collectors.toList());
    }

    public Collection<Volunteer> findAllFrontOfHouse() {
        return repository.findAll()
            .stream()
            .filter(Objects::nonNull)
            .filter(Volunteer::isActive)
            .filter(obj -> frontOfHouseVolunteerType.stream()
                .anyMatch(obj.getVolunteerType()::contains))
            .collect(Collectors.toList());
    }

    public Collection<Volunteer> findAllRepairers() {
        return repository.findAll()
            .stream()
            .filter(Objects::nonNull)
            .filter(Volunteer::isActive)
            .filter(obj -> repairVolunteerType.stream()
                .anyMatch(obj.getVolunteerType()::contains))
            .collect(Collectors.toList());
    }

    public void delete(final UUID id) {
        disable(id);
    }

    public void disable(final UUID id) {
        modifyActive(id, false);
    }

    public void enable(final UUID id) {
        modifyActive(id, true);
    }

    private void modifyActive(final UUID id, final boolean state) throws NoSuchElementException {
        if (id == null) throw new IllegalArgumentException("No ID provided.");
        Volunteer o = null;

        try {
            o = repository.findById(id)
                .orElseThrow();
        } catch (final NoSuchElementException e) {
            log.error("Unable to find Volunteer with id: " + id);
            throw new NoSuchElementException("Volunteer with ID: " + id + " - not found.", e);
        }

        o.setActive(state);
        repository.saveAndFlush(o);
    }
}
