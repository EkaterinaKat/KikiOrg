package com.katyshevtseva.kikiorg.view.controller.structure;

import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.fx.dialogconstructor.DcTextField;
import com.katyshevtseva.fx.dialogconstructor.DialogConstructor;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.Activity;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.Param;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.ParamValue;
import com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
                    gridPane.add(new Label("Activities"), l, k);
                } else if (k == 0) {
                    gridPane.add(new Label(indexParamMap.get(l).getTitle()), l, k);
                } else if (l == 0) {
                    gridPane.add(new Label(indexActivityMap.get(k).getTitle()), l, k);
                } else {
                    Activity activity = indexActivityMap.get(k);
                    Param param = indexParamMap.get(l);
                    Label label = new Label(getValuesString(activity, param));
                    label.setOnMouseClicked(event -> OrganizerWindowCreator.getInstance().openValuesSelectDialog(
                            new ValuesSelectController(param, activity, this::fillPane)
                    ));
                    gridPane.add(label, l, k);
                }
            }
        }

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
