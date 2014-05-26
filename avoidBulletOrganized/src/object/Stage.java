package object;


import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import main.Play;
/**
 * ��� Stage�� �����Դϴ�.
 * DrugGame ���� ��� Stage�� �� Ŭ������ ����մϴ�.
 */
abstract public class Stage{
	
	/** 
	 * �ش� ���������� ���̵��Դϴ�.
	 * {@link #setDifficulty(int) setDifficulty}���� 
	 * Ŭ���� �ܺο��� ������ �� �ֽ��ϴ�.
	 * Ŭ���� ���ο����� ���ʴ� ������ ���� �ʽ��ϴ�.
	 */
	public final static int EASY=0;
	public final static int MODERATE=1;
	public final static int HARD=2;
	
	/** 
	 * ����� �������̽��� Play Ŭ������ �� �ʵ���� 
	 * real time ���� �ʱ�ȭ �մϴ�.
	 * Stage Ŭ������ ���õ� ��� ��ü�� ������ �� �ֽ��ϴ�.
	 * ���� ���� �ٲ��� �ʱ� �����մϴ�.
	 */
	public boolean keyUp = false;
	public boolean keyDown = false;
	public boolean keyLeft = false;
	public boolean keyRight = false;
	public boolean keySpace = false;
	
	/** �ʴ� ������ */
	public int fps;
	/** �÷��̾��� ü��*/
	public double gauge;
	/** Stage �� �÷��� �Ǵµ�  �� �ɸ��� �ð�*/
	public int span;
	/**
	 * �� �������� 1�� �����ϴ� ���Դϴ�. 
	 * {@link #fps fps}�� ���� ���� �ʴ� �������� ��ȭ�մϴ�.
	 * �ʱⰪ�� 0�Դϴ�.
	 * ���� Stage ���� ��� �ð��� �˰� �ʹٸ� {@link #second() second()} �޼��尡
	 * �� �����մϴ�. 
	 */
	public int count=0;
	/**
	 * �� �÷��װ� true �� ��Ʈ�� ��� Stage �ܺο����� 
	 * �� Stage�� Ŭ���� ���� ���ߴٰ� �ν��մϴ�.
	 * �� ��� �ܺο����� �ൿ�� ������ ���� �ʽ��ϴ�.
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
	
	abstract public void init();//Stage �ʱ�ȭ
	abstract public void play();//���� ���� �� �޼��� ����
	abstract public void draw();//play() ���� ��ȭ�� ȭ���� ����
	abstract public boolean continuing();//���� Stage �� ���� ������. ��)20�� ������?

}