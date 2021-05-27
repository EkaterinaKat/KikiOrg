package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Huddle;
import com.katyshevtseva.kikiorg.view.utils.OrgUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.*;
import java.util.stream.Collectors;

import static com.katyshevtseva.fx.FxUtils.associateButtonWithControls;
import static com.katyshevtseva.fx.FxUtils.disableNonNumericChars;

class HuddleCheckController implements FxController {
    @FXML
    private ComboBox<Huddle> comboBox;
    @FXML
    private Button addButton;
    @FXML
    private VBox accountsPane;
    @FXML
    private Button checkButton;
    @FXML
    private Label resultLabel;
    @FXML
    private TextField amountTextField;
    private Map<Account, CheckBox> accountCheckBoxMap;

    @FXML
    private void initialize() {
        disableNonNumericChars(amountTextField);
        setComboBoxItems();
        fillGridPane();
        associateButtonWithControls(checkButton, comboBox);
        addButton.setOnAction(event -> OrgUtils.getDialogBuilder().openTextFieldDialog("", s -> {
            Core.getInstance().huddleCheckService().createHuddle(s);
            emptyAllCheckBoxes();
            resultLabel.setText("");
            setComboBoxItems();
        }));
        comboBox.setOnAction(event -> {
            setSelectedAccountsBySelectedHuddle();
            amountTextField.setText(comboBox.getValue().getAmount().toString());
            resultLabel.setText("");
        });
        checkButton.setOnAction(event -> resultLabel.setText(Core.getInstance().huddleCheckService()
                .checkAndRewriteHuddleInfo(comboBox.getValue(), Long.parseLong(amountTextField.getText()), getAllSelectedAccounts())));
    }

    private void fillGridPane() {
        accountCheckBoxMap = new HashMap<>();
        List<Account> accounts = Core.getInstance().financeService().getAllAccounts()
                .stream().sorted(Comparator.comparing(Account::getTitle)).collect(Collectors.toList());
        for (Account account : accounts) {
            CheckBox checkBox = new CheckBox(account.toString());
            accountCheckBoxMap.put(account, checkBox);
            accountsPane.getChildren().addAll(checkBox, FxUtils.getPaneWithHeight(10));
        }
    }

    private void setComboBoxItems() {
        FxUtils.setComboBoxItems(comboBox, Core.getInstance().huddleCheckService().getAllHuddles());
    }

    private void emptyAllCheckBoxes() {
        for (CheckBox checkBox : accountCheckBoxMap.values())
            checkBox.setSelected(false);
    }

    private List<Account> getAllSelectedAccounts() {
        List<Account> accounts = new ArrayList<>();
        for (Map.Entry<Account, CheckBox> entry : accountCheckBoxMap.entrySet())
            if (entry.getValue().isSelected())
                accounts.add(entry.getKey());
        return accounts;
    }

    private void setSelectedAccountsBySelectedHuddle() {
        for (Map.Entry<Account, CheckBox> entry : accountCheckBoxMap.entrySet())
            entry.getValue().setSelected(comboBox.getValue().getAccounts().contains(entry.getKey()));
    }
}

