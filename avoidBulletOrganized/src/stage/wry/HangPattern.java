package stage.wry;

import java.awt.Image;
import java.util.Random;

import object.Pattern;
import object.SingleObject;

//hang rolling
public class HangPattern extends Pattern{
	private boolean created=false;
	private WryStage stage;
	
	protected HangPattern(WryStage stage){
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

//hang fragments
class Fragments extends Pattern{
	private boolean created=false;
	private WryStage stage;
	
	protected Fragments(WryStage stage){
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


//each hang
class Hang extends SingleObject{

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

	public Hang(double x, double y, double dacc, Image image) {
		super(x, y, 0, 0, image);
		this.dacc=dacc;
	}	
}