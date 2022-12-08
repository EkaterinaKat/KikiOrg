package com.katyshevtseva.kikiorg.view.controller.structure;

import com.katyshevtseva.fx.Styler;
import com.katyshevtseva.fx.Styler.StandardColor;
import com.katyshevtseva.fx.Styler.ThingToColor;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.fx.dialogconstructor.DcTextField;
import com.katyshevtseva.fx.dialogconstructor.DialogConstructor;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.Activity;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.Param;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.ParamValue;
import com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.katyshevtseva.fx.Styler.StandardColor.*;

public class ActivitiesController implements FxController {
    @FXML
    private GridPane gridPane;
    @FXML
    private Button activityCreationButton;

    @FXML
    private void initialize() {
        fillPane();
        activityCreationButton.setOnAction(event -> {
            DcTextField titleField = new DcTextField(true, "");
            DialogConstructor.constructDialog(() -> {
                Core.getInstance().structureService().createActivity(titleField.getValue());
                fillPane();
            }, titleField);
        });
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
                    gridPane.add(getActivityNode(indexActivityMap.get(k).getTitle()), l, k);
                } else {
                    Activity activity = indexActivityMap.get(k);
                    Param param = indexParamMap.get(l);
                    Node node = getRegularNode(getValuesString(activity, param));
                    node.setOnMouseClicked(event -> OrganizerWindowCreator.getInstance().openValuesSelectDialog(
                            new ValuesSelectController(param, activity, this::fillPane)
                    ));
                    gridPane.add(node, l, k);
                }
            }
        }
    }

    private Node getHeadlineNode(String s) {
        return getTableCell(s, 20, WHITE, BLACK, WHITE);
    }

    private Node getActivityNode(String s) {
        return getTableCell(s, 15, BLACK, PASTEL_PINK, GRAY);
    }

    private Node getRegularNode(String s) {
        return getTableCell(s, 15, BLACK, WHITE, GRAY);
    }

    private Node getTableCell(String text, int textSize, StandardColor textColor, StandardColor backgroundColor,
                              StandardColor borderColor) {
        Label label = new Label(text);
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

    private Map<Integer, Activity> getIndexActivityMap() {
        Map<Integer, Activity> map = new HashMap<>();
        List<Activity> activities = Core.getInstance().structureService().getActivities();
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
