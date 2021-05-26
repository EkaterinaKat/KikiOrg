package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.fx.component.ComponentBuilder.Component;
import com.katyshevtseva.fx.component.MultipleChoiceController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceOperationService.OperationType;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceSearchService;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import static com.katyshevtseva.fx.FxUtils.associateButtonWithControls;
import static com.katyshevtseva.fx.FxUtils.setComboBoxItems;
import static com.katyshevtseva.kikiorg.view.utils.OrgUtils.getComponentBuilder;

class SearchController implements FxController {
    private final FinanceService financeService = Core.getInstance().financeService();
    private final FinanceSearchService searchService = Core.getInstance().financeSearchService();
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
    private void initialize() {

    }
}
