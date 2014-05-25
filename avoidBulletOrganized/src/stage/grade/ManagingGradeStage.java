package stage.grade;

import java.awt.Font;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import object.Bullet;
import object.MultiFrameObject;
import object.Pattern;
import stage.Stage;


public class ManagingGradeStage extends Stage {
	private double bHeal,cDamage,fDamage,fGaugeDamage,penalty;
	private int verticalSpeed=-100;
	private int span=20;
	private int horizontalSpeed=0;
	private Image ground_img;
	private Image b,c,f;
	private Image explosion_img;
	private GradePattern bPattern,cPattern,fPattern;
	public double grade;
	public boolean hit=false;
	
	@Override
	public void init() {
		switch(difficulty){
		case EASY:
			grade=2.4;bHeal=-0.2;cDamage=0.2;fDamage=0.5;fGaugeDamage=10;penalty=6;break;
		case MODERATE:
			grade=2.0;bHeal=-0.2;cDamage=0.3;fDamage=0.7;fGaugeDamage=13;penalty=7;break;
		case HARD:
			grade=2.2;bHeal=-0.1;cDamage=0.4;fDamage=0.9;fGaugeDamage=16;penalty=8;break;
		}
		player_img = new ImageIcon("player.gif").getImage();
		ground_img = new ImageIcon("ground.gif").getImage();
		b = new ImageIcon("bullet.gif").getImage();
		c=new ImageIcon("another_bullet.gif").getImage();
		f=new ImageIcon("FFF.gif").getImage();
		explosion_img = new ImageIcon("explosion.gif").getImage();
		background_img = new ImageIcon("background1.png").getImage();
		
		bPattern=new GradePattern(b,explosion_img,0,1000,bHeal,0,this);
		cPattern=new GradePattern(c,explosion_img,5,1000,cDamage,0,this);
		fPattern=new GradePattern(f,explosion_img,10,5000,fDamage,fGaugeDamage,this);

		player=new Player(f_width/2,f_height-100,0,0,player_img,this);
		addPattern(new Pattern() {
			private boolean born=false;
			@Override
			public void whenCrash() {}
			@Override
			public boolean removeWhen(Bullet bl) {
				return second()>17;
			}
			@Override
			public boolean inRange(Bullet bl) {
				return false;
			}
			@Override
			public boolean createWhen() {
				return born==false;
			}
			@Override
			public Bullet create() {
				born=true;
				return player;
			}
		});
		
		addPattern(new Pattern() {
			@Override
			public boolean inRange(Bullet bl) {
				return false;
			}
			@Override
			public void whenCrash() {}
			@Override
			public boolean removeWhen(Bullet bl) {
				return ((MultiFrameObject)bl).shouldRemove();
			}
			@Override
			public boolean createWhen() {
				return hit;
			}
			@Override
			public Bullet create() {
				hit=false;
				return new MultiFrameObject(player.x-30, player.y-30, explosion_img, 100);
			}
		});
		
		addPatterns(new Pattern[]{bPattern,cPattern,fPattern});
	}

	@Override
	public void play() {
		updateAllPatterns();
	}
	@Override
	public void draw() {
		drawBackground();
		drawAllPattern();
		drawPlayer();
		drawGround();
		drawGrade();
	}
	@Override
	public boolean continuing() {
		if(gauge<=0){
			stageFailed=true;
			return false;
		}
		return second()<=span;
	}
	
	private void drawBackground(){
		buffg.drawImage(background_img, 0, 0, play);
	}
	private void drawPlayer(){
		buffg.drawImage(player_img, (int)player.x, (int)player.y, play);
	}
	private void drawGround(){
		buffg.drawImage(ground_img, 0, f_height-90, play);
	}
	private void drawGrade(){
		buffg.setFont(new Font("Default", Font.BOLD, 20));
		// 폰트설정을합니다. 기본폰트, 굵게, 사이즈20
		buffg.drawString("TIME : " + second(), f_xpos/2, f_ypos+400); // 텍스트, x좌표, y좌표
		buffg.drawString("HIT : " + String.format("%.0f",gauge), f_xpos/2, f_ypos+420);
		buffg.drawString("GRADE : " + String.format("%.1f",grade), f_xpos/2, f_ypos+440);
	}
}

class GradePattern extends Pattern{
	Image explosion,image;
	int startTime;
	double damage,gaugeDamage;
	double frequency=500;
	
	private ArrayList<MultiFrameObject> explosionList=new ArrayList<MultiFrameObject>(); 
	
	public GradePattern(Image image,Image explosion,
			int startTime,double frequency,
			double damage,double gaugeDamage,
			Stage stage){
		super(image,stage);
		this.image=image;
		this.explosion=explosion;
		this.startTime=startTime;
		this.damage=damage;
		this.gaugeDamage=gaugeDamage;
		this.frequency=frequency;
	}
	
	public Bullet create() {
		double num=rand.nextDouble();
		int pos=rand.nextInt(stage.f_width);
		return new Bullet(pos, 0, (Math.PI/2.0)*(num-0.5), frequency/stage.fps,image);
	}
	public boolean createWhen(){
		return stage.count%(10*stage.fps/frequency+10)==0 && stage.second()>=startTime;
	}
	public boolean removeWhen(Bullet bl){
		return bl.inRange(stage.player)
				|| !bl.inArea(0,stage.f_width,0,stage.f_height)
				|| stage.second()>17;
	}
	public void whenCrash(){
		stage.gauge-=gaugeDamage;
		((ManagingGradeStage)stage).grade-=damage;
		((ManagingGradeStage)stage).hit=true;
	}
	public boolean inRange(Bullet bl){
		return bl.inRange(stage.player);
	}
}
