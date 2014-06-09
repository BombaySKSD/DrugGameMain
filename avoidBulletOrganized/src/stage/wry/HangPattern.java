package stage.wry;

import java.awt.Image;
import java.util.Random;

import object.Pattern;
import object.SingleObject;
import object.Stage;

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
		if(bl.inArea(0, stage.getWidth(), 0, stage.getHeight()-80-bl.image.getHeight(null))){
			return false;
		}else{
			stage.hangCrashed=true;
			return true;
		}
	}
	@Override
	public boolean inRange(SingleObject bl) {
		return false;
	}
	@Override
	public boolean createWhen() {
		return stage.second()>=1 && !created;
	}
	@Override
	public SingleObject create() {
		created=true;
		return new Hang(stage.getWidth()/2-stage.hang_img.getWidth(null)/2-50,0,
				stage.refrigerator_vel,stage.hang_img);
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
}

//hang fragments
class FragmentPattern extends Pattern{
	private int total=0;
	private WryStage stage;
	
	protected FragmentPattern(WryStage stage){
		this.stage=stage;
	}
	
	@Override
	public void whenCrash() {}
	
	@Override
	public boolean removeWhen(SingleObject bl) {
		return !bl.inArea(0, stage.getWidth(), 0, stage.getHeight()*2);
	}
	
	@Override
	public boolean inRange(SingleObject bl) {
		return false;
	}
	
	@Override
	public boolean createWhen() {
		return total<=8 && stage.hangCrashed;
	}
	
	@Override
	public SingleObject create() {
		total++;
		return new Fragments(stage.getWidth()/2-stage.hang_frag.getWidth(null)/2-50, 
				stage.getHeight()-80-stage.hang_frag.getHeight(null), 
				Math.PI*(0.75+0.5*Math.random()), Math.random()*1.5+0.8, stage.hang_frag, stage);
	}
	//each frags
	class Fragments extends SingleObject{
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

		public Fragments(double x, double y, double angle, double speed, Image image,Stage stage) {
			super(x, y, angle, speed, image);
			this.stage=stage;
			vx=speed*Math.sin(angle);
			vy=speed*Math.cos(angle);
		}	
	}
}

//hang contents
class HangContent extends Pattern{
	private int total=0;
	private WryStage stage;
	
	protected HangContent(WryStage stage){
		this.stage=stage;
	}
	
	@Override
	public void whenCrash() {}
	
	@Override
	public boolean removeWhen(SingleObject bl) {
		return !bl.inArea(0, stage.getWidth(), 0, stage.getHeight()*2);
	}
	
	@Override
	public boolean inRange(SingleObject bl) {
		return false;
	}
	
	@Override
	public boolean createWhen() {
		return total<=100 && stage.hangCrashed;
	}
	
	@Override
	public SingleObject create() {
		total++;
		return new Content(stage.getWidth()/2-stage.hang_content.getWidth(null)/2-50, 
				stage.getHeight()-100-stage.hang_content.getHeight(null), 
				Math.PI*(0.75+0.5*Math.random()), Math.random()+0.5, stage.hang_content, stage);
	}
	//each contents
		class Content extends SingleObject{
			Random rand=new Random();
			double acc=0.005;
			double vx,vy;
			Stage stage;
			boolean fallen=false;
			
			@Override
			public void move() {
				if(fallen){
					return;
				}else if(y>=stage.getHeight()-95){
					fallen=true;
				}
				x+=vx;
				y+=vy;
				vy+=acc;
				rotate(0.07*rand.nextDouble());
			}

			public Content(double x, double y, double angle, double speed, Image image,Stage stage) {
				super(x, y, angle, speed, image);
				this.stage=stage;
				vx=speed*Math.sin(angle);
				vy=speed*Math.cos(angle);
			}	
		}
}


