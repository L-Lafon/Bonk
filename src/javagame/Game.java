package javagame;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
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
    
    public static boolean ISFULLSCREEN, ISMUSIC, ISSFX;
    public static int LEVEL = 1;
    public static int SUBLEVEL = 0;
    
    /**
     * 
     * @param gamename 
     */
    public Game(String gamename) {
        super(gamename);
        this.addState(new Menu(MENU));
        this.addState(new Play(PLAY));
        this.addState(new Instr(INSTR));
        this.addState(new Config(CONFIG));
        this.addState(new LevelSelect(LEVELSELECT));
        this.addState(new Pause(PAUSE));
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
        this.enterState(MENU);
    }
    
    /**
     * 
     * @param args 
     * @throws java.io.IOException 
     */
    public static void main(String[] args) throws IOException {
        AppGameContainer appgc;
        Wini ini = new Wini(new File("settings.ini"));
        ISFULLSCREEN = ini.get("Display", "fullscreen", boolean.class);
        ISMUSIC = ini.get("Sound", "music", boolean.class);
        ISSFX = ini.get("Sound", "sfx", boolean.class);
        
        try {
            Writer file = new FileWriter("data.csv");
            file.write("block,condition,background,pressed,coins,reaction-time\n");
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
