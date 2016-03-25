/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import static java.lang.Integer.min;
import java.util.Random;

/**
 *
 * @author Nguyen Quoc Nhan
 */
public class Game {
    private int score;
    private double gameTime;
    private long lastTime;
    private long currentTime;
    private Maze maze;
    private Mouse mouse;
    private Position exit;
    private static int level = 1;

    public static enum GameState { PAUSE, PLAYING, LOADING, GAMEOVER, MAINMENU, PASS};
    
    private static GameState gameState;

    public Game() {
        init();
    }    

    public double getGameTime() {
        return gameTime;
    }
    
    public static GameState getGameState(){
        return gameState;
    }
    
    public void init(){
        gameState = GameState.MAINMENU;
        score = 0;
        gameTime = 0;
        lastTime = System.currentTimeMillis() / 10;
        currentTime = System.currentTimeMillis() / 10;
        maze = new Maze();
        mouse = new Mouse();
        exit = new Position();
    }
   
    public void updateGame(){
        currentTime = System.currentTimeMillis() / 10;
        gameTime += currentTime - lastTime;
        lastTime = currentTime;
        score = 0;
        if(gameTime >= 18000) gameState = gameState.GAMEOVER;
        if(gameTime % (4500 - min((level-1)*500,3500) ) == 0 && gameTime != 0) {
            //if(exit.getR() != 0 && exit.getC() != 0) maze.setEmpty(exit);
            /*while(true){
                int rx = new Random().nextInt(maze.getMaxR());
                int ry = new Random().nextInt(maze.getMaxC());
                exit.setPosition(new Position(rx, ry));
                if(!maze.isWall(exit) && !maze.isCheese(exit) && !maze.isMouse(exit)){
                    maze.setExit(exit);
                    break;
                }
            }*/
            maze.setExit();
        }
    }
    
    

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Maze getMaze() {
        return maze;
    }

    public void setMaze(Maze maze) {
        this.maze = maze;
    }

    public Mouse getMouse() {
        return mouse;
    }

    public void setMouse(Mouse mouse) {
        this.mouse = mouse;
    }
    
    public static void upLevel(){
        level++;
    }

    public int getLevel() {
        return level;
    }

    public void setLastTime() {
        this.lastTime = System.currentTimeMillis() / 10;
    }
    
    
    
    public void restartGame(){
        gameTime = 0;
        lastTime = System.currentTimeMillis() / 10;
        currentTime = System.currentTimeMillis() / 10;
        maze.init();
        mouse.resetMouse();
        gameState = GameState.MAINMENU;
        maze.createMaze();
    }

    public static void setGameState(GameState gameState) {
        Game.gameState = gameState;
    }
    
    public void keyListenerGame(KeyEvent evt){
        if(evt.getKeyCode() == KeyEvent.VK_UP){
            //System.out.println("Up........");
            mouse.goNorth(maze);
        }else if(evt.getKeyCode() == KeyEvent.VK_DOWN){
            //System.out.println("Down.......");
            mouse.goSouth(maze);
        }else if(evt.getKeyCode() == KeyEvent.VK_RIGHT){
            //System.out.println("Right...........");
            mouse.goEast(maze);
        }else if(evt.getKeyCode() == KeyEvent.VK_LEFT){
            //System.out.println("Left...........");
            mouse.goWest(maze);
        }
    }
    
}
