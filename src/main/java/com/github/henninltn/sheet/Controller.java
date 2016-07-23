package com.github.henninltn.sheet;

import com.github.henninltn.Evaluator;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Map;
import java.util.HashMap;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

/**
 * メインシーンのコントローラ
 */
public class Controller implements Initializable {

    @FXML
    Label outputArea;
    @FXML
    TextArea inputArea;

    private boolean onShift,
                    onEnter;

    private int inputCount,
                outputCount;

    private Map<String, Integer> globalScope;
    private Evaluator evaluator;

    /**
     * コンストラクタに代わり、モデルの初期化とイベントの登録を行う
     * @param location
     * @param resources
     */
    public void initialize(URL location, ResourceBundle resources) {
        onShift = false;
        onEnter = false;

        inputCount = 0;
        outputCount = 0;

        globalScope = new HashMap<>();
        evaluator = new Evaluator(globalScope);

        inputArea.setOnKeyPressed((event) -> {
            switch (event.getCode()) {
                case SHIFT:
                    onShift = true;
                    break;
                case ENTER:
                    onEnter = true;
            }
            if (onShift && onEnter) {
                String input = inputArea.getText().replace("\n", "");
                String output = evaluator.eval(input);
                outputArea.setText(outputArea.getText() +
                        "i[" + inputCount + "] = " + input + "\n" +
                        "o[" + outputCount + "] = " + output + "\n");
                inputCount++;
                outputCount++;
                inputArea.setText("");
            }
        });

        inputArea.setOnKeyReleased((event) -> {
            switch (event.getCode()) {
                case SHIFT:
                    onShift = false;
                    break;
                case ENTER:
                    onEnter = false;
            }
        });
    }
}
