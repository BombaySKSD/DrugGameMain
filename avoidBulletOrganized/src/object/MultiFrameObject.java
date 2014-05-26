package object;

import java.awt.Image;

public class MultiFrameObject extends SingleObject{
	private long lifeSpan=1;
	private long initialTime;

	public MultiFrameObject(double x, double y,Image image,long lifeSpan){
		super(x, y, 0, 0, image);
		this.lifeSpan=lifeSpan;
		initialTime=System.currentTimeMillis();
	}
	
	@Override
	public void move() {
		super.move();
	}
	final public boolean shouldRemove(){
		return lifeSpan+initialTime<=System.currentTimeMillis();
	}
}
