package stage.alcohol;

import java.awt.Font;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Random;

import main.Play;

import object.Pattern;
import object.SingleObject;
import object.Stage;
import stage.grade.ManagingGradeStage;
import stage.grade.Player;

public class AlcoholStage extends Stage {
	private final static int span=20;

	public int drunken=0;
	public double damage_speed;
	public boolean ion_took=false;
	public Player player;
	boolean soju_ate=false;
	boolean dawn808_ate=false;
	
	Random special_time = new Random();
	int pocari_time=special_time.nextInt(5)+5;
	int dawn808_time=special_time.nextInt(5)+10;
	
	private Image
	player_img = getImage("player.gif"),
	ground_img = getImage("ground.gif"),
	explosion_img = getImage("explosion.gif"),
	background_img = getImage("background1.png"),
	soju=getImage("soju.gif").getScaledInstance(15,40,Image.SCALE_FAST),
	dawn808=getImage("dawn808.gif").getScaledInstance(15,40,Image.SCALE_FAST),
	pocari=getImage("pocari.gif").getScaledInstance(15,40,Image.SCALE_FAST);
	
	public void init() {
		switch(getDifficulty()) {
		case EASY:
			damage_speed=0.5/fps;
		case MODERATE:
			damage_speed=0.75/fps;
		case HARD:
			damage_speed=1.0/fps;
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
				return count%30==0;
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
				return new SingleObject(rand.nextInt(780)+10,1,0.0,0.4,pocari);
			}
			@Override
			public boolean createWhen() {
				return second()==pocari_time;
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
				return new SingleObject(rand.nextInt(780)+10,1,0.0,0.4,dawn808);
			}
			@Override
			public boolean createWhen() {
				return second()==dawn808_time;
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
		soju_ate=false;
		updateAllPatterns();
	}

	@Override
	public void draw() {
		drawImage(background_img, 0, 0);
		drawAllPatterns();
		drawImage(ground_img, 0, getHeight()-90);
		
		setFont(new Font("Default", Font.BOLD, 20));
		drawString("TIME : " + String.format("%.0f",second()), getWidth()/2-200, getHeight()/2+240);
		drawString("HIT : " + String.format("%.0f",gauge), getWidth()/2-200, getHeight()/2+260);
		drawString("DRUNKEN : " + drunken, getWidth()/2-200, getHeight()/2+280);
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
