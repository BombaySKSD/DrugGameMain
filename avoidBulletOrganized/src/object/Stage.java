package object;


import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import main.Play;

abstract public class Stage{
	public final static int EASY=0;
	public final static int MODERATE=1;
	public final static int HARD=2;
	
	public boolean keyUp = false;
	public boolean keyDown = false;
	public boolean keyLeft = false;
	public boolean keyRight = false;
	public boolean keySpace = false;
	
	public int difficulty;
	public int fps;
	public double gauge;
	public int span;
	public int count=0;
	public boolean stageFailed=false;
	
	private Graphics buffg;
	private Play play;
	private long initialTime;
	private ArrayList<Pattern> patternList=new ArrayList<Pattern>();
	
	public Stage(){}
	public Stage(Play play,int difficulty){
		setPlay(play);
		setDifficulty(difficulty);
	}
	
	final public void setDifficulty(int difficulty){
		this.difficulty=difficulty;
	}
	final public void setBuffer(Graphics buffg){
		this.buffg=buffg;
	}
	final public void setPlay(Play play){
		this.play=play;
		fps=play.fps;
		gauge=play.gauge;
		initialTime=System.currentTimeMillis();
	}
	
	final public int getWidth(){
		return play.f_width;
	}
	final public int getHeight(){
		return play.f_height;
	}
	
	final public void addPattern(Pattern pattern){
		patternList.add(pattern);
	}
	final public void updateAllPatterns(){
		for(Pattern pattern:patternList){
			pattern.update();
		}
	}
	final public void drawAllPatterns(){
		for(Pattern pattern:patternList)
			pattern.draw(buffg,play);
	}
	final public void drawImage(Image image,int x,int y){
		buffg.drawImage(image, x, y, play);
	}
	final public void drawString(String str,int x,int y){
		buffg.drawString(str, x, y);
	}
	final public void setFont(Font font){
		buffg.setFont(font);
	}
	
	final public int second(){
		return (int)(System.currentTimeMillis()-initialTime)/1000+1;
	}
	final public Image getImage(String filepath){
		return new ImageIcon(filepath).getImage();
	}
	
	abstract public void init();//Stage 초기화
	abstract public void play();//루프 마다 이 메서드 실행
	abstract public void draw();//play() 이후 변화된 화면을 제작
	abstract public boolean continuing();//아직 Stage 가 진행 중인지. 예)20초 지났나?

}