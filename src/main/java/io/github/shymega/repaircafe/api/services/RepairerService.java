package io.github.shymega.repaircafe.api.services;

import io.github.shymega.repaircafe.api.db.RepairerRepository;
import io.github.shymega.repaircafe.api.domain.Repairer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RepairerService {
    @Autowired
    private RepairerRepository repository;

    public Repairer create(Repairer o) {
        return repository.saveAndFlush(o);
    }

    public void delete(Long id) { disable(id); }

    public void disable(Long id) {
        modifyActive(id, false);
    }

    public void enable(Long id) {
        modifyActive(id, true);
    }

    private void modifyActive(Long id, boolean state) {
        Repairer repairer = repository.findById(id)
                .orElseThrow();
        repairer.setActive(state);
        repository.save(repairer);
    }
}
