package com.katyshevtseva.kikiorg.view.controller.structure;

import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.general.NoArgsKnob;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.Activity;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.Param;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.ParamValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.katyshevtseva.fx.FxUtils.closeWindowThatContains;
import static com.katyshevtseva.fx.FxUtils.getPaneWithHeight;

@RequiredArgsConstructor
public class ValuesSelectController implements FxController {
    private final Param param;
    private final Activity activity;
    private final NoArgsKnob infoUpdateKnob;
    private Map<CheckBox, ParamValue> checkBoxParamValueMap;
    @FXML
    private VBox valuesPane;
    @FXML
    private Button okButton;

    @FXML
    private void initialize() {
        fillPane();
        okButton.setOnAction(event -> {
            Core.getInstance().structureService().addParamValues(activity, param, getSelectedValues());
            infoUpdateKnob.execute();
            closeWindowThatContains(okButton);
        });
    }

    private List<ParamValue> getSelectedValues() {
        return checkBoxParamValueMap.entrySet().stream()
                .filter(entry -> entry.getKey().isSelected())
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    private void fillPane() {
        valuesPane.getChildren().clear();
        checkBoxParamValueMap = new HashMap<>();
        for (ParamValue paramValue : param.getValues()) {
            CheckBox checkBox = new CheckBox(paramValue.getTitle());
            checkBox.setSelected(activity.getParamValues().contains(paramValue));
            checkBoxParamValueMap.put(checkBox, paramValue);
            valuesPane.getChildren().addAll(checkBox, getPaneWithHeight(20));
        }
    }
}
