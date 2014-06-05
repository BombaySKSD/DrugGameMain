package stage.wry;

import java.awt.Image;
import java.util.Random;

import object.Pattern;
import object.SingleObject;
import object.Stage;
import stage.grade.Player;

public class WryStage extends Stage {

	protected boolean refrigeratorFallen=false;
	
	protected Image 
	sighe=getImage("sighe.png").getScaledInstance(20, 30, Image.SCALE_FAST),
	player_img=getImage("player.gif"),
	ground_img=getImage("ground.gif"),
	background_img = getImage("background1.png"),
	refrigerator_img=getImage("refrigerator.jpg").getScaledInstance(100, 150, Image.SCALE_FAST), 
	refrigerator_broken=getImage("refrigerator_broken.png").getScaledInstance(100, 150, Image.SCALE_FAST),
	refrigerator_door=getImage("refrigerator_broken.png").getScaledInstance(100, 150, Image.SCALE_FAST);
	
	
	protected double refrigerator_vel=0.00001;
	
	@Override
	public void init() {
		
		//fridge falling
		addPattern(new FridgePattern(this));
		
		//fallen fridge
		addPattern(new FallenFridge(this));
		
		//sighe bullets
		addPattern(new SighePattern(this));
		
		//player
		addPattern(new Pattern() {
			private boolean born=false;
			@Override
			public void whenCrash() {}
			@Override
			public boolean removeWhen(SingleObject bl) {
				return second()>17;
			}
			@Override
			public boolean inRange(SingleObject bl) {
				return false;
			}
			@Override
			public boolean createWhen() {
				return born==false;
			}
			@Override
			public SingleObject create() {
				born=true;
				return new Player(50,getHeight()-100,0,0,player_img,WryStage.this);
			}
		});
	}

	@Override
	public void play() {
		updateAllPatterns();

	}

	@Override
	public void draw() {
		drawImage(background_img, 0, 0);
		drawAllPatterns();
		drawImage(ground_img, 0, getHeight()-90);

	}

	@Override
	public boolean continuing() {
		return true;
	}

}

class Sighe extends SingleObject{

	Random rand=new Random();
	double acc=0.005;
	double vx,vy;
	Stage stage;
	boolean impact=false;
	
	@Override
	public void move() {
		x+=vx;
		y+=vy;
		vy+=acc;
		if(!inArea(0, stage.getWidth(), -1, stage.getHeight()+100) && !impact){
			vx*=-1;
			impact=true;
		}
		rotate(0.07*rand.nextDouble());
	}

	public Sighe(double x, double y, double angle, double speed, Image image,Stage stage) {
		super(x, y, angle, speed, image);
		this.stage=stage;
		vx=speed*Math.sin(angle);
		vy=speed*Math.cos(angle);
	}	
}
class Refrigerator extends SingleObject{

	Random rand=new Random();
	double dacc=0;
	double acc=0;
	double v=0;
	
	@Override
	public void move() {
		y+=v;
		v+=acc;
		acc+=dacc;
	}

	public Refrigerator(double x, double y, double dacc, Image image) {
		super(x, y, 0, 0, image);
		this.dacc=dacc;
	}	
}
