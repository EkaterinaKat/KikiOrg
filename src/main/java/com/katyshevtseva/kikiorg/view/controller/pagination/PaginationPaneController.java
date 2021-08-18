package com.katyshevtseva.kikiorg.view.controller.pagination;

import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.general.OneArgKnob;
import com.katyshevtseva.general.Page;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.List;

public class PaginationPaneController<T> implements FxController {
    private final PageSource<T> pageSource;
    private final OneArgKnob<List<T>> contentReceiver;
    private int pageNum = 0;
    private int totalPages = 0;
    @FXML
    private Button prevButton;
    @FXML
    private Button nextButton;
    @FXML
    private Label label;

    @FXML
    private void initialize() {
        loadPage();

        prevButton.setOnAction(event -> {
            pageNum--;
            loadPage();
        });

        nextButton.setOnAction(event -> {
            pageNum++;
            loadPage();
        });
    }

    public PaginationPaneController(PageSource<T> pageSource, OneArgKnob<List<T>> contentReceiver) {
        this.pageSource = pageSource;
        this.contentReceiver = contentReceiver;
    }

    public void loadPage() {
        Page<T> page = pageSource.getPage(pageNum);
        contentReceiver.execute(page.getContent());
        totalPages = page.getTotalPages();
        label.setText((pageNum + 1) + "/" + page.getTotalPages());
        setButtonAbility();
    }

    private void setButtonAbility() {
        prevButton.setDisable(pageNum == 0);
        nextButton.setDisable((pageNum + 1) == totalPages);
    }

    @FunctionalInterface
    public interface PageSource<T> {
        Page<T> getPage(int pageNum);
    }
}
