package javagame;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class Menu extends BasicGameState {
    
    // public String mouse = "No input yet!";
    /*
    Image baboon;
    int imagex = 200;
    int imagey = 200;
    */
    
    public Menu(int state) {
        
    }
    
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        //baboon = new Image("res/baboon.png");
    }
    
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        /*
        g.drawString(mouse, 50, 50);
        g.drawRect(50, 100, 60, 120);
        Image baboon = new Image("res/baboon.png");
        g.drawImage(baboon, 200, 130);
        */
        //g.drawImage(baboon, imagex, imagey);
        g.fillOval(75, 100, 100, 100);
        g.drawString("Play Now!", 80, 70);
    }
    
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        /*
        int xpos = Mouse.getX();
        int ypos = Mouse.getY();
        mouse = "Mouse position x: " + xpos  + " y: " + ypos;
        
        Input input = gc.getInput();
        if (input.isKeyDown(Input.KEY_UP)) {
            imagey -= 1;
        }
        if (input.isKeyDown(Input.KEY_DOWN)) {
            imagey += 1;
        }
        if (input.isKeyDown(Input.KEY_LEFT)) {
            imagex -= 1;
        }
        if (input.isKeyDown(Input.KEY_RIGHT)) {
            imagex += 1;
        }
        */
        Input input = gc.getInput();
        int xpos = Mouse.getX();
        int ypos = Mouse.getY();
        if ((xpos > 75 && xpos < 175) && (ypos > 160 && ypos <260)) {
            if (input.isMouseButtonDown(0)) {
                sbg.enterState(1);
            }
        }
    }
    
    public int getID() {
        return 0;
    }
    
}