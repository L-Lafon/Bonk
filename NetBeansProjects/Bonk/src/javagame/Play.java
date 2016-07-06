package javagame;

import java.util.ArrayList;
import java.util.List;

import java.awt.Font;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import org.lwjgl.input.Mouse;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.state.*;

public class Play extends BasicGameState {
    
    public static final float SPEED = 0.5F;
    
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
        boolean furyLoad;
        float furyWait;
        float furyTime;
        float furyLoadTime;
        float furyLoadWait;
        Image furyImage;
        Image[] furyAnimation;
        Image[] flames;
        int furyStep; // étape d'animation chargement
        int score;
        int furySpr; // étape d'animation fury
        float furyAnimTime;
        public Player() throws SlickException {
            this.image = new Image("res/idee_perso.png");
            this.row = 1;
            this.pos = new Vec2D(30,120*(this.row+1)+10);
            this.speed = 0F;
            this.timer = 0F;
            this.wait = 100F; 
            this.animate = false;
            this.fury = false;
            this.furyWait = 900F; // tps de la furie
            this.furyTime = 0F; // timer de la furie
            this.furyLoad = false;
            this.furyLoadTime = 0; // timer de chargement de la furie
            this.furyLoadWait = 1000; // tps de chargement de la furie
            this.furyStep = 0;
            this.furyImage = new Image("res/idee_perso_fury.png");
            this.furyAnimation = new Image[] {
                new Image("res/idee_perso1.png"),
                new Image("res/idee_perso2.png"),
                new Image("res/idee_perso3.png"),
                new Image("res/idee_perso4.png"),
            };
            this.flames = new Image[] {
                new Image("res/flame_effect_1.png"),
                new Image("res/flame_effect_2.png"),
                new Image("res/flame_effect_3.png"),
                new Image("res/flame_effect_4.png"),
            };
            this.furySpr = 0;
            this.score = 0;
            this.furyAnimTime = 0F;
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
            this.speed = SPEED;
        }
    }
    
    class Wall {
        Vec2D pos;
        Image image;
        float speed;
        public Wall(Image image) {
            this.image = image;
            this.pos = new Vec2D(640,120);
            this.speed = SPEED;
        }
    }
    
    class Coin {
        Vec2D pos;
        Image image;
        Image[] images;
        float speed;
        public Coin() throws SlickException {
            this.image = new Image ("res/coin1.png");
            this.images = new Image[] {
                new Image("res/coin1.png"),
                new Image("res/coin2.png"),
                new Image("res/coin3.png"),
                new Image("res/coin4.png"),
            };
            this.pos = new Vec2D(640,120);
            this.speed = SPEED;
        }
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
    
    class Stats {
        /*
        tps de reaction
        nb de pieces
        nb appuis
        
        */
        //int nbWallDestr;
        //int score;
        float tpsReac;
        int coins;
        
        public Stats() throws SlickException {
            /*
            this.nbWallDestr = 0;
            this.score = 0;
            this.tpsReac = 0;
            */
            this.reset();
        }
        
        public void reset() {
            //this.nbWallDestr = 0;
            //this.score = 0;
            this.tpsReac = 0;
            this.coins = 0;
        }
        
        public void write() {
            try {
                    Writer file = new BufferedWriter(new FileWriter("data.csv", true));
                    file.append(Float.toString(stats.tpsReac)+"\n");
                    file.close();
                } catch (IOException iOException) {
                }
        }
    }
    
    Vec2D winSize;
    
    Player player;
    
    Block block;
    
    Wall wall; // Make JAVA programming great again !
    
    List<Coin> activeCoins;
    float coinStage = 0;
    float coinTime = 0;
    float coinWait = 300;
    float coinAnim = 0;
    int coinFrame = 0;
    
    float random = 0;
    
    TrueTypeFont font;
    
    Button sound;
    
    //kou rouloukoukou rouloukoukou
    Stats stats; // #okjesors
    
    FileWriter statfile;
    
    
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
            //sbg.enterState(0);
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
            
    /**
     *
     * @param gc
     * @param sbg
     * @throws SlickException
     * @throws IOException
     */
    
    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException{
        stats = new Stats();
        
        try {
            Writer file = new FileWriter("data.csv");
            file.write("background,pressed,coins,reaction-time\n");
            file.close();
        } catch (IOException iOException) {
        }
        
        
        winSize = new Vec2D(gc.getWidth(), gc.getHeight());
        wall = new Wall(new Image("res/wall.png"));
        block = new Block();
        activeCoins= new ArrayList<>();
        
        // initialisation police de caractère
        Font awtFont = new Font("Lucida Sans", Font.BOLD, 24) {};
        font = new TrueTypeFont(awtFont, false);
        
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
        
        player = new Player();
        
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
            if (player.furyLoad) {
                if (player.furyLoadTime < 250) {
                    player.furyAnimation[0].draw(player.pos.x, player.pos.y);
                }
                else if (player.furyLoadTime < 500) {
                    player.furyAnimation[1].draw(player.pos.x, player.pos.y);
                }
                else if (player.furyLoadTime < 750) {
                    player.furyAnimation[2].draw(player.pos.x, player.pos.y);
                }
                else {
                    player.furyAnimation[3].draw(player.pos.x, player.pos.y);
                }
            }
            else {
                player.image.draw(player.pos.x, player.pos.y);
            }
            
        } else {
            g.drawString("Fury activated", 30,30);
            
            player.flames[player.furySpr].draw(player.pos.x - 85, player.pos.y - 50);
            player.furyImage.draw(player.pos.x, player.pos.y);
        } 
        
        
        wall.image.draw(wall.pos.x, wall.pos.y);
        
        for (Coin coin:activeCoins) {
            coin.images[coinFrame].draw(coin.pos.x, coin.pos.y);
        }
        
        new Image("res/test_hud.png").draw();
        
        g.setFont(font);
        g.setColor(Color.yellow);
        g.drawString(Integer.toString(player.score),515,56);
        
    }
    
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        block.pos.x -= delta * block.speed;
        Input input = gc.getInput();
        
        if (block.pos.x < -winSize.x) { //passage au bg suivant
            block.pos.x = 0;
            block.count += 1;
            if (block.count == block.block.size()) {
                block.count = 0;
            }
        }
        
        if (block.block.get((block.count + 1) % block.block.size()).wall) { // apparition du mur
            wall.pos.x = winSize.x + block.pos.x;
            stats.tpsReac += delta;
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
        
        if (input.isKeyPressed(Input.KEY_SPACE) && player.fury == false) {
            player.furyLoad = true;
            stats.write();
            stats.reset();
        }
        
        if (player.furyLoad) {
            player.furyLoadTime += delta;
        }
        
        if (player.furyLoadTime > player.furyLoadWait) {
            player.furyLoad = false;
            player.fury = true;
            player.furyLoadTime = 0;
        }
        
        if (player.fury) {
            player.furyTime += delta;
            player.furyAnimTime += delta;
        }
        
        if (player.furyAnimTime > 75) {
            player.furySpr = (player.furySpr + 1) % 4;
            player.furyAnimTime = 0;
        }
        
        if (player.furyTime > player.furyWait ) {
            player.fury = false;
            player.furyTime = 0;
        }
        
        
        coinAnim += delta;
        
        if (coinAnim > 100) {
            coinFrame = (coinFrame+1) % 4;
            coinAnim = 0;
        }
    }
    
    public int getID() {
        return 1;
    }
    
}