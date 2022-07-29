/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.harkaitz.hrkono;

import java.io.IOException;
import java.io.InputStream;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author harkaitz
 */
public class Gui extends GuiController {
    GuiController gui;
    Gui(BorderPane pane) throws IOException {
        gui = GuiController.load(pane);
    }
}
