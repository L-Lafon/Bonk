package javagame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.io.*;

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
    
    class Player {
        Vec2D pos;
        Image image;
        float speed;
        float timer;
        float wait;
        int row;
        boolean animate;
        public Player(Image image) {
            this.image = image;
            this.row = 1;
            this.pos = new Vec2D(30,160*this.row+30);
            this.speed = 0F;
            this.timer = 0F;
            this.wait = 100F; 
            this.animate = false;
        }
    }
    
    class Bg {
        Vec2D pos;
        Image image;
        int count;
        float speed;
        List<Image> block;
        public Bg(Image image) {
            this.image = image;
            this.count = 0;
            this.pos = new Vec2D(0,0);
            this.block = new ArrayList<Image>();
        }
    }
    
    Vec2D winSize;
    
    Player player;
    
    List<Image> bgBlock = new ArrayList<Image>();
    Vec2D bgPos = new Vec2D(0,0);
    int bgCount = 0;
    float bgSpeed = 0.25F; // vitesse
    
    public Play(int state) {
        
    }
    
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        winSize = new Vec2D(gc.getWidth(), gc.getHeight());
        bgBlock.clear();
        
        String[] colors = {"blue","red","green","yellow",
                           "blue","pink","pink"};
        
       
        
        
        for (String color : colors) {
            bgBlock.add(new Image("res/bg-"+color+".png"));
        }
        
        player = new Player(new Image("res/char.png"));
        
    }
    
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        
        bgBlock.get(bgCount).draw(bgPos.x,bgPos.y);
        if (bgCount < bgBlock.size()) {
            bgBlock.get(bgCount + 1).draw(winSize.x + bgPos.x,bgPos.y);
        }
        else {
            bgBlock.get(0).draw(winSize.x + bgPos.x,bgPos.y);
        }
        player.image.draw(player.pos.x, player.pos.y);
        
        //g.drawString(Float.toString(bgBlock.size()), 100, 100);
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
        
        if (input.isKeyPressed(Input.KEY_UP) && player.row > 0) {
            //System.out.println("key up");
            player.row -= 1;
            player.animate = true;
        }
        if (input.isKeyPressed(Input.KEY_DOWN) && player.row < 2) {
            //System.out.println("key down");
            player.row += 1;
            player.animate=true;
        }
        if (player.animate == true) {
            player.timer += delta;
            // delta * 160 / player.wait
            if (player.pos.y < 160*player.row+30) {
                player.pos.y += 160*delta/player.wait;
                //System.out.println("if1");
            }
            if (player.pos.y > 160*player.row+30) {
                player.pos.y -= 160*delta/player.wait;
                //System.out.println("if2");
            }
        }
        if (player.timer > player.wait) {
            //System.out.println("reset timer");
            player.timer=0;
            player.animate=false;
            player.pos.y = 160*player.row+30;
        }
        
        //player.pos.y = 160*player.row+30;
        
        
    }
    
    public int getID() {
        return 1;
    }
    
}