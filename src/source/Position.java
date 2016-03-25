/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

/**
 *
 * @author Nguyen Quoc Nhan
 */
public class Position {
    private int r, c;

    public Position() {
        r = 0;
        c = 0;
    }
    
    public Position(Position pos) {
        this.r = pos.getR();
        this.c = pos.getC();
    }

    public Position(int r, int c) {
        this.r = r;
        this.c = c;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public int getC() {
        return c;
    }

    public void setC(int c) {
        this.c = c;
    }
    
    public void setPosition(Position position){
        this.r = position.getR();
        this.c = position.getC();
    }
}
