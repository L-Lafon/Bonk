package SpaceShip;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.geom.Polygon;
 
public class SpaceShip extends BasicGame {

    class Vec2D {
	float x, y;
    	public Vec2D(float x, float y) { this.x = x; this.y = y; }
    }
    
    Vec2D winSize;
    int score = 0;

    Image skyImage;
    Vec2D skyPos = new Vec2D(0,0);
    float skySpeed = 0.05F;

    Image shipImage;
    Vec2D shipPos;
    Vec2D shipSize = new Vec2D(50,50);
    float shipSpeed = 0.5F;

    Image bombImage;
    Vec2D bombSize = new Vec2D(40,40);
    float bombStage = 0; 
    float bombSpeed = 0.25F;
    float bombWait = 900;
    float bombTime = 0;
    List<Vec2D> activeBombs = new ArrayList<Vec2D>();

    Image fireImage;
    Vec2D fireSize = new Vec2D(10,5);
    float fireSpeed = 0.5F;
    float fireWait = 100;
    float fireTime = 0;
    List<Vec2D> activeFires;
    
    public SpaceShip() {
        super("SpaceShip");
    }
    
    @Override
    public void init(GameContainer container) throws SlickException {
	winSize = new Vec2D(container.getWidth(), container.getHeight());
        shipPos = new Vec2D(0, 0.5F * (winSize.y - shipSize.y));
    	skyImage = new Image("data/background.png");
    	shipImage = new Image("data/spaceship.png");
    	bombImage = new Image("data/enemy.png");
    	fireImage = new Image("data/fire.png");
	activeFires = new ArrayList<Vec2D>();
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        score += delta; // automatic score increment per frame
        Input input = container.getInput();
    	if(input.isKeyDown(Input.KEY_LEFT)) { // move left
            shipPos.x = Math.max(0, shipPos.x - delta * shipSpeed);
    	}
    	if(input.isKeyDown(Input.KEY_RIGHT)) { // move right
            shipPos.x = Math.min(winSize.x - shipSize.x, shipPos.x + delta * shipSpeed);
    	}
    	if(input.isKeyDown(Input.KEY_UP)) { // move up
            shipPos.y = Math.max(0, shipPos.y - delta * shipSpeed);
    	}
    	if(input.isKeyDown(Input.KEY_DOWN)) { // move down
            shipPos.y = Math.min(winSize.y - shipSize.y, shipPos.y + delta * shipSpeed);
    	}
        skyPos.x -= delta * skySpeed;
        if(skyPos.x < -winSize.x) { // cyclic translation for sky
            skyPos.x = 0;
        }
    	bombTime += delta; // create new bomb after some idle time
    	if(bombTime > bombWait) {
            createBomb(); bombTime = 0;
    	}
    	fireTime += delta; // create new fire after some idle time
    	if(fireTime > fireWait && input.isKeyDown(Input.KEY_SPACE)) {
	    createFire(); fireTime = 0;
    	}
    	updateBombs(delta);
    	updateFires(delta);
    	destroyBombs(delta);
    	destroyShip(delta);
    }

    private void createBomb() {
    	Vec2D bomb = new Vec2D(winSize.x, winSize.y * (0.25F + 0.25F*bombStage));
        bombStage = (bombStage + 1) % 3;
    	activeBombs.add(bomb);
    }

    private void createFire() {
	Vec2D fire = new Vec2D(shipPos.x + shipSize.x, shipPos.y + 0.5F*shipSize.y);
	activeFires.add(fire);
    }

    private void updateBombs(int delta) {
    	List<Vec2D> inactiveBombs = new ArrayList<Vec2D>();
	for(Vec2D bomb: activeBombs) {
            bomb.x -= delta * bombSpeed;
            if(bomb.x < 0) {
                inactiveBombs.add(bomb);
                score -= 1000*delta; // score malus for missed bomb
            }
	}
	activeBombs.removeAll(inactiveBombs);
    }

    private void updateFires(int delta) {
    	List<Vec2D> inactiveFires = new ArrayList<Vec2D>();
    	for(Vec2D fire: activeFires) {
            fire.x += delta * fireSpeed;
            if(fire.x > winSize.x) {
    		inactiveFires.add(fire);
                score -= 100*delta; // score malus for useless fire
            }
    	}
    	activeFires.removeAll(inactiveFires);
    }

    private void destroyBombs(int delta) {
    	List<Vec2D> inactiveBombs = new ArrayList<Vec2D>();
    	List<Vec2D> inactiveFires = new ArrayList<Vec2D>();
    	
    	for(Vec2D fire: activeFires) {
            Polygon firePoly = new Polygon(
                new float[] {
                    fire.x, fire.y,
                    fire.x, fire.y + fireSize.y, 
                    fire.x + fireSize.x, fire.y + fireSize.y,
                    fire.x + fireSize.x, fire.y 
                }
            );
            for(Vec2D bomb: activeBombs) {
		Polygon bombPoly = new Polygon(
                    new float[] {
			bomb.x, bomb.y,
                        bomb.x, bomb.y + bombSize.y, 
			bomb.x + bombSize.x, bomb.y + bombSize.y, 
			bomb.x + bombSize.x, bomb.y 
                    }
		);
		if(firePoly.intersects(bombPoly)) {
                    inactiveBombs.add(bomb);
                    inactiveFires.add(fire);
                    score += 2000 * delta; // bonus score for destroyed bomb
		}
            }
    	}
    	activeFires.removeAll(inactiveFires);
    	activeBombs.removeAll(inactiveBombs);
    }

    private void destroyShip(int delta) {
        Polygon shipPoly = new Polygon(
            new float[] {
                shipPos.x, shipPos.y,
                shipPos.x, shipPos.y + shipSize.y, 
                shipPos.x + shipSize.x, shipPos.y + shipSize.y,
                shipPos.x + shipSize.x, shipPos.y 
            }
        );
        for(Vec2D bomb: activeBombs) {
            Polygon bombPoly = new Polygon(
                new float[] {
                    bomb.x, bomb.y,
                    bomb.x, bomb.y + bombSize.y, 
                    bomb.x + bombSize.x, bomb.y + bombSize.y, 
                    bomb.x + bombSize.x, bomb.y 
                }
            );
            if(shipPoly.intersects(bombPoly)) {
                activeBombs.remove(bomb);
                score -= 20000 * delta; // score malus for destroyed ship
                shipPos.x = 0; shipPos.y = 0.5F * (winSize.y - shipSize.y);
                return;
            }
        }
        
    }
    
    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
    	skyImage.draw(skyPos.x, skyPos.y, winSize.x, winSize.y);
    	skyImage.draw(skyPos.x + winSize.x, skyPos.y, winSize.x, winSize.y);
	shipImage.draw(shipPos.x, shipPos.y, shipSize.x, shipSize.y);
	for(Vec2D bomb: activeBombs) {
            bombImage.draw(bomb.x, bomb.y, bombSize.x, bombSize.y);
	}
	for(Vec2D fire: activeFires) {
            fireImage.draw(fire.x, fire.y, fireSize.x, fireSize.y);
	}
        g.drawString("Score = " + String.valueOf(score), 0.75F*winSize.x, 10);
    }

    public static void main(String[] args) {
        try {
            AppGameContainer app = new AppGameContainer(new SpaceShip());
            app.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }
}