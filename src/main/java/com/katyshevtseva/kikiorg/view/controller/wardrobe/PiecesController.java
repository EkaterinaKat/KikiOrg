package com.katyshevtseva.kikiorg.view.controller.wardrobe;

import com.katyshevtseva.fx.Size;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.fx.component.ComponentBuilder;
import com.katyshevtseva.fx.component.ComponentBuilder.Component;
import com.katyshevtseva.fx.component.controller.GalleryController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.WardrobeService;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

class PiecesController implements FxController {
    private final WardrobeService service = Core.getInstance().wardrobeService();
    @FXML
    private VBox galleryPane;

    @FXML
    private void initialize() {
        Component<GalleryController> component = new ComponentBuilder().setSize(new Size(700, 750))
                .getGalleryComponent(3,
                        ImageUtils.toImageContainers(service.getAllPieces()),
                        imageContainer -> System.out.println(imageContainer.getUrl()));

        galleryPane.getChildren().add(component.getNode());
    }
}
