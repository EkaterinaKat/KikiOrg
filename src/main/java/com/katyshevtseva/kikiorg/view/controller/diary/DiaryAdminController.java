package com.katyshevtseva.kikiorg.view.controller.diary;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.Styler;
import com.katyshevtseva.fx.dialog.StandardDialogBuilder;
import com.katyshevtseva.fx.dialogconstructor.DcTextArea;
import com.katyshevtseva.fx.dialogconstructor.DcTextField;
import com.katyshevtseva.fx.dialogconstructor.DialogConstructor;
import com.katyshevtseva.fx.switchcontroller.SectionController;
import com.katyshevtseva.general.GeneralUtils;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.diary.entity.IndValue;
import com.katyshevtseva.kikiorg.core.sections.diary.entity.Indicator;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.springframework.lang.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.katyshevtseva.fx.FxUtils.frame;
import static com.katyshevtseva.fx.FxUtils.getPaneWithHeight;
import static com.katyshevtseva.fx.Styler.StandardColor.GRAY;
import static com.katyshevtseva.fx.Styler.ThingToColor.TEXT;
import static com.katyshevtseva.fx.Styler.getColorfullStyle;

public class DiaryAdminController implements SectionController {
    @FXML
    private GridPane gridPane;
    @FXML
    private Button newIndicatorButton;
    @FXML
    private Label titleLabel;
    @FXML
    private Label descLabel;
    @FXML
    private VBox valuesPane;
    private Map<Long, Label> indicatorIdPointLabelMap;

    @FXML
    private void initialize() {
        fillIndicatorTable();
        newIndicatorButton.setOnAction(event -> openIndicatorEditWindow(null));
    }

    private void fillIndicatorTable() {
        fillIndicatorTable(null);
    }

    private void fillIndicatorTable(Indicator indicatorToShow) {
        boolean indToShowWasShowed = false;
        gridPane.getChildren().clear();
        List<Indicator> habits = Core.getInstance().diaryService().getAllIndicators();
        indicatorIdPointLabelMap = new HashMap<>();
        int rowIndex = 0;
        for (Indicator indicator : habits) {
            Label label = new Label(indicator.getTitleAndArchivedInfo());
            label.setContextMenu(getContextMenu(indicator));
            Label point = new Label();
            indicatorIdPointLabelMap.put(indicator.getId(), point);
            label.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                showIndicator(indicator);
            });
            gridPane.add(point, 0, rowIndex);
            gridPane.add(label, 1, rowIndex);
            rowIndex++;

            if (indicator.equals(indicatorToShow)) {
                showIndicator(indicator);
                indToShowWasShowed = true;
            }

            if (indicator.getArchived()) {
                label.setStyle(getColorfullStyle(TEXT, GRAY));
            }
        }
        if (!indToShowWasShowed) {
            showIndicator(null);
        }
    }

    private ContextMenu getContextMenu(Indicator indicator) {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem editItem = new MenuItem("Edit");
        editItem.setOnAction(event1 -> openIndicatorEditWindow(indicator));
        contextMenu.getItems().add(editItem);

        MenuItem newValueItem = new MenuItem("New value");
        newValueItem.setOnAction(event -> openValueEditDialog(indicator, null));
        contextMenu.getItems().add(newValueItem);

        MenuItem archiveItem = new MenuItem("Archive");
        archiveItem.setOnAction(event1 -> {
            Core.getInstance().diaryService().archive(indicator);
            fillIndicatorTable(indicator);
        });
        contextMenu.getItems().add(archiveItem);

        MenuItem hideItem = new MenuItem("Hide");
        hideItem.setOnAction(event1 -> {
            Core.getInstance().diaryService().hide(indicator);
            fillIndicatorTable(indicator);
        });
        contextMenu.getItems().add(hideItem);

        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(event1 -> {
            String question = String.format("Delete %s?", indicator.getTitle());
            new StandardDialogBuilder().openQuestionDialog(question, b -> {
                if (b) {
                    Core.getInstance().diaryService().delete(indicator);
                    fillIndicatorTable(indicator);
                }
            });
        });
        contextMenu.getItems().add(deleteItem);

        return contextMenu;
    }

    private void showIndicator(@Nullable Indicator indicator) {
        titleLabel.setText(indicator != null ? indicator.getTitleAndArchivedInfo() : "");
        descLabel.setText(indicator != null ? indicator.getDescription() : "");

        indicatorIdPointLabelMap.values().forEach(label -> label.setText(""));
        if (indicator != null) {
            indicatorIdPointLabelMap.get(indicator.getId()).setText("* ");
        }
        fillValuesPane(indicator);
    }

    private void fillValuesPane(@Nullable Indicator indicator) {
        valuesPane.getChildren().clear();

        if (indicator == null) {
            return;
        }

        for (IndValue value : indicator.getSortedValues()) {
            Label label = new Label(value.getTitleAndDesc());
            label.setStyle(getColorfullStyle(TEXT, Styler.StandardColor.BLACK));
            FxUtils.setWidth(label, 400);
            label.setWrapText(true);

            Node node = frame(label, 10);

            valuesPane.getChildren().addAll(node, getPaneWithHeight(10));
            if (!GeneralUtils.isEmpty(value.getColor())) {
                try {
                    node.setStyle(getColorfullStyle(Styler.ThingToColor.BACKGROUND, value.getColor()));
                    Styler.correctLabelColorIfNeeded(label, value.getColor());
                } catch (Exception e) {
                }
            } else {
                node.setStyle(Styler.getBlackBorderStyle());
            }

            if (value.isDefaultValue()) {
                node.setStyle(node.getStyle() + Styler.getBlackBorderStyle() + Styler.getBorderWidth(5));
            }

            node.setOnContextMenuRequested(event -> showValueContextMenu(event, node, value, indicator));
        }
    }

    private void openIndicatorEditWindow(Indicator indicator) {
        DcTextField titleField = new DcTextField(true, indicator == null ? "" : indicator.getTitle());
        DcTextArea descField = new DcTextArea(false, indicator == null ? "" : indicator.getDescription());
        DialogConstructor.constructDialog(() -> {
            Core.getInstance().diaryService().save(indicator, titleField.getValue(), descField.getValue());
            fillIndicatorTable(indicator);
        }, titleField, descField);
    }

    private void openValueEditDialog(Indicator indicator, IndValue value) {
        DcTextField titleField = new DcTextField(true, value == null ? "" : value.getTitle());
        DcTextField colorField = new DcTextField(false, value == null ? "" : value.getColor());
        DcTextArea descField = new DcTextArea(false, value == null ? "" : value.getDescription());
        DialogConstructor.constructDialog(() -> {
            Core.getInstance().diaryService().save(indicator, value, titleField.getValue(),
                    descField.getValue(), colorField.getValue());
            fillIndicatorTable(indicator);
        }, titleField, colorField, descField);
    }

    private void showValueContextMenu(ContextMenuEvent event, Node node, IndValue value, Indicator indicator) {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem editItem = new MenuItem("Edit");
        editItem.setOnAction(event1 -> openValueEditDialog(indicator, value));
        contextMenu.getItems().add(editItem);

        MenuItem makeDefaultItem = new MenuItem("Make default");
        makeDefaultItem.setOnAction(event1 -> {
            Core.getInstance().diaryService().makeDefault(value);
            fillIndicatorTable(indicator);
        });
        contextMenu.getItems().add(makeDefaultItem);

        contextMenu.show(node, event.getScreenX(), event.getScreenY());
    }
}
