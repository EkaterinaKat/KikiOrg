package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceOperationService.OperationType;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceSearchService;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceSearchService.SearchRequest;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceService;
import com.katyshevtseva.kikiorg.view.controller.finance.HistoryController.TableUpdateKnob;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import static com.katyshevtseva.fx.FxUtils.associateButtonWithControls;
import static com.katyshevtseva.fx.FxUtils.setComboBoxItems;

class SearchController implements FxController {
    private final FinanceService financeService = Core.getInstance().financeService();
    private final FinanceSearchService searchService = Core.getInstance().financeSearchService();
    private TableUpdateKnob tableUpdateKnob;
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

    SearchController(TableUpdateKnob tableUpdateKnob) {
        this.tableUpdateKnob = tableUpdateKnob;
    }

    @FXML
    private void initialize() {
        searchButton.setOnAction(event -> search());
        associateButtonWithControls(searchButton, typeComboBox);

        setComboBoxItems(typeComboBox, OperationType.values());
    }

    private void search() {
        SearchRequest request = new SearchRequest();
        request.setOperationType(typeComboBox.getValue());
        tableUpdateKnob.execute(searchService.search(request));
    }
}
