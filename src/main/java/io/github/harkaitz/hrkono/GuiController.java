/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.harkaitz.hrkono;

import io.github.harkaitz.hrkono.game.Board;
import io.github.harkaitz.hrkono.game.Command;
import io.github.harkaitz.hrkono.game.ono.BoardManager;
import io.github.harkaitz.hrkono.game.ono.OnoBoard;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

/**
 * FXML Controller class
 *
 * @author harkaitz
 */
public class GuiController implements Initializable {

    Board  game         = null;
    @FXML
    private BorderPane pane;

    /**
     * Initializes the controller class.
     */
    
    @FXML
    private TextArea log;
    @FXML
    private Canvas canvas;
    @FXML
    private VBox canvasBox;
    @FXML
    private ToolBar commandToolbar;
    @FXML
    private Label numPLayers;
    @FXML
    private Menu newButton;
    @FXML
    private Circle circle;
    @FXML
    private Label whoMovesText;
    @FXML
    private MenuButton pointsButton;
    @FXML
    private Label stats;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            game = BoardManager.newBoard();
        } catch (Exception ex) {
            error(ex);
        }
        log.widthProperty().addListener((obs, oldVal, newVal) -> {
            update();
        });
        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED,
	      (MouseEvent e) -> {
                  try {
                      int [] xyz = game.percentToPoint
                        (e.getX()/canvas.getWidth(),
                         e.getY()/canvas.getHeight(),0);
                      game.collectCommand(xyz[0]+","+xyz[1]);
                      update();
                  } catch (Exception ex) {
                      error(ex);
                  }
              }
	  );
        for(String type : BoardManager.listBoardTypes()) {
            MenuItem item = new MenuItem(type);
            item.setOnAction((ActionEvent event) -> {
                try {
                    game = BoardManager.newBoard(type);
                } catch (Exception ex) {
                    error(ex);
                }
                if(event!=null) { update(); }
            });
            newButton.getItems().add(item);
        }
        update();
    }
    public void update() {
        /* Update players button. */
        
        numPLayers.setText(Integer.toString(game.getPlayerCount()));
        try {
            int turn = game.getTurn();
            circle.setFill(Paint.valueOf(Board.getPlayerColor(turn)));
            whoMovesText.setText(
                    "Player " +Integer.toString(turn+1)+" moves:");
            pointsButton.getItems().clear();
            for(int pn=0;pn<game.players.length;pn++) {
                if(game.players[pn]==null) continue;
                MenuItem item = new MenuItem(
                        ((pn==turn)?"* ":"  ")+
                        "Player " +Integer.toString(pn+1)+
                        " points "+Integer.toString(game.getPoints(pn))
                );
                pointsButton.getItems().add(item);
            }
        } catch (Exception ex) {
            error(ex);
        }
        canvas.setWidth(log.getWidth()-20);
        canvas.setHeight(log.getWidth()-20);
        commandToolbar.getItems().clear();
        for(Command cmd : game.getCommands()) {
            commandToolbar.getItems().add(cmd);
        }
        game.paint(canvas);
        log.setText(game.getMessage());
        stats.setText(game.getStats());
    }
    public void error(Exception err) {
        log.setText(err.getMessage());
        err.printStackTrace();
    }
    public static GuiController load(BorderPane iPane) throws IOException {
        FXMLLoader r = new FXMLLoader();
        InputStream is = GuiController.class.getClassLoader()
                .getResourceAsStream("io/github/harkaitz/hrkono/Gui.fxml");
        r.load(is);
        GuiController c = r.getController();
        iPane.setCenter(c.pane.getCenter());
        iPane.setTop(c.pane.getTop());
        iPane.setBottom(c.pane.getBottom());
        return (GuiController) c;
    }

    private void playerButtonPress(ActionEvent event) {
        update();
    }

    @FXML
    private void addPlayer(ActionEvent event) {
        try {
            game.addPlayer("local");
        } catch (Exception ex) {
            error(ex);
        }
        update();
    }

    @FXML
    private void delPlayer(ActionEvent event) {
        try {
            game.delPlayer();
        } catch (Exception ex) {
            error(ex);
        }
        update();
    }

    @FXML
    private void nextPlayer(ActionEvent event) {
        try {
            game.nextTurn();
        } catch (Exception ex) {
            error(ex);
        }
        update();
    }
    

    
}
