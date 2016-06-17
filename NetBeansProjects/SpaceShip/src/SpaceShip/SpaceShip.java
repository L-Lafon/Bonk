package SpaceShip;

import java.util.ArrayList; // structures extensibles
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

    private Image background;
    private Image background2;
    private Image background3;
    private Image background4;
    
    private Image[] bgList = new Image[4];
    
    private Image spaceship;
    float spaceshipX = 100;
    float spaceshipY = 100;
    float spaceshipHeight = 50;
    float spaceshipDx = 0.5F;
    float spaceshipDy = 0.5F;
    private Image sampleEnemy;
    private Image sampleFire;
    private float bgpos = 0;

    public SpaceShip() {
        super("SpaceShip");
    }
    
    @Override
    public void init(GameContainer container) throws SlickException {
    	background = new Image("data/background.png");
        background2 = new Image("data/background2.png");
        background3 = new Image("data/background3.png");
        background4 = new Image("data/background4.png");
        
        bgList[0] = 
        
    	spaceship = new Image("data/spaceship.png");
    	sampleEnemy = new Image("data/enemy.png");
    	sampleFire = new Image("data/fire.png");
	activeFireLocations = new ArrayList<SpaceShip.Location>();
    }

    float enemyEveryT = 1000;
    float lastEnemyTime = 0;
    float enemySpeed = 0.5F;
	
    float fireEveryT = 100;
    float lastFireTime = 0;
    float fireSpeed = 0.5F;
    
    @Override
    public void update(GameContainer container, int delta)
            throws SlickException {
        bgpos = bgpos - 1;
        if (bgpos == -640) {
            bgpos = 0;
        }
    	
    	if(container.getInput().isKeyDown(Input.KEY_LEFT)) {
            spaceshipX -= delta * spaceshipDx;
    	}
    	if(container.getInput().isKeyDown(Input.KEY_RIGHT)) {
            spaceshipX += delta * spaceshipDx;
    	}
    	if(container.getInput().isKeyDown(Input.KEY_UP)) {
            spaceshipY -= delta * spaceshipDy;
    	}
    	if(container.getInput().isKeyDown(Input.KEY_DOWN)) {
            spaceshipY += delta * spaceshipDy;
    	}
    	
    	lastEnemyTime += delta;
    	if(lastEnemyTime > enemyEveryT) {
            createEnemy();
            lastEnemyTime = 0;
            System.out.println("creating enemy!");
    	}
    	
    	lastFireTime += delta;
    	if(container.getInput().isKeyDown(Input.KEY_SPACE)) {
            if(lastFireTime > fireEveryT) {
	    	createFire();
	    	lastFireTime = 0;
	    	System.out.println("creating fire!");
	    }
    	}
        
    	updateEnemies(delta);
    	updateFires(delta);
    	killEnemiesIfNecessary();
    }

    private void killEnemiesIfNecessary() {
    	List<Integer> enemiesToBeRemoved = new ArrayList<Integer>();
    	List<Location> firesToBeRemoved = new ArrayList<SpaceShip.Location>();
    	
    	for(Location fireLocation: activeFireLocations) {
            for (int enemyIndex = 0; enemyIndex < enemyLocations.size(); ++enemyIndex) {
		Polygon firePoly = new Polygon(
                    new float[] {
			fireLocation.x, fireLocation.y, 
			fireLocation.x, fireLocation.y + fireHeight, 
			fireLocation.x + fireWidth, fireLocation.y + fireHeight, 
			fireLocation.x + fireWidth, fireLocation.y 
                    }
		);
		Location enemyLocation = enemyLocations.get(enemyIndex);
		Polygon enemyPoly = new Polygon(
                    new float[] {
			enemyLocation.x, enemyLocation.y, 
			enemyLocation.x, enemyLocation.y + enemyHeight, 
			enemyLocation.x + enemyWidth, enemyLocation.y + enemyHeight, 
			enemyLocation.x + enemyWidth, enemyLocation.y 
                    }
		);
				
		if(firePoly.intersects(enemyPoly)) {
                    enemiesToBeRemoved.add(enemyIndex);
                    firesToBeRemoved.add(fireLocation);
		}
            }
    	}
    	
    	activeFireLocations.removeAll(firesToBeRemoved);
    	for(Integer enemyIndex: enemiesToBeRemoved) {
            activeEnemies.remove((int)enemyIndex);
            enemyLocations.remove((int)enemyIndex);
            System.out.println("killed one!");
    	}
    }

    private void updateFires(int delta) {
    	List<Location> toBeRemoved = new ArrayList<SpaceShip.Location>();
    	
    	for(Location fl: activeFireLocations) {
            fl.x += delta * fireSpeed;
            if(fl.x > windowWidth) {
    		toBeRemoved.add(fl);
            }
    	}
    	
    	activeFireLocations.removeAll(toBeRemoved);
    }

    private void updateEnemies(int delta) {
    	List<Integer> toBeRemoved = new ArrayList<Integer>();
	for(int i = 0; i < activeEnemies.size(); ++i) {
            enemyLocations.get(i).x -= enemySpeed * delta;
            if(enemyLocations.get(i).x < 0) {
                toBeRemoved.add(i);
            }
	}
		
	for(Integer i: toBeRemoved) {
            activeEnemies.remove((int)i);
            enemyLocations.remove((int)i);
	}

    }

    List<Image> activeEnemies = new ArrayList<Image>();
    
    class Location {
    	public Location(float x, float y) {
            this.x = x;
            this.y = y;
        }
	float x;
    	float y;
    }
    
    List<Location> enemyLocations = new ArrayList<Location>();
    
    float highEnemySpawnY = 100;
    float lowEnemySpawnY = 300;
    boolean lastEnemyWasHigh;
    private int windowWidth;
    private List<Location> activeFireLocations;
    private float spaceshipWidth = 50;
    private float enemyWidth = 40;
    private float enemyHeight = 40;
    private float fireWidth = 10;
    private float fireHeight = 5;
    
    private void createFire() {
	Location newFireLocation = new Location(spaceshipX + spaceshipWidth, spaceshipY + spaceshipHeight * .5f);
	activeFireLocations.add(newFireLocation);
    }

    private void createEnemy() {
    	Image newEnemy = sampleEnemy.copy();
    	Location newEnemyLocation = new Location(windowWidth, 100);
    	if(lastEnemyWasHigh) {
            lastEnemyWasHigh = false;
            newEnemyLocation.y = lowEnemySpawnY;
    	} else {
            lastEnemyWasHigh = true;
            newEnemyLocation.y = highEnemySpawnY;
    	}
    	
    	activeEnemies.add(newEnemy);
    	enemyLocations.add(newEnemyLocation);
    }

    @Override
    public void render(GameContainer container, Graphics g)
            throws SlickException {
	windowWidth = container.getWidth();
	//draw the background
    	background.draw(bgpos, 0);
    	// draw the spaceship
	spaceship.draw(spaceshipX, spaceshipY, spaceshipWidth, spaceshipHeight);
	// draw all the enemies
	for(int i = 0; i < activeEnemies.size(); ++i) {
            activeEnemies.get(i).draw(enemyLocations.get(i).x, enemyLocations.get(i).y, enemyWidth, enemyHeight);
	}
        // draw all the fires
	for(Location fl: activeFireLocations) {
            sampleFire.draw(fl.x, fl.y, fireWidth, fireHeight);
	}
    }

    public static void main(String[] args) {
        try {
            AppGameContainer app = new AppGameContainer(new SpaceShip());
            app.setDisplayMode(640, 400, false);
            app.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }
}