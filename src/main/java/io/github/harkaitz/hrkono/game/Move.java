/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.harkaitz.hrkono.game;

import java.util.Objects;

/**
 *
 * @author harkaitz
 */
public abstract class Move {
    public int player = 0;
    public abstract void Operate(Board b) throws Exception;

    @Override
    public abstract String toString();

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(toString());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Move other = (Move) obj;
        return Objects.equals(this.toString(), other.toString());
    }

    


    
    
}
