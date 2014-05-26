package stage.grade;

import java.awt.Image;
import java.util.Random;

import object.Pattern;
import object.SingleObject;

class GradePattern extends Pattern{
	Image explosion,image;
	int startTime;
	double damage,gaugeDamage;
	double frequency=500;
	private ManagingGradeStage stage;
	
	public GradePattern(Image image,Image explosion,
			int startTime,double frequency,
			double damage,double gaugeDamage,
			ManagingGradeStage stage){
		this.image=image;
		this.explosion=explosion;
		this.startTime=startTime;
		this.damage=damage;
		this.gaugeDamage=gaugeDamage;
		this.frequency=frequency;
		this.stage=stage;
	}
	
	public SingleObject create() {
		Random rand=new Random();
		double num=rand.nextDouble();
		int pos=rand.nextInt(stage.getWidth());
		return new SingleObject(pos, 0, (Math.PI/2.0)*(num-0.5), frequency/stage.fps,image);
	}
	public boolean createWhen(){
		return stage.count%(10*stage.fps/frequency+1)==0 && stage.second()>=startTime;
	}
	public boolean removeWhen(SingleObject bl){
		return bl.inRange(stage.player)
				|| !bl.inArea(0,stage.getWidth(),0,stage.getHeight())
				|| stage.second()>17;
	}
	public void whenCrash(){
		stage.gauge-=gaugeDamage;
		stage.grade-=damage;
		stage.hit=true;
	}
	public boolean inRange(SingleObject bl){
		return bl.inRange(stage.player);
	}
}