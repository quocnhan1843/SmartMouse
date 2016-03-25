/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import javax.swing.JPanel;

/**
 *
 * @author Nguyen Quoc Nhan
 */
public abstract class MyCanvas extends JPanel implements KeyListener{

    private static final boolean[] keyboardState = new boolean[525];
    
    public MyCanvas() {
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.addKeyListener(this);
    }    
    
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keyboardState[e.getKeyCode()] = true;
        keyReleasedDrawImage(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyboardState[e.getKeyCode()] = false;
        keyReleasedDrawImage(e);
    }    
    
    public abstract void keyReleasedDrawImage(KeyEvent e);    
    public abstract void Draw(Graphics2D g2d);
    
    @Override
    public void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D)g;        
        super.paintComponent(g2d);        
        Draw(g2d);
    }
}
