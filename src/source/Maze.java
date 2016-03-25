/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Random;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 *
 * @author Nguyen Quoc Nhan
 */
public class Maze {
    private char board[][];
    private int maxR;
    private int maxC;
    private int score;
    GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    int width = gd.getDisplayMode().getWidth();
    int height = gd.getDisplayMode().getHeight();
    int numOfCheese;
    Game game;
    
    private final int kx[] = {-1, -1, -1, 0, 0, 1, 1, 1};
    private final int ky[] = {-1, 0, 1, -1, 1, -1, 0, 1};
    
    private final int dr1[] = {-1, 0, 0, 1};
    private final int dc1[] = {0, 1, -1, 0};

    private final int dr2[] = {-2, 0, 0, 2};
    private final int dc2[] = {0, 2, -2, 0};
    
    public Maze(){
        this.maxC = height / 28;
        this.maxR = width / 24;
        this.board = new char[maxR + 5][maxC + 5];
        this.score = 0;
        init();
    }

    public int getScore() {
        return score;
    }
    
    public void init(){
        for (int i = 0; i <= maxR; i++) {
            for (int j = 0; j <= maxC; j++) {
                this.board[i][j] = 'w';
            }
        }
    }
    
    public void setMaze(Maze maze){
        this.board = maze.getBoard();     
        this.maxC = maze.getMaxC();
        this.maxR = maze.getMaxR();
        this.score = maze.getScore();
    }
    
    public void createMaze(){
        dfsCreateMaze();
    }
    
    public void dfsCreateMaze(){
        
        Position[] stackPosition = new Position[1000000+5];
        int top = 0;
        stackPosition[top] = new Position(1, 1);
        while(top >= 0){
            Position currentPosition = stackPosition[top];
            int currentPositionR = currentPosition.getR();
            int currentPositionC = currentPosition.getC();
            boolean flag = false;
            for(int i=0; i<4; i++){
                int r1 = currentPositionR + dr1[i];
                int c1 = currentPositionC + dc1[i];
                
                int r2 = currentPositionR + dr2[i];
                int c2 = currentPositionC + dc2[i];
                
                Position temp1 = new Position(r1, c1);
                Position temp2 = new Position(r2, c2);
                
                if(validPosition(temp1) && isWall(temp1) && validPosition(temp2) && isWall(temp2)){
                    flag = true;
                    break;
                }
            }
            if(!flag) top--;
            else
            while(flag){
                int random = new Random().nextInt(this.dc1.length);
                
                int r1 = currentPositionR + dr1[random];
                int c1 = currentPositionC + dc1[random];
                
                int r2 = currentPositionR + dr2[random];
                int c2 = currentPositionC + dc2[random];
                
                Position temp1 = new Position(r1, c1);
                Position temp2 = new Position(r2, c2);
                
                if(validPosition(temp1) && isWall(temp1) && validPosition(temp2) && isWall(temp2)){
                    flag = false;
                    
                    setEmpty(temp1);
                    setEmpty(temp2);
                    
                    top++;
                    stackPosition[top] = new Position(temp2);
                }
            }
        }
        
        int cnt = 20;
        while(cnt > 0){
            int drR = new Random().nextInt(maxR);
            int drC = new Random().nextInt(maxC);
            int count = 0;
            for(int i=0; i<8; i++){
                int r = drR + kx[i];
                int c = drC + ky[i];
                
                Position pos = new Position(r, c);
                if(!validPosition(pos)){
                    count = 3;
                    break;
                }else if(isWall(pos)){
                    count++;
                }
                if(count > 2) break;
            }
            if(count == 2){
                if(isWall(new Position(drR + dr1[0], drC + dc1[0])) && isWall(new Position(drR + dr1[3], drC + dc1[3]))){
                    //setEmpty(new Position(drR, drC));
                    this.board[drR][drC] = 'e';
                    cnt--;
                }
                else if(isWall(new Position(drR + dr1[1], drC + dc1[1])) && isWall(new Position(drR + dr1[2], drC + dc1[2]))){
                    //setEmpty(new Position(drR, drC));
                    this.board[drR][drC] = 'e';
                    cnt--;
                }
            }
        }
        
        setMouse(new Position(1, 1));
        Position start = new Position(maxR - 1, maxC - 2);
        Position end = new Position(1,1);
        //bfs(start,end);
        //setExit(start);
        setCheeses();
    }
    
    public void setCheeses() {
        Random r = new Random();
        numOfCheese = 0;

        Position[] listCheese = new Position[100];

        for (int i = 0; i < this.maxR; i++) {
            for (int j = 0; j < this.maxC; j++) {
                Position positionCheese = new Position(i,j);
                int count = 0;
                for (int id = 0; id < 8; id++) {
                    Position temp = new Position();
                    temp.setPosition(new Position(positionCheese.getR() + kx[id], positionCheese.getC() + ky[id]) );
                    if (validPosition(temp) && isWall(temp)) {
                        count++;
                    }
                }

                if (count == 7) {
                    this.board[i][j] = 'b';
                    numOfCheese++;
                }
            }

        
        }

    }
    
    public void bfs(Position start, Position end){
        
        for(int i=0; i<maxR; i++){
            for(int j=0; j<maxC; j++){
                if(board[i][j] == 't') board[i][j] = 'e';
            }
        }
        
        Position[][] checkPosition = new Position[60+5][60 + 5];
        int[][] checkDist = new int[1000+5][1000+5];
        for(int i=0; i<60; i++){
            for(int j=0; j<60; j++){
                checkPosition[i][j] = new Position(-1, -1);
                checkDist[i][j] = -1;
            }
        }
        //bfs tim duong di tot nhat;
        Position[] queuePosition = new Position[1000000+5];
     
        int front = 0,rear = 0;
        queuePosition[rear] = new Position(start.getR(),start.getC());
        checkDist[start.getR()][start.getC()] = 1;
        //System.out.println("Start: " + start.getR() + "-" +  start.getC());
        //System.out.println("End: " + end.getR() + "-" + end.getC());
        while(rear <= front){
            
            Position currentPosition = new Position();
           // System.out.println("rear ==== " + rear);
            currentPosition.setPosition(queuePosition[rear]);
            checkDist[currentPosition.getR()][currentPosition.getC()] = 1;
            rear++;
            for(int i=0; i<4; i++){
                int r = currentPosition.getR() + dr1[i];
                int c = currentPosition.getC() + dc1[i];
                //System.out.println(r + " " + c + " " + board[r][c]);
                /*if(r == end.getR() && c == end.getC()){
                    rear = front + 1;
                    break;
                }*/
                Position nextPosition = new Position(r,c);
                if(validPosition(nextPosition) && isWall(nextPosition) == false ){
                    Position pos = checkPosition[nextPosition.getR()][nextPosition.getC()];
                    if(pos.getR() == -1 && pos.getC() == -1){
                        checkPosition[nextPosition.getR()][nextPosition.getC()].setPosition(currentPosition);
                        front++;
                        queuePosition[front] = new Position(nextPosition);
                       // System.out.println("front == " +front +  " ----> " + queuePosition[front].getR() + " " + queuePosition[front].getC());
                    }
                }
            }
        }
        int x = end.getR(), y = end.getC();
        Position pos = new Position();
        pos.setPosition(checkPosition[x][y]);
        x = pos.getR();
        y = pos.getC();
        //System.out.println("x === " + x + " -- y ==== " + y );
        while( x != -1 && y != -1){
            //System.out.println("x === " + x + " -- y ==== " + y );
            board[x][y] = 't';
            pos.setPosition(checkPosition[x][y]);
            x = pos.getR();
            y = pos.getC();
            if(x == start.getR() && y == start.getC()) break;
        }
        
        
    }
    
    public void bfs1(Position start){
        int[][] checkDist = new int[1000+5][1000+5];
        for(int i=0; i<60; i++){
            for(int j=0; j<60; j++){
                checkDist[i][j] = -1;
            }
        }
        //bfs tim duong di tot nhat;
        Position[] queuePosition = new Position[1000000+5];
     
        int front = 0,rear = 0;
        queuePosition[rear] = new Position(start.getR(),start.getC());
        checkDist[start.getR()][start.getC()] = 0;
        
        while(rear <= front){
            
            Position currentPosition = new Position();
            currentPosition.setPosition(queuePosition[rear]);
            rear++;
            for(int i=0; i<4; i++){
                int r = currentPosition.getR() + dr1[i];
                int c = currentPosition.getC() + dc1[i];
                //System.out.println(r + " " + c + " " + board[r][c]);
                /*if(r == end.getR() && c == end.getC()){
                    rear = front + 1;
                    break;
                }*/
                Position nextPosition = new Position(r,c);
                if(validPosition(nextPosition) && isWall(nextPosition) == false && checkDist[r][c] == -1 ){
                    checkDist[r][c] = checkDist[currentPosition.getR()][currentPosition.getC()] + 1;
                    front++;
                    queuePosition[front] = new Position(nextPosition);
                }
            }
        }
        int mxPos = -1;
        int r = 1, c = 1;
        for(int i=0; i<60; i++){
            for(int j=0; j<60; j++){
                if(checkDist[i][j] > mxPos){
                    mxPos = checkDist[i][j];
                    r = i;
                    c = j;
                }
            }
        }
        //bfs(new Position(start),new Position(r, c));
        board[r][c] = 'x';
    }

    public char[][] getBoard() {
        return board;
    }
        
    public boolean isEmpty(Position pos){
        if(board[pos.getR()][pos.getC()] == 'e') return true;
        return false;
    }
    
    public boolean validPosition(Position pos){
        if(0 <= pos.getR() && pos.getR() < maxR && 0 <= pos.getC() && pos.getC() < maxC ) return true;
        return false;
    }
    
    public boolean canGo(Position pos){
        if(validPosition(pos)){ 
            if(isCheese(pos)) {
                this.score += 10;
                try {
                AudioStream eat;
                URL eatURL = this.getClass().getResource("/sound/eat.wav");
                eat = new AudioStream(new FileInputStream(new File(eatURL.toURI())));
                AudioPlayer.player.start(eat);
            } catch (IOException ex) {
            } catch (URISyntaxException ex) {
            }
            }
            if(isExit(pos)){
                Game.setGameState(Game.GameState.PASS);
                Game.upLevel();
            }
            if( isWall(pos) == false && isMouse(pos) == false) return true;
        }
        return false;
    }
    
    public boolean isWall(Position pos){
        if(board[pos.getR()][pos.getC()] == 'w') return true;
        return false;
    }
    
    public boolean isCat(Position pos){
        if(board[pos.getR()][pos.getC()] == 'c') return true;
        return false;
    }
    
    public boolean isMouse(Position pos){
        if(board[pos.getR()][pos.getC()] == 'm') return true;
        return false;
    }
    
    public  boolean isCheese(Position pos){
        if(board[pos.getR()][pos.getC()] == 'b') return true;
        return false;
    }
    
    public  boolean isExit(Position pos){
        if(board[pos.getR()][pos.getC()] == 'x') return true;
        return false;
    }
    
    public void setWall(Position pos){
        board[pos.getR()][pos.getC()] = 'w'; 
    }
    
    public void setEmpty(Position pos){
        board[pos.getR()][pos.getC()] = 'e'; 
    }
    
    public void setMouse(Position pos){
        board[pos.getR()][pos.getC()] = 'm'; 
    }
    
    public void setCheese(Position pos){
        board[pos.getR()][pos.getC()] = 'b'; 
    }
    
    public void setCat(Position pos){
        board[pos.getR()][pos.getC()] = 'c'; 
    }
    
    public void setExit(){
        //board[pos.getR()][pos.getC()] = 'x'; 
        Position pos = new Position(1,1);
        for(int i=0; i<maxR; i++){
            for(int j=0; j<maxC; j++){
                if(this.board[i][j] == 'x'){
                    this.board[i][j] = 'e';
                }
            }
        }
        for(int i=0; i<maxR; i++){
            for(int j=0; j<maxC; j++){
                if(this.board[i][j] == 'm'){
                    bfs1(new Position(i, j));
                    return;
                }
            }
        }
    }

    public int getMaxR() {
        return maxR;
    }

    public int getMaxC() {
        return maxC;
    }

    public void setScore(int score) {
        this.score = score;
    }
    
}
