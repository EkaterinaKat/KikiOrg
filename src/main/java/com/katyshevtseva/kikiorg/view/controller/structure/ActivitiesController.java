package com.katyshevtseva.kikiorg.view.controller.structure;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.Styler;
import com.katyshevtseva.fx.Styler.StandardColor;
import com.katyshevtseva.fx.Styler.ThingToColor;
import com.katyshevtseva.fx.dialog.StandardDialogBuilder;
import com.katyshevtseva.fx.dialogconstructor.DcTextField;
import com.katyshevtseva.fx.dialogconstructor.DialogConstructor;
import com.katyshevtseva.fx.switchcontroller.SectionController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.structure.ActivityStatus;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.Activity;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.Param;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.ParamValue;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.katyshevtseva.fx.Styler.StandardColor.*;
import static com.katyshevtseva.general.GeneralUtils.wrapTextByWords;
import static com.katyshevtseva.kikiorg.core.sections.structure.ActivityStatus.*;
import static com.katyshevtseva.kikiorg.view.utils.KikiOrgWindowCreator.DialogInfo.STR_VALUE_SELECT;
import static com.katyshevtseva.kikiorg.view.utils.KikiOrgWindowCreator.windowCreator;

public class ActivitiesController implements SectionController {
    @FXML
    private GridPane gridPane;
    @FXML
    private Button activityCreationButton;
    @FXML
    private ComboBox<ActivityStatus> statusComboBox;

    @FXML
    private void initialize() {
        FxUtils.setComboBoxItems(statusComboBox, ActivityStatus.values(), ACTIVE);
        statusComboBox.setOnAction(event -> fillPane());
        activityCreationButton.setOnAction(event -> {
            DcTextField titleField = new DcTextField(true, "");
            DialogConstructor.constructDialog(() -> {
                Core.getInstance().structureService().createActivity(titleField.getValue());
                fillPane();
            }, titleField);
        });
    }

    @Override
    public void update() {
        fillPane();
    }

    private void fillPane() {
        gridPane.getChildren().clear();
        Map<Integer, Activity> indexActivityMap = getIndexActivityMap();
        Map<Integer, Param> indexParamMap = getIndexParamMap();

        for (int k = 0; k <= indexActivityMap.size(); k++) {
            for (int l = 0; l <= indexParamMap.size(); l++) {
                if (k == 0 && l == 0) {
                    gridPane.add(getHeadlineNode("Activities"), l, k);
                } else if (k == 0) {
                    gridPane.add(getHeadlineNode(indexParamMap.get(l).getTitle()), l, k);
                } else if (l == 0) {
                    Activity activity = indexActivityMap.get(k);
                    Node node = getActivityNode(activity.getTitle());
                    node.setOnContextMenuRequested(event -> showActivityContextMenu(event, activity, node));
                    gridPane.add(node, l, k);
                } else {
                    Activity activity = indexActivityMap.get(k);
                    Param param = indexParamMap.get(l);
                    Node node = getRegularNode(getValuesString(activity, param), valuesAreCorrect(activity, param));
                    node.setOnMouseClicked(event -> windowCreator().openDialog(STR_VALUE_SELECT,
                            new ValuesSelectController(param, activity, this::fillPane)
                    ));
                    gridPane.add(node, l, k);
                }
            }
        }
    }

    private void showActivityContextMenu(ContextMenuEvent event, Activity activity, Node node) {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem editItem = new MenuItem("Edit");
        editItem.setOnAction(event1 -> {
            DcTextField titleField = new DcTextField(true, activity.getTitle());
            DialogConstructor.constructDialog(() -> {
                Core.getInstance().structureService().edit(activity, titleField.getValue());
                fillPane();
            }, titleField);
        });

        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(event1 -> new StandardDialogBuilder().openQuestionDialog("Delete?", b -> {
            if (b) {
                Core.getInstance().structureService().delete(activity);
                fillPane();
            }
        }));

        if (activity.getStatus() == ACTIVE || activity.getStatus() == SUSPENDED) {
            MenuItem finishItem = new MenuItem("Finish");
            finishItem.setOnAction(event1 -> {
                Core.getInstance().structureService().setStatus(activity, FINISHED);
                fillPane();
            });
            contextMenu.getItems().add(finishItem);
        }

        if (activity.getStatus() == FINISHED || activity.getStatus() == SUSPENDED) {
            MenuItem returnToWorkItem = new MenuItem("Return to work");
            returnToWorkItem.setOnAction(event1 -> {
                Core.getInstance().structureService().setStatus(activity, ACTIVE);
                fillPane();
            });
            contextMenu.getItems().add(returnToWorkItem);
        }

        if (activity.getStatus() == FINISHED || activity.getStatus() == ACTIVE) {
            MenuItem suspendItem = new MenuItem("Suspend");
            suspendItem.setOnAction(event1 -> {
                Core.getInstance().structureService().setStatus(activity, SUSPENDED);
                fillPane();
            });
            contextMenu.getItems().add(suspendItem);
        }


        contextMenu.getItems().addAll(editItem, deleteItem);
        contextMenu.show(node, event.getScreenX(), event.getScreenY());
    }

    private Node getHeadlineNode(String s) {
        return getTableCell(s, 20, WHITE, BLACK, WHITE);
    }

    private Node getActivityNode(String s) {
        return getTableCell(s, 15, BLACK, PASTEL_PINK, GRAY);
    }

    private Node getRegularNode(String s, boolean dataIsCorrect) {
        return getTableCell(s, 15, BLACK, dataIsCorrect ? WHITE : RED, GRAY);
    }

    private Node getTableCell(String text, int textSize, StandardColor textColor, StandardColor backgroundColor,
                              StandardColor borderColor) {
        Label label = new Label(wrapTextByWords(text, 20));
        label.setStyle(Styler.getColorfullStyle(ThingToColor.TEXT, textColor) +
                Styler.getTextSizeStyle(textSize));
        label.setPadding(new Insets(textSize));

        Pane pane = new Pane();
        pane.setStyle(Styler.getColorfullStyle(ThingToColor.BACKGROUND, backgroundColor) +
                Styler.getColorfullStyle(ThingToColor.BORDER, borderColor));
        pane.getChildren().add(label);
        return pane;
    }

    private String getValuesString(Activity activity, Param param) {
        List<ParamValue> values = activity.getParamValues().stream()
                .filter(paramValue -> paramValue.getParam().equals(param)).collect(Collectors.toList());
        if (values.isEmpty()) {
            return "-";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (ParamValue value : values) {
            stringBuilder.append("* ").append(value.getTitle()).append("\n");
        }
        return stringBuilder.toString();
    }

    private boolean valuesAreCorrect(Activity activity, Param param) {
        List<ParamValue> values = activity.getParamValues().stream()
                .filter(paramValue -> paramValue.getParam().equals(param)).collect(Collectors.toList());

        if (param.isRequired() && values.size() == 0) {
            return false;
        }
        return !param.isSingleValue() || values.size() <= 1;
    }

    private Map<Integer, Activity> getIndexActivityMap() {
        Map<Integer, Activity> map = new HashMap<>();
        List<Activity> activities = Core.getInstance().structureService().getActivities(statusComboBox.getValue());
        for (int i = 0; i < activities.size(); i++) {
            map.put(i + 1, activities.get(i));
        }
        return map;
    }

    private Map<Integer, Param> getIndexParamMap() {
        Map<Integer, Param> map = new HashMap<>();
        List<Param> params = Core.getInstance().structureService().getParams();
        for (int i = 0; i < params.size(); i++) {
            map.put(i + 1, params.get(i));
        }
        return map;
    }
}
