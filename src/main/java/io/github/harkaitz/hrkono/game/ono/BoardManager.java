/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.harkaitz.hrkono.game.ono;

import io.github.harkaitz.hrkono.game.Board;
import io.github.harkaitz.hrkono.game.ono.OnoBoard;
import java.util.ArrayList;

/**
 *
 * @author harkaitz
 */
public class BoardManager {
    static public String [] listBoardTypes() {
        String [] str = {
            "ono8x8","ono4x4"
        };
        return str;
    }
    static public Board newBoard(String type) throws Exception {
        switch(type) {
            case "ono8x8":
                return new OnoBoard(8,8,10);
            case "ono4x4":
                return new OnoBoard(4,4,10);
            default:
                throw new Exception("Game type not found.");
        }
    }
    static public Board newBoard() throws Exception {
        return newBoard("ono4x4");
    }
}
