package stage;


import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.Play;
import object.Pattern;
import stage.grade.Player;

abstract public class Stage{
	public final static int EASY=0;
	public final static int MODERATE=1;
	public final static int HARD=2;
	
	public boolean keyUp = false;
	public boolean keyDown = false;
	public boolean keyLeft = false;
	public boolean keyRight = false;
	public boolean keySpace = false;
	
	public int f_width;
	public int f_height;
	public int f_xpos;
	public int f_ypos;
	public int difficulty;
	public int fps;
	
	public Player player;
	public double gauge;
	public int span;
	
	public int count=0;
	public boolean stageFailed=false;
	public Graphics buffg;
	
	public Play play;
	
	public Image player_img;
	public Image background_img;
	
	private long initialTime;
	private ArrayList<Pattern> patternList=new ArrayList<Pattern>();
	
	public Stage(){}
	public Stage(Play play,int difficulty){
		setPlay(play);
		setDifficulty(difficulty);
	}
	
	final public void setPlay(Play play){
		this.play=play;
		f_width=play.f_width;
		f_height=play.f_height;
		f_xpos=play.f_xpos;
		f_ypos=play.f_ypos;
		fps=play.fps;
		gauge=play.gauge;
		initialTime=System.currentTimeMillis();
	}
	final public void setDifficulty(int difficulty){
		this.difficulty=difficulty;
	}
	
	final public void addPattern(Pattern pattern){
		patternList.add(pattern);
	}
	final public void addPatterns(Pattern[] patternArray){
		patternList.addAll(Arrays.asList(patternArray));
	}
	final public void addPatterns(List<Pattern> patternList){
		this.patternList.addAll(patternList);
	}
	final public void updateAllPatterns(){
		for(Pattern pattern:patternList){
			pattern.update();
		}
	}
	final public void drawAllPattern(){
		for(Pattern pattern:patternList)
			pattern.draw(buffg,play);
	}
	final public int second(){
		return (int)(System.currentTimeMillis()-initialTime)/1000;
	}
	
	abstract public void init();//Stage 초기화
	abstract public void play();//루프 마다 이 메서드 실행
	abstract public void draw();//play() 이후 변화된 화면을 제작
	abstract public boolean continuing();//아직 Stage 가 진행 중인지. 예)20초 지났나?

}