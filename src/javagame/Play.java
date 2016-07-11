/**
 * @author Virumbrales-Lafon
 * @version 1.0
 * @see https://uf-mi.u-bordeaux.fr/ter-2016/virumbrales-lafon
 */

package javagame;

import java.util.ArrayList;
import java.util.List;

import java.awt.Font;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ini4j.Wini;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.state.*;

public class Play extends BasicGameState {
    
    public static final float SPEED = 0.5F;
    
    class Vec2D {
        /**
         * A 2D vector containing x and y coordinates.
         * @param x horizontal coordinate
         * @param y vertical coordinate
         */
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
        
        /**
         * Creates the main character
         * @throws SlickException 
         */
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
            this.furyLoadWait = 500; // tps de chargement de la furie
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
        
        /**
         * Creates a background with an image and a boolean
         * @param image the image of the background
         * @param wall  indicates if there is a wall on the background
         */
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
        
        /**
         * Creates an empty list of backgrounds
         */
        public Block() {
            this.count = 0;
            this.pos = new Vec2D(0,120);
            this.block = new ArrayList<>();
            this.speed = SPEED;
        }
        
        /**
         * Get a background at a given index
         * @param index index of the background
         * @return a background
         */
        public Bg get(int index) {
            return this.block.get(index);
        }
        
        /**
         * Add a background to the block
         * @param bg the background to add
         * @return true
         */
        public boolean add(Bg bg) {
            return this.block.add(bg);
        }
        
        public int size() {
            return this.block.size();
        }
    }
    
    class Wall {
        Vec2D pos;
        Image image;
        Image[] imgBroken;
        float speed;
        int broken;
        
        /**
         * Creates a wall with an image, a Vec2D (position) and a speed
         * @throws SlickException 
         */
        public Wall() throws SlickException {
            this.image = new Image("res/wall.png");
            this.imgBroken = new Image[] {
                new Image("res/wall_0.png"),
                new Image("res/wall_1.png"),
                new Image("res/wall_2.png")
            };
            this.pos = new Vec2D(640,120);
            this.speed = SPEED;
            this.broken = -1;
        }
    }
    
    class Coin {
        Vec2D pos;
        Image[] images;
        float speed;
        
        /**
         * Creates a coin with 4 images, a Vec2D (position) and a speed
         * @throws SlickException 
         */
        public Coin() throws SlickException {
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
        float timer;
        boolean hasPressed;
        
        /**
         * Creates some stats about the player's performance
         * @throws SlickException 
         */
        public Stats() throws SlickException {
            /*
            this.nbWallDestr = 0;
            this.score = 0;
            this.tpsReac = 0;
            */
            this.reset();
        }
        
        /**
         * Resets the stats
         */
        public void reset() {
            //this.nbWallDestr = 0;
            //this.score = 0;
            this.tpsReac = 0;
            this.coins = 0;
            this.timer = 0;
            this.hasPressed = false;
        }
        
        /**
         * Writes the stats to a .csv file
         * @param count index of the background
         */
        public void write(int count) {
            try {
                    Writer file = new BufferedWriter(new FileWriter("data.csv", true));
                    file.append(Integer.toString(count)+","
                                +Boolean.toString(this.hasPressed)+","
                                +Integer.toString(this.coins)+","
                                +Float.toString(stats.tpsReac)+"\n");
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
    
    
    //kou rouloukoukou rouloukoukou
    Stats stats; // #okjesors
    
    FileWriter statfile;
    
    Music music;
    Sound sndWall, sndCoin, sndFury, sndLoad;
    boolean isMusic, isSFX;
    
    /**
     * Creates the play screen
     * @param state the index of the state in the state-based game
     */
    public Play(int state) {
        
    }
    
    /**
     * Destroys the wall when outside the game window or when the player breaks it
     */
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
        
        if(wall.broken == -1 && playerPoly.intersects(wallPoly)) {
            if (player.fury==false) {
            System.out.println("GAME OVER");
            //sbg.enterState(0);
            }
            else {
                sndWall.play();
                System.out.println("WALL DESTROYING");
                wall.broken = player.row;
                //wall.pos.x = -500;
            } 
        }
    }
    
    /**
     * Creates a coin in a random row and adds it to the list of active coins
     * @throws SlickException 
     */
    public void createCoin() throws SlickException {
        coinStage = (int)(Math.random() * 3);
        //System.out.println("creating coin");
        Coin coin = new Coin();
        coin.pos.x = 640;
        coin.pos.y = 120*(coinStage+1)+35;
        activeCoins.add(coin);
    }
    
    /**
     * Deletes the coins outside the game window or when the player collects them
     */
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
                    stats.coins +=1 ;
                    inactiveCoins.add(coin);
                    if (Game.ISSFX) {
                        sndCoin.play();
                    }
                }
            }
        }
        activeCoins.removeAll(inactiveCoins);
    }
    
    /**
     * Initialize the variables
     * @param gc  the game container
     * @param sbg the state based game
     * @throws SlickException 
     */
    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException{        
    /**
     *
     * @param gc the game container
     * @param sbg state based game
     * @throws SlickException
     * @throws IOException
     */
        stats = new Stats();
        
        try {
            Writer file = new FileWriter("data.csv");
            file.write("background,pressed,coins,reaction-time\n");
            file.close();
        } catch (IOException iOException) {
        }
        
        
        winSize = new Vec2D(gc.getWidth(), gc.getHeight());
        wall = new Wall();
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
                block.add(new Play.Bg(new Image("res/bg-"+letter+".png"), nextwall));
                nextwall = false;
            }
            else {
                nextwall = true;
            }
            
        }
        
        player = new Player();
        
        try {
            Wini ini = new Wini(new File("settings.ini"));
            isMusic = ini.get("Sound", "music", boolean.class);
            isSFX = ini.get("Sound", "sfx", boolean.class);
        } catch (IOException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        music = new Music("res/thefatrat-unity.ogg");
        sndCoin = new Sound("res/coin.wav");
        sndLoad = new Sound("res/load.wav");
        sndFury = new Sound("res/fury.wav");
        sndWall = new Sound("res/wall.wav");
        
        
    }
    
    /**
     * Draws everything on screen
     * @param gc  the game container
     * @param sbg the state based game
     * @param g   the Graphics object
     * @throws SlickException 
     */
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        
        block.get(block.count).image.draw(block.pos.x,block.pos.y);
        if (block.count < block.size() - 1) {
            block.get(block.count + 1).image.draw(winSize.x + block.pos.x,block.pos.y);
        }
        else {
            block.get(0).image.draw(winSize.x + block.pos.x,block.pos.y);
        }
        
        if (wall.broken != -1) {
            wall.imgBroken[wall.broken].draw(wall.pos.x, wall.pos.y);
        }
        else{
            wall.image.draw(wall.pos.x, wall.pos.y);
        }
        
        
        if (player.fury==false) {
            if (player.furyLoad) {
                if (player.furyLoadTime < 125) {
                    player.furyAnimation[0].draw(player.pos.x, player.pos.y);
                }
                else if (player.furyLoadTime < 250) {
                    player.furyAnimation[1].draw(player.pos.x, player.pos.y);
                }
                else if (player.furyLoadTime < 375) {
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
        
        
        
        for (Coin coin:activeCoins) {
            coin.images[coinFrame].draw(coin.pos.x, coin.pos.y);
        }
        
        new Image("res/test_hud.png").draw();
        
        g.setFont(font);
        g.setColor(Color.yellow);
        g.drawString(Integer.toString(player.score),515,56);
        
    }
    
    /**
     * Updates all the variables
     * @param gc    the game container
     * @param sbg   the state based game
     * @param delta the time spent since the last call of the update method
     * @throws SlickException 
     */
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        if (!music.playing() && Game.ISMUSIC) {
            music.loop();
            music.setVolume(0.5F);
        }
        block.pos.x -= delta * block.speed;
        Input input = gc.getInput();
        
        
        if (input.isKeyPressed(Input.KEY_A)) {
            gc.setFullscreen(true);
        }
        if (input.isKeyPressed(Input.KEY_Z)) {
            gc.setFullscreen(false);
        }
        if (input.isKeyPressed(Input.KEY_ESCAPE)) {
            music.stop();
            sbg.enterState(0);
        }
        
        
        /*
        Display next Bg
        */
        if (block.pos.x < -winSize.x) {
            block.pos.x = 0;
            block.count += 1;
            stats.write(block.count);
            stats.reset();
            if (block.count == block.size()) {
                block.count = 0;
            }
        }
        
        /*
        Display the wall
        */
        if (block.get((block.count + 1) % block.size()).wall) { // apparition du mur
            wall.pos.x = winSize.x + block.pos.x;
            stats.timer += delta;
        }
        else {
            if (wall.pos.x > -64 && wall.pos.x < 640) {
                wall.pos.x -= delta * wall.speed;
            }
            else {
                wall.pos.x = 640;
                wall.broken = -1;
            }
            
        }        
        
        /*
        Input Up-Down
        */
        if (input.isKeyPressed(Input.KEY_UP) && player.row > 0) {
            player.row -= 1;
            player.animate = true;
        }
        if (input.isKeyPressed(Input.KEY_DOWN) && player.row < 2) {
            player.row += 1;
            player.animate=true;
        }
        
        /*
        Up-Down animation
        */
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
        
        /*
        Destroy the wall if on-screen
        */
        if (wall.pos.x<640 && wall.pos.x>-64) {
            destroyWall();
        }
        
        /*
        Create, move, animate and destroy coins
        */
        coinTime += delta;
        if (coinTime > coinWait) {
            createCoin();
            coinTime = 0;
        }
        for (Coin coin : activeCoins) {
            coin.pos.x -= delta*coin.speed;
        }
        coinAnim += delta;
        if (coinAnim > 100) {
            coinFrame = (coinFrame+1) % 4;
            coinAnim = 0;
        }
        destroyCoin();
        
        /*
        Load Fury
        */
        if (input.isKeyPressed(Input.KEY_SPACE) && player.fury == false) {
            sndLoad.play();
            player.furyLoad = true;
            stats.hasPressed = true;
            stats.tpsReac = stats.timer;
        }
        if (player.furyLoad) {
            player.furyLoadTime += delta;
        }
        
        /*
        Initiate Fury
        */
        if (player.furyLoadTime > player.furyLoadWait) {
            if (Game.ISSFX) {
                sndFury.play();
            }
            player.furyLoad = false;
            player.fury = true;
            player.furyLoadTime = 0;
        }
        if (player.fury) {
            player.furyTime += delta;
            player.furyAnimTime += delta;
        }
        
        /*
        Animate Fury
        */
        if (player.furyAnimTime > 75) {
            player.furySpr = (player.furySpr + 1) % 4;
            player.furyAnimTime = 0;
        }
        if (player.furyTime > player.furyWait ) {
            player.fury = false;
            player.furyTime = 0;
        }
        
        
        
    }
    
    public int getID() {
        /**
         * @return the id state of the class
         */
        return 1;
    }
    
}