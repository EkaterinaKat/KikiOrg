package com.katyshevtseva.kikiorg.view.controller.diary;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.Styler;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.katyshevtseva.fx.FxUtils.frame;
import static com.katyshevtseva.fx.FxUtils.getPaneWithHeight;

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
    private Button editButton;
    @FXML
    private VBox valuesPane;
    @FXML
    private Button newValueButton;
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
        gridPane.getChildren().clear();
        List<Indicator> habits = Core.getInstance().diaryService().getIndicators();
        indicatorIdPointLabelMap = new HashMap<>();
        int rowIndex = 0;
        for (Indicator indicator : habits) {
            Label label = new Label(indicator.getTitle());
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
            }
        }
    }

    private void showIndicator(Indicator indicator) {
        titleLabel.setText(indicator.getTitle());
        descLabel.setText(indicator.getDescription());

        indicatorIdPointLabelMap.values().forEach(label -> label.setText(""));
        indicatorIdPointLabelMap.get(indicator.getId()).setText("* ");
        editButton.setOnAction(event1 -> openIndicatorEditWindow(indicator));
        newValueButton.setOnAction(event -> openValueEditDialog(indicator, null));
        fillValuesPane(indicator);
    }

    private void fillValuesPane(Indicator indicator) {
        valuesPane.getChildren().clear();
        for (IndValue value : indicator.getSortedValues()) {
            Label label = new Label(value.getTitleAndDesc());
            label.setStyle(Styler.getColorfullStyle(Styler.ThingToColor.TEXT, Styler.StandardColor.BLACK));
            FxUtils.setWidth(label, 400);
            label.setWrapText(true);

            Node node = frame(label, 10);

            valuesPane.getChildren().addAll(node, getPaneWithHeight(10));
            if (!GeneralUtils.isEmpty(value.getColor())) {
                Styler.setBackgroundColorAndCorrectTextColor(node, label, value.getColor());
            } else {
                node.setStyle(Styler.getBlackBorderStyle());
            }
            node.setOnContextMenuRequested(event -> showValueContextMenu(event, node, value, indicator));
        }
    }

    private void openIndicatorEditWindow(Indicator indicator) {
        DcTextField titleField = new DcTextField(true, indicator == null ? "" : indicator.getTitle());
        DcTextArea descField = new DcTextArea(false, indicator == null ? "" : indicator.getDescription());
        DialogConstructor.constructDialog(() -> {
            Core.getInstance().diaryService().save(indicator, titleField.getValue(), descField.getValue());
            fillIndicatorTable();
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

        contextMenu.show(node, event.getScreenX(), event.getScreenY());
    }
}
