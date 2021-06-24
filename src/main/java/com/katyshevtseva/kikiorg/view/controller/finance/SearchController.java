package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.Size;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.fx.component.ComponentBuilder.Component;
import com.katyshevtseva.fx.component.MultipleChoiceController;
import com.katyshevtseva.general.OneArgKnob;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceOperationService.Operation;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceOperationService.OperationType;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceSearchService;
import com.katyshevtseva.kikiorg.core.sections.finance.OperationEnd;
import com.katyshevtseva.kikiorg.core.sections.finance.SearchRequest;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.util.List;

import static com.katyshevtseva.fx.FxUtils.associateButtonWithControls;
import static com.katyshevtseva.fx.FxUtils.setComboBoxItems;
import static com.katyshevtseva.kikiorg.view.utils.OrgUtils.getComponentBuilder;

class SearchController implements FxController {
    private final Size fromAndToComponentSize = new Size(150, 240);
    private final FinanceSearchService searchService = Core.getInstance().financeSearchService();
    private OneArgKnob<List<Operation>> tableUpdateKnob;
    private MultipleChoiceController<OperationEnd> fromController;
    private MultipleChoiceController<OperationEnd> toController;
    @FXML
    private ComboBox<OperationType> typeComboBox;
    @FXML
    private TextField minTextField;
    @FXML
    private TextField maxTextField;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private Pane fromPane;
    @FXML
    private Pane toPane;
    @FXML
    private Button searchButton;
    @FXML
    private Button clearDatesButton;

    SearchController(OneArgKnob<List<Operation>> tableUpdateKnob) {
        this.tableUpdateKnob = tableUpdateKnob;
    }

    @FXML
    private void initialize() {
        searchButton.setOnAction(event -> search());
        associateButtonWithControls(searchButton, typeComboBox);
        clearDatesButton.setOnAction(event -> {
            startDatePicker.setValue(null);
            endDatePicker.setValue(null);
        });
        typeComboBox.setOnAction(event -> adjustFromAndToPanes());
        setComboBoxItems(typeComboBox, OperationType.values());
        FxUtils.disableNonNumericChars(minTextField);
        FxUtils.disableNonNumericChars(maxTextField);
    }

    private void search() {
        SearchRequest request = new SearchRequest();
        request.setOperationType(typeComboBox.getValue());
        request.setMinAmount(minTextField.getText());
        request.setMaxAmount(maxTextField.getText());
        request.setStart(startDatePicker.getValue());
        request.setEnd(endDatePicker.getValue());
        request.setFrom(fromController.getSelectedItems(), fromController.getAllItems());
        request.setTo(toController.getSelectedItems(), toController.getAllItems());
        tableUpdateKnob.execute(searchService.search(request));
    }

    private void adjustFromAndToPanes() {
        fromPane.getChildren().clear();
        toPane.getChildren().clear();

        Component<MultipleChoiceController<OperationEnd>> fromComponent = getComponentBuilder().setSize(fromAndToComponentSize)
                .getMultipleChoiceComponent(searchService.getFrom(typeComboBox.getValue()));
        Component<MultipleChoiceController<OperationEnd>> toComponent = getComponentBuilder().setSize(fromAndToComponentSize)
                .getMultipleChoiceComponent(searchService.getTo(typeComboBox.getValue()));

        fromController = fromComponent.getController();
        toController = toComponent.getController();

        fromPane.getChildren().add(fromComponent.getNode());
        toPane.getChildren().add(toComponent.getNode());
    }
}
