package object;


import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import main.Play;
/**
 * 모든 Stage의 원형입니다.
 * DrugGame 내의 모든 Stage는 이 클래스를 상속합니다.
 */
abstract public class Stage{
	
	/** 
	 * 해당 스테이지의 난이도입니다.
	 * {@link #setDifficulty(int) setDifficulty}으로 
	 * 클래스 외부에서 설정할 수 있습니다.
	 * 클래스 내부에서의 사용례는 정해져 있지 않습니다.
	 */
	public final static int EASY=0;
	public final static int MODERATE=1;
	public final static int HARD=2;
	
	/** 
	 * 사용자 인터페이스인 Play 클래스가 이 필드들을 
	 * real time 으로 초기화 합니다.
	 * Stage 클래스와 관련된 모든 객체가 참조할 수 있습니다.
	 * 직접 값을 바꾸지 않길 권장합니다.
	 */
	public boolean keyUp = false;
	public boolean keyDown = false;
	public boolean keyLeft = false;
	public boolean keyRight = false;
	public boolean keySpace = false;
	
	/** 초당 프레임 */
	public int fps;
	/** 플레이어의 체력*/
	public double gauge;
	/** Stage 가 플레이 되는데  총 걸리는 시간*/
	public int span;
	/**
	 * 매 루프마다 1씩 증가하는 값입니다. 
	 * {@link #fps fps}의 값에 따라 초당 증가량이 변화합니다.
	 * 초기값은 0입니다.
	 * 현재 Stage 에서 경과 시간을 알고 싶다면 {@link #second() second()} 메서드가
	 * 더 안전합니다. 
	 */
	public int count=0;
	/**
	 * 이 플래그가 true 로 세트된 경우 Stage 외부에서는 
	 * 이 Stage가 클리어 되지 못했다고 인식합니다.
	 * 이 경우 외부에서의 행동은 정해져 있지 않습니다.
	 */
	public boolean stageFailed=false;
	
	private Graphics buffg;
	private Play play;
	private long initialTime;
	private int difficulty;
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
	
	final public int getDifficulty(){
		return difficulty;
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
	
	final public double second(){
		return (System.currentTimeMillis()-initialTime)/1000+1;
	}
	final public Image getImage(String filepath){
		return new ImageIcon(filepath).getImage();
	}
	
	abstract public void init();//Stage 초기화
	abstract public void play();//루프 마다 이 메서드 실행
	abstract public void draw();//play() 이후 변화된 화면을 제작
	abstract public boolean continuing();//아직 Stage 가 진행 중인지. 예)20초 지났나?

}