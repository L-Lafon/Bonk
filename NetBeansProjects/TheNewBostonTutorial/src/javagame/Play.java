package javagame;

import java.util.ArrayList;
import java.util.List;


import org.newdawn.slick.*;
import org.newdawn.slick.geom.Polygon;
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
        float wait; //temps de l'animation
        int row;
        boolean animate; 
        boolean fury;
        float furyWait;
        float furyTime;
        Image furyImage;
        int score;
        public Player(Image image, Image furyImage) {
            this.image = image;
            this.row = 1;
            this.pos = new Vec2D(30,120*(this.row+1)+10);
            this.speed = 0F;
            this.timer = 0F;
            this.wait = 100F; 
            this.animate = false;
            this.fury = false;
            this.furyWait = 900F;
            this.furyTime = 0F;
            this.furyImage = furyImage;
            this.score = 0;
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
            this.pos = new Vec2D(0,120);
            this.block = new ArrayList<>();
            this.speed = 0.5F;
        }
    }
    
    class Wall {
        Vec2D pos;
        Image image;
        float speed;
        public Wall(Image image) {
            this.image = image;
            this.pos = new Vec2D(640,120);
            this.speed = 0.5F;
        }
    }
    
    class Coin {
        Vec2D pos;
        Image image;
        float speed;
        public Coin() throws SlickException {
            this.image = new Image ("res/coin.png");
            this.pos = new Vec2D(640,120);
            this.speed = 0.5F;
        }
    }
    
    class Stats {
        int nbWallDestr;
        int score;
        public Stats() throws SlickException {
            this.nbWallDestr = 0;
            this.score = 0;
        }
    }
    
    Vec2D winSize;
    
    Player player;
    
    Block block;
    
    Wall wall; // Make JAVA programming great again !
    
    List<Coin> activeCoins;
    float coinStage = 0;
    float coinTime = 0;
    float coinWait = 256;
    
    float random = 0;
    
    public Play(int state) {
        
    }
    
    public void destroyWall() {
        
        Polygon playerPoly = new Polygon(
            new float[] {
                player.pos.x, player.pos.y,
                player.pos.x, player.pos.y + 100, // 100=taille perso
                player.pos.x + 100, player.pos.y + 100,
                player.pos.x + 100, player.pos.y 
            }
        );
        Polygon wallPoly = new Polygon(
            new float[] {
                wall.pos.x, wall.pos.y, // =0
                wall.pos.x, wall.pos.y + 480, // 480=longueur du mur
                wall.pos.x + 64, wall.pos.y + 480,
                wall.pos.x + 64, wall.pos.y 
            }
        );
        
        if(playerPoly.intersects(wallPoly)) {
            if (player.fury==false) {
            System.out.println("GAME OVER");
            //score += 2000 * delta; // bonus score for destroyed bomb
            }
            else {
                System.out.println("WALL DESTROYING");
                wall.pos.x = -100;
            } 
        }
    }
    
    public void createCoin() throws SlickException {
        coinStage = (int)(Math.random() * 3);
        //System.out.println("creating coin");
        Coin coin = new Coin();
        coin.pos.x = 640;
        coin.pos.y = 120*(coinStage+1)+35;
        activeCoins.add(coin);
    }
    
    public void destroyCoin() {
        List<Coin> inactiveCoins = new ArrayList<>();
        
        Polygon playerPoly = new Polygon(
            new float[] {
                player.pos.x, player.pos.y,
                player.pos.x, player.pos.y + 100, // 100=taille perso
                player.pos.x + 100, player.pos.y + 100,
                player.pos.x + 100, player.pos.y 
            }
        );
        
        for(Coin coin: activeCoins) {
            
            if (coin.pos.x < -50) {
                inactiveCoins.add(coin);
            }
            else {
                Polygon coinPoly = new Polygon(
                    new float[] {
                        coin.pos.x, coin.pos.y, // =0
                        coin.pos.x, coin.pos.y + 50, // taille coin
                        coin.pos.x + 50, coin.pos.y + 50,
                        coin.pos.x + 50, coin.pos.y 
                    }
                );

                if(playerPoly.intersects(coinPoly)) {
                    //System.out.println("Grab coin");
                    player.score +=1;
                    inactiveCoins.add(coin);
                }
            }
        }
        activeCoins.removeAll(inactiveCoins);
    }
            
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        winSize = new Vec2D(gc.getWidth(), gc.getHeight());
        wall = new Wall(new Image("res/wall.png"));
        block = new Block();
        activeCoins= new ArrayList<>();
        
        //String[] letters = "EJQYEBBXBQYEJYQEXJYJJXBYXQBQXE".split("");
        String[] letters = "YGXJYJYYQQEGXJGXEEGXQEGXBJQBBB".split("");
        
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
        
        player = new Player(new Image("res/char.png"),new Image("res/charFury.png"));
        
    }
    
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        
        block.block.get(block.count).image.draw(block.pos.x,block.pos.y);
        if (block.count < block.block.size() - 1) {
            block.block.get(block.count + 1).image.draw(winSize.x + block.pos.x,block.pos.y);
        }
        else {
            block.block.get(0).image.draw(winSize.x + block.pos.x,block.pos.y);
        }
        if (player.fury==false) {
            player.image.draw(player.pos.x, player.pos.y);
        } else {
            g.drawString("Fury activated", 30,30);
            player.furyImage.draw(player.pos.x, player.pos.y);
        } 
        
        
        wall.image.draw(wall.pos.x, wall.pos.y);
        
        for (Coin coin:activeCoins) {
            coin.image.draw(coin.pos.x, coin.pos.y);
        }
        
        g.drawString("Score : "+Integer.toString(player.score),500,30);
        
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
        
        if (block.block.get((block.count + 1) % block.block.size()).wall) {
            wall.pos.x = winSize.x + block.pos.x;
        }
        else {
            wall.pos.x = 640;
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
            if (player.pos.y < 120*(player.row+1)+10) {
                player.pos.y += 120*delta/player.wait;
            }
            if (player.pos.y > 120*(player.row+1)+10) {
                player.pos.y -= 120*delta/player.wait;
            }
        }
        
        if (player.timer > player.wait) {
            player.timer=0;
            player.animate=false;
            player.pos.y = 120*(player.row+1)+10;
        }
        
        if (wall.pos.x<640) {
            destroyWall();
        }
        
        coinTime += delta;
        if (coinTime > coinWait) {
            createCoin();
            coinTime = 0;
        }
        
        for (Coin coin : activeCoins) {
            coin.pos.x -= delta*coin.speed;
        }
        
        destroyCoin();
        
        if (input.isKeyPressed(Input.KEY_SPACE)) {
            player.fury = true;
        }
        
        if (player.fury) {
            player.furyTime += delta;
        }
        
        if (player.furyTime>player.furyWait ) {
            player.fury = false;
            player.furyTime = 0;
        }
    }
    
    public int getID() {
        return 1;
    }
    
}