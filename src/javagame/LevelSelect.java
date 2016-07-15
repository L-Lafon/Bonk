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
public class LevelSelect extends BasicGameState {
    
    public LevelSelect(int state) {
        
    }
    
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

    Button[] buttons;
    Image[] butDisabled;

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        Game.SUBLEVEL = 0;
        buttons = new Button[] {
            new Button(new Image("res/Button1.png"), new Image("res/Button1Active.png"), 64,  128),
            new Button(new Image("res/Button2.png"), new Image("res/Button2Active.png"), 256, 128),
            new Button(new Image("res/Button3.png"), new Image("res/Button3Active.png"), 448, 128),
            new Button(new Image("res/Button4.png"), new Image("res/Button4Active.png"), 160, 320),
            new Button(new Image("res/Button5.png"), new Image("res/Button5Active.png"), 352, 320),
        };
        butDisabled = new Image[] {
            new Image("res/Button1Inactive.png"), new Image("res/Button2Inactive.png"),
            new Image("res/Button3Inactive.png"), new Image("res/Button4Inactive.png"), 
            new Image("res/Button5Inactive.png")
        };
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics grphcs) throws SlickException {
        for (int x=0; x<=4; x++) {
            if (!Game.LEVELSPASSED[x]) {
                if (buttons[x].active) {
                    buttons[x].imageActive.draw(buttons[x].x1, buttons[x].y1);
                }
                else {
                    buttons[x].image.draw(buttons[x].x1, buttons[x].y1);
                }
            }
            else {
                butDisabled[x].draw(buttons[x].x1, buttons[x].y1);
            }
        }
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        Input input = gc.getInput();
        int xpos = Mouse.getX();
        int ypos = 480-Mouse.getY();
        
        for (int x=0; x<=4; x++) {
            if (!Game.LEVELSPASSED[x] && buttons[x].hover(xpos, ypos)) {
                buttons[x].active = true;
                if (input.isMousePressed(0)) {
                    Game.LEVELSPASSED[x] = true;
                    Game.LEVEL = x+1;
                    sbg.getState(Game.PLAY).init(gc, sbg);
                    sbg.enterState(Game.PLAY);
                }
            }
            else {
                buttons[x].active = false;
            }
        }
    }
    
    @Override
    public int getID() {
        return 4;
    }    
}
