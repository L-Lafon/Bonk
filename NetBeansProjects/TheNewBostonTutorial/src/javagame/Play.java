package javagame;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class Play extends BasicGameState {
    
    class Vec2D {
        float x;
        float y;
        public Vec2D(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }
    
    Vec2D winSize;
    
    Image blue,red,green,yellow,pink,grey,purple;
    Image charImage;
    int charPos = 1;
    float charPx = 160*charPos+30;
    
    /*
    position verticale du perso : 30px en dessous de la limite sup du couloir
    */
    
    List<Image> bgBlock = new ArrayList<Image>();
    Vec2D bgPos = new Vec2D(0,0);
    int bgCount = 0;
    
    float bgSpeed = 0.25F; // vitesse
    
    public Play(int state) {
        
    }
    
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        winSize = new Vec2D(gc.getWidth(), gc.getHeight());
        
        blue   = new Image("res/bg-blue.png");
        red    = new Image("res/bg-red.png");
        green  = new Image("res/bg-green.png");
        yellow = new Image("res/bg-yellow.png");
        pink   = new Image("res/bg-pink.png");
        grey   = new Image("res/bg-grey.png");
        purple = new Image("res/bg-purple.png");
        
        bgBlock.add(blue);
        bgBlock.add(red);
        bgBlock.add(green);
        bgBlock.add(yellow);
        bgBlock.add(blue);
        bgBlock.add(pink);
        bgBlock.add(pink);
        
        charImage = new Image("res/char.png");
        
    }
    
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        // g.drawString("this is the Play State!", 100, 100);
        bgBlock.get(bgCount).draw(bgPos.x,bgPos.y);
        if (bgCount < 7) {
            bgBlock.get(bgCount + 1).draw(winSize.x + bgPos.x,bgPos.y);
        }
        else {
            bgBlock.get(0).draw(winSize.x + bgPos.x,bgPos.y);
        }
        charImage.draw(30, charPx);
        
    }
    
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        bgPos.x -= delta * bgSpeed;
        Input input = gc.getInput();
        
        if (bgPos.x < -winSize.x) {
            bgPos.x = 0;
            bgCount += 1;
            if (bgCount == 7) {
                bgCount = 0;
            }
        }
        
        if (input.isKeyPressed(Input.KEY_UP) && charPos > 0) {
            System.out.println("key up");
            charPos -= 1;
        }
        if (input.isKeyPressed(Input.KEY_DOWN) && charPos < 2) {
            System.out.println("key down");
            charPos += 1;
        }
        
        if (charPx > 160*charPos+25 && charPx < 160*charPos+35) {
            //System.out.println(charPos);
            charPx = 160*charPos+30;
        }
        if (charPx < 160*charPos+30) {
            charPx += delta * 2;
        }
        if (charPx > 160*charPos+30) {
            charPx -= delta * 2;
        }
        
    }
    
    public int getID() {
        return 1;
    }
    
}