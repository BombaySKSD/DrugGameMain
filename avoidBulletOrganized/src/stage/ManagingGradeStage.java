package stage;

import java.awt.Font;
import java.awt.Image;
import javax.swing.ImageIcon;

import object.Bullet;
import object.Pattern;


public class ManagingGradeStage extends Stage {

	private double bHeal,cDamage,fDamage,fGaugeDamage,penalty;
	private int verticalSpeed=-100;
	private int horizontalSpeed=0;
	
	private Image ground_img;
	private Image b,c,f;
	private Image explosion_img;
	
	private GradePattern bPattern,cPattern,fPattern;

	public double grade;
	
	@Override
	public void init() {
		span=20;
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
		
		bPattern=new GradePattern(b,explosion_img,this,0,bHeal,0);
		cPattern=new GradePattern(c,explosion_img,this,5,cDamage,0);
		fPattern=new GradePattern(f,explosion_img,this,10,fDamage,fGaugeDamage);
		
		addPatterns(new Pattern[]{bPattern,cPattern,fPattern});
	}

	@Override
	public void play() {
		updateAllPatterns();
		updatePlayer();
	}
	@Override
	public void draw() {
		drawBackground();
		drawAllPattern();
		drawPlayer();
		drawGround();
		drawGrade();
		evaluateResult();
	}
	@Override
	public boolean continuing() {
		if(gauge<=0){
			stageFailed=true;
			return false;
		}
		return second()<=span;
	}
	
	private void updatePlayer(){
		if (keyLeft && player_x > 9) {
			player_x -= 7;
		}
		if (keyRight && player_x + player_img.getWidth(null)+5 < f_width) {
			player_x += 7;
		}
		if (keyUp) {
			if(verticalSpeed==-100){
				verticalSpeed=15;
				if(keyLeft)
					horizontalSpeed=-3;
				else if (keyRight)
					horizontalSpeed=3;
				else
					horizontalSpeed=0;
			}
		}
		if(verticalSpeed<=-14)
			verticalSpeed=-100;
		else if(verticalSpeed!=-100){
			verticalSpeed--;
			player_x+=horizontalSpeed;
			player_y-=verticalSpeed;
		}
	}
	private void evaluateResult(){
		if(grade>=3.3){
			buffg.drawString("학점 관리 성공", f_xpos/2+200, f_ypos+420);
		}else{
			buffg.drawString("학점 관리 실패", f_xpos/2+200, f_ypos+420);
		}
	}
	
	private void drawBackground(){
		buffg.drawImage(background_img, 0, 0, play);
	}
	private void drawPlayer(){
		buffg.drawImage(player_img, player_x, player_y, play);
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
	int frequency=200;
	
	public GradePattern(Image image,Image explosion, Stage stage,int startTime,double damage,double gaugeDamage){
		super(image,stage);
		this.image=image;
		this.explosion=explosion;
		this.startTime=startTime;
		this.damage=damage;
		this.gaugeDamage=gaugeDamage;
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
		return bl.inRange(stage.player_x, stage.player_y, stage.player_img, image)
				|| bl.inScreen(stage)
				|| stage.second()>17;
	}
	public void whenCrash(){
		stage.gauge-=gaugeDamage;
		((ManagingGradeStage)stage).grade-=damage;
		stage.buffg.drawImage(explosion, stage.player_x-explosion.getWidth(null)/2+5,
				stage.player_y-explosion.getHeight(null)/2+7, stage.play);
	}
}
