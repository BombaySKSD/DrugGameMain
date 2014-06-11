package stage.start;

import java.awt.Image;

import object.Stage;

public class StartPage extends Stage{

	private Image background=getImage("start.gif").getScaledInstance(800, 600, Image.SCALE_FAST);
	
	@Override
	public void init() {}

	@Override
	public void play() {}

	@Override
	public void draw() {
		drawImage(background,0,0);
	}

	@Override
	public boolean continuing() {
		return true;
	}

}
