package com.katyshevtseva.kikiorg.core.sections.dtt;

import com.katyshevtseva.kikiorg.core.sections.dtt.entity.DatelessTask;
import com.katyshevtseva.kikiorg.core.sections.dtt.entity.Sphere;
import com.katyshevtseva.kikiorg.core.sections.dtt.repo.DatelessTaskRepo;
import com.katyshevtseva.kikiorg.core.sections.dtt.repo.SphereRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SphereService {
    private final SphereRepo repo;
    private final DatelessTaskRepo taskRepo;

    public Sphere save(Sphere existing, String title, boolean active) {
        if (existing == null) {
            existing = new Sphere();
        }
        existing.setTitle(title);
        existing.setActive(active);
        return repo.save(existing);
    }

    public List<Sphere> getAll() {
        return repo.findAll();
    }

    public void delete(Sphere sphere) {
        for (DatelessTask task : taskRepo.findBySphere(sphere)) {
            taskRepo.delete(task);
        }
        repo.delete(sphere);
    }
}
