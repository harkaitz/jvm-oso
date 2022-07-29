/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.harkaitz.hrkono.game;

import static java.lang.Math.floor;
import static java.lang.Math.round;
import java.util.ArrayList;
import java.util.HashSet;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;


/**
 *
 * @author harkaitz
 */
public abstract class Board {
    public final int max_x,max_y,max_z;
    private final Piece [][][] pieces;
    public Integer [] players;
    protected int turn;
    HashSet<Move> moves;
    String        message = "";
    public Board(int max_x,int max_y,int max_z,int maxPlayers) {
        this.max_x  = max_x;
        this.max_y  = max_y;
        this.max_z  = max_z;
        this.pieces = new Piece[max_x][max_y][max_z];
        this.players = new Integer[maxPlayers];
        for(int x=0;x<max_x;x++) {
            for(int y=0;y<max_y;y++) {
                for(int z=0;z<max_z;z++) {
                    this.pieces[x][y][z] = new Piece();
                }
            }
        }
        this.turn = 0;
        this.updateMoves(); /* this.moves */
        
    }
    
    /* ------------- Message --------------------------------------- */
    public void setMessage(String m) { message = m; }
    public String getMessage() { return message; }
    public String getStats() {
        String r = "";
        for(int p=0;p<players.length;p++) {
            if(players[p]!=null) {
                r += String.format("Player: %02d %d points\n",p+1,players[p]);
            }
        }
        r+=String.format("Turn: %d\n",turn);
        r+="\n";
        return r;
    }
    
    
    /* ------------- Piece Management ------------------------------ */
    public Piece    get(int x,int y,int z) {
        if(!inBounds(x,y,z)) return new Piece();
        return pieces[x-1][y-1][z-1];
    }
    public Piece    set(int x,int y,int z,Piece p) {
        if(!inBounds(x,y,z)) return p;
        return (pieces[x-1][y-1][z-1] = p);
    }
    public Piece    pop(int x,int y,int z) {
        Piece p = get(x,y,z);
        if(p.isEmpty()) { return p; }
        set(x,y,z,new Piece());
        return p;
    }
    public Piece    get(int x,int y)          { return get(x,y,1);   }
    public Piece    set(int x,int y,Piece p)  { return set(x,y,1,p); }
    public Piece    pop(int x,int y)          { return pop(x,y,1);   }
    public void     add(Move move) { moves.add(move); }
    
    
    /* -------------- PLAYER MANAGEMENT --------------------- */
    public int     nextTurn() throws Exception {
        for(int t=turn+1;true;t++) {
            if(t>=players.length) { t=0; }
            if(t==turn) {
                if(players[t]==null) {
                    throw new Exception("No players.");
                }
                return turn;
            }
            if(players[t]!=null) {
                turn = t;
                updateMoves();
                return turn;
            }
        }
    }
    public int     getTurn() throws Exception {
        return (players[turn]==null)?nextTurn():turn; 
    }
    public void    setPlayer(int num,String user) throws Exception {
        if(num>=players.length || num<0) {
            throw new Exception("Invalid user number.");
        }
        players[num] = 0;
        getTurn(); /* Select the new user if the current is null. */
        updateMoves(); /* Update moves, a new user is in town. */
    }
    public void    addPlayer(String user) throws Exception {
        for(int i=0;i<players.length;i++) {
            if(players[i] == null) {
                setPlayer(i,user);
                return;
            }
        }
        throw new Exception("Maximun players reached.");
    }
    public void    delPlayer() throws Exception {
        players[turn] = null;
        getTurn();
    }
    public Integer getPoints(int num) { 
        try {
            return players[num];
        } catch(Exception e) {
            return null;
        }
    }
    public void    setPoints(int num,int count) {
        if(getPoints(num) != null) { 
            players[num] = count; 
        }
    }
    public void    addPoints(int num,int count) {
        if(getPoints(num) != null) { 
            players[num] += count; 
        }
    }
    public int     getPlayerCount() {
        int c = 0;
        for(int i=0;i<players.length;i++) {
            if(players[i]!=null) c++;
        }
        return c;
    }
    public static String  getPlayerColor(int num) {
        switch(num%2) {
            case     0: return "white";
            case     1: return "black";
            case    -1: return "#ffbf00"; /* GOLD. */
            default   : return "black"; /* This is imposible. */
        }
    }
    
    /* -------------- Move management ----------------- */
    public final HashSet<Move> getMoves() { return moves; }
    public final void runMove(Move m) throws Exception { 
        System.err.println(m);
        m.Operate(this);
        updateMoves();
    }
    public final void updateMoves() {
        this.moves  = new HashSet();
        if(getPoints(turn) != null) { /* Do not update if slot empty. */
            overrideAddMoves();
            if(isGameOver()) {
                setMessage("Player "+Integer.toString(whoIsWinning()+1)+" wins!!");
            }
        }
    }
    public final Move getMove(String move) {
        for(Move m : moves) {
            if(m.toString().equalsIgnoreCase(move)) {
                return m;
            }
        }
        return null;
    }
    public final Command []getCommands() { 
        return overrideGetCommands();
    }
    protected abstract void      overrideAddMoves();
    protected abstract Command []overrideGetCommands();
    String collected = "";
    public boolean collectCommand(String str) throws Exception {
        if(collected.length()==0) {
            collected = getTurn()+"-"+str;
        } else {
            collected += "-"+str;
        }
        System.err.println(collected);
        /* Execute if move exists. */
        Move move = getMove(collected);
        if(move!=null) {
            runMove(move);
            collected = "";
            return false;
        }
        /* Return true if the command continues. */
        for(Move m : moves) {
            if(m.toString().indexOf(collected)==0) {
                return true;
            }
        }
        collected = "";
        return false;
    }
    public final boolean isGameOver() {
        if(players.length==0) {
            return false;
        } else {
            return moves.isEmpty();
        }
    }
    public final int whoIsWinning() {
        Integer points = -99999; int p = -1;
        for(int pp=0;pp<players.length;pp++) {
            if(players[pp]==null) {
                
            } else if(points==null || players[pp]>points) {
                p = pp; points = players[pp];
            }
        }
        return p;
    }
    
    
    
    /* --------------- String ----------------------------------- */
    @Override
    public String toString() {
        String r = "== BOARD =====================\n";
        r = getStats();
        for(int y=1;y<=max_x;y++) {
            r += String.format("%2d|",y);
            for(int x=1;x<=max_x;x++) {
                r += String.format("%c|",get(x,y).id);
            }
            r += "\n";
        }
        r += "  :";
        for(int x=1;x<=max_x;x++) {
            r+=String.format("%d:",x);
        }
        r+= "\n";
        r+= "Moves:";
        for(Move m : getMoves()) {
            r+=" "+m.toString();
        }
        r+= "\n";
                
        return r;
    }
    
    
    /* ------------------ MATH -------------------------------- */
    public static int [][] diffs(int x,int y,int z,int diffs[][]) {
        int [][] rets = new int[diffs.length][3];
        for(int i=0;i<diffs.length;i++) {
            rets[i][0] = x+diffs[i][0];
            rets[i][1] = y+diffs[i][1];
            rets[i][2] = z+diffs[i][2];
        }
        return rets;
    }
    public boolean inBounds(int x,int y,int z) {
        if(x>max_x || x<1) return false;
        if(y>max_y || y<1) return false;
        if(z>max_z || z<1) return false;
        return true;
    }
    public boolean inBounds(int [] p) {
        return inBounds(p[0],p[1],p[2]);
    }
    public int [] percentToPoint(double dx,double dy,double dz) {
        int [] r={
            (int)floor(1+(dx*max_x)),
            (int)floor(1+(dy*max_y)),
            (int)floor(1+(dz*max_z))};
        return r;
        
    }
    
    /* --------------------------------------------------------------- */
    public void paint(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Paint oddColor  = Paint.valueOf("#c96527");
        Paint evenColor = Paint.valueOf("#fcdfb3");
        double boxSize = canvas.getWidth()/max_x;
        Font font = new Font(boxSize);
        gc.setFont(font);
        for(int x=0;x<max_x;x++) {
            for(int y=0;y<max_y;y++) {
                Paint color = ((x+y)%2==0)?oddColor:evenColor;
                gc.setFill(color);
                gc.fillRect(x*boxSize,y*boxSize,boxSize,boxSize);
                Piece p = this.get(x+1, y+1);
                if(!p.isEmpty()) {
                    gc.setFill(Paint.valueOf(getPlayerColor(p.player)));
                    gc.fillText(Character.toString(p.id),(x+0.2)*boxSize,(y+1-0.2)*boxSize, boxSize);
                }
            }
        }
    }

    
}
