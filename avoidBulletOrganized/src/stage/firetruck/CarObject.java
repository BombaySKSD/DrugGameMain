package stage.firetruck;

import java.awt.Image;

import object.SingleObject;

public class CarObject extends SingleObject {

	double speed;
	double angle;
	int trafficlight;
	
	public CarObject(double x, double y, double angle, double speed, Image image, int trafficlight){
		super(x, y, angle, speed, image);
		this.speed = speed;
		this.angle = angle;
		this.trafficlight = trafficlight;
	}
}
