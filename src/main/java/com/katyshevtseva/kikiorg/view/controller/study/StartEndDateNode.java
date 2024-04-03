package com.katyshevtseva.kikiorg.view.controller.study;

import com.katyshevtseva.date.DateUtils;
import com.katyshevtseva.date.Period;
import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.LabelBuilder;
import com.katyshevtseva.general.NoArgsKnob;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Getter;

import java.util.Date;

@Getter
public class StartEndDateNode {
    private final DatePicker startDP = new DatePicker();
    private final DatePicker endDP = new DatePicker();
    private final VBox node = new VBox();

    public StartEndDateNode(NoArgsKnob onDateChangeListener, Date startDate, Date endDate) {
        FxUtils.setDate(startDP, startDate);
        FxUtils.setDate(endDP, endDate);
        startDP.setOnAction(event -> onDateChangeListener.execute());
        endDP.setOnAction(event -> onDateChangeListener.execute());

        Node startNode = getDpNode(startDP, "start");
        Node endNode = getDpNode(endDP, "end");

        node.getChildren().addAll(endNode, startNode);
    }

    private Node getDpNode(DatePicker dp, String text) {
        Button upBtn = new Button("▲");
        FxUtils.setHeight(upBtn, 15);
        upBtn.setOnAction(event -> {
            Date newDate = DateUtils.shiftDate(FxUtils.getDate(dp), DateUtils.TimeUnit.DAY, 10);
            FxUtils.setDate(dp, newDate);
        });
        Button downBtn = new Button("▼");
        FxUtils.setHeight(downBtn, 15);
        downBtn.setOnAction(event -> {
            Date newDate = DateUtils.shiftDate(FxUtils.getDate(dp), DateUtils.TimeUnit.DAY, -10);
            FxUtils.setDate(dp, newDate);
        });

        return new HBox(
                new LabelBuilder().text(text).minWidth(40).build(),
                dp,
                new VBox(upBtn, downBtn));
    }

    public Period getPeriod() {
        return FxUtils.getPeriod(startDP, endDP);
    }


}
