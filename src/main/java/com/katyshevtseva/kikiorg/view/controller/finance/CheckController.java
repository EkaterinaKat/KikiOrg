package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.katyshevtseva.fx.Styler.ThingToColor.*;
import static com.katyshevtseva.fx.Styler.getColorfullStyle;

class CheckController implements FxController {
    private List<Account> oldAccountList;
    private final Map<Account, Boolean> accountCheckedMap = new HashMap<>();
    @FXML
    private VBox root;

    @FXML
    private void initialize() {
        updateTable();
    }

    void updateTable() {
        root.getChildren().clear();
        List<Account> newAccountList = Core.getInstance().financeService.getActiveAccounts();

        for (Account account : newAccountList) {
            Node node = accountToNode(account);
            setChecked(account, node, isChecked(account));
            root.getChildren().addAll(node, FxUtils.getPaneWithHeight(10));
        }

        oldAccountList = newAccountList;
    }

    private boolean isChecked(Account account) {
        if (oldAccountList == null)
            return false;

        boolean prevValue = accountCheckedMap.getOrDefault(account, false);
        if (!prevValue)
            return false;

        for (Account oldAcc : oldAccountList) {
            if (oldAcc.equals(account))
                return oldAcc.getAmount() == account.getAmount();
        }
        return false;
    }

    private Node accountToNode(Account account) {
        Label label = new Label(account.getTitleWithAdditionalInfo() + " " + account.getAmount());
        label.setPadding(new Insets(10));
        label.setOnMouseClicked(event -> setChecked(account, label, !accountCheckedMap.get(account)));
        return label;
    }

    private void setChecked(Account account, Node node, boolean checked) {
        setStyle(node, checked);
        accountCheckedMap.put(account, checked);
    }

    private void setStyle(Node node, boolean green) {
        node.setStyle(green ?
                getColorfullStyle(BACKGROUND, "#98FF98") +
                        getColorfullStyle(TEXT, "#1A7500")
                        + getColorfullStyle(BORDER, "#1A7500") :
                getColorfullStyle(BACKGROUND, "#993500") +
                        getColorfullStyle(TEXT, "#FFB28A")
                        + getColorfullStyle(BORDER, "#993500"));
    }
}
