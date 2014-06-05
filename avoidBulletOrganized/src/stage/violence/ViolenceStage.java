package stage.violence;

import java.awt.Font;
import java.awt.Image;
import java.util.*;

import main.Play;

import object.Pattern;
import object.SingleObject;
import object.Stage;
import stage.grade.Player;

public class ViolenceStage extends Stage {
	private final static int span = 20;
	public Player player;
	
	boolean screen_turned=false;
	int sit_case=-1;
	int erupt_position=400;
	boolean bullet_hit=false;
	int bullet_damage=2;

	private Image
	player_img = getImage("player.gif"),
	player_invisible = getImage("picture_violence/player_invisible.gif"),
	ground_img = getImage("ground.gif"),
	background_img = getImage("background1.png"),
	warning = getImage("picture_violence/warning.gif").getScaledInstance(70,35,Image.SCALE_FAST),
	eruption = getImage("picture_violence/eruption.gif").getScaledInstance(70,800,Image.SCALE_FAST),
	bullet = getImage("picture_violence/bullet.gif");

	@Override
	public void init() {
		player = new Player(getWidth() / 2, getHeight() - 100, 0, 0, player_img, ViolenceStage.this);
		player.velocity=4.0;

		Random situation = new Random();
		situation.setSeed(System.currentTimeMillis());
		sit_case = situation.nextInt(3);
		
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
		 *  pattern of sudden situation
		 */
		addPattern(new Pattern() {
			@Override
			public SingleObject create() {
				return null;
			}
			@Override
			public boolean createWhen() {
				if (second()==4) {
					switch (sit_case) {
					case 0: // left/right arrowkey switched
						player.velocity=-4.0;
						break;
					case 1: // terribly fast
						player.velocity=12.0;
						break;
					case 2: // terribly slow
						player.velocity=0.8;
						break;
					case 3: // character be invisible
						break;
					case 4: // screen turned 180 degree
						screen_turned=true;
						break;
					default:
						break;
					}
				}
				return false;
			}
			@Override
			public boolean removeWhen(SingleObject bl) {
				return false;
			}
			@Override
			public void whenCrash() {
				
			}
			@Override
			public boolean inRange(SingleObject bl) {
				return false;
			}
		});
		/**
		 * pattern of warning
		 */
		addPattern(new Pattern() {
			Random rand = new Random();
			@Override
			public SingleObject create() {
				return new SingleObject(erupt_position,500,0.0,0,warning);
			}
			@Override
			public boolean createWhen() {
				if (count%600==0)
					erupt_position=rand.nextInt(780)+10;
				return count%600==0;
			}
			@Override
			public boolean removeWhen(SingleObject bl) {
				return count%600==300;
			}
			@Override
			public void whenCrash() {
			}
			@Override
			public boolean inRange(SingleObject bl) {
				return bl.inRange(player);
			}
		});
		/**
		 * pattern of eruption
		 */
		addPattern(new Pattern() {
			@Override
			public SingleObject create() {
				return new SingleObject(erupt_position,-100,0.0,0,eruption);
			}
			@Override
			public boolean createWhen() {
				return count%600==300;
			}
			@Override
			public boolean removeWhen(SingleObject bl) {
				return count%600==450;
			}
			@Override
			public void whenCrash() {
				gauge-=0.1;
			}
			@Override
			public boolean inRange(SingleObject bl) {
				return bl.inRange(player);
			}
		});
		/**
		 * pattern of bullet
		 */
		addPattern(new Pattern() {
			Random rand = new Random();
			@Override
			public SingleObject create() {
				return new SingleObject(rand.nextInt(780)+10,0,(rand.nextDouble()-0.5)*0.5,1.0,bullet);
			}
			@Override
			public boolean createWhen() {
				return count%60==0;
			}
			@Override
			public boolean removeWhen(SingleObject bl) {
				return bl.y>=getHeight()-80 || (bullet_hit && bl.inRange(player));
			}
			@Override
			public void whenCrash() {
				bullet_hit=true;
				gauge-=bullet_damage;
			}
			@Override
			public boolean inRange(SingleObject bl) {
				return bl.inRange(player);
			}
		});
		/**
		 * pattern of bullet curtain
		 */
		addPattern(new Pattern() {
			@Override
			public SingleObject create() {
				return new SingleObject(25+(count%700)*50,(count%700)*0.5,0,0.5,bullet);
			}
			@Override
			public boolean createWhen() {
				return count%700<=15;
			}
			@Override
			public boolean removeWhen(SingleObject bl) {
				return bl.y>=getHeight()-80 || (bullet_hit && bl.inRange(player));
			}
			@Override
			public void whenCrash() {
				bullet_hit=true;
				gauge-=bullet_damage;
			}
			@Override
			public boolean inRange(SingleObject bl) {
				return bl.inRange(player);
			}
		});
		/**
		 * pattern of sniping bullet
		 */
		addPattern(new Pattern() {
			@Override
			public SingleObject create() {
				return new SingleObject(player.x,0,0,1.2,bullet);
			}
			@Override
			public boolean createWhen() {
				return count%500==0;
			}
			@Override
			public boolean removeWhen(SingleObject bl) {
				return bl.y>=getHeight()-80 || (bullet_hit && bl.inRange(player));
			}
			@Override
			public void whenCrash() {
				bullet_hit=true;
				gauge-=bullet_damage;
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
		bullet_hit=false;
	}

	@Override
	public void draw() {
		drawImage(background_img, 0, 0);
		drawAllPatterns();
		drawImage(ground_img, 0, getHeight()-90);
		
		setFont(new Font("Default", Font.BOLD, 20));
		drawString("TIME : " + String.format("%.0f",second()), getWidth()/2-200, getHeight()/2+240);
		drawString("HIT : " + String.format("%.0f",gauge), getWidth()/2-200, getHeight()/2+260);
		if (second()>=4) {
			switch(sit_case) {
			case 0:
				drawString("LEFT? RIGHT?", getWidth()/2-200, getHeight()/2+280);
				break;
			case 1:
				drawString("SPEEDSTER!", getWidth()/2-200, getHeight()/2+280);
				break;
			case 2:
				drawString("TERRIBLE TURTLE!", getWidth()/2-200, getHeight()/2+280);
				break;
			default:
				drawString("LUCKY!", getWidth()/2-200, getHeight()/2+280);
				break;
			}
		}
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
