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

public class PlayCoins extends BasicGameState {
    
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
        float furyWait, furyTime; //duree furie
        float furyLoadTime, furyLoadWait; //duree chargement furie
        Image furyImage;
        Image[] furyAnimation;
        Image[] flames;
        int furyStep; // étape d'animation chargement
        int score;
        int furySpr; // étape d'animation fury
        int furyState, furyStateMax;
        float furyAnimTime;
        boolean malus, bonus;
        float malusTime, malusWait;
        
        /**
         * Creates the main character
         * @throws SlickException 
         */
        public Player() throws SlickException {
            this.image = new Image("res/sprites/idee_perso.png");
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
            this.furyLoadTime = 0F; // timer de chargement de la furie
            this.furyLoadWait = 500F; // tps de chargement de la furie
            this.furyStep = 0;
            this.furyImage = new Image("res/sprites/idee_perso_fury.png");
            this.furyAnimation = new Image[] {
                new Image("res/sprites/idee_perso1.png"),
                new Image("res/sprites/idee_perso2.png"),
                new Image("res/sprites/idee_perso3.png"),
                new Image("res/sprites/idee_perso4.png"),
            };
            this.flames = new Image[] {
                new Image("res/sprites/flame_effect_1.png"),
                new Image("res/sprites/flame_effect_2.png"),
                new Image("res/sprites/flame_effect_3.png"),
                new Image("res/sprites/flame_effect_4.png"),
            };
            this.furySpr = 0;
            this.score = 0;
            this.furyAnimTime = 0F;
            this.malus = false;
            this.malusTime = 0F;
            this.malusWait = 500F;
            this.bonus = false;
            this.furyState = 0;
            this.furyStateMax = 5;//combien de pieces pour la furie
        }
    }
    
    class Bg {
        Image[] image;
        int index;
        float alpha;
        Vec2D pos;
        
        public Bg() throws SlickException {
            this.image = new Image[] {
                new Image("res/backgrounds/bg-E.png"),
                new Image("res/backgrounds/bg-W.png"),
                new Image("res/backgrounds/bg-Y.png"),
                new Image("res/backgrounds/bg-J.png"),
                new Image("res/backgrounds/bg-A.png"),
                new Image("res/backgrounds/bg-B.png"),
                new Image("res/backgrounds/bg-M.png"),
                new Image("res/backgrounds/bg-Q.png"),
            };
            this.pos = new Vec2D(0,120);
            this.index = (int) (Math.random()*this.image.length);
            this.alpha = 1f;
        }
    }
    
    class Wall {
        Vec2D pos;
        Image image;
        Image[] imgBroken;
        float speed;
        int broken;
        boolean isMoving;
        
        /**
         * Creates a wall with an image, a Vec2D (position) and a speed
         * @throws SlickException 
         */
        public Wall() throws SlickException {
            this.image = new Image("res/sprites/wall.png");
            this.imgBroken = new Image[] {
                new Image("res/sprites/wall_0.png"),
                new Image("res/sprites/wall_1.png"),
                new Image("res/sprites/wall_2.png")
            };
            this.pos = new Vec2D(640,120);
            this.speed = SPEED;
            this.broken = -1;
            this.isMoving = false;
        }
        
        public boolean isOnScreen() {
            return (this.pos.x < 640 && this.pos.x > -64);
        }
    }
    
    class Finish {
        Vec2D pos;
        Image image;
        boolean isMoving;
        
        public Finish() throws SlickException {
            this.image = new Image("res/sprites/finish.png");
            this.pos = new Vec2D(660,120);
            this.isMoving = false;
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
        public Coin(String letter) throws SlickException {
            this.images = new Image[] {
                new Image("res/sprites/coin1"+letter+".png"),
                new Image("res/sprites/coin2"+letter+".png"),
                new Image("res/sprites/coin3"+letter+".png"),
                new Image("res/sprites/coin4"+letter+".png"),
            };
            this.pos = new Vec2D(640,120);
            this.speed = SPEED;
        }
    }
    
    class CoinManager {
        List<Coin> activeCoins;
        float coinStage, coinTime, coinWait, coinAnim;
        int coinFrame;
        float coinGroupTime, coinGroupWait;
        int coinCount, coinMax;
        boolean coinPause, levelOver;
        String[] letters;
        int ltrIndex;
        
        public CoinManager(String[] letters) {
            this.letters = letters;
            this.coinStage = 0;
            this.activeCoins = new ArrayList<>();
            this.coinTime = 0;
            this.coinWait = 150;
            this.coinAnim = 0;
            this.coinFrame = 0;
            this.coinGroupTime = 0;
            this.coinGroupWait = 1000;
            this.coinCount = 0;
            this.coinMax = 5;
            this.ltrIndex = 0;
            this.levelOver = false;
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
        boolean hasPressed, hasBrokenWall;
        
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
            this.hasBrokenWall = false;
        }
        
        /**
         * Writes the stats to a .csv file
         * @param count index of the background
         */
        public void write(String letter) {
            try {
                Writer file = new BufferedWriter(new FileWriter("data.csv", true));
                file.append(Integer.toString(Game.LEVEL)+","
                            +names[Game.SUBLEVEL]+","
                            +letter+","
                            +Boolean.toString(this.hasPressed)+","
                            +Boolean.toString(this.hasBrokenWall)+","
                            +Integer.toString(this.coins)+","
                            +Float.toString(this.tpsReac)+"\n");
                file.close();
            } catch (IOException iOException) {
            }
        }
    }
    
    class HUD {
        Image front, back, coin;
        int gaugePosX, gaugePosY, gaugeWidth, gaugeHeight;
        public HUD() throws SlickException {
            this.front = new Image("res/sprites/hudFront.png");
            this.back = new Image("res/sprites/hudBack.png");
            this.gaugePosX = 32;
            this.gaugePosY = 32;
            this.gaugeWidth = 320;
            this.gaugeHeight = 64;
            this.coin = new Image("res/sprites/coin2Y.png");
        }
    }
    
    Vec2D winSize;
    
    Player player;
        
    Wall wall; // Make JAVA programming great again !
    
    Finish finish;
    
    CoinManager coins;
        
    TrueTypeFont font;
        
    Bg bg;
    
    HUD hud;
    
    String[] names, letters;
    
    //kou rouloukoukou rouloukoukou
    Stats stats; // #okjesors
    
    FileWriter statfile;
    
    Music music;
    Sound sndWall, sndCoin, sndFury, sndLoad, sndMalus, sndBonus;
    boolean isMusic, isSFX;
    
    Image imgMalus;
    Image imgBonus;
    
    /**
     * Creates the play screen
     * @param state the index of the state in the state-based game
     */
    public PlayCoins(int state) {
        
    }
    
    public void reset() throws SlickException {
        stats = new Stats();
        
        
        
        wall = new Wall();
        finish = new Finish();
                
        try {
            Wini lvlIni = new Wini(new File("levels.ini"));
            names = lvlIni.get(Integer.toString(Game.LEVEL), "order").split(",");
            letters = lvlIni.get(Integer.toString(Game.LEVEL), names[Game.SUBLEVEL]).split("");
        } catch (IOException ex) {
            letters = "EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE".split("");
            Logger.getLogger(PlayCoins.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        coins = new CoinManager(letters);
        
        
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
        
        if(!player.malus && wall.broken == -1 && playerPoly.intersects(wallPoly)) {
            if (player.fury==false) {
            player.malus = true;
            sndMalus.play();
            player.score = Math.max(0, player.score - 10);
            //sbg.enterState(0);
            }
            else {
                sndWall.play();
                sndBonus.play();
                player.bonus = true;
                wall.broken = player.row;
                player.score += 10;
                stats.hasBrokenWall = true;
                //wall.pos.x = -500;
            } 
        }
    }
    
    /**
     * Creates a coin in a random row and adds it to the list of active coins
     * @throws SlickException 
     */
    public void createCoin(String letter) throws SlickException {
        coins.coinStage = (int)(Math.random() * 3);
        Coin coin = new Coin(letter);
        coin.pos.x = 640;
        coin.pos.y = 120*(coins.coinStage+1)+35;
        coins.activeCoins.add(coin);
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
        
        for(Coin coin: coins.activeCoins) {
            
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
                    player.score +=1;
                    stats.coins +=1 ;
                    player.furyState = Math.min(player.furyStateMax, player.furyState + 1);
                    inactiveCoins.add(coin);
                    if (Game.ISSFX) {
                        sndCoin.play();
                    }
                }
            }
        }
        coins.activeCoins.removeAll(inactiveCoins);
    }
    
    public void drawString(Graphics g, String string, int xpos, int ypos, TrueTypeFont font, Color mainColor, Color outlineColor, int border) {
        g.setFont(font);
        g.setColor(outlineColor);
        for (int x = 1; x <= border; x++) {
            g.drawString(string, xpos-x, ypos-x);
            g.drawString(string, xpos-x, ypos);
            g.drawString(string, xpos-x, ypos+x);
            g.drawString(string, xpos, ypos-x);
            g.drawString(string, xpos, ypos+x);
            g.drawString(string, xpos+x, ypos-x);
            g.drawString(string, xpos+x, ypos);
            g.drawString(string, xpos+x, ypos+x);
        }
        
        g.setColor(mainColor);
        g.drawString(string, xpos, ypos);
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
        reset();
        bg = new Bg();
        player = new Player();
        winSize = new Vec2D(gc.getWidth(), gc.getHeight());
        hud = new HUD();
        
        // initialisation police de caractère
        Font awtFont = new Font("Lucida Sans", Font.BOLD, 24) {};
        font = new TrueTypeFont(awtFont, false);

        
        try {
            Wini ini = new Wini(new File("settings.ini"));
            isMusic = Boolean.valueOf(ini.get("Sound", "music"));
            isSFX = Boolean.valueOf(ini.get("Sound", "sfx"));
        } catch (IOException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        imgMalus = new Image("res/sprites/malus.png");
        imgBonus = new Image("res/sprites/bonus.png");
        
        //music = Game.OST[3];
        music = Game.OST[(int) (Math.random()*Game.OST.length)];
        sndCoin = new Sound("res/sounds/PickupCoin.wav");
        sndLoad = new Sound("res/sounds/load.wav");
        sndFury = new Sound("res/sounds/fury.wav");
        sndWall = new Sound("res/sounds/wall.wav");
        sndMalus = new Sound("res/sounds/malus.wav");
        sndBonus = new Sound("res/sounds/bonus.wav");
    }
    
    /**
     * Draws everything on screen
     * @param gc  the game container
     * @param sbg the state based game
     * @param g   the Graphics object
     * @throws SlickException 
     */
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        bg.image[(bg.index+1)%bg.image.length].draw(bg.pos.x,bg.pos.y);
        bg.image[(bg.index+1)%bg.image.length].draw(bg.pos.x+640, bg.pos.y);
        bg.image[bg.index].draw(bg.pos.x,bg.pos.y,new Color(1f,1f,1f,bg.alpha));
        bg.image[bg.index].draw(bg.pos.x+640, bg.pos.y,new Color(1f,1f,1f,bg.alpha));
        
        if (wall.broken != -1) {
            wall.imgBroken[wall.broken].draw(wall.pos.x, wall.pos.y);
        }
        else{
            wall.image.draw(wall.pos.x, wall.pos.y);
        }
        
        finish.image.draw(finish.pos.x,finish.pos.y);
        
        
        if (player.fury==false) {
            if (player.furyLoad) {
                int index = Math.min((int) (player.furyLoadTime/(player.furyLoadWait/4)),3);
                player.furyAnimation[index].draw(player.pos.x, player.pos.y);
            }
            else {
                player.image.draw(player.pos.x, player.pos.y);
            }
            
        } else {
            g.drawString("Fury activated", 30,30);
            
            player.flames[player.furySpr].draw(player.pos.x - 85, player.pos.y - 50);
            player.furyImage.draw(player.pos.x, player.pos.y);
        }
        
        for (Coin coin:coins.activeCoins) {
            coin.images[coins.coinFrame].draw(coin.pos.x, coin.pos.y);
        }
        
        hud.back.draw();
        if (player.furyState == player.furyStateMax) {
            g.setColor(Color.green);
        }
        else {
            g.setColor(Color.orange);
        }

        g.fillRect(hud.gaugePosX, hud.gaugePosY, (hud.gaugeWidth*player.furyState)/player.furyStateMax, hud.gaugeHeight);
        
        hud.front.draw();
        
        hud.coin.draw(384,40);
        
        g.setFont(font);
        g.setColor(Color.yellow);
        g.drawString("x " + Integer.toString(player.score),450,48);
        
        if (player.malus) {
            imgMalus.draw(player.pos.x + 150,player.pos.y + 25);
        }
        if (player.bonus) {
            imgBonus.draw(player.pos.x + 150,player.pos.y + 25);
        }
        
        if (finish.pos.x < -64) {
            drawString(g, "Congratulations !", 200, 200, font, Color.yellow, Color.black, 2);
            if (Game.SUBLEVEL == 2) {
                drawString(g, "Press SPACE to go back to", 200, 300, font, Color.yellow, Color.black, 2);
                drawString(g, "the main menu.", 200, 350, font, Color.yellow, Color.black, 2);
            }
            else {
                drawString(g, "Press SPACE when you're ready for", 200, 300, font, Color.yellow, Color.black, 2);
                drawString(g, "the next level.", 200, 350, font, Color.yellow, Color.black, 2);
            }
        }
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
        bg.pos.x -= delta * SPEED;
        bg.alpha -= delta * 0.0001;
        if (bg.alpha < 0) {
            bg.alpha = 1f;
            bg.index = (bg.index+1)%bg.image.length;
        }
        Input input = gc.getInput();
        
        if (input.isKeyPressed(Input.KEY_A)) {
            gc.setFullscreen(true);
        }
        if (input.isKeyPressed(Input.KEY_Z)) {
            gc.setFullscreen(false);
        }
        /*
        if (input.isKeyPressed(Input.KEY_ESCAPE)) {
            music.stop();
            sbg.enterState(0);
        }
        */
        
        /*
        Display next Bg
        */
        if (bg.pos.x < -winSize.x) {
            bg.pos.x = 0;
        }
        
        /*
        Display the wall
        */
        if (wall.isMoving) {
            wall.pos.x -= delta * SPEED;
            stats.timer += delta;
        }
        if (wall.pos.x < -64) {
            wall.pos.x = 640;
            wall.isMoving = false;
            wall.broken = -1;
        }
        
        /*
        Display the finish line
        */
        if (finish.isMoving) {
            finish.pos.x -= delta*SPEED;
        }
        if (finish.pos.x < -64) {
            finish.isMoving = false;
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
        if (player.malus) {
            player.malusTime += delta;
            if (player.malusTime > player.malusWait) {
                player.malus = false;
                player.malusTime = 0;
            }
        }
        if (player.bonus){
            player.malusTime += delta;
            if (player.malusTime > player.malusWait) {
                player.bonus = false;
                player.malusTime = 0;
            }
        }
        
        /*
        Create, move, animate and destroy coins
        */
        coins.coinTime += delta;
        coins.coinGroupTime += delta;
        if (coins.coinGroupTime > coins.coinGroupWait) {
            coins.coinPause = !coins.coinPause;
            if (!coins.coinPause) {
                System.out.println(coins.letters[coins.ltrIndex]);
                stats.write(coins.letters[coins.ltrIndex]);
                stats.reset();
                coins.ltrIndex += 1;
                if (coins.ltrIndex == coins.letters.length-1) {
                    //sbg.enterState(Game.PAUSE);
                    coins.levelOver = true;
                    finish.isMoving = true;
                }
            }
            
            //coins.ltrIndex = (coins.ltrIndex + 1) % coins.letters.length;
            coins.coinGroupTime = 0;
        }
        if (!coins.levelOver && !coins.coinPause && coins.coinTime > coins.coinWait) {
            if (coins.letters[coins.ltrIndex].charAt(0) != 'X') {
                createCoin(coins.letters[coins.ltrIndex]);
                coins.coinTime = 0;
            }
            else {
                wall.isMoving = true;
            }
        }
        for (Coin coin : coins.activeCoins) {
            coin.pos.x -= delta*coin.speed;
        }
        coins.coinAnim += delta;
        if (coins.coinAnim > 100) {
            coins.coinFrame = (coins.coinFrame+1) % 4;
            coins.coinAnim = 0;
        }
        destroyCoin();
        
        /*
        Load Fury
        */
        if (input.isKeyPressed(Input.KEY_SPACE)) {
            if (finish.pos.x < -64) {
                if (Game.SUBLEVEL < 2) {
                    Game.SUBLEVEL += 1;
                    reset();
                }
                else {
                    sbg.enterState(Game.MENU);
                }
            }
            else if (!player.fury && !player.furyLoad && player.furyState == player.furyStateMax) {
                sndLoad.play();
                player.furyLoad = true;
                stats.hasPressed = true;
                stats.tpsReac = stats.timer;
            }
            
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
            player.furyState = 0;
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