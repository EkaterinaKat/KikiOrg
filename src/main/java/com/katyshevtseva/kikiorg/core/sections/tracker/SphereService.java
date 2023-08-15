package com.katyshevtseva.kikiorg.core.sections.tracker;

import com.katyshevtseva.kikiorg.core.sections.tracker.entity.Sphere;
import com.katyshevtseva.kikiorg.core.sections.tracker.entity.Task;
import com.katyshevtseva.kikiorg.core.sections.tracker.repo.SphereRepo;
import com.katyshevtseva.kikiorg.core.sections.tracker.repo.TaskRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SphereService {
    private final SphereRepo repo;
    private final TaskRepo taskRepo;
    private final TaskService taskService;

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
        for (Task task : taskRepo.findBySphere(sphere)) {
            taskService.delete(task);
        }
        repo.delete(sphere);
    }
}
