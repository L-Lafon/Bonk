/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javagame;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author corentin
 */
public class Pause extends BasicGameState {
    
    /**
     * All you need to create a button
     */
    public class Button {
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
    
    Button ready;

    Pause(int state) {
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        Game.SUBLEVEL += 1;
        ready = new Button(new Image("res/Ready.png"), new Image("res/ReadyActive.png"), 128, 320);
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        g.drawString("you win",0,0);
        if (ready.active) {
            ready.imageActive.draw(ready.x1, ready.y1);
        }
        else {
            ready.image.draw(ready.x1, ready.y1);
        }
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
        Input input = gc.getInput();
        int xpos = Mouse.getX();
        int ypos = 480-Mouse.getY();
        
        if (ready.hover(xpos, ypos)) {
            ready.active = true;
            if (input.isMousePressed(0)) {
                sbg.getState(Game.PLAY).init(gc, sbg);
                sbg.enterState(Game.PLAY);
            }
        }
        else {
            ready.active = false;
        }
    }
    
    @Override
    public int getID() {
        return 5;
    }
}
