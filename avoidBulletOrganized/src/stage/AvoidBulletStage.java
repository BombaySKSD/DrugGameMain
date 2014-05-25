/*package stage;

import java.awt.Font;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.ImageIcon;

import object.Bullet;

import main.Play;

public class AvoidBulletStage extends Stage{
	char rank='F';
	double hitpoint=10.0;
	int hitted=0;
	int lv2_angle=-20;
	boolean increasing=true;
	ArrayList<Bullet> bullet_List = new ArrayList<Bullet>();

	Image player_img;
	Image ground_img;
	Image bullet_img;
	Image explosion_img;
	Image background_img;
	
	public void init(){
		player_img = new ImageIcon("player.gif").getImage();
		ground_img = new ImageIcon("ground.gif").getImage();
		bullet_img = new ImageIcon("bullet.gif").getImage();
		explosion_img = new ImageIcon("explosion.gif").getImage();
		background_img = new ImageIcon("background1.png").getImage();
	}
	public void play(){
		if (hitted>0) 
			hitted--;
		PlayerProcess();
		BulletProcess_LV1();
		BulletProcess_LV2();
		BulletProcess_LV3();
		RemoveBulletProcess();
	}
	public void draw(){
		Draw_Background();
		Draw_Player();
		Draw_Bullet();
		Draw_Ground();
		Draw_Status();
		Draw_Rank();
		if (hitted>0)
			Draw_Explosion();
	}
	public boolean continuing(){
		return count<200;
	}
	
	void PlayerProcess() {
		if (keyLeft == true) {
			if (player_x > 9)
				player_x -= 7;
		}
		if (keyRight == true) {
			if (player_x + player_img.getWidth(null)+5 < f_width)
				player_x += 7;
		}
	}
	
	//BulletProcess
	void BulletProcess_LV1() {
		Random rand=new Random();
		
		// 총알
		if (count%40==0) {
			double num=rand.nextDouble();
			for (int i=-15; i<=15; i++)
			{
				Bullet bl = new Bullet(f_width/2, 0, (Math.PI/2.0)*((double)i+num*1.2)/15.0, 250/fps);
				bullet_List.add(bl);
			}
		}
	}
	
	void BulletProcess_LV2() {
		Bullet bl;
		if (count%2==0) {
			bl = new Bullet(f_width/2, 0, (Math.PI/2.0)*(double)lv2_angle/20.0, 400/fps);
			bullet_List.add(bl);
			bl = new Bullet(f_width/2, 0, -(Math.PI/2.0)*(double)lv2_angle/20.0, 400/fps);
			bullet_List.add(bl);
			
			if (increasing && lv2_angle>=20)
				increasing=false;
			if (!increasing && lv2_angle<=-20)
				increasing=true;
			
			if (increasing)
				lv2_angle++;
			else
				lv2_angle--;
		}
	}
	
	void BulletProcess_LV3() {
		Bullet bl;
		if (count%90<=8 && count%2==0) {
			bl = new Bullet(player_x, 0, 0, (300+(count%90)*50)/fps);
			bullet_List.add(bl);
		}
	}
	
	void RemoveBulletProcess() {
		for (int i=0;i<bullet_List.size();i++) {
			Bullet bl=bullet_List.get(i);
			bl.move();
			if ( bl.x < 0 || bl.x > f_width || bl.y > f_height-80 ) {
				bullet_List.remove(bl);
			}
			if ( Crash(player_x,player_y,bl.x,bl.y,player_img,bullet_img) ) {
				hitpoint++;
				hitted=2;
				bullet_List.remove(bl);
			}
		}
	}
	
	//BulletProcess End
	public boolean Crash(int x1, int y1, double x2, double y2, Image img1, Image img2) {
		return Math.abs((x1 + img1.getWidth(null) / 2)
				- (x2 + img2.getWidth(null) / 2)) < (img2.getWidth(null) / 2 + img1.getWidth(null) / 2)
				&& Math.abs((y1 + img1.getHeight(null) / 2) - (y2 + img2.getHeight(null) / 2))
				< (img2.getHeight(null) / 2 + img1.getHeight(null) / 2);
	}
	
	public void Draw_Background() {
		buffg.drawImage(background_img, 0, 0, play);
	}
	public void Draw_Player() {
		buffg.drawImage(player_img, player_x, player_y, play);
	}
	public void Draw_Ground() {
		buffg.drawImage(ground_img, 0, f_height-90, play);
	}
	public void Draw_Bullet() {
		for (int i = 0; i < bullet_List.size(); ++i) {
			Bullet bl = (Bullet) (bullet_List.get(i));
			buffg.drawImage(bullet_img, (int)bl.x, (int)bl.y, play);
		}
	}
	public void Draw_Status() {
		buffg.setFont(new Font("Default", Font.BOLD, 20));
		// 폰트설정을합니다. 기본폰트, 굵게, 사이즈20
		buffg.drawString("TIME : " + count/50, f_xpos+300, f_ypos+450); // 텍스트, x좌표, y좌표
		buffg.drawString("HIT : " + hitpoint, f_xpos+300, f_ypos+480);
	}
	public void Draw_Explosion() {
		buffg.drawImage(explosion_img, player_x-explosion_img.getWidth(null)/2+5,
				player_y-explosion_img.getHeight(null)/2+7, play);
	}
	public void Draw_Rank() {
		double time=count/50.0;
		if (hitpoint==0) {
			if (time>=20) rank='S';
			else if (time>=15) rank='A';
			else if (time>=12) rank='B';
			else if (time>=9) rank='C';
			else if (time>=6) rank='D';
			else if (time>=3) rank='E';
			else rank='F';
		}else {
			if (time>=20 && time/hitpoint>1.5) rank='S';
			else if (time>=15 && time/hitpoint>1.25) rank='A';
			else if (time>=12 && time/hitpoint>1.0) rank='B';
			else if (time>=9 && time/hitpoint>0.85) rank='C';
			else if (time>=6 && time/hitpoint>0.7) rank='D';
			else if (time>=3 && time/hitpoint>0.6) rank='E';
			else rank='F';
		}
		buffg.drawString("RANK : " + rank, f_xpos+300, f_ypos+510);
	}
}
*/