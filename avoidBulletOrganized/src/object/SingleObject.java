package object;
import java.awt.Image;


public class SingleObject {
	public double x;
	public double y;
	double angle; // 0일 경우 수직선, 양수일 경우 오른쪽 방향, 음수일 경우 왼쪽 방향
	double speed;
	double tiltedRad;
	public Image image;
	public double rpsec;//round per sec
	
	public SingleObject(double x, double y, double angle, double speed,Image image) {
		this.x = x;
		this.y = y;
		this.angle = angle;
		this.speed = speed;
		this.tiltedRad=angle;
		this.image=image;
		rpsec=20;
	}

	public void move() {
		x += speed * Math.sin(angle);
		y += speed * Math.cos(angle);
	}
	
	public void rotate(double rad){
		this.tiltedRad+=rad;
	}
	
	public boolean inRange(SingleObject player){
		return Math.abs((player.x + player.image.getWidth(null) / 2)
				- (this.x + image.getWidth(null) / 2)) < (image.getWidth(null) / 2 + player.image.getWidth(null) / 2)
				&& Math.abs((player.y + player.image.getHeight(null) / 2) - (this.y + image.getHeight(null) / 2))
				< (image.getHeight(null) / 2 + player.image.getHeight(null) / 2); 
	}
	public boolean inArea(int x1,int x2,int y1, int y2){
		return (x <= x2 && x >= x1 && y <= y2 && y >= y1);
	}	
}