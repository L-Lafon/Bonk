package javagame;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.ini4j.Wini;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

/**
 * 
 * @author lea & corentin
 */
public class Game extends StateBasedGame {
    
    public static final String GAMENAME = "BONK";
    public static final int MENU = 0;
    public static final int PLAY = 1;
    public static final int INSTR = 2;
    public static final int CONFIG = 3;
    public static final int LEVELSELECT = 4;
    public static final int PAUSE = 5;
    public static final int CREDITS = 6;
    
    public static boolean ISFULLSCREEN, ISMUSIC, ISSFX;
    public static int LEVEL = 1;
    public static int SUBLEVEL = 0;
    public static boolean[] LEVELSPASSED = {false,false,false,false,false};
    
    static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
    static Date date = new Date();
    public static final String DATE = dateFormat.format(date);
    
    public static int HIGHSCORE;
        
    public static Music[] OST;
    
    /**
     * 
     * @param gamename 
     */
    public Game(String gamename) {
        super(gamename);
        this.addState(new Menu(MENU));
        this.addState(new PlayCoins(PLAY));
        this.addState(new Instr(INSTR));
        this.addState(new Config(CONFIG));
        this.addState(new LevelSelect(LEVELSELECT));
        this.addState(new Pause(PAUSE));
        this.addState(new Credits(CREDITS));
    }
    
    
    
    
    /**
     * 
     * @param gc the game container
     * @throws SlickException 
     */
    @Override
    public void initStatesList(GameContainer gc) throws SlickException {
        this.getState(MENU).init(gc, this);
        this.getState(PLAY).init(gc, this);
        this.getState(INSTR).init(gc, this);
        this.getState(CONFIG).init(gc, this);
        this.getState(LEVELSELECT).init(gc, this);
        this.getState(PAUSE).init(gc, this);
        this.getState(CREDITS).init(gc, this);
        this.enterState(MENU);
    }
    
    /**
     * 
     * @param args 
     * @throws java.io.IOException 
     */
    public static void main(String[] args) throws IOException, SlickException {
        AppGameContainer appgc;
        Wini ini = new Wini(new File("settings.ini"));
        Wini param = new Wini(new File("levels.ini"));
        ISFULLSCREEN = Boolean.valueOf(ini.get("Display", "fullscreen"));
        ISMUSIC = Boolean.valueOf(ini.get("Sound", "music"));
        ISSFX = Boolean.valueOf(ini.get("Sound", "sfx"));
        HIGHSCORE = Integer.valueOf(param.get("param", "highscore"));
        
        OST = new Music[] {
            new Music("res/music/thefatrat-unity.ogg"),
            new Music("res/music/thefatrat-monody.ogg"),
            new Music("res/music/thefatrat-dancing-naked.ogg"),
            new Music("res/music/thefatrat-windfall.ogg"),
            new Music("res/music/thefatrat-time-lapse.ogg"),
            new Music("res/music/thefatrat-infinite-power.ogg")
        };
        
        try {
            String filename = "data_"+DATE+".csv";
            Writer file = new FileWriter(filename);
            file.write("block,condition,letter,pressed,broken wall,coins,reaction time\n");
            file.close();
        } catch (IOException iOException) {
        }
        
        try {
            appgc = new AppGameContainer(new Game(GAMENAME));
            appgc.setShowFPS(false);
            appgc.setDisplayMode(640, 480, ISFULLSCREEN);
            appgc.start();
        }
        catch(SlickException e) {
            e.printStackTrace();
        }
    }
    
}
