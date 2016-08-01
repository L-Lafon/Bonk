/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javagame;

import java.awt.Font;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author corentin
 */
public class Credits extends BasicGameState {
    
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
    
    class Category {
        String name;
        String people;
        
        public Category (String name, String people) {
            this.name = name;
            this.people = people;
        }
    }
    
    Category[] credits;
    Button ButtonBack;
    int creditsIndex;
    float creditsTime, creditsWait;
    TrueTypeFont fontName, fontPeople;
    
    public Credits(int state) {
        
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        
        Font awtFont = new Font("Lucida Sans", Font.BOLD, 24) {};
        fontName = new TrueTypeFont(awtFont, false);
        awtFont = new Font("Lucida Sans", Font.BOLD, 20) {};
        fontPeople = new TrueTypeFont(awtFont, false);
        
        ButtonBack = new Button(new Image("res/buttons/Back.png"), new Image("res/buttons/BackActive.png"), 10, 10);
        credits = new Category[] {
            new Category("Programming",String.join("\n",
                            "Léa Lafon",
                            "Corentin Virumbrales")),
            new Category("Graphics",String.join("\n",
                            "Léa Lafon",
                            "Corentin Virumbrales")),
            new Category("Music",String.join("\n",
                            "Dancing Naked - TheFatRat",
                            "Infinite Power - TheFatRat",
                            "Monody - TheFatRat",
                            "Time Lapse - TheFatRat",
                            "Unity - TheFatRat",
                            "Windfall - TheFatRat (TastyRecords)")),
            new Category("Sounds",String.join("\n",
                            "Léa Lafon",
                            "Corentin Virumbrales",
                            "greenhourglass (freesound.org)",
                            "jeremysykes (freesound.org)",
                            "nebulousflynn (freesound.org)")),
            new Category("Software and tools used",String.join("\n",
                            "Slick2D Java API",
                            "NetBeans Java IDE",
                            "RealWorld Paint",
                            "GIMP",
                            "Audacity",
                            "bfxr")),
            new Category("Special Thanks",String.join("\n",
                            "Glyn Goodall",
                            "Christophe Schlick",
                            "TheNewBoston"))
        };
        creditsIndex = 0;
        creditsTime = 0;
        creditsWait = 5000;
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        if (ButtonBack.active) {
            ButtonBack.imageActive.draw(ButtonBack.x1, ButtonBack.y1);
        }
        else {
            ButtonBack.image.draw(ButtonBack.x1, ButtonBack.y1);
        }
        //g.drawString("test\ntest", 100, 100);
        //g.setFont(fontName);
        g.drawString(credits[creditsIndex].name, 100, 100);
        //g.setFont(fontPeople);
        g.drawString(credits[creditsIndex].people, 150, 200);
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        Input input = gc.getInput();
        int xpos = Mouse.getX();
        int ypos = 480-Mouse.getY();
        
        creditsTime += delta;
        if (creditsTime > creditsWait) {
            creditsIndex = (creditsIndex + 1)%credits.length;
            creditsTime = 0;
        }
        
        if (ButtonBack.hover(xpos, ypos)) {
            ButtonBack.active = true;
            if (input.isMousePressed(0)) {
                sbg.enterState(Game.MENU);
            }
        }
        else {
            ButtonBack.active = false;
        }
    }
    
    @Override
    public int getID() {
        return 6;
    }
}
