/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.harkaitz.hrkono.game;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

/**
 *
 * @author harkaitz
 */
public class Command extends Button {
    public String icon;
    public String text;
    public String command;
    public Board  board;
    public Command(Board board,String text,String command) {
        super(text);
        this.icon    = command;
        this.text    = text;
        this.command = command;
        this.board   = board;
        this.setOnAction((ActionEvent event) -> {
            try {
                this.board.collectCommand(command);
            } catch (Exception ex) {
                Logger.getLogger(Command.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
    }
    @Override
    public String toString() {
        return command;
    }
}
