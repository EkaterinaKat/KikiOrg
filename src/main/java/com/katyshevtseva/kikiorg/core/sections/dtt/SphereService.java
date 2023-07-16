package com.katyshevtseva.kikiorg.core.sections.dtt;

import com.katyshevtseva.kikiorg.core.sections.dtt.entity.Sphere;
import com.katyshevtseva.kikiorg.core.sections.dtt.repo.SphereRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SphereService {
    private final SphereRepo repo;
    private final FakeService fakeService;

    public Sphere save(Sphere existing, String title, boolean active) {
        if (existing == null) {
            existing = new Sphere();
        }
        existing.setTitle(title);
        existing.setActive(active);
        return repo.save(existing);
    }

    public List<Sphere> getAll() {
        List<Sphere> spheres = new ArrayList<>();
        spheres.add(fakeService.getProgTrackerFakeSphere());
        spheres.addAll(repo.findAll());
        return spheres;
    }

    public void delete(Sphere sphere) {
        repo.delete(sphere);
    }
}
