package com.katyshevtseva.kikiorg.view.controller.structure;

import com.katyshevtseva.fx.Styler;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.fx.dialog.StandardDialogBuilder;
import com.katyshevtseva.fx.dialogconstructor.DcTextField;
import com.katyshevtseva.fx.dialogconstructor.DialogConstructor;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.Param;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.ParamValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;

import static com.katyshevtseva.fx.FxUtils.getPaneWithHeight;


public class ParamsController implements FxController {
    @FXML
    private VBox paramPane;
    @FXML
    private Button paramCreationButton;

    @FXML
    private void initialize(){
        fillPane();
        paramCreationButton.setOnAction(event -> {
            DcTextField titleField = new DcTextField(true, "");
            DialogConstructor.constructDialog(() -> {
                Core.getInstance().structureService().createParam(titleField.getValue());
                fillPane();
            }, titleField);
        });
    }

    private void fillPane() {
        paramPane.getChildren().clear();

        for(Param param: Core.getInstance().structureService().getParams()) {
            Label label = new Label(param.getTitle());
            label.setStyle(Styler.getTextSizeStyle(25));
            label.setContextMenu(getContextMenu(param));
            paramPane.getChildren().add(label);

            for (ParamValue paramValue: param.getValues()) {
                Label label1 = new Label(paramValue.getTitle());
                label1.setStyle(Styler.getTextSizeStyle(15));
                label1.setContextMenu(getContextMenu(paramValue));
                paramPane.getChildren().add(label1);
            }

            paramPane.getChildren().add(getPaneWithHeight(25));
        }
    }

    private ContextMenu getContextMenu(Param param) {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem addValueItem = new MenuItem("Add value");
        addValueItem.setOnAction(event -> {
            DcTextField titleField = new DcTextField(true, "");
            DialogConstructor.constructDialog(() -> {
                Core.getInstance().structureService().createParamValue(titleField.getValue(), param);
                fillPane();
            }, titleField);
        });

        MenuItem editItem = new MenuItem("Edit");
        editItem.setOnAction(event -> {
            DcTextField titleField = new DcTextField(true, param.getTitle());
            DialogConstructor.constructDialog(() -> {
                Core.getInstance().structureService().edit(param, titleField.getValue());
                fillPane();
            }, titleField);
        });

        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(event -> new StandardDialogBuilder().openQuestionDialog("Delete?", b -> {
            if (b) {
                Core.getInstance().structureService().delete(param);
                fillPane();
            }
        }));

        contextMenu.getItems().add(addValueItem);
        contextMenu.getItems().add(editItem);
        contextMenu.getItems().add(deleteItem);
        return contextMenu;
    }

    private ContextMenu getContextMenu(ParamValue paramValue) {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem editItem = new MenuItem("Edit");
        editItem.setOnAction(event -> {
            DcTextField titleField = new DcTextField(true, paramValue.getTitle());
            DialogConstructor.constructDialog(() -> {
                Core.getInstance().structureService().edit(paramValue, titleField.getValue());
                fillPane();
            }, titleField);
        });

        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(event -> new StandardDialogBuilder().openQuestionDialog("Delete?", b -> {
            if (b) {
                Core.getInstance().structureService().delete(paramValue);
                fillPane();
            }
        }));

        contextMenu.getItems().add(editItem);
        contextMenu.getItems().add(deleteItem);
        return contextMenu;
    }
}
