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

    private final List<VolunteerTypeEnum> repairVolunteerTypeList = List.of(VolunteerTypeEnum.REPAIRER);
    private final List<VolunteerTypeEnum> frontOfHouseVolunteerTypeList = List.of(VolunteerTypeEnum.FRONT_OF_HOUSE);

    public Volunteer create(Volunteer o) {
        return repository.saveAndFlush(o);
    }

    public Collection<Volunteer> findAllRepairersBySkills(List<SkillsEnum> skillsEnumList) {
        return repository.findAll()
            .stream()
            .filter(Objects::nonNull)
            .filter(obj -> skillsEnumList.stream()
                .anyMatch(obj.getSkills()::contains))
            .filter(obj -> repairVolunteerTypeList.stream()
                .anyMatch(obj.getVolunteerType()::equals))
            .collect(Collectors.toList());
    }

    public Collection<Volunteer> findAllFrontOfHouse() {
        return repository.findAll()
            .stream()
            .filter(Objects::nonNull)
            .filter(obj -> frontOfHouseVolunteerTypeList.stream()
                .anyMatch(obj.getVolunteerType()::equals))
            .collect(Collectors.toList());
    }

    public Collection<Volunteer> findAllRepairers() {
        return repository.findAll()
            .stream()
            .filter(Objects::nonNull)
            .filter(obj -> repairVolunteerTypeList.stream()
                .anyMatch(obj.getVolunteerType()::equals))
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

    private void modifyActive(UUID id, boolean state) throws NoSuchElementException {
        if (id == null) throw new IllegalArgumentException("No ID provided.");
        Volunteer o = null;

        try {
            o = repository.findById(id)
                .orElseThrow();
        } catch (NoSuchElementException e) {
            log.error("Unable to find Volunteer with id: " + id);
            throw new NoSuchElementException("Volunteer with ID: " + id + " - not found.", e);
        }

        o.setActive(state);
        repository.saveAndFlush(o);
    }
}
