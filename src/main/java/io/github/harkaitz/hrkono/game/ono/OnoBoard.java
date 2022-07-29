/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.harkaitz.hrkono.game.ono;

import io.github.harkaitz.hrkono.game.Board;
import io.github.harkaitz.hrkono.game.Command;
import io.github.harkaitz.hrkono.game.Move;
import io.github.harkaitz.hrkono.game.Piece;

/**
 *
 * @author harkaitz
 */
public class OnoBoard extends Board {

    public OnoBoard(int max_x, int max_y,int maxPlayers) throws Exception {
        super(max_x, max_y, 1,maxPlayers);
        this.addPlayer("p1");
        this.addPlayer("p2");
    }
    
    @Override
    protected void overrideAddMoves() {
        for(int x=1;x<=max_x;x++) {
            for(int y=1;y<=max_y;y++) {
                if(this.get(x, y).isEmpty()) {
                    add(new PutMove(x,y,'o',turn));
                    add(new PutMove(x,y,'n',turn));
                }
            }
        }
    }
    static int [][] Ono = {
        {1, 1,0},{0, 1,0},{-1, 1,0},
        {1, 0,0},         {-1, 0,0},
        {1,-1,0},{0,-1,0},{-1,-1,0}
    };
    static int [][] onO = {
        {-1,-1,0},{0,-1,0},{1,-1,0},
        {-1, 0,0},         {1, 0,0},
        {-1, 1,0},{0, 1,0},{1, 1,0}
    };
    static int [][] oNo = {
        {1, 1,0},{0, 1,0},{-1, 1,0},
        {1, 0,0},         {-1, 0,0},
        {1,-1,0},{0,-1,0},{-1,-1,0}
    };
    static int [][] oNO = {
        {2, 2,0},{0, 2,0},{-2, 2,0},
        {2, 0,0},         {-2, 0,0},
        {2,-2,0},{0,-2,0},{-2,-2,0}
    };

    
    
    
    class PutMove extends Move {
        int x,y; char id;
        
        PutMove(int x,int y,char id,int player) {
            this.x = x; this.y = y; this.id = id;
            this.player = player;
        }
        @Override
        public void Operate(Board b) throws Exception {
            Piece p0 = new Piece(id,this.player);
            b.set(x, y, p0);
            if(id=='n') {
                int [][] pOno = Board.diffs(x,y,1,Ono);
                int [][] ponO = Board.diffs(x,y,1,onO);
                Integer toAdd = 0;
                for(int i=0;i<pOno.length;i++) {
                    Piece p1=b.get(pOno[i][0],pOno[i][1]);
                    Piece p2=b.get(ponO[i][0],ponO[i][1]);
                    if(p1.id!='o' || p2.id!='o') continue;
                    p1.player = -1; p2.player = -1; p0.player = -1;
                    toAdd++;
                }
                b.addPoints(player,toAdd/2);
            }
            if(id=='o') {
                int [][] poNo = Board.diffs(x,y,1,oNo);
                int [][] poNO = Board.diffs(x,y,1,oNO);
                for(int i=0;i<poNo.length;i++) {
                    Piece p1=b.get(poNo[i][0],poNo[i][1]);
                    Piece p2=b.get(poNO[i][0],poNO[i][1]);
                    if(p1.id!='n'||p2.id!='o') continue;
                    p1.player = -1; p2.player = -1; p0.player = -1;
                    b.addPoints(player,1);
                }
            }
            b.nextTurn();
        }
        @Override
        public String toString() {
            return Integer.toString(player)+"-"+id+"-"+Integer.toString(x)+","+Integer.toString(y);
        }
    }
    @Override
    protected Command[] overrideGetCommands() {
        Command [] cmds = {new Command(this,"o","o"),new Command(this,"n","n")};
        return cmds;
    }
}
