
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
 *
 * @author lea
 */
public class Config extends BasicGameState {
    
    public Config(int state) {
    }
    
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
    
    Image bgInstr;
    Button buttonBack;
    
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        bgInstr = new Image("res/instr.png");
        
        buttonBack = new Config.Button(new Image("res/Back.png"), new Image("res/BackActive.png"),128,256,512,320);
    }
    
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        g.drawImage(bgInstr, 0, 0);
        
        if (buttonBack.active) {
            g.drawImage(buttonBack.imageActive, buttonBack.x1, buttonBack.y1);
        }
        else {
            g.drawImage(buttonBack.image, buttonBack.x1, buttonBack.y1);
        }
    }
    
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

    public int getID() {
        return 3;
    }
}
