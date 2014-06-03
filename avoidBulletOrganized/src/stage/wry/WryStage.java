package stage.wry;

import java.awt.Image;
import java.util.Random;

import object.Pattern;
import object.SingleObject;
import object.Stage;
import stage.grade.Player;

public class WryStage extends Stage {

	protected boolean refrigeratorFallen=false;
	
	private Image 
	sighe=getImage("sighe.png").getScaledInstance(20, 30, Image.SCALE_FAST),
	player_img=getImage("player.gif"),
	ground_img=getImage("ground.gif"),
	background_img = getImage("background1.png"),
	refrigerator_img=getImage("refrigerator.jpg").getScaledInstance(100, 150, Image.SCALE_FAST), 
	refrigerator_broken=getImage("refrigerator_broken.png").getScaledInstance(100, 150, Image.SCALE_FAST),
	refrigerator_door=getImage("refrigerator_door").getScaledInstance(50,75,Image.SCALE_FAST);
	
	private double refrigerator_vel=0.00001;
	
	@Override
	public void init() {
		addPattern(new Pattern() {
			boolean created=false;
			@Override
			public void whenCrash() {}
			@Override
			public boolean removeWhen(SingleObject bl) {
				if(refrigeratorFallen){
					return false;
				}else{
					if(bl.inArea(0, getWidth(), 0, getHeight()-100-bl.image.getHeight(null)))
						return false;
					else{
						refrigeratorFallen=true;
						return true;
					}
				}
			}
			@Override
			public boolean inRange(SingleObject bl) {
				return false;
			}
			@Override
			public boolean createWhen() {
				return second()>0 && !created;
			}
			@Override
			public SingleObject create() {
				created=true;
				return new Refrigerator(getWidth()/2-refrigerator_broken.getWidth(null)/2-50,0,
						refrigerator_vel,refrigerator_img);
			}
		});
		addPattern(new Pattern() {
			boolean created=false;
			@Override
			public void whenCrash() {}
			@Override
			public boolean removeWhen(SingleObject bl) {
				if(refrigeratorFallen){
					return false;
				}else{
					if(bl.inArea(0, getWidth(), 0, getHeight()-100-bl.image.getHeight(null)))
						return false;
					else{
						refrigeratorFallen=true;
						return true;
					}
				}
			}
			@Override
			public boolean inRange(SingleObject bl) {
				return false;
			}
			@Override
			public boolean createWhen() {
				return second()>0 && !created;
			}
			@Override
			public SingleObject create() {
				created=true;
				return new Sighe(getWidth()/2-refrigerator_door.getWidth(null)/2-50,0,
						refrigerator_vel,refrigerator_img);
			}
		});
		addPattern(new Pattern() {
			
			@Override
			public void whenCrash() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean removeWhen(SingleObject bl) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean inRange(SingleObject bl) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean createWhen() {
				return refrigeratorFallen;
			}
			
			@Override
			public SingleObject create() {
				// TODO Auto-generated method stub
				return new Refrigerator(getWidth()/2-refrigerator_broken.getWidth(null)/2,
					getHeight()-100-refrigerator_broken.getHeight(null),
					0,refrigerator_broken);
			}
		});
		addPattern(new Pattern() {
			int total=0;
			@Override
			public void whenCrash() {
				// TODO Auto-generated method stub
			}
			
			@Override
			public boolean removeWhen(SingleObject bl) {
				return !bl.inArea(-100, getWidth()+100, 0, getHeight());
			}
			
			@Override
			public boolean inRange(SingleObject bl) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean createWhen() {
				return total<17 && refrigeratorFallen;
			}
			
			@Override
			public SingleObject create() {
				Random rand=new Random();
				Sighe bl=new Sighe(getWidth()/2, getHeight()-100, 
						Math.PI*(0.75+0.5*rand.nextDouble()), rand.nextDouble()*1.5+0.8, sighe,WryStage.this);
				total++;
				return bl;
			}
		});
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
