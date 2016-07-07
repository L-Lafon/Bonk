package javagame;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

/**
 * Menu Class for the beggining of the game : play, instructions, ...
 * Menu class is extended to BasicGameState class into Slick
 * @author lea & corentin
 */
public class Menu extends BasicGameState {
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
    
    Image bgMenu;
    Button buttonPlay, buttonInstr, buttonSettings, buttonSound;
    
    /**
     * 
     * @param state 
     */
    public Menu(int state) {
        
    }
    
    /**
     * Initialise the backgroung and buttons used
     * @param gc
     * @param sbg
     * @throws SlickException 
     */
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        bgMenu = new Image("res/title_new.png");
        
        buttonPlay = new Button(new Image("res/Play.png"), new Image("res/PlayActive.png"),128,256);
        buttonInstr = new Button(new Image("res/Instructions.png"), new Image("res/InstructionsActive.png"),128,320);
        buttonSettings = new Button(new Image("res/Settings.png"), new Image("res/SettingsActive.png"),128,384);
        buttonSound = new Button(new Image("res/SoundOff.png"), new Image("res/SoundOn.png"), 500, 30);
    }
    
    /**
     * Draws everything on screen
     * @param gc
     * @param sbg
     * @param g
     * @throws SlickException 
     */
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        g.drawImage(bgMenu, 0, 0);
        
        if (buttonPlay.active) {
            g.drawImage(buttonPlay.imageActive, buttonPlay.x1, buttonPlay.y1);
        }
        else {
            g.drawImage(buttonPlay.image, buttonPlay.x1, buttonPlay.y1);
        }
        
        if (buttonInstr.active) {
            g.drawImage(buttonInstr.imageActive, buttonInstr.x1, buttonInstr.y1);
        }
        else {
            g.drawImage(buttonInstr.image, buttonInstr.x1, buttonInstr.y1);
        }
        
        if (buttonSettings.active) {
            g.drawImage(buttonSettings.imageActive, buttonSettings.x1, buttonSettings.y1);
        }
        else {
            g.drawImage(buttonSettings.image, buttonSettings.x1, buttonSettings.y1);
        }
        
        if (buttonSound.active) {
            g.drawImage(buttonSound.imageActive, buttonSound.x1, buttonSound.y1);
        } else {
            g.drawImage(buttonSound.image, buttonSound.x1, buttonSound.y1);
        }
    }
    
    /**
     * Updates all the variables
     * @param gc the game container
     * @param sbg the state based games
     * @param delta the time spent since the last call of the update method
     * @throws SlickException 
     */
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        Input input = gc.getInput();
        int xpos = Mouse.getX();
        int ypos = 480-Mouse.getY();
        
        if (buttonPlay.hover(xpos, ypos)) {
            buttonPlay.active = true;
            if (input.isMousePressed(0)) {
                sbg.enterState(1);
            }
        }
        else {
            buttonPlay.active = false;
        }
        
        if (buttonInstr.hover(xpos, ypos)) {
            buttonInstr.active = true;
            if (input.isMousePressed(0)) {
                sbg.enterState(2);
            }
        }
        else {
            buttonInstr.active = false;
        }
        
        if (buttonSettings.hover(xpos, ypos)) {
            buttonSettings.active = true;
            if (input.isMousePressed(0)) {
                sbg.enterState(3);
            }
        }
        else {
            buttonSettings.active = false;
        }
        //System.out.println("Mouse : "+xpos+" , "+ypos);
        if (buttonSound.hover(xpos, ypos)) {
            if (input.isMousePressed(0)) {
                if (buttonSound.active) {
                    buttonSound.active = false;
                } else {
                    buttonSound.active = true;
                } 
            }
            
        }
    }
    
    /**
     * get the id state of the class
     * @return 0
     */
    public int getID() {
        return 0;
    }
    
}