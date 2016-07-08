/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javagame;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

/**
 * Menu class is extended to BasicGameState class into Slick
 * @author lea & corentin
 */
public class Instr extends BasicGameState {
    
    /**
     * 
     * @param state 
     */
    public Instr(int state) {
        
    }
    
    /**
     * All you need to create a button
     */
    class Button {
        Image image, imageActive;
        int x1, x2, y1, y2;
        boolean active = false;
        /**
         * Button method display button with 2 image which can switch 
         * at the positions x1 and y1 entered 
         * @param image
         * @param imageActive
         * @param x1
         * @param y1 
         */
        public Button(Image image, Image imageActive, int x1, int y1) {
            this.image = image;
            this.imageActive = imageActive;
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x1 + this.image.getWidth();
            this.y2 = y1 + this.image.getHeight();
        }
        /**
         * Hover method checks if the mouse pointer is on the button
         * @param xpos
         * @param ypos
         * @return true if the mouse pointer is on the button, else false
         */
        public boolean hover(int xpos, int ypos) {
            return xpos > this.x1 && xpos < this.x2 && ypos > this.y1 && ypos < this.y2;
        }
    }
    
    Image bgInstr;
    Button buttonBack;
    
    /**
     * Initialise the backgroung and buttons used
     * @param gc
     * @param sbg
     * @throws SlickException 
     */
    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        bgInstr = new Image("res/instr.png");
        
        buttonBack = new Button(new Image("res/Back.png"), new Image("res/BackActive.png"),128,256);
    }
    
    /**
     * Draws everything on screen
     * @param gc
     * @param sbg
     * @param g
     * @throws SlickException 
     */
    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        
        g.drawImage(bgInstr, 0, 0);
        
        if (buttonBack.active) {
            g.drawImage(buttonBack.imageActive, buttonBack.x1, buttonBack.y1);
        }
        else {
            g.drawImage(buttonBack.image, buttonBack.x1, buttonBack.y1);
        }
    }
    
    /**
     * Updates all the variables
     * @param gc the game container
     * @param sbg the state based games
     * @param delta the time spent since the last call of the update method
     * @throws SlickException 
     */
    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        
        Input input = gc.getInput();
        int xpos = Mouse.getX();
        int ypos = 480-Mouse.getY();
        
        if (buttonBack.hover(xpos, ypos)) {
            buttonBack.active = true;
            if (input.isMousePressed(0)) {
                sbg.enterState(0);
            }
        }
        else {
            buttonBack.active = false;
        }
    }
    
    /**
     * get the id state of the class
     * @return 2
     */
    @Override
    public int getID() {
        return 2;
    }
}