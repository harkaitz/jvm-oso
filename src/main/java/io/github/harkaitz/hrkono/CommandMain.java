package io.github.harkaitz.hrkono;

import io.github.harkaitz.hrkono.game.Move;
import io.github.harkaitz.hrkono.game.ono.OnoBoard;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandMain extends GenericShell {

    OnoBoard game; 
    public CommandMain() throws Exception {
        game = new OnoBoard(8,8,4);
    }
    
    @Override
    protected String overrideAddHelp() {
        String r =
                "click <coor>     : Select coordinate.\n"+
                "print            : Print game status.\n"+
                "options          : Print available operations.\n"+
                "game [VAL]       : Start a new game.\n"+
                "addplayer NUM NAME : Add player.\n";
        for(Move m : game.getMoves()) {
            r += m.toString()+" ";
        }
        r+="\n";
        return r;
    }

    @Override
    protected boolean overrideExecute(String[] str) throws Exception {
        if(str[0].equalsIgnoreCase("print")) {
            print(1,game);
            return true;
        }
        if(str[0].equalsIgnoreCase("addplayer")) {
            game.setPlayer(Integer.parseInt(str[1]),str[2]);
            return true;
        }
        Move m = game.getMove(str[0]);
        if(m != null) {
            game.runMove(m);
            return true;
        }
        return false;
    }
    public static void main(String [] args) {
        CommandMain s;
        try {
            s = new CommandMain();
            s.run();
        } catch (Exception ex) {
            Logger.getLogger(CommandMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
