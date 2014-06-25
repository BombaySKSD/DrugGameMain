package stage.alcohol;

import java.awt.Font;
import java.awt.Image;
import java.util.Random;

import object.Pattern;
import object.SingleObject;
import object.Stage;
import stage.grade.Player;

public class AlcoholStage extends Stage {
	private final static int span=20;

	public int drunken=0;
	public double damage_speed;
	public boolean ion_took=false;
	public Player player;
	boolean soju_ate=false;
	boolean dawn808_ate=false;
	
	boolean dawn808_created=false;
	boolean pocari_created=false;
	
	double totter_weight;
	
	Random special_time = new Random();
	int pocari_time=special_time.nextInt(5)+5;
	int dawn808_time=special_time.nextInt(5)+10;
	
	private Image
	player_img = getImage("player.gif"),
	ground_img = getImage("ground_moon.png").getScaledInstance(800,100,Image.SCALE_FAST),
	background_img = getImage("background_moon.png").getScaledInstance(800,600,Image.SCALE_FAST),
	soju=getImage("soju.gif").getScaledInstance(15,40,Image.SCALE_FAST),
	dawn808=getImage("dawn808.gif").getScaledInstance(15,40,Image.SCALE_FAST),
	pocari=getImage("pocari.gif").getScaledInstance(15,40,Image.SCALE_FAST),
	
	totter1=getImage("totter1.gif").getScaledInstance(25,40,Image.SCALE_FAST),
	totter2=getImage("totter2.gif").getScaledInstance(25,40,Image.SCALE_FAST),
	totter3=getImage("totter3.gif").getScaledInstance(25,40,Image.SCALE_FAST);
	
	public void init() {
		switch(getDifficulty()) {
		case EASY:
			damage_speed=1.0/fps;
			totter_weight=0.4;
			break;
		case MODERATE:
			damage_speed=1.1/fps;
			totter_weight=0.5;
			break;
		case HARD:
			damage_speed=1.2/fps;
			totter_weight=0.6;
			break;
		}
		player=new Player(getWidth()/2,getHeight()-100,0,0,player_img,AlcoholStage.this);
		player.velocity=4.0;
		
		/**
		 *  pattern player SingleObject
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
		 * pattern of soju SingleObject
		 */
		addPattern(new Pattern() {
			Random rand = new Random();
			@Override
			public SingleObject create() {
				return new SingleObject(rand.nextInt(760)+20,1,0.0,1.0+rand.nextDouble()*0.2,soju);
			}

			@Override
			public boolean createWhen() {
				return (count%36==0 && getDifficulty()==EASY)
						|| (count%30==0 && getDifficulty()==MODERATE)
						|| (count%30==0 && getDifficulty()==HARD);
			}

			@Override
			public boolean removeWhen(SingleObject bl) {
				return bl.y>=getHeight()-80 || (soju_ate && bl.inRange(player));
			}

			@Override
			public void whenCrash() {
				soju_ate=true;
			}

			@Override
			public boolean inRange(SingleObject bl) {
				return bl.inRange(player);
			}
		});
		/**
		 * pattern of diagonal soju SingleObject
		 */
		addPattern(new Pattern() {
			Random rand = new Random();
			@Override
			public SingleObject create() {
				return new SingleObject(rand.nextInt(760)+20,1,(rand.nextDouble()-0.5)*Math.PI*0.3,1.0+rand.nextDouble()*0.2,soju);
			}

			@Override
			public boolean createWhen() {
				return count%90==0 && getDifficulty()==HARD;
			}

			@Override
			public boolean removeWhen(SingleObject bl) {
				return bl.y>=getHeight()-80 || (soju_ate && bl.inRange(player));
			}

			@Override
			public void whenCrash() {
				soju_ate=true;
			}

			@Override
			public boolean inRange(SingleObject bl) {
				return bl.inRange(player);
			}
		});
		/**
		 * pattern of pocari object
		 */
		addPattern(new Pattern() {
			Random rand = new Random();
			@Override
			public SingleObject create() {
				pocari_created=true;
				return new SingleObject(rand.nextInt(780)+10,1,0.0,0.4,pocari);
			}
			@Override
			public boolean createWhen() {
				return second()>=pocari_time && !pocari_created;
			}

			@Override
			public boolean removeWhen(SingleObject bl) {
				return bl.y>=getHeight()-80 || (ion_took && bl.inRange(player));
			}

			@Override
			public void whenCrash() {
				ion_took=true;
				gauge+=20;
			}

			@Override
			public boolean inRange(SingleObject bl) {
				return bl.inRange(player);
			}
		});
		/**
		 * pattern of dawn808 object
		 */
		addPattern(new Pattern() {
			Random rand = new Random();
			@Override
			public SingleObject create() {
				dawn808_created=true;
				return new SingleObject(rand.nextInt(780)+10,1,0.0,0.4,dawn808);
			}
			@Override
			public boolean createWhen() {
				return second()>=dawn808_time && !dawn808_created;
			}

			@Override
			public boolean removeWhen(SingleObject bl) {
				return bl.y>=getHeight()-80 || (dawn808_ate && bl.inRange(player));
			}

			@Override
			public void whenCrash() {
				dawn808_ate=true;
				drunken=0;
				gauge+=15;
			}

			@Override
			public boolean inRange(SingleObject bl) {
				return bl.inRange(player);
			}
		});
	}

	@Override
	public void play() {
		gauge-=damage_speed*drunken;
		if (soju_ate) {
			if (ion_took) drunken+=2;
			else drunken++;
		}
		Random totter = new Random();
		player.x+=(totter.nextDouble()-0.5)*drunken*totter_weight;
		soju_ate=false;
		updateAllPatterns();
	}

	@Override
	public void draw() {
		drawImage(background_img, 0, 0);
		drawAllPatterns();
		if (drunken!=0) {
			if (count%24<=7) drawImage(totter1, (int)player.x-7, (int)player.y-22);
			else if (count%24<=15) drawImage(totter2, (int)player.x-7, (int)player.y-22);
			else drawImage(totter3, (int)player.x-7, (int)player.y-22);
		}
		drawImage(ground_img, 0, getHeight()-90);
		
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
		drawString("DRUNKEN : " + drunken, getWidth()/2-200, getHeight()/2+280);
		if (ion_took) 
			drawString("이온 섭취(hp+20,이후 소주 흡수량 2배)", getWidth()/2-50, getHeight()/2+260);
		if (dawn808_ate)
			drawString("여명808 섭취(hp+15,술 완전 회복)", getWidth()/2-50, getHeight()/2+240);
	}

	@Override
	public boolean continuing() {
		if(gauge<=0){
			stageFailed=true;
			return false;
		}
		return second()<=span;
	}
}
