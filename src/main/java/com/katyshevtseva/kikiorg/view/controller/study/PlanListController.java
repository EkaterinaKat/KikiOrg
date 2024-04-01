package com.katyshevtseva.kikiorg.view.controller.study;

import com.katyshevtseva.date.DateUtils;
import com.katyshevtseva.date.Period;
import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.LabelBuilder;
import com.katyshevtseva.fx.Styler;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.fx.dialog.StandardDialogBuilder;
import com.katyshevtseva.general.NoArgsKnob;
import com.katyshevtseva.general.OneArgKnob;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.study.entity.Plan;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.katyshevtseva.time.TimeUtil.getTimeStringByMinutes;

@RequiredArgsConstructor
public class PlanListController implements FxController {
    private final OneArgKnob<Plan> onPlanEditClickListener;
    private final NoArgsKnob updateListKnob;
    private final int blockWidth;
    @FXML
    private VBox planBox;

    @FXML
    private void initialize() {

    }

    public void show(List<Plan> plans) {
        planBox.getChildren().clear();
        for (Plan plan : plans) {
            planBox.getChildren().addAll(getPlanNode(plan), FxUtils.getPaneWithHeight(10));
        }
    }

    public Node getPlanNode(Plan plan) {
        int textWidth = blockWidth - 50;

        Period period = new Period(plan.getStart().getValue(), plan.getEnd().getValue());
        String titleText = plan.getSubject().getTitle() + " " + (DateUtils.getNumberOfDays(period) + 1) + " д.";
        Label titleLabel = new LabelBuilder().text(titleText).textSize(25).width(textWidth).build();

        String infoString = String.format("Dates: %s\nMin days: %d \nMin time: %s ",
                DateUtils.getStringRepresentationOfPeriod(period),
                plan.getMinDays(),
                getTimeStringByMinutes(plan.getMinMinutesADay().intValue(), true));
        Label infoLabel = new LabelBuilder().text(infoString).width(textWidth).build();

        VBox textContentBox = new VBox();
        textContentBox.getChildren().addAll(titleLabel, FxUtils.getPaneWithHeight(15), infoLabel);

        HBox contentBox = new HBox();
        ImageView imageView = new ImageView();
        imageView.setImage(SubjectImageUtil.getImage(plan.getSubject()));
        contentBox.getChildren().addAll(imageView, FxUtils.getPaneWithWidth(15), textContentBox);

        Pane root = new Pane();
        FxUtils.setWidth(root, blockWidth);
        root.setStyle(Styler.getBlackBorderStyle()
                + Styler.getColorfullStyle(Styler.ThingToColor.BACKGROUND, getColor(plan)));
        root.getChildren().add(FxUtils.frame(contentBox, 20));
        root.setOnContextMenuRequested(event -> openContextMenu(plan, root, event));

        return root;
    }

    private void openContextMenu(Plan plan, Node node, ContextMenuEvent event) {
        ContextMenu contextMenu = new ContextMenu();

        if (onPlanEditClickListener != null) {
            MenuItem editItem = new MenuItem("Edit");
            editItem.setOnAction(event1 -> onPlanEditClickListener.execute(plan));
            contextMenu.getItems().add(editItem);
        }

        if (!plan.isArchived()) {
            MenuItem archiveItem = new MenuItem("Archive");
            archiveItem.setOnAction(event1 -> {
                Core.getInstance().planService.archive(plan);
                updateListKnob.execute();
            });
            contextMenu.getItems().add(archiveItem);
        }

        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(event1 -> {
            String question = "Delete?";
            new StandardDialogBuilder().openQuestionDialog(question, b -> {
                if (b) {
                    Core.getInstance().planService.delete(plan);
                    updateListKnob.execute();
                }
            });
        });
        contextMenu.getItems().add(deleteItem);

        contextMenu.show(node, event.getScreenX(), event.getScreenY());
    }

    private Styler.StandardColor getColor(Plan plan) {
        if (Core.getInstance().planService.planCompleted(plan)) {
            return Styler.StandardColor.SCREAMING_GREEN;
        }
        if (plan.isArchived()) {
            return Styler.StandardColor.GRAY;
        }
        return Styler.StandardColor.WHITE;
    }
}
