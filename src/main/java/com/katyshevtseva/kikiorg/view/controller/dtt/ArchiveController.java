package com.katyshevtseva.kikiorg.view.controller.dtt;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.Size;
import com.katyshevtseva.fx.component.ComponentBuilder;
import com.katyshevtseva.fx.component.controller.PageableBlockListController;
import com.katyshevtseva.fx.switchcontroller.SectionController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.dtt.DttLogService;
import com.katyshevtseva.kikiorg.core.sections.dtt.entity.DttLog;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import static com.katyshevtseva.date.DateUtils.READABLE_DATE_FORMAT;

public class ArchiveController implements SectionController {
    private static final Size TASK_LIST_SIZE = new Size(800, 800);
    private PageableBlockListController<DttLog> blockListController;
    private final DttLogService service = Core.getInstance().dttLogService();
    @FXML
    private VBox root;

    @FXML
    private void initialize() {
        ComponentBuilder.Component<PageableBlockListController<DttLog>> blockListComponent =
                new ComponentBuilder().setSize(TASK_LIST_SIZE).getPageableBlockListComponent();
        root.getChildren().add(blockListComponent.getNode());
        blockListController = blockListComponent.getController();
    }

    @Override
    public void update() {
        blockListController.show(service::getLogs, (this::actionToNode));
    }

    private Node actionToNode(DttLog log, int blockWidth) {
        VBox vBox = new VBox();
        Label label = new Label(log.getContent());
        vBox.getChildren().addAll(
                label,
                FxUtils.getPaneWithHeight(10),
                new Label(READABLE_DATE_FORMAT.format(log.getDate().getValue())));
        return FxUtils.frame(vBox, 10);
    }
}
