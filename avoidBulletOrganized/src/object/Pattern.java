package object;
import java.awt.Graphics2D;
import java.util.ArrayList;

import main.Play;

abstract public class Pattern{
	private ArrayList<SingleObject> list=new ArrayList<SingleObject>();
	
	final protected void draw(Graphics2D buffg,Play play){
		for(int i=0;i<list.size();i++){
			SingleObject bl=list.get(i);
			buffg.rotate(-bl.tiltedRad,bl.x+bl.image.getWidth(play)/2,bl.y+bl.image.getHeight(play)/2);
			buffg.drawImage(bl.image, (int)bl.x, (int)bl.y,play);
			buffg.rotate(bl.tiltedRad,bl.x+bl.image.getWidth(play)/2,bl.y+bl.image.getHeight(play)/2);
		}
	}
	final protected void update(){
		if(createWhen()){
			list.add(create());
		}
		for(int i=0;i<list.size();i++){
			SingleObject bl=list.get(i);
			if(inRange(bl)){
				whenCrash();
			}
			if(removeWhen(bl)){
				list.remove(bl);
				i--;
			}
		}
		for(int i=0;i<list.size();i++){
			SingleObject bl=list.get(i);
			bl.move();
		}
	}
	
	abstract public SingleObject create();
	abstract public boolean createWhen();
	abstract public boolean removeWhen(SingleObject bl);
	abstract public void whenCrash();
	abstract public boolean inRange(SingleObject bl);
}



