// SPDX-FileCopyrightText: 2023-2025 Dom Rodriguez (shymega) <shymega@shymega.org.uk>
//
// SPDX-License-Identifier: AGPL-3.0-only

package org.vermaproject.apps.server.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vermaproject.apps.server.db.entities.Volunteer;
import org.vermaproject.apps.server.db.repositories.VolunteerRepository;
import org.vermaproject.apps.server.enums.SkillSet;
import org.vermaproject.apps.server.enums.VolunteerType;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class VolunteerService {
    private final List<VolunteerType> repairVolunteerType = List.of(VolunteerType.REPAIRER);
    private final List<VolunteerType> frontOfHouseVolunteerType = List.of(VolunteerType.FRONT_OF_HOUSE);
    private final List<VolunteerType> administratorVolunteerType = List.of(VolunteerType.ADMINISTRATOR);

    @Autowired
    private VolunteerRepository repository;

    public Volunteer create(final Volunteer o) {
        return repository.saveAndFlush(o);
    }

    public Collection<Volunteer> findAllRepairersBySkills(final List<SkillSet> skillSetList) {
        return repository.findAll()
            .stream()
            .filter(Objects::nonNull)
            .filter(Volunteer::isActive)
            .filter(obj -> skillSetList.stream()
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

    public Collection<Volunteer> findAllAdministrators() {
        return repository.findAll()
            .stream()
            .filter(Objects::nonNull)
            .filter(Volunteer::isActive)
            .filter(obj -> administratorVolunteerType.stream()
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
