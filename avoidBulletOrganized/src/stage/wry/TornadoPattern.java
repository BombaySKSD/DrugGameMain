package stage.wry;

import java.awt.Image;
import java.util.Random;

import javax.swing.ImageIcon;

import object.Pattern;
import object.SingleObject;

public class TornadoPattern extends Pattern {
	private static Random rand=new Random();
	
	private WryStage stage;
	private int where;
	
	public TornadoPattern(WryStage stage){
		this.stage=stage;
		where=stage.getWidth()/2;
	}
	
	@Override
	public SingleObject create() {
		return new TornadoPart(where,stage.getHeight()-90,rand.nextInt(8));
	}

	@Override
	public boolean createWhen() {
		return stage.count%5==0;
	}

	@Override
	public boolean removeWhen(SingleObject bl) {
		return !bl.inArea(-100, stage.getWidth()+100, -100, stage.getHeight());
	}

	@Override
	public void whenCrash() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean inRange(SingleObject bl) {
		// TODO Auto-generated method stub
		return false;
	}
}

class TornadoPart extends SingleObject{

	private final static int numImage=8;
	private static Image[] imageSet=new Image[numImage];
	
	private int whichImage;
	
	static{
		for(int i=0;i<numImage;i++){
			imageSet[i]=getImage("tornado/tornado"+(i+1)+".png");
		}
	}
	public TornadoPart(double x, double y,int whichImage) {
		super(x, y, Math.PI, 5, imageSet[whichImage]);
		this.whichImage=whichImage;
	}
	
	@Override
	public void move() {
		super.move();
		image=imageSet[(++whichImage)%numImage];
	}
	
	private static Image getImage(String path){
		return new ImageIcon(path).getImage();
	}
	
}
