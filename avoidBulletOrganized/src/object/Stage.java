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
	 * �� �÷��װ� true �� ��Ʈ�� ��� ���� ������ ������ �ν��մϴ�.
	 * �� ��� �ܺο����� �ൿ�� ������ ���� ������, �޴� ȭ���̳� ���� ���� ȭ������ �Ѿ ��
	 * �� �÷��׸� ��Ʈ�ϸ� �����մϴ�.
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
	 * Stage ���� ���� ���̵� ���(��: EASY)�� �־��ָ� �˴ϴ�.
	 * @param difficulty ���̵�(EASY,MODERATE,HARD)
	 */
	final public void setDifficulty(int difficulty){
		this.difficulty=difficulty;
	}
	/**
	 * �׷��� ���۸� �����մϴ�. Stage ���� ���� �־���� �մϴ�.
	 * @param buffg �׷��� ���� ��ü
	 */
	final public void setBuffer(Graphics buffg){
		this.buffg=buffg;
	}
	/**
	 * �ܺ� �������̽� ��ü�� �����մϴ�.
	 * keyLeft �� Ű ������ ���⼭ ������ ��ü���� �����մϴ�.
	 * @param play �ܺ� �������̽� ��ü
	 */
	final public void setPlay(Play play){
		this.play=play;
		fps=play.fps;
		gauge=play.gauge;
		initialTime=System.currentTimeMillis();
	}
	/**
	 * @return �ش� Stage�� ���̵�
	 */
	final public int getDifficulty(){
		return difficulty;
	}
	/**
	 * @return �ش� Stage�� ȭ�� �ʺ�
	 */
	final public int getWidth(){
		return play.f_width;
	}
	/**
	 * @return �ش� Stage�� ȭ�� ����
	 */
	final public int getHeight(){
		return play.f_height;
	}
	/**
	 * Pattern ��ü �ϳ��� Stage �� ���� ����Ʈ�� �߰��մϴ�.
	 * Stage Ŭ������ ���� �� ����Ʈ ���� Pattern �� ���ʴ�� ������Ʈ �մϴ�.
	 * (���� �߰��� Pattern �� ���߿� �߰��� Pattern �� ������ ������ ���� �� �ֽ��ϴ�.) 
	 * @param pattern �߰��� ������ ��Ÿ���� Pattern Ŭ����
	 */
	final public void addPattern(Pattern pattern){
		patternList.add(pattern);
	}
	/**
	 * Stage �� ���� ����Ʈ�� ��� �ִ� {@link Pattern} ��ü�� ���¸� �߰��� ���ʷ� ������Ʈ �մϴ�.
	 * �� �޼���� ���������� ��  {@link Pattern} ��ü�� {@link Pattern#move()} �� �ѹ��� ȣ���մϴ�. 
	 */
	final public void updateAllPatterns(){
		for(Pattern pattern:patternList){
			pattern.update();
		}
	}
	/**
	 * Stage �� ���� ����Ʈ�� ��� �ִ� {@link Pattern} ��ü�� �߰��� ���ʷ� ����� �׷��� ���ۿ� �׸��ϴ�.
	 */
	final public void drawAllPatterns(){
		for(Pattern pattern:patternList)
			pattern.draw(buffg,play);
	}
	/**
	 * {@link Pattern} Ŭ������ �̿����� �ʰ� ����̳� �� ���� ������ ������Ʈ�� �� ������ ���� �׸��ϴ�. 
	 * @param image �׸� �̹���
	 * @param x �̹����� ���� ���� �׵θ��� x��ǥ
	 * @param y �̹����� ���� ���� �׵θ��� y��ǥ
	 */
	final public void drawImage(Image image,int x,int y){
		buffg.drawImage(image, x, y, play);
	}
	/**
	 * ������ ��ġ�� ������ ���ڿ��� �� ������ ���� ��Ÿ���ϴ�. 
	 * @param str ��Ÿ�� ���ڿ�
	 * @param x ���ڿ��� ���� ���� �׵θ��� x��ǥ
	 * @param y ���ڿ��� ���� ���� �׵θ��� y��ǥ
	 */
	final public void drawString(String str,int x,int y){
		buffg.drawString(str, x, y);
	}
	/**
	 * {@link Stage#drawString(String, int, int) drawString}�� ȣ���Ͽ� ���ڿ��� ��Ÿ���� ���� ��Ʈ ������ �� �� �ֽ��ϴ�.
	 * @param font ���� ȭ�鿡 �� ���ڿ��鿡 ����� ��Ʈ
	 */
	final public void setFont(Font font){
		buffg.setFont(font);
	}
	
	/**
	 * ���� Stage�� ��� �ð��� �ʴ����� ��Ÿ����(millisecond ������ �ƴմϴ�), ��Ȯ���� �Ҽ������� millisecond ���ر��� �����˴ϴ�.
	 * ����������  {@link System#currentTimeMillis()}�� ����մϴ�.
	 * @return
	 */
	final public double second(){
		return (System.currentTimeMillis()-initialTime)/1000+1;
	}
	/**
	 * gif,png ���� ������ ���� �̹����� �μ��� �Ѱܹ޾� {@link SingleObject} ��ü�� ������ �� �� �ִ� Image ��ü�� �����մϴ�. 
	 * @param filepath �̹��� ������ ����(���)���, ���� ���� ���� ���� ��� ���� �̸�(filename.extension).
	 * @return �ش� �̹����� Image ��ü
	 */
	final public Image getImage(String filepath){
		return new ImageIcon(filepath).getImage();
	}
	
	/**
	 * Stage ��ü�� �۵��� �����ϱ� ���� �ʱ�ȭ�� �� �� �ִ� �޼����Դϴ�.
	 * Stage Ŭ������ �ý��� ������ �ǵ��� �����ڰ� ȣ��ȴٴ� ������ �����Ƿ�
	 * ����� ������ �ʱ�ȭ�� ��� �� �޼��� �ȿ��� ���ؾ� �մϴ�.
	 * �� �޼���� �������� ���� �� �� �ѹ� ȣ��˴ϴ�.
	 * {@link Stage#getImage(String)},Stage{@link #addPattern(Pattern)} �޼������ ȣ���� �� �޼��� �ȿ����� �̷������ �մϴ�.
	 */
	abstract public void init();//Stage �ʱ�ȭ
	/**
	 * {@link Stage#updateAllPatterns()}�� ����� ���� �������� ���� Stage ���� ���¸� ������ ������ ������Ʈ �ϴ� ��� �� �޼��� �ȿ��� ȣ���ϸ� �����մϴ�.
	 * �� �޼��尡 ����� ���� ������ �ѹ� ȣ��� ������ {@link Stage#count}������ 1�� �����մϴ�. 
	 */
	abstract public void play();//���� ���� �� �޼��� ����
	/**
	 * draw�� �����ϴ� (�� ����� �̹��� ���ۿ� �׸��� �۾��� �ϴ�) ��� �޼���� �� �޼��� �ȿ��� ȣ��Ǿ�� �մϴ�.
	 * (��:{@link #drawAllPatterns()}, {@link Stage#drawImage(Image, int, int)})
	 * �׷��� ���� ����� ����� ���ǵ��� �ʽ��ϴ�.
	 * �� �޼��� �ȿ��� ȣ��� ������� �̹��� ���۰� ������Ʈ �ǹǷ� ���� ȣ��� draw �޼���� ȿ���� ������ ������ ���� ���� �ֽ��ϴ�.
	 */
	abstract public void draw();//play() ���� ��ȭ�� ȭ���� ����
	/**
	 * ����� �������� ���ǹ����� �� �޼����� ���ϰ��� �����մϴ�.
	 * �� �����Ӹ��� {@link #play()}���� ���� ȣ��ǹǷ� �����Ͻʽÿ�.
	 * @return �ش� Stage �� ��� �Ǿ�� �ϸ� true��, �ݴ��� ��� false�� �����մϴ�.
	 */
	abstract public boolean continuing();//���� Stage �� ���� ������. ��)20�� ������?

}