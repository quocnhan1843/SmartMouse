/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import java.awt.Color;

/**
 *
 * @author Nguyen Quoc Nhan
 */
public class Mouse {
    private Position position;
    private Color color;
    private String player;

    public Mouse() {
        this.position = new Position(1,1);
        this.color = Color.RED;
        this.player = "Player";
    }
    
    public void resetMouse(){
        this.position.setR(1);
        this.position.setC(1);
        this.color = Color.RED;
        this.player = "Player";
    }
    
    public Mouse(Position position, Color color, String player) {
        this.position = position;
        this.color = color;
        this.player = player;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }
    
    public void goEast(Maze maze){
        Position currentPositon = new Position(this.position);
        Position nextPosition = new Position(currentPositon.getR() + 1, currentPositon.getC());
        if(maze.canGo(nextPosition)){
            maze.setMouse(nextPosition);
            maze.setEmpty(currentPositon);
            this.position = nextPosition;
        }
    }
    
    public void goWest(Maze maze){
        Position currentPositon = new Position(this.position);
        Position nextPosition = new Position(currentPositon.getR() - 1, currentPositon.getC());
        if(maze.canGo(nextPosition)){
            maze.setMouse(nextPosition);
            maze.setEmpty(currentPositon);
            this.position = nextPosition;
        }
    }
    
    public void goSouth(Maze maze){
        Position currentPositon = new Position(this.position);
        Position nextPosition = new Position(currentPositon.getR(), currentPositon.getC() + 1);
        if(maze.canGo(nextPosition)){
            maze.setMouse(nextPosition);
            maze.setEmpty(currentPositon);
            this.position = nextPosition;
        }
    }
    public void goNorth(Maze maze){
        Position currentPositon = new Position(this.position);
        Position nextPosition = new Position(currentPositon.getR() , currentPositon.getC() -1 );
        if(maze.canGo(nextPosition)){
            maze.setMouse(nextPosition);
            maze.setEmpty(currentPositon);
            this.position = nextPosition;
        }
    }
}
