package stage.wry;

import java.awt.Image;
import java.util.Random;

import object.Pattern;
import object.SingleObject;
import object.Stage;

//fridge falling
public class FridgePattern extends Pattern{
	private boolean created=false;
	private WryStage stage;
	
	protected FridgePattern(WryStage stage){
		this.stage=stage;
	}
	
	@Override
	public void whenCrash() {}
	@Override
	public boolean removeWhen(SingleObject bl) {
		if(stage.refrigeratorFallen || bl.inArea(0, stage.getWidth(), 0, stage.getHeight()-100-bl.image.getHeight(null))){
			return false;
		}else{
			stage.refrigeratorFallen=true;
			return true;
		}
	}
	@Override
	public boolean inRange(SingleObject bl) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean createWhen() {
		return stage.second()>14 && !created;
	}
	@Override
	public SingleObject create() {
		created=true;
		return new Refrigerator(stage.getWidth()/2-stage.refrigerator_img.getWidth(null)/2-50,0,
				stage.refrigerator_vel,stage.refrigerator_img);
	}
}

//fallen fridge
class FallenFridge extends Pattern{
	private boolean created=false;
	private WryStage stage;
	
	protected FallenFridge(WryStage stage){
		this.stage=stage;
	}
	
	@Override
	public void whenCrash() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean removeWhen(SingleObject bl) {
		// TODO Auto-generated method stub
		return !bl.inArea(0, stage.getWidth(), 0, stage.getHeight()*2);
	}
	
	@Override
	public boolean inRange(SingleObject bl) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean createWhen() {
		return stage.refrigeratorFallen && created==false;
	}
	
	@Override
	public SingleObject create() {
		created=true;
		return new Refrigerator(stage.getWidth()/2-stage.refrigerator_broken.getWidth(null)/2-50,
				stage.getHeight()-240-stage.refrigerator_broken.getHeight(null),
				stage.refrigerator_vel/100,stage.refrigerator_broken);
	}
}

//fridge door
class FridgeDoor extends Pattern{
	private boolean created=false;
	private WryStage stage;
	
	protected FridgeDoor(WryStage stage){
		this.stage=stage;
	}
	@Override
	public void whenCrash() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public boolean removeWhen(SingleObject bl) {
		return !bl.inArea(-100, stage.getWidth()+100, 0, stage.getHeight());
	}
	
	@Override
	public boolean inRange(SingleObject bl) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean createWhen() {
		return created==false && stage.refrigeratorFallen;
	}
	
	@Override
	public SingleObject create() {
		Random rand=new Random();
		Sighe bl=new Sighe(stage.getWidth()/2, stage.getHeight()-200, 
				Math.PI*(0.75+0.5*rand.nextDouble()), rand.nextDouble()*1.5+0.8, stage.refrigerator_door,stage);
		created=true;
		return bl;
	}
}

//sighe bullets
class SighePattern extends Pattern{
	int total=0;
	private WryStage stage;
	
	protected SighePattern(WryStage stage){
		this.stage=stage;
	}
	@Override
	public void whenCrash() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public boolean removeWhen(SingleObject bl) {
		return !bl.inArea(-100, stage.getWidth()+100, 0, stage.getHeight());
	}
	
	@Override
	public boolean inRange(SingleObject bl) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean createWhen() {
		return total<17 && stage.refrigeratorFallen;
	}
	
	@Override
	public SingleObject create() {
		Random rand=new Random();
		Sighe bl=new Sighe(stage.getWidth()/2, stage.getHeight()-100, 
				Math.PI*(0.75+0.5*rand.nextDouble()), rand.nextDouble()*1.5+0.8, stage.sighe,stage);
		total++;
		return bl;
	}
}

//sighe
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

//fridge
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