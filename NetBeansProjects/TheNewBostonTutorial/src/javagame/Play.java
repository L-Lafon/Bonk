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
        int count;
        float speed;
        List<Image> block;
        public Bg() {
            this.count = 0;
            this.pos = new Vec2D(0,0);
            this.block = new ArrayList<Image>();
            this.speed = 0.25F;
        }
    }
    
    class Wall {
        Vec2D pos;
        Image image;
        float speed;
        public Wall(Image image) {
            this.image = image;
            this.pos = new Vec2D(640,0);
            this.speed = 0.25F;
        }
    }
    
    Vec2D winSize;
    
    Player player;
    
    Bg bg;
    
    /*
    List<Image> bgBlock = new ArrayList<Image>();
    Vec2D bgPos = new Vec2D(0,0);
    int bgCount = 0;
    float bgSpeed = 0.25F; // vitesse
    */
    
    public Play(int state) {
        
    }
    
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        winSize = new Vec2D(gc.getWidth(), gc.getHeight());
        bg = new Bg();
        String[] colors = {"blue","red","green","yellow",
                           "blue","pink","pink"};
        
       
        
        
        for (String color : colors) {
            bg.block.add(new Image("res/bg-"+color+".png"));
        }
        
        player = new Player(new Image("res/char.png"));
        
    }
    
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        
        bg.block.get(bg.count).draw(bg.pos.x,bg.pos.y);
        if (bg.count < 6) {
            bg.block.get(bg.count + 1).draw(winSize.x + bg.pos.x,bg.pos.y);
        }
        else {
            bg.block.get(0).draw(winSize.x + bg.pos.x,bg.pos.y);
        }
        player.image.draw(player.pos.x, player.pos.y);
        
        //g.drawString(Float.toString(bg.pos.x), 100, 100);
    }
    
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        bg.pos.x -= delta * bg.speed;
        //System.out.println(bg.pos.x);
        Input input = gc.getInput();
        
        if (bg.pos.x < -winSize.x) {
            bg.pos.x = 0;
            bg.count += 1;
            if (bg.count == 7) {
                bg.count = 0;
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