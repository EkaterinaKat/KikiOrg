package com.katyshevtseva.kikiorg.view.controller.structure;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.Target;
import com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class BoardController implements FxController {
    @FXML
    private VBox targetContainer;

    @FXML
    private void initialize() {
        setContent();
    }

    private void setContent() {
        targetContainer.getChildren().clear();

        for (Target target : Core.getInstance().targetService().getAllStartedTargets()) {
            targetContainer.getChildren().add(FxUtils.getPaneWithHeight(20));
            targetContainer.getChildren().add(OrganizerWindowCreator.getInstance()
                    .getTargetPaneNode(new TargetPaneController(target, this::setContent)));
        }
        targetContainer.getChildren().add(FxUtils.getPaneWithHeight(20));
    }
}
