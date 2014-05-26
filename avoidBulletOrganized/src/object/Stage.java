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
	 * 이 플래그가 true 로 세트된 경우 게임 오버인 것으로 인식합니다.
	 * 이 경우 외부에서의 행동은 정해져 있지 않지만, 메뉴 화면이나 게임 오버 화면으로 넘어갈 때
	 * 이 플래그를 세트하면 유용합니다.
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
	
	/**
	 * Stage 시작 전에 난이도 상수(예: EASY)를 넣어주면 됩니다.
	 * @param difficulty 난이도(EASY,MODERATE,HARD)
	 */
	final public void setDifficulty(int difficulty){
		this.difficulty=difficulty;
	}
	/**
	 * 그래픽 버퍼를 설정합니다. Stage 시작 전에 넣어줘야 합니다.
	 * @param buffg 그래픽 버퍼 객체
	 */
	final public void setBuffer(Graphics buffg){
		this.buffg=buffg;
	}
	/**
	 * 외부 인터페이스 객체를 설정합니다.
	 * keyLeft 등 키 값들은 여기서 설정된 객체들을 참고합니다.
	 * @param play 외부 인터페이스 객체
	 */
	final public void setPlay(Play play){
		this.play=play;
		fps=play.fps;
		gauge=play.gauge;
		initialTime=System.currentTimeMillis();
	}
	/**
	 * @return 해당 Stage의 난이도
	 */
	final public int getDifficulty(){
		return difficulty;
	}
	/**
	 * @return 해당 Stage의 화면 너비
	 */
	final public int getWidth(){
		return play.f_width;
	}
	/**
	 * @return 해당 Stage의 화면 높이
	 */
	final public int getHeight(){
		return play.f_height;
	}
	/**
	 * Pattern 객체 하나를 Stage 의 내장 리스트에 추가합니다.
	 * Stage 클래스는 실행 시 리스트 내의 Pattern 을 차례대로 업데이트 합니다.
	 * (먼저 추가된 Pattern 이 나중에 추가된 Pattern 에 가려져 보이지 않을 수 있습니다.) 
	 * @param pattern 추가할 패턴을 나타내는 Pattern 클래스
	 */
	final public void addPattern(Pattern pattern){
		patternList.add(pattern);
	}
	/**
	 * Stage 의 내장 리스트에 들어 있는 {@link Pattern} 객체의 상태를 추가된 차례로 업데이트 합니다.
	 * 이 메서드는 내부적으로 각  {@link Pattern} 객체의 {@link Pattern#move()} 를 한번씩 호출합니다. 
	 */
	final public void updateAllPatterns(){
		for(Pattern pattern:patternList){
			pattern.update();
		}
	}
	/**
	 * Stage 의 내장 리스트에 들어 있는 {@link Pattern} 객체를 추가된 차례로 내장된 그래픽 버퍼에 그립니다.
	 */
	final public void drawAllPatterns(){
		for(Pattern pattern:patternList)
			pattern.draw(buffg,play);
	}
	/**
	 * {@link Pattern} 클래스를 이용하지 않고 배경이나 땅 같은 간단한 오브젝트를 한 프레임 동안 그립니다. 
	 * @param image 그릴 이미지
	 * @param x 이미지의 왼쪽 위쪽 테두리의 x좌표
	 * @param y 이미지의 왼쪽 위쪽 테두리의 y좌표
	 */
	final public void drawImage(Image image,int x,int y){
		buffg.drawImage(image, x, y, play);
	}
	/**
	 * 지정된 위치에 지정된 문자열을 한 프레임 동안 나타냅니다. 
	 * @param str 나타낼 문자열
	 * @param x 문자열의 왼쪽 위쪽 테두리의 x좌표
	 * @param y 문자열의 왼쪽 위쪽 테두리의 y좌표
	 */
	final public void drawString(String str,int x,int y){
		buffg.drawString(str, x, y);
	}
	/**
	 * {@link Stage#drawString(String, int, int) drawString}을 호출하여 문자열을 나타내기 전에 폰트 지정을 할 수 있습니다.
	 * @param font 이후 화면에 쓸 문자열들에 적용될 폰트
	 */
	final public void setFont(Font font){
		buffg.setFont(font);
	}
	
	/**
	 * 현재 Stage의 경과 시간을 초단위로 나타내되(millisecond 단위가 아닙니다), 정확도는 소수점으로 millisecond 수준까지 측정됩니다.
	 * 내부적으로  {@link System#currentTimeMillis()}을 사용합니다.
	 * @return
	 */
	final public double second(){
		return (System.currentTimeMillis()-initialTime)/1000+1;
	}
	/**
	 * gif,png 등의 포맷을 가진 이미지를 인수로 넘겨받아 {@link SingleObject} 객체의 생성에 쓸 수 있는 Image 객체를 리턴합니다. 
	 * @param filepath 이미지 파일의 절대(상대)경로, 같은 폴더 내에 있을 경우 파일 이름(filename.extension).
	 * @return 해당 이미지의 Image 객체
	 */
	final public Image getImage(String filepath){
		return new ImageIcon(filepath).getImage();
	}
	
	/**
	 * Stage 객체가 작동을 시작하기 전에 초기화를 할 수 있는 메서드입니다.
	 * Stage 클래스는 시스템 내에서 의도한 생성자가 호출된다는 보장이 없으므로
	 * 사용자 고유의 초기화는 모두 이 메서드 안에서 행해야 합니다.
	 * 이 메서드는 스테이지 시작 전 단 한번 호출됩니다.
	 * {@link Stage#getImage(String)},Stage{@link #addPattern(Pattern)} 메서드들의 호출은 이 메서드 안에서만 이루어져야 합니다.
	 */
	abstract public void init();//Stage 초기화
	/**
	 * {@link Stage#updateAllPatterns()}나 사용자 정의 변수조작 같은 Stage 내의 상태를 프레임 단위로 업데이트 하는 경우 이 메서드 안에서 호출하면 적절합니다.
	 * 이 메서드가 내장된 루프 내에서 한번 호출될 때마다 {@link Stage#count}변수가 1씩 증가합니다. 
	 */
	abstract public void play();//루프 마다 이 메서드 실행
	/**
	 * draw로 시작하는 (즉 내장된 이미지 버퍼에 그리는 작업을 하는) 모든 메서드는 이 메서드 안에서 호출되어야 합니다.
	 * (예:{@link #drawAllPatterns()}, {@link Stage#drawImage(Image, int, int)})
	 * 그렇지 않을 경우의 결과는 정의되지 않습니다.
	 * 이 메서드 안에서 호출된 순서대로 이미지 버퍼가 업데이트 되므로 먼저 호출된 draw 메서드는 효과가 가려져 보이지 않을 수도 있습니다.
	 */
	abstract public void draw();//play() 이후 변화된 화면을 제작
	/**
	 * 내장된 루프에서 조건문에서 이 메서드의 리턴값을 참조합니다.
	 * 매 프레임마다 {@link #play()}보다 먼저 호출되므로 참조하십시오.
	 * @return 해당 Stage 가 계속 되어야 하면 true를, 반대의 경우 false를 리턴합니다.
	 */
	abstract public boolean continuing();//아직 Stage 가 진행 중인지. 예)20초 지났나?

}