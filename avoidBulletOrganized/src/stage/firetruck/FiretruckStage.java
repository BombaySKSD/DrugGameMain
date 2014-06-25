package stage.firetruck;

import java.awt.Font;
import java.awt.Image;
import java.util.*;

import object.Pattern;
import object.SingleObject;
import object.Stage;
import stage.grade.Player;

public class FiretruckStage extends Stage {
	private final static int span = 20;
	public Player player;
	
	boolean boy_hit=false;
	int boy_damage=2;
	int boy_delay;
	public int traffic=0; // 0 : 빨간불, 1 : 노란불, 2 : 파란불
	public int getTraffic(){
		return traffic;
	}
	int traffic_count=count;
	
	Random cartypedecider = new Random();
	int cartype=1;
	double speed_gap;
	double car_y=460;
	boolean car_hit=false;
	double firetruck_damage;
	double firetruck_x=1200;
	double firetruck_y=440;
	double firetruck_time;
	boolean firetruck_hit=false;
	
	private Image
	player_img = getImage("player.gif"),
	ground_img = getImage("ground_moon.png").getScaledInstance(800,100,Image.SCALE_FAST),
	background_img = getImage("background_moon.png").getScaledInstance(800,600,Image.SCALE_FAST),
	darken_1 =  getImage("darken_1.gif").getScaledInstance(800,600,Image.SCALE_FAST),
	darken_2 =  getImage("darken_2.gif").getScaledInstance(800,600,Image.SCALE_FAST),
	darken_3 =  getImage("darken_3.gif").getScaledInstance(800,600,Image.SCALE_FAST),
	hit = getImage("explosion.gif").getScaledInstance(30,30,Image.SCALE_FAST),

	boy = getImage("picture_firetruck/boy.gif").getScaledInstance(12,20,Image.SCALE_FAST),
	
	bluelight = getImage("picture_firetruck/bluelight.gif").getScaledInstance(138,44,Image.SCALE_FAST),
	yellowlight = getImage("picture_firetruck/yellowlight.gif").getScaledInstance(138,44,Image.SCALE_FAST),
	redlight = getImage("picture_firetruck/redlight.gif").getScaledInstance(138,44,Image.SCALE_FAST),
	
	car1 = getImage("picture_firetruck/car1.gif").getScaledInstance(126,51,Image.SCALE_FAST),
	car2 = getImage("picture_firetruck/car2.gif").getScaledInstance(119,46,Image.SCALE_FAST),
	car3 = getImage("picture_firetruck/car3.gif").getScaledInstance(134,42,Image.SCALE_FAST),
	firetruck = getImage("picture_firetruck/firetruck.gif").getScaledInstance(200,70,Image.SCALE_FAST),
	afterimage = getImage("picture_firetruck/firetruck_afterimage.gif").getScaledInstance(220,70,Image.SCALE_FAST);
	
	class Car {
		double x;
		double y;
		double speed;
		Image image;
		
		Car (double x, double y, double speed, Image image) {
			this.x=x;
			this.y=y;
			this.speed=speed;
			this.image=image;
		}
		
		public void move() {
			switch (traffic) {
			case 2:
				x-=speed;
				break;
			case 1:
				x-=speed*0.5;
				break;
			default:
				break;
			}
		}
	}
	
	ArrayList<Car> carlist = new ArrayList<Car>();
	Car carobject;

	@Override
	public void init() {
		player = new Player(getWidth() / 2, getHeight() - 100, 0, 0, player_img, FiretruckStage.this);
		player.velocity=4.0;
		
		Random time = new Random();
		switch(getDifficulty()) {
		case EASY:
			boy_delay=600;
			speed_gap=0;
			firetruck_time=0.0;
			firetruck_damage=0.5;
			break;
		case MODERATE:
			boy_delay=400;
			speed_gap=0.1;
			firetruck_time=time.nextDouble()*0.5;
			firetruck_damage=0.7;
			break;
		case HARD:
			boy_delay=300;
			speed_gap=0.2;
			firetruck_time=time.nextDouble();
			firetruck_damage=0.9;
			break;
		}
		
		/**
		 *  pattern of player
		 */
		addPattern(new Pattern() {
			private boolean born=false;
			@Override
			public void whenCrash() {}
			@Override
			public boolean removeWhen(SingleObject bl) {
				return false;
			}
			@Override
			public boolean inRange(SingleObject bl) {
				return false;
			}
			@Override
			public boolean createWhen() {
				return born==false;
			}
			@Override
			public SingleObject create() {
				born=true;
				return player;
			}
		});
		
		/**
		 *  pattern of boy~
		 */
		addPattern(new Pattern() {
			int width=getWidth();
			int height=getHeight()-100;
			int girth=width+2*height;
			int boy_num=21;
			
			@Override
			public SingleObject create() {
				int position = (girth/(boy_num-1))*(count%boy_delay);
				int pos_x,pos_y;
				
				if (position<=height) {
					pos_x=0;
					pos_y=height-position;
				}
				else if (position<=width+height) {
					pos_x=position-height;
					pos_y=0;
				}
				else {
					pos_x=width;
					pos_y=position-width-height;
				}
				
				double ang=-(double)((count%boy_delay-10)/(16.0+second()%20))*Math.PI;
				return new SingleObject(pos_x,pos_y,ang,0.4+(second()%20.0)/9.0,boy);
				//return new SingleObject(pos_x,pos_y,(rand.nextDouble()-0.5)*Math.PI,0.4,boy);
			}

			@Override
			public boolean createWhen() {
				return count%boy_delay<boy_num && second()<=2.7;
			}

			@Override
			public boolean removeWhen(SingleObject bl) {
				return bl.x>=width+40 || bl.x<=-40
						|| bl.y>=height+140 || bl.y<=-40
						|| (boy_hit && bl.inRange(player));
			}

			@Override
			public void whenCrash() {
				boy_hit=true;
				gauge-=boy_damage;
			}

			@Override
			public boolean inRange(SingleObject bl) {
				return bl.inRange(player);
			}	
		});
	}

	@Override
	public void play() {
		updateAllPatterns();
		boy_hit=false;
		car_hit=false;
		
		traffic_count++;
		cartype=cartypedecider.nextInt(3)+1;
		if (second()<=15 && traffic_count>=400) {
			traffic = (traffic+2)%3;
			traffic_count=0;
		}
		if (second()>15.5) traffic=0;
		
		/**
		 * pattern of car
		 */
		if (second()>=5 && second()<=11.5 && count%300==0 && traffic!=0) {
			switch(cartype) {
			case 1:
				carobject = new Car(getWidth(),car_y,0.9-speed_gap,car1);
				carlist.add(carobject);
				break;
			case 2:
				carobject = new Car(getWidth(),car_y,0.9,car2);
				carlist.add(carobject);
				break;
			case 3:
				carobject = new Car(getWidth(),car_y,0.9+speed_gap,car3);
				carlist.add(carobject);
				break;
			}
		}
		
		for (int i=0; i<carlist.size(); i++) {
			carobject = carlist.get(i);
			carobject.move();
			if (carobject.x<=-150)
				carlist.remove(i);
			if ( Crash(player.x,player.y,carobject.x,carobject.y,player_img,carobject.image) ) {
				gauge-=0.05;
				player.x-=2.5;
				car_hit=true;
			}
		}
		
		/**
		 * pattern of firetruck
		 */
		if (second()>=16) {
			firetruck_x=getWidth()-(second()-17.0+firetruck_time)*800;
		}
		if ( Crash(player.x,player.y,firetruck_x,firetruck_y,player_img,firetruck) ) {
			gauge-=firetruck_damage;
			player.x-=5.0;
			firetruck_hit=true;
		}
	}

	@Override
	public void draw() {
		int background_type;
		if (second()<=15.5) background_type=0;
		else if (second()<=16) background_type=1;
		else if (second()<=16.5) background_type=2;
		else background_type=3;
		
		switch (background_type) {
		case 0:
			drawImage(background_img, 0, 0);
			break;
		case 1:
			drawImage(background_img, 0, 0);
			drawImage(darken_1, 0, 0);
			break;
		case 2:
			drawImage(darken_1, 0, 0);
			drawImage(darken_2, 0, 0);
			break;
		case 3:
			drawImage(darken_2, 0, 0);
			drawImage(darken_3, 0, 0);
			break;
		}
		
		drawAllPatterns();
		
		for (int i=0; i<carlist.size(); i++) {
			carobject=carlist.get(i);
			drawImage(carobject.image,(int)carobject.x,(int)carobject.y);
		}
		if (car_hit) drawImage(hit,(int)player.x-15,(int)player.y-15);
		drawImage(afterimage,(int)firetruck_x,(int)firetruck_y);
		drawImage(firetruck,(int)firetruck_x,(int)firetruck_y);
		drawImage(ground_img, 0, getHeight()-90);
		
		if (second()>=5) {
			switch(traffic) {
			case 0:
				drawImage(redlight,600,80);
				break;
			case 1:
				drawImage(yellowlight,600,80);
				break;
			case 2:
				drawImage(bluelight,600,80);
				break;
			}
		}
		setFont(new Font("Default", Font.BOLD, 20));
		switch(getDifficulty()) {
		case EASY:
			drawString("쉬움",100,getHeight()/2+240);
			break;
		case MODERATE:
			drawString("보통",100,getHeight()/2+240);
			break;
		case HARD:
			drawString("어려움",100,getHeight()/2+240);
			break;
		}
		drawString("TIME : " + String.format("%.0f",totalSecond()), getWidth()/2-200, getHeight()/2+240);
		drawString("HP : " + String.format("%.0f",gauge), getWidth()/2-200, getHeight()/2+260);
	}

	@Override
	public boolean continuing() {
		if(gauge<=0){
			stageFailed=true;
			return false;
		}
		return second()<=span;
	}
	
	public boolean Crash(double x1, double y1, double x2, double y2, Image img1, Image img2) {
		boolean check = false;
		if (Math.abs((x1 + img1.getWidth(null) / 2)
				- (x2 + img2.getWidth(null) / 2)) < (img2.getWidth(null) / 2 + img1.getWidth(null) / 2)
				&& Math.abs((y1 + img1.getHeight(null) / 2) - (y2 + img2.getHeight(null) / 2))
				< (img2.getHeight(null) / 2 + img1.getHeight(null) / 2))
			check = true;
		else
			check = false;
		
		return check;
	}

}
