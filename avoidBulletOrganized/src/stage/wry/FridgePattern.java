package stage.wry;

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
		if(stage.refrigeratorFallen){
			System.out.println("adadsf");
			return false;
		}else{
			if(bl.inArea(0, stage.getWidth(), 0, stage.getHeight()-100-bl.image.getHeight(null)))
				return false;
			else{
				stage.refrigeratorFallen=true;
				return true;
			}
		}
	}
	@Override
	public boolean inRange(SingleObject bl) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean createWhen() {
		return stage.second()>0 && !created;
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
		return false;
	}
	
	@Override
	public boolean inRange(SingleObject bl) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean createWhen() {
		return stage.refrigeratorFallen;
	}
	
	@Override
	public SingleObject create() {
		// TODO Auto-generated method stub
		return new Refrigerator(stage.getWidth()/2-stage.refrigerator_broken.getWidth(null)/2,
				stage.getHeight()-90-stage.refrigerator_broken.getHeight(null),
			0,stage.refrigerator_broken);
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