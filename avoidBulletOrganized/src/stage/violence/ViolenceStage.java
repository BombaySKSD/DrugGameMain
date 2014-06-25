package stage.violence;

import java.awt.Font;
import java.awt.Image;
import java.util.*;

import object.Pattern;
import object.SingleObject;
import object.Stage;
import stage.grade.Player;

public class ViolenceStage extends Stage {
	private final static int span = 20;
	public Player player;
	
	double bullet_speed;
	int sit_case=-1;
	int fury_case=-1;
	int erupt_position=400;
	boolean bullet_hit=false;
	int bullet_damage=1;
	boolean furied=false;

	private Image
	player_img = getImage("player.gif"),
	ground_img = getImage("ground_moon.png").getScaledInstance(800,100,Image.SCALE_FAST),
	background_img = getImage("background_moon.png").getScaledInstance(800,600,Image.SCALE_FAST),
	warning = getImage("picture_violence/magic_field.gif").getScaledInstance(70,25,Image.SCALE_FAST),
	lightning1 = getImage("picture_violence/lightning.gif").getScaledInstance(70,630,Image.SCALE_FAST),
	lightning2 = getImage("picture_violence/lightning2.gif").getScaledInstance(70,630,Image.SCALE_FAST),
	bullet = getImage("picture_violence/stardust1.png").getScaledInstance(10,40,Image.SCALE_FAST),
	
	sit1 = getImage("picture_violence/night.gif").getScaledInstance(800,511,Image.SCALE_FAST),
	sit2 = getImage("picture_violence/torch_mode.gif").getScaledInstance(1600,1000,Image.SCALE_FAST),
	sit3 = getImage("picture_violence/dark-curtain.gif").getScaledInstance(800,600,Image.SCALE_FAST),
	
	switch_on = getImage("picture_violence/switch_on.gif").getScaledInstance(60,150,Image.SCALE_FAST),
	switch_off = getImage("picture_violence/switch_off.gif").getScaledInstance(60,150,Image.SCALE_FAST),
	
	fury1 = getImage("picture_violence/fury.gif").getScaledInstance(25,40,Image.SCALE_FAST),
	fury2 = getImage("picture_violence/fury2.gif").getScaledInstance(25,40,Image.SCALE_FAST),
	fury3 = getImage("picture_violence/fury3.gif").getScaledInstance(25,40,Image.SCALE_FAST),
	fury4 = getImage("picture_violence/fury4.gif").getScaledInstance(25,40,Image.SCALE_FAST),
	
	fear1 = getImage("picture_violence/fear1.gif").getScaledInstance(25,40,Image.SCALE_FAST),
	fear2 = getImage("picture_violence/fear2.gif").getScaledInstance(25,40,Image.SCALE_FAST),
	
	confusion1 = getImage("picture_violence/confusion1.gif").getScaledInstance(25,40,Image.SCALE_FAST),
	confusion2 = getImage("picture_violence/confusion2.gif").getScaledInstance(25,40,Image.SCALE_FAST),
	confusion3 = getImage("picture_violence/confusion3.gif").getScaledInstance(25,40,Image.SCALE_FAST),
	confusion4 = getImage("picture_violence/confusion4.gif").getScaledInstance(25,40,Image.SCALE_FAST);
	
	@Override
	public void init() {
		player = new Player(getWidth() / 2, getHeight() - 100, 0, 0, player_img, ViolenceStage.this);
		player.velocity=4.0;
		
		switch(getDifficulty()) {
		case EASY:
			bullet_speed=0.5;
			break;
		case MODERATE:
			bullet_speed=1.0;
			break;
		case HARD:
			bullet_speed=1.1;
			break;
		}

		Random situation = new Random();
		situation.setSeed(System.currentTimeMillis());
		sit_case = situation.nextInt(3);
		fury_case = situation.nextInt(3);
		
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
				if ((int)second()==7 && !furied) {
					switch (fury_case) {
					case 0:  //left/right arrow key switched
						player.velocity=-4.0;
						break;
					case 1:  //terribly fast
						player.velocity=12.0;
						break;
		            case 2:  //terribly slow
		            	player.velocity=1.0;
		            	break;
		            default:
		                break;
					}
		               furied=true;
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
		 * pattern of bullet
		 */
		addPattern(new Pattern() {
			Random rand = new Random();
			@Override
			public SingleObject create() {
				return new SingleObject(rand.nextInt(780)+10,0,(rand.nextDouble()-0.5)*0.5,bullet_speed,bullet);
			}
			@Override
			public boolean createWhen() {
				return (count%90==0 && getDifficulty()==EASY)
						|| (count%60==0 && getDifficulty()==MODERATE)
						|| (count%60==0 && getDifficulty()==HARD);
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
				return new SingleObject(25+(count%700)*50,(count%700)*0.5,0.1*(count%2*2-1),0.5,bullet);
			}
			@Override
			public boolean createWhen() {
				return count%700<=15 && getDifficulty()==HARD;
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
		/**
		 * pattern of warning
		 */
		addPattern(new Pattern() {
			Random rand = new Random();
			@Override
			public SingleObject create() {
				return new SingleObject(erupt_position,485,0.0,0,warning);
			}
			@Override
			public boolean createWhen() {
				if (count%600==0)
					erupt_position=rand.nextInt(830)-55;
				return (count%600==0 && getDifficulty()==EASY)
						|| (count%600==50 && getDifficulty()==MODERATE)
						|| (count%600==100 && getDifficulty()==HARD);
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
				if (count%10<=4)
					return new SingleObject(erupt_position,-100,0.0,0,lightning1);
				else
					return new SingleObject(erupt_position,-100,0.0,0,lightning2);
			}
			@Override
			public boolean createWhen() {
				return (count%600>=300 && count%600<450 && count%9==0);
			}
			@Override
			public boolean removeWhen(SingleObject bl) {
				return count%9==8;
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
		getSound("audio/violence_music.wav").play();
	}

	@Override
	public void play() {
		updateAllPatterns();
		bullet_hit=false;
	}

	@Override
	public void draw() {
		drawImage(background_img, 0, 0);
		if (second()>=7 && fury_case==0) {
			if (count%12<=2) drawImage(confusion1, (int)player.x-7, (int)player.y-22);
			else if (count%12<=5) drawImage(confusion2, (int)player.x-7, (int)player.y-22);
			else if (count%12<=8) drawImage(confusion3, (int)player.x-7, (int)player.y-22);
			else drawImage(confusion4, (int)player.x-7, (int)player.y-22);
		}
		if (second()>=7 && fury_case==1) {
			if (count%12<=2) drawImage(fury1, (int)player.x-7, (int)player.y-22);
			else if (count%12<=5) drawImage(fury3, (int)player.x-7, (int)player.y-22);
			else if (count%12<=8) drawImage(fury2, (int)player.x-7, (int)player.y-22);
			else drawImage(fury4, (int)player.x-7, (int)player.y-22);
		}
		if (second()>=7 && fury_case==2) {
			if (count%12<=5) drawImage(fear1, (int)player.x-7, (int)player.y-22);
			else drawImage(fear2, (int)player.x-7, (int)player.y-22);
		}
		
		drawAllPatterns();
		if (second()>=5) {
			switch(sit_case) {
			case 0:
				if (count%300>=125) drawImage(sit1, 0, 0);
				break;
			case 1:
				drawImage(sit2, (int)player.x-795, (int)player.y-555);
				break;
			case 2:
				drawImage(sit3, 0, count%150);
				break;
			default:
				drawString("LUCKY!", getWidth()/2-200, getHeight()/2+280);
				break;
			}
		}
		
		if (second()<4) drawImage(switch_on, 1050-(int)(second()*100), 100);
		if (second()>=4 && second()<5) drawImage(switch_on, 650, 100);
		if (second()>=5 && second()<6) drawImage(switch_off, 650, 100);

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
		if (second()>=5 && second()<7) {
			switch(sit_case) {
			case 0:
				drawString("컴퓨터 전원 이상 (깜빡깜빡)", getWidth()/2-200, getHeight()/2+280);
				break;
			case 1:
				drawString("컴퓨터 전원 이상 (시야 감소)", getWidth()/2-200, getHeight()/2+280);
				break;
			case 2:
				drawString("컴퓨터 전원 이상 (커튼)", getWidth()/2-200, getHeight()/2+280);
				break;
			default:
				break;
			}
		}
		if (second()>=7) {
			switch(fury_case) {
			case 0:
				drawString("상태 : 혼란 (좌우 키 반전)", getWidth()/2-200, getHeight()/2+280);
				break;
			case 1:
				drawString("상태 : 분노 (속도 증가)", getWidth()/2-200, getHeight()/2+280);
				break;
			case 2:
				drawString("상태 : 공포 (속도 감소)", getWidth()/2-200, getHeight()/2+280);
				break;
			default:
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
