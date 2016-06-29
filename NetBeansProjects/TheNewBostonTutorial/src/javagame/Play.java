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
        Image image;
        boolean wall;
        
        public Bg(Image image, boolean wall) {
            this.image = image;
            this.wall = wall;
        }
    }
    
    class Block {
        Vec2D pos;
        int count;
        float speed;
        List<Bg> block;
        public Block() {
            this.count = 0;
            this.pos = new Vec2D(0,0);
            this.block = new ArrayList<Bg>();
            this.speed = 0.5F;
        }
    }
    
    class Wall {
        Vec2D pos;
        Image image;
        float speed;
        public Wall(Image image) {
            this.image = image;
            this.pos = new Vec2D(600,0);
            this.speed = 0.5F;
        }
    }
    
    Vec2D winSize;
    
    Player player;
    
    Block block;
    
    Wall wall; // Make JAVA programming great again !
    
    public Play(int state) {
        
    }
    
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        winSize = new Vec2D(gc.getWidth(), gc.getHeight());
        wall = new Wall(new Image("res/wall.png"));
        block = new Block();
        
        String[] letters = "EJQYEBBXBQYEJYQEXJYJJXBYXQBQXE".split("");
        
        boolean nextwall = false;
        
        for (String letter : letters) {
            if (letter.charAt(0) != 'X') {
                block.block.add(new Bg(new Image("res/bg-"+letter+".png"), nextwall));
                nextwall = false;
            }
            else {
                nextwall = true;
            }
            
        }
        
        player = new Player(new Image("res/char.png"));
        
    }
    
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        
        block.block.get(block.count).image.draw(block.pos.x,block.pos.y);
        if (block.count < block.block.size() - 1) {
            block.block.get(block.count + 1).image.draw(winSize.x + block.pos.x,block.pos.y);
        }
        else {
            block.block.get(0).image.draw(winSize.x + block.pos.x,block.pos.y);
        }
        player.image.draw(player.pos.x, player.pos.y);
        
        wall.image.draw(wall.pos.x, wall.pos.y);
        
        g.drawString(Float.toString(block.count), 100, 100);
    }
    
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        block.pos.x -= delta * block.speed;
        Input input = gc.getInput();
        
        if (block.pos.x < -winSize.x) {
            block.pos.x = 0;
            block.count += 1;
            if (block.count == block.block.size()) {
                block.count = 0;
            }
        }
        
        if (input.isKeyPressed(Input.KEY_UP) && player.row > 0) {
            player.row -= 1;
            player.animate = true;
        }
        
        if (input.isKeyPressed(Input.KEY_DOWN) && player.row < 2) {
            player.row += 1;
            player.animate=true;
        }
        
        if (player.animate == true) {
            player.timer += delta;
            if (player.pos.y < 160*player.row+30) {
                player.pos.y += 160*delta/player.wait;
            }
            if (player.pos.y > 160*player.row+30) {
                player.pos.y -= 160*delta/player.wait;
            }
        }
        
        if (player.timer > player.wait) {
            player.timer=0;
            player.animate=false;
            player.pos.y = 160*player.row+30;
        }
        
    }
    
    public int getID() {
        return 1;
    }
    
}