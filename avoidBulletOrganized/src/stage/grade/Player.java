package stage.grade;

import java.awt.Image;

import object.SingleObject;
import object.Stage;


public class Player extends SingleObject{
	private Stage stage;
	private int verticalSpeed=-100;
	public Player(double x, double y, double angle, double speed, Image image,Stage stage) {
		super(x, y, angle, speed, image);
		this.stage=stage;
	}

	@Override
	public void move() {
		if(stage.count%5!=0)
			return;
		
		if (stage.keyLeft && x > 9) {
			x -= 7;
		}
		if (stage.keyRight && x + image.getWidth(null)+5 < stage.getWidth()) {
			x += 7;
		}
		if (stage.keyUp && verticalSpeed==-100) {
			verticalSpeed=15;
		}
		if(verticalSpeed<=-14)
			verticalSpeed=-100;
		else if(verticalSpeed!=-100){
			verticalSpeed--;
			y-=verticalSpeed;
		}
	}

}