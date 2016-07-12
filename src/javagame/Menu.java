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
    
    Image bgMenu;
    Button buttonPlay, buttonInstr, buttonSettings, buttonExit;
    
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
    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        bgMenu = new Image("res/title_new.png");
        
        buttonPlay = new Button(new Image("res/Play.png"), new Image("res/PlayActive.png"),128,256);
        buttonInstr = new Button(new Image("res/Instructions.png"), new Image("res/InstructionsActive.png"),128,320);
        buttonSettings = new Button(new Image("res/Settings.png"), new Image("res/SettingsActive.png"),128,384);
        buttonExit = new Button(new Image("res/Quit.png"), new Image("res/QuitActive.png"), 10, 10);
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
        g.drawImage(bgMenu, 0, 0);
        
        Button[] buts = new Button[] {
            buttonPlay, buttonInstr, buttonSettings, buttonExit
        };
        
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
        
        if (buttonPlay.hover(xpos, ypos)) {
            buttonPlay.active = true;
            if (input.isMousePressed(0)) {
                sbg.enterState(Game.LEVELSELECT);
            }
        }
        else {
            buttonPlay.active = false;
        }
        
        if (buttonInstr.hover(xpos, ypos)) {
            buttonInstr.active = true;
            if (input.isMousePressed(0)) {
                sbg.enterState(Game.INSTR);
            }
        }
        else {
            buttonInstr.active = false;
        }
        
        if (buttonSettings.hover(xpos, ypos)) {
            buttonSettings.active = true;
            if (input.isMousePressed(0)) {
                sbg.enterState(Game.CONFIG);
            }
        }
        else {
            buttonSettings.active = false;
        }
        
        if (buttonExit.hover(xpos, ypos)) {
            buttonExit.active = true;
            if (input.isMousePressed(0)) {
                gc.exit();
            }
        }
        else {
            buttonExit.active = false;
        }
        //System.out.println("Mouse : "+xpos+" , "+ypos);
        
        
    }
    
    /**
     * get the id state of the class
     * @return 0
     */
    @Override
    public int getID() {
        return 0;
    }
    
}