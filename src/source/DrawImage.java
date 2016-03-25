/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import static java.lang.Integer.min;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 *
 * @author Nguyen Quoc Nhan
 */
public class DrawImage extends MyCanvas{

    private Game game;
    private int UNIT = 24;
    
    private BufferedImage wall;
    private BufferedImage mouse;
    private BufferedImage cheese;
    private BufferedImage door;
    private BufferedImage rat;
    private BufferedImage pass;
    private BufferedImage fail;
    private BufferedImage one;
    private BufferedImage two;
    private BufferedImage three;
    private AudioStream countDownSound;
    public int count = 0;

    public DrawImage() throws IOException, URISyntaxException {
        game = new Game();
        count = 0;
        loadContent();
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void loadContent() throws IOException, URISyntaxException{
        URL wallURL =this.getClass().getResource("/image/wall.png");
        URL mouseURL =this.getClass().getResource("/image/mouse.png");
        URL cheeseURL =this.getClass().getResource("/image/cheese.png");
        URL doorURL =this.getClass().getResource("/image/door.png");
        URL ratURL =this.getClass().getResource("/image/rat.png");
        URL passURL =this.getClass().getResource("/image/passed.png");
        URL failURL = this.getClass().getResource("/image/fail.png");
        URL oneURL = this.getClass().getResource("/image/one.png");
        URL twoURL = this.getClass().getResource("/image/two.png");
        URL threeURL = this.getClass().getResource("/image/three.png");
        
        
        wall = ImageIO.read(wallURL);
        mouse = ImageIO.read(mouseURL);
        cheese = ImageIO.read(cheeseURL);
        door = ImageIO.read(doorURL);
        rat = ImageIO.read(ratURL);
        pass = ImageIO.read(passURL);
        fail = ImageIO.read(failURL);
        one = ImageIO.read(oneURL);
        two = ImageIO.read(twoURL);
        three = ImageIO.read(threeURL);
    }

    public void setGame(Game game) {
        this.game = game;
    }
    
    
    
    @Override
    public void keyReleasedDrawImage(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            Window.win.disAble();
            Window.window.setVisible(true);
        }
        game.keyListenerGame(e);
    }

    @Override
    public void Draw(Graphics2D g2d) {
        Maze maze = game.getMaze();
        char[][] board = maze.getBoard();
        for(int i=0; i <= maze.getMaxR(); i++){
            for(int j=0; j<maze.getMaxC(); j++){
                Position pos = new Position(i, j);
                if(maze.isWall(pos)){
                    g2d.drawImage(wall,i * UNIT, 50 + j * UNIT, null);
                }
                else if(maze.isMouse(pos)){
                    g2d.drawImage(mouse, i * UNIT, 50 + j * UNIT, null);
                }else if(board[i][j] == 't'){
                    g2d.setColor(Color.GRAY);
                    g2d.fillOval(i * UNIT, 50 + j*UNIT, UNIT, UNIT);
                }else if(maze.isCheese(pos)){
                    g2d.drawImage(cheese, i * UNIT, 50 + j * UNIT, null);
                }else if(maze.isExit(new Position(i, j))){
                    g2d.drawImage(door, i * UNIT, 50 + j * UNIT, null);
                }else if(board[i][j] == 'y'){
                    g2d.drawImage(rat, i * UNIT, 50 + j * UNIT, null);
                }
            }
        }
        g2d.setFont(new Font(Font.MONOSPACED,Font.BOLD,UNIT));
        g2d.setColor(Color.red);
        g2d.drawString("Time:", 10 , 30);
        long time = 18000 - (long) game.getGameTime();
        long minute = time /6000;
        long seconds = time % 6000;
        g2d.drawString("0" + Long.toString(minute), 85, 31);
        g2d.drawString(":", 115, 31);
        String sec = Double.toString( (double) seconds / 100 );
        if(seconds / 100 < 10) g2d.drawString("0" + sec, 130,31);
        else g2d.drawString(sec, 130,31);
        
        g2d.drawString("Score:", 350 , 30);
        g2d.drawString(Integer.toString(game.getMaze().getScore()), 440, 31);
        
        g2d.drawString("Time out:", 600 , 30);
        g2d.drawString(Integer.toString((45 - min((game.getLevel()-1)*5,35) ) - (int) (game.getGameTime()% (4500 - min((game.getLevel()-1)*500,3500) )) / 100) + "s", 730, 31);
                
        g2d.drawString("Level:", 980 , 30);
        g2d.drawString(Integer.toString(game.getLevel()), 1070, 31);
                
        new Monitor();
        
        if(Game.getGameState() == Game.GameState.MAINMENU){
            if(count == 3) g2d.drawImage(three, Monitor.width/2 - pass.getWidth()/2, Monitor.height/2 - pass.getHeight()/2, null);
            if(count == 2) g2d.drawImage(two, Monitor.width/2 - pass.getWidth()/2, Monitor.height/2 - pass.getHeight()/2, null);
            if(count == 1) g2d.drawImage(one, Monitor.width/2 - pass.getWidth()/2, Monitor.height/2 - pass.getHeight()/2, null);
        }        
        if(Game.getGameState() == Game.GameState.PASS){
            g2d.drawImage(pass, Monitor.width/2 - pass.getWidth()/2, Monitor.height/2 - pass.getHeight()/2, null);
            
            try {
                URL countDownURL = this.getClass().getResource("/sound/count_down.wav");
                countDownSound = new AudioStream(new FileInputStream(new File(countDownURL.toURI())));
                AudioPlayer.player.start(countDownSound);
            } catch (IOException ex) {
            } catch (URISyntaxException ex) {
            }
        }else if(Game.getGameState() == Game.GameState.GAMEOVER){
            g2d.drawImage(fail, Monitor.width/2 - pass.getWidth()/2, Monitor.height/2 - pass.getHeight()/2, null);
            
        }
    }   
}
