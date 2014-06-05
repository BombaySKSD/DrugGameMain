package stage.wry;

import java.awt.Image;
import java.util.Random;

import javax.swing.ImageIcon;

import object.Pattern;
import object.SingleObject;

public class TornadoPattern extends Pattern {
	private static Random rand=new Random();
	
	private WryStage stage;
	private int where,xvel;
	
	public TornadoPattern(WryStage stage){
		this.stage=stage;
		where=stage.getWidth()/2;
	}
	
	@Override
	public SingleObject create() {
		where+=xvel;
		xvel+=rand.nextBoolean()?3:-3;
		xvel*=Math.abs(xvel)>10?0.8:2;
		xvel*=0<where && where<stage.getWidth()?1:-1;
		return new TornadoPart(where,stage.getHeight()-90,rand.nextInt(8));
	}

	@Override
	public boolean createWhen() {
		return stage.count%15==0 && (6<stage.second() && stage.second()<8);
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

	private final static int numImage=8,numSize=50;
	private final static int initialWidth=50,initialHeight=25,diff=4;
	private static Image[][] imageSet=new Image[numImage][numSize];
	
	private int whichImage,whichSize;
	
	static{
		for(int i=0;i<numImage;i++){
			Image tmp=getImage("tornado/tornado"+(i+1)+".png");
			for(int j=0;j<numSize;j++){
				imageSet[i][j]=tmp.getScaledInstance(initialWidth+j*diff*2, initialHeight+j*diff, Image.SCALE_FAST);
			}
		}
	}
	public TornadoPart(double x, double y,int whichImage) {
		super(x, y, Math.PI, 1, imageSet[whichImage][0]);
		this.whichImage=whichImage;
		whichSize=0;
	}
	
	@Override
	public void move() {
		super.move();
		image=imageSet[(++whichImage/6)%numImage][(++whichSize/10)%numSize];
		x-=image.getWidth(null)/200;
	}
	
	private static Image getImage(String path){
		return new ImageIcon(path).getImage();
	}
	
}
