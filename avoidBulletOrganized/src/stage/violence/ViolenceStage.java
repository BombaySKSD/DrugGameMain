package stage.violence;

import java.awt.Font;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Random;

import main.Play;

import object.Pattern;
import object.SingleObject;
import object.Stage;
import stage.alcohol.AlcoholStage;
import stage.grade.Player;

public class ViolenceStage extends Stage {
	private final static int span = 20;
	public Player player;
	
	boolean player_invisible=false;
	boolean screen_turned=false;

	private Image
	player_img = getImage("player.gif"),
	ground_img = getImage("ground.gif"),
	background_img = getImage("background1.png");

	@Override
	public void init() {
		player = new Player(getWidth() / 2, getHeight() - 100, 0, 0, player_img, ViolenceStage.this);
		player.velocity=4.0;
		
		/**
		 *  pattern player SingleObject
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
				if (second()==4) {
					Random situation = new Random();
					switch (situation.nextInt(5)) {
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
						player_invisible=true;
						break;
					case 4: // screen turned 180 degree
						screen_turned=true;
						break;
					default:
						break;
					}
				}
				return null;
			}
			@Override
			public boolean createWhen() {
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

	}

	@Override
	public void play() {

	}

	@Override
	public void draw() {

	}

	@Override
	public boolean continuing() {
		return false;
	}

}
