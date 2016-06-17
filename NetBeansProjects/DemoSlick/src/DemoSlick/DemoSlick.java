package DemoSlick;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class DemoSlick extends BasicGame {

    static private int width = 1024, height = 576;
    private float xpos = width/2, ypos = height/2;
    private GameContainer container;
    private Image image;

    public DemoSlick() { // Equivalent du __init__
	super("DemoSlick");
    }

    @Override
    public void init(GameContainer container) throws SlickException {
    	this.container = container;
        this.image = new Image("res/ZB.gif");
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        g.setColor(new Color(255,0,0));
        g.drawLine(0.1F*width, 0.9F*height, 0.9F*width, 0.9F*height);
        g.setColor(new Color(0, 255, 0));
        g.drawString("Simple demo for slick2D", 0.25F*width, 0.9F*height);
        g.drawImage(image, xpos, ypos);
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        // coordonnees en float
        xpos = xpos + 0.5F*delta; if (xpos > width) xpos = 0;
        ypos = ypos + 0.03F*delta; if (ypos > height) ypos = 0;
    }

    @Override
    public void keyReleased(int key, char c) {
        System.out.println(c);
        if (Input.KEY_ESCAPE == key) {
            container.exit();
	}
    }

    public static void main(String[] args) throws SlickException {
        AppGameContainer game = new AppGameContainer(new DemoSlick());
        game.setDisplayMode(width, height, false);
	game.start();
    }
}