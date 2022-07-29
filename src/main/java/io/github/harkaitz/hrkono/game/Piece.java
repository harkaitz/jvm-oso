/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.harkaitz.hrkono.game;

/**
 *
 * @author harkaitz
 */
public class Piece {
    public char id     = ' ';
    public int  player =   0;
    public Piece()        { }
    public Piece(char id,int player) {this.id = id; this.player = player; }
    public boolean isEmpty() { return id == ' '; }
}
