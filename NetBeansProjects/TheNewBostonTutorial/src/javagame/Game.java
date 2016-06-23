package javagame;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class Game extends StateBasedGame {
    
    public static final String gamename = "RUNNER";
    public static final int menu = 0;
    public static final int play = 1;
    public static final int instr = 2;
    public static final int config = 3;

    public Game(String gamename) {
        super(gamename);
        this.addState(new Menu(menu));
        this.addState(new Play(play));
        this.addState(new Instr(instr));
        this.addState(new Config(config));
    }
    
    public void initStatesList(GameContainer gc) throws SlickException {
        this.getState(menu).init(gc, this);
        this.getState(play).init(gc, this);
        this.enterState(menu);
    }
    
    public static void main(String[] args) {
        AppGameContainer appgc;
        try {
            appgc = new AppGameContainer(new Game(gamename));
            appgc.setShowFPS(false);
            appgc.setDisplayMode(640, 480, false);
            appgc.start();
        }
        catch(SlickException e) {
            e.printStackTrace();
        }
    }
    
}
