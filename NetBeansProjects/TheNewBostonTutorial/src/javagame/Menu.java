package javagame;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class Menu extends BasicGameState {
    
    // public String mouse = "No input yet!";
    /*
    int imagex = 200;
    int imagey = 200;
    */
    
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
    
    Image imgMenu;
    Button buttonPlay, buttonInstr, buttonSettings;
    
    public Menu(int state) {
        
    }
    
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        imgMenu = new Image("res/menu.png");
        
        buttonPlay = new Button(new Image("res/Play.png"), new Image("res/PlayActive.png"),128,256,512,320);
        buttonInstr = new Button(new Image("res/Instructions.png"), new Image("res/InstructionsActive.png"),128,320,512,384);
        buttonSettings = new Button(new Image("res/Settings.png"), new Image("res/SettingsActive.png"),128,384,512,448);
    }
    
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        /*
        g.drawString(mouse, 50, 50);
        g.drawRect(50, 100, 60, 120);
        Image baboon = new Image("res/baboon.png");
        
        //g.drawImage(baboon, imagex, imagey);
        g.fillOval(75, 100, 100, 100);
        g.drawString("Play Now!", 80, 70);
        */
        g.drawImage(imgMenu, 0, 0);
        
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
        int ypos = 480-Mouse.getY();
        
        /*
        if ((xpos > 260 && xpos < 380) && (ypos > 180 && ypos <230)) {
            if (input.isMouseButtonDown(0)) {
                sbg.enterState(1);
            }
        }
        if ((xpos > 170 && xpos < 475) && (ypos > 115 && ypos <155)) {
            if (input.isMouseButtonDown(0)) {
                System.out.println("Instructions pressed");
            }
        }
        if ((xpos > 216 && xpos < 426) && (ypos > 48 && ypos <98)) {
            if (input.isMouseButtonDown(0)) {
                System.out.println("Settings pressed");
            }
        }
        */
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
                System.out.println("Instructions pressed");
            }
        }
        else {
            buttonInstr.active = false;
        }
        
        if (buttonSettings.hover(xpos, ypos)) {
            buttonSettings.active = true;
            if (input.isMouseButtonDown(0)) {
                System.out.println("Settings pressed");
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