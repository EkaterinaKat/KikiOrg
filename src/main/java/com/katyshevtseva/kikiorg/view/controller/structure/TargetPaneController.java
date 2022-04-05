package com.katyshevtseva.kikiorg.view.controller.structure;

import com.katyshevtseva.fx.Styler;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.fx.dialog.StandardDialogBuilder;
import com.katyshevtseva.general.NoArgsKnob;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.polarperiod.PeriodUtils;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.Target;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import static com.katyshevtseva.fx.Styler.ThingToColor.BACKGROUND;
import static com.katyshevtseva.fx.Styler.ThingToColor.TEXT;
import static com.katyshevtseva.fx.Styler.getColorfullStyle;

class TargetPaneController implements FxController {
    private final Target target;
    private final NoArgsKnob boardUpdateKnob;
    @FXML
    private VBox root;
    @FXML
    private Label titleLabel;
    @FXML
    private Label hierarchyLabel;
    @FXML
    private Label periodLabel;
    @FXML
    private Button doneButton;
    @FXML
    private Button rejectButton;

    TargetPaneController(Target target, NoArgsKnob boardUpdateKnob) {
        this.target = target;
        this.boardUpdateKnob = boardUpdateKnob;
    }

    @FXML
    private void initialize() {
        if (PeriodUtils.isOverdue(target.getPeriod())) {
            root.setStyle(getColorfullStyle(BACKGROUND, Styler.StandardColor.RED));
            titleLabel.setStyle(getColorfullStyle(TEXT, Styler.StandardColor.WHITE));
            hierarchyLabel.setStyle(getColorfullStyle(TEXT, Styler.StandardColor.WHITE));
            periodLabel.setStyle(getColorfullStyle(TEXT, Styler.StandardColor.WHITE));
            doneButton.setStyle(getColorfullStyle(BACKGROUND, Styler.StandardColor.RED) + getColorfullStyle(TEXT, Styler.StandardColor.WHITE));
            rejectButton.setStyle(getColorfullStyle(BACKGROUND, Styler.StandardColor.RED) + getColorfullStyle(TEXT, Styler.StandardColor.WHITE));
        }

        titleLabel.setWrapText(true);
        hierarchyLabel.setWrapText(true);
        periodLabel.setWrapText(true);

        titleLabel.setText(target.getTitle());
        hierarchyLabel.setText(Core.getInstance().targetService().getTargetHierarchyOverviewString(target));
        periodLabel.setText(PeriodUtils.getFullPeriodInfo(target.getPeriod()));

        doneButton.setOnAction(event -> new StandardDialogBuilder().openQuestionDialog("Done?", answer -> {
            if (answer) {
                Core.getInstance().structureHierarchyNodeService().done(target);
                boardUpdateKnob.execute();
            }
        }));

        rejectButton.setOnAction(event -> new StandardDialogBuilder().openQuestionDialog("Reject?", answer -> {
            if (answer) {
                Core.getInstance().structureHierarchyNodeService().reject(target);
                boardUpdateKnob.execute();
            }
        }));
    }
}
