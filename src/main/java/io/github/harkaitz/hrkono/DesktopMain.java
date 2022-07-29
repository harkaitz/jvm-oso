/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.harkaitz.hrkono;

import com.sun.javafx.PlatformUtil;
import java.io.IOException;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 *
 * @author harkaitz
 */
public class DesktopMain extends Application {
    Gui gui;
    @Override
    public void start(Stage stage) throws IOException  {
        BorderPane pane = new BorderPane();
        gui = new Gui(pane);
        Scene scene = new Scene(pane);
        stage.setTitle("HrkOno");
        stage.setScene(scene);
        if(PlatformUtil.isAndroid() || PlatformUtil.isIOS()) {
            Rectangle2D sb = Screen.getPrimary().getVisualBounds();
            pane.setPrefWidth(sb.getWidth());
            pane.setPrefHeight(sb.getHeight());
        }
        stage.show();
    }
    static public void main(String [] args) { 
        launch();
    }
}
