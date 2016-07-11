
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javagame;

import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ini4j.Wini;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

/**
 * 
 * @author lea & corentin
 */
public class Config extends BasicGameState {
    
    /**
     * 
     * @param state 
     */
    public Config(int state) {
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
            this.active = false;
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
    
    Image bgConfig;
    Button buttonBack, buttonFS, buttonMusic, buttonSFX;
    Wini ini;
    TrueTypeFont font;
    
    /**
     * Initialise the backgroung and buttons used
     * @param gc
     * @param sbg
     * @throws SlickException 
     */
    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        bgConfig = new Image("res/config.png");
        
        Font awtFont = new Font("Lucida Sans", Font.BOLD, 48) {};
        font = new TrueTypeFont(awtFont, false);
        
        buttonBack = new Config.Button(new Image("res/Back.png"), new Image("res/BackActive.png"),10,10);
        buttonFS = new Config.Button(new Image("res/button_off.png"), new Image("res/button_on.png"), 450, 150);
        buttonMusic = new Config.Button(new Image("res/button_off.png"), new Image("res/button_on.png"), 450, 250);
        buttonSFX = new Config.Button(new Image("res/button_off.png"), new Image("res/button_on.png"), 450, 350);
        
        try {
            ini = new Wini(new File("settings.ini"));
            buttonFS.active = ini.get("Display", "fullscreen", boolean.class);
            buttonMusic.active = ini.get("Sound", "music", boolean.class);
            buttonSFX.active = ini.get("Sound", "sfx", boolean.class);
        } catch (IOException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        }
        
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
        g.drawImage(bgConfig, 0, 0);
        
        if (buttonBack.active) {
            g.drawImage(buttonBack.imageActive, buttonBack.x1, buttonBack.y1);
        }
        else {
            g.drawImage(buttonBack.image, buttonBack.x1, buttonBack.y1);
        }
        
        g.setFont(font);
        g.setColor(Color.black);
        g.drawString("Fullscreen", 53, 153);
        g.drawString("Music", 53, 253);
        g.drawString("Sound effects", 53, 353);
        g.setColor(Color.yellow);
        g.drawString("Fullscreen", 50, 150);
        g.drawString("Music", 50, 250);
        g.drawString("Sound effects", 50, 350);
        
        
        Button[] buts = new Button[] {buttonFS, buttonMusic, buttonSFX};
        for (Button but:buts) {
            if (but.active) {
                g.drawImage(but.imageActive, but.x1, but.y1);
            }
            else {
                g.drawImage(but.image, but.x1, but.y1);
            }
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
        
        if (buttonFS.hover(xpos, ypos) && input.isMousePressed(0)) {
            try {
                buttonFS.active = !buttonFS.active;
                gc.setFullscreen(buttonFS.active);
                ini.put("Display", "fullscreen", buttonFS.active);
                ini.store();
            } catch (IOException ex) {
                Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if (buttonMusic.hover(xpos, ypos) && input.isMousePressed(0)) {
            try {
                buttonMusic.active = !buttonMusic.active;
                Game.ISMUSIC = buttonMusic.active;
                ini.put("Sound", "music", buttonMusic.active);
                ini.store();
            } catch (IOException ex) {
                Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if (buttonSFX.hover(xpos, ypos) && input.isMousePressed(0)) {
            try {
                buttonSFX.active = !buttonSFX.active;
                Game.ISSFX = buttonSFX.active;
                ini.put("Sound", "sfx", buttonSFX.active);
                ini.store();
            } catch (IOException ex) {
                Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * get the id state of the class
     * @return 3
     */
    @Override
    public int getID() {
        return 3;
    }
}
