package stage.grade;

import java.awt.Image;

import object.SingleObject;
import object.Stage;


public class Player extends SingleObject{
	private Stage stage;
	private int verticalSpeed=-100;
	public double velocity;
	public Player(double x, double y, double angle, double speed, Image image,Stage stage) {
		super(x, y, angle, speed, image);
		this.stage=stage;
	}

	@Override
	public void move() {
		if(stage.count%5!=0)
			return;
		
		if (stage.keyLeft /*&& x > 9*/) {
			x -= velocity;
		}
		if (stage.keyRight /*&& x + image.getWidth(null)+5 < stage.getWidth()*/) {
			x += velocity;
		}
		if (x<3) x=3;
		if (x>stage.getWidth()-13) x=stage.getWidth()-13;
		
		if (stage.keyUp && verticalSpeed==-100) {
			verticalSpeed=15;
		}
		if(verticalSpeed<=-14)
			verticalSpeed=-100;
		else if(verticalSpeed!=-100 && stage.count%2==0){
			verticalSpeed--;
			y-=verticalSpeed;
		}
	}

}
