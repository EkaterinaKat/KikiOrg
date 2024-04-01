package com.katyshevtseva.kikiorg.view.controller.study;

import com.katyshevtseva.image.ImageContainer;
import com.katyshevtseva.image.ImageContainerCache;
import com.katyshevtseva.kikiorg.core.sections.study.entity.Subject;
import javafx.scene.image.Image;

import static com.katyshevtseva.kikiorg.view.utils.ViewConstants.SUBJECT_IMAGES_LOCATION;

public class SubjectImageUtil {
    private static final ImageContainerCache icc = ImageContainerCache.getInstance();

    public static Image getImage(Subject subject) {
        ImageContainer imageContainer = icc.getImageContainer(
                subject.getTitle() + ".png", SUBJECT_IMAGES_LOCATION, 100);
        return imageContainer.getImage();
    }
}
