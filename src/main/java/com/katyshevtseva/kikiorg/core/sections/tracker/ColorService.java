package com.katyshevtseva.kikiorg.core.sections.tracker;

import com.katyshevtseva.kikiorg.core.repo.ColorRepo;
import com.katyshevtseva.kikiorg.core.sections.tracker.entity.ColorEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
class ColorService {
    private final ColorRepo colorRepo;

    ColorEntity getSavedColorEntity(ColorEntity color) {
        Optional<ColorEntity> optionalColor = colorRepo.findFirstByRedAndGreenAndBlue(color.getRed(), color.getGreen(), color.getBlue());
        return optionalColor.orElseGet(() -> colorRepo.save(color));
    }
}
