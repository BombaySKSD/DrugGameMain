
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

abstract public class Stage{
	public final static int EASY=0;
	public final static int MODERATE=1;
	public final static int HARD=2;
	
	protected int count=0;
	protected boolean keyUp = false;
	protected boolean keyDown = false;
	protected boolean keyLeft = false;
	protected boolean keyRight = false;
	protected boolean keySpace = false;
	
	protected int f_width;
	protected int f_height;
	protected int f_xpos;
	protected int f_ypos;
	protected int difficulty;
	protected int fps;
	
	int player_x;
	int player_y;
	double gauge;
	boolean stageFailed=false;
	int span;
	
	Play play;
	Stage stage;
	Graphics buffg;
	ArrayList<Pattern> patternList=new ArrayList<Pattern>();
	
	Image player_img;
	Image background_img;
	
	Stage(){}
	Stage(Play play,int difficulty){
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
		player_x=f_width/2;
		player_y=f_height-100;
		gauge=play.gauge;
		
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
		return count/fps+1;
	}
	
	abstract public void init();//Stage �ʱ�ȭ
	abstract public void play();//���� ���� �� �޼��� ����
	abstract public void draw();//play() ���� ��ȭ�� ȭ���� ����
	abstract public boolean continuing();//���� Stage �� ���� ������. ��)20�� ������?

}