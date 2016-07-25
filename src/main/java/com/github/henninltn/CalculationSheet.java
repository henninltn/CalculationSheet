package com.github.henninltn;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFXアプリケーションのエントリーポイント
 */
public class CalculationSheet extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("sheet/sheet.fxml"));
            primaryStage.setTitle("Calculation Sheet");
            primaryStage.setScene(new Scene(root, 400, 600));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
