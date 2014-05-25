package object;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Random;

import stage.Stage;

import main.Play;

abstract public class Pattern{
	private ArrayList<Bullet> list=new ArrayList<Bullet>();
	protected Image image;
	protected Random rand=new Random();
	protected Stage stage;
	
	public Pattern(){}
	public Pattern(ArrayList<Bullet> list,Image image){
		this.list=list;
	}
	public Pattern(Image image,Stage stage){
		this.image=image;
		this.stage=stage;
	}
	
	final public void setImage(Image image){
		this.image=image;
	}
	final public void setStage(Stage stage){
		this.stage=stage;
	}
	final public void draw(Graphics buffg,Play play){
		for(int i=0;i<list.size();i++){
			Bullet bl=list.get(i);
			buffg.drawImage(bl.image, (int)bl.x, (int)bl.y, play);
		}
	}
	final public void update(){
		if(createWhen()){
			list.add(create());
		}
		for(int i=0;i<list.size();i++){
			Bullet bl=list.get(i);
			if(inRange(bl)){
				whenCrash();
			}
			if(removeWhen(bl)){
				list.remove(bl);
				i--;
			}
		}
		for(int i=0;i<list.size();i++){
			Bullet bl=list.get(i);
			bl.move();
		}
	}
	
	abstract public Bullet create();
	abstract public boolean createWhen();
	abstract public boolean removeWhen(Bullet bl);
	abstract public void whenCrash();
	abstract public boolean inRange(Bullet bl);
}



