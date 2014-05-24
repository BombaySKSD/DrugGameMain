import java.awt.Image;

public class Bullet {
	double x;
	double y;
	double angle; // 0�� ��� ������, ����� ��� ������ ����, ������ ��� ���� ����
	double speed;

	Bullet(double x, double y, double angle, double speed) {
		this.x = x;
		this.y = y;
		this.angle = angle;
		this.speed = speed;
	}

	public void move() {
		x += speed * Math.sin(angle);
		y += speed * Math.cos(angle);
	}
	public boolean inRange(double x,double y,Image img1,Image img2){
		return Math.abs((x + img1.getWidth(null) / 2)
				- (this.x + img2.getWidth(null) / 2)) < (img2.getWidth(null) / 2 + img1.getWidth(null) / 2)
				&& Math.abs((y + img1.getHeight(null) / 2) - (this.y + img2.getHeight(null) / 2))
				< (img2.getHeight(null) / 2 + img1.getHeight(null) / 2); 
	}
	
}

