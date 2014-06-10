package stage.grade;

import java.awt.Font;
import java.awt.Image;
import object.SingleObject;
import object.MultiFrameObject;
import object.Pattern;
import object.Stage;


public class ManagingGradeStage extends Stage {
	private final static int span=20;
	private double bHeal,cDamage,fDamage,fGaugeDamage,penalty;
	private Image 
	player_img = getImage("player.gif"),
	ground_img = getImage("ground.gif"),
	b = getImage("rainB.gif").getScaledInstance(40,40,Image.SCALE_FAST),
	c=getImage("seedC.gif").getScaledInstance(40,40,Image.SCALE_FAST),
	f=getImage("missileF.gif").getScaledInstance(60,90,Image.SCALE_FAST),
	explosion_img = getImage("explosion.gif"),
	background_img = getImage("background1.png"),
	success=getImage("success.gif"),
	fail=getImage("fail.gif");
	
	public double grade;
	public Player player;
	public boolean hit=false;
	
	@Override
	public void init() {
		switch(getDifficulty()){
		case EASY:
			grade=2.4;bHeal=-0.2;cDamage=0.2;fDamage=0.5;fGaugeDamage=10;penalty=6;break;
		case MODERATE:
			grade=2.0;bHeal=-0.2;cDamage=0.3;fDamage=0.7;fGaugeDamage=13;penalty=7;break;
		case HARD:
			grade=2.2;bHeal=-0.1;cDamage=0.4;fDamage=0.9;fGaugeDamage=16;penalty=8;break;
		}
		player=new Player(getWidth()/2,getHeight()-100,0,0,player_img,ManagingGradeStage.this);
		player.velocity=4.0;
		
		addPattern(new GradePattern('b',b,explosion_img,0,1000,bHeal,0,this));
		addPattern(new GradePattern('c',c,explosion_img,5,1000,cDamage,0,this));
		addPattern(new GradePattern('f',f,explosion_img,10,3000,fDamage,fGaugeDamage,this));
		// pattern player SingleObject
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
		// pattern of explosion_img SingleObject
		addPattern(new Pattern() {
			@Override
			public boolean inRange(SingleObject bl) {
				return false;
			}
			@Override
			public void whenCrash() {}
			@Override
			public boolean removeWhen(SingleObject bl) {
				return ((MultiFrameObject)bl).shouldRemove();
			}
			@Override
			public boolean createWhen() {
				return hit;
			}
			@Override
			public SingleObject create() {
				hit=false;
				return new MultiFrameObject(player.x-30, player.y-30, explosion_img, 100);
			}
		});
		// pattern of success/fail
		addPattern(new Pattern() {
			private boolean evaluated=false;
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
				return second()>=17;
			}
			@Override
			public SingleObject create() {
				Image image;
				if(ManagingGradeStage.this.grade>=3.3){
						image=success;
					if(!evaluated){
						evaluated=true;
						gauge+=15;
					}
				}else{
						image=fail;
					if(!evaluated){
						evaluated=true;
						gauge-=(3.3-grade)*penalty*10;
					}
				}
				return new SingleObject(getWidth()/3, getHeight()/3,0,0, image);
			}
		});
	}

	@Override
	public void play() {System.out.println("ddf");
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
		drawString("GRADE : " + String.format("%.1f",grade), getWidth()/2-200, getHeight()/2+280);
		
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
