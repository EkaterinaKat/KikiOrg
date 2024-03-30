package com.katyshevtseva.kikiorg.view.controller.study;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.time.TimeUtil;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import lombok.Getter;

public class TimeNode {
    private final TextField minTF;
    private final TextField hourTF;
    @Getter
    private final Node node;

    public TimeNode() {
        minTF = getTimeTF();
        hourTF = getTimeTF();

        HBox hBox = new HBox();
        hBox.getChildren().addAll(hourTF, FxUtils.getPaneWithWidth(10), minTF);
        node = hBox;
    }

    private TextField getTimeTF() {
        TextField tf = new TextField();
        FxUtils.disableNonNumericChars(tf);
        FxUtils.setWidth(tf, 70);
        tf.setText("0");
        return tf;
    }

    public int getTotalMin() {
        System.out.println(TimeUtil.getTotalMin(hourTF, minTF));
        return TimeUtil.getTotalMin(hourTF, minTF);
    }

    public void setTotalMin(int minutes) {
        System.out.println(minutes);
        TimeUtil.setTime(minutes, hourTF, minTF);
    }
}
