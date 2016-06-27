package javagame;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class Menu extends BasicGameState {
    
    class Button {
        Image image, imageActive;
        int x1, x2, y1, y2;
        boolean active = false;
        
        public Button(Image image, Image imageActive, int x1, int y1, int x2, int y2) {
            this.image = image;
            this.imageActive = imageActive;
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }
        
        public boolean hover(int xpos, int ypos) {
            if (xpos > this.x1 && xpos < this.x2 && ypos > this.y1 && ypos < this.y2) {
                return true;
            }
            else {
                return false;
            }
        }
    }
    
    Image bgMenu;
    Button buttonPlay, buttonInstr, buttonSettings;
    
    public Menu(int state) {
        
    }
    
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        bgMenu = new Image("res/title.png");
        
        buttonPlay = new Button(new Image("res/Play.png"), new Image("res/PlayActive.png"),128,256,512,320);
        buttonInstr = new Button(new Image("res/Instructions.png"), new Image("res/InstructionsActive.png"),128,320,512,384);
        buttonSettings = new Button(new Image("res/Settings.png"), new Image("res/SettingsActive.png"),128,384,512,448);
    }
    
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
        
    }
    
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        
        Input input = gc.getInput();
        int xpos = Mouse.getX();
        int ypos = 480-Mouse.getY();
        
        if (buttonPlay.hover(xpos, ypos)) {
            buttonPlay.active = true;
            if (input.isMouseButtonDown(0)) {
                sbg.enterState(1);
            }
        }
        else {
            buttonPlay.active = false;
        }
        
        if (buttonInstr.hover(xpos, ypos)) {
            buttonInstr.active = true;
            if (input.isMouseButtonDown(0)) {
                sbg.enterState(2);
            }
        }
        else {
            buttonInstr.active = false;
        }
        
        if (buttonSettings.hover(xpos, ypos)) {
            buttonSettings.active = true;
            if (input.isMouseButtonDown(0)) {
                sbg.enterState(3);
            }
        }
        else {
            buttonSettings.active = false;
        }
    }
    
    public int getID() {
        return 0;
    }
    
}