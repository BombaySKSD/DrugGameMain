package object;
import java.awt.Graphics;
import java.util.ArrayList;

import main.Play;

abstract public class Pattern{
	private ArrayList<SingleObject> list=new ArrayList<SingleObject>();
	
	protected Pattern(){}
	
	final protected void draw(Graphics buffg,Play play){
		for(int i=0;i<list.size();i++){
			SingleObject bl=list.get(i);
			buffg.drawImage(bl.image, (int)bl.x, (int)bl.y, play);
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



