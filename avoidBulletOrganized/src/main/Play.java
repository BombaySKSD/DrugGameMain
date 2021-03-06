package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.JFrame;

import object.Stage;
import stage.alcohol.AlcoholStage;
import stage.firetruck.FiretruckStage;
import stage.grade.ManagingGradeStage;
import stage.start.StartPage;
import stage.violence.ViolenceStage;

@SuppressWarnings("serial")
final public class Play extends JFrame implements KeyListener{
	public int fps=1000;
	public int f_width;
	public int f_height;
	public int f_xpos;
	public int f_ypos;
	public double gauge;
	
	int prev_stage=-1;
	
	private Image buffImage; // 더블버퍼링을위한버퍼
	private Random rand;

	Image anotherImage;
	boolean another=false,first=true;
	boolean startPress=false;
	
	public double initialTime;
	@SuppressWarnings("rawtypes")
	/**
	 * Stage 배열
	 */
	private Class[] stages={ManagingGradeStage.class,
		ViolenceStage.class,
		AlcoholStage.class,
		FiretruckStage.class};//실행될 Stages 리스트
	private Stage stage;
	// 변수 생성 끝
	
	public Play() {		
		f_width=800;
		f_height=600;
		gauge=100.0;
		rand=new Random();
		setTitle("Coconut Talk");
		setSize(f_width, f_height);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		// 프레임이 윈도우에 표시될때 위치를 세팅하기 위해
		// 현재 모니터의 해상도 값을 받아옵니다.

		f_xpos = (int) (screen.getWidth() / 2 - f_width / 2);
		f_ypos = (int) (screen.getHeight() / 2 - f_height / 2);
		// 프레임을 모니터 화면 정중앙에 배치시키기 위해 좌표 값을 계산합니다.

		setLocation(f_xpos, f_ypos);// 프레임을 화면에 배치
		setResizable(false); // 프레임의 크기를 임의로 변경못하게 설정
		setVisible(true); // 프레임을 눈에 보이게 함

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// 프레임오른쪽위X 버튼을클릭시프로그램종료
		addKeyListener(this);
		// 키보드입력처리받아들이기
	}

	public void play() {
		stage=new StartPage();
		stage.setPlay(this);
		while(startPress==false){
			long initialTime=System.currentTimeMillis();
			repaint();
			try {
				long delay=1000/fps-(System.currentTimeMillis()-initialTime);
				if(delay>0){
					Thread.sleep(delay);
				}
			} catch (InterruptedException e) {}
		}
		initialTime=System.currentTimeMillis();
		for(int i=0;true;i++){
			int nextNum;
			if (prev_stage==-1) nextNum=rand.nextInt(stages.length);
			else {
				do nextNum=rand.nextInt(stages.length);
				while (prev_stage==nextNum);
			}
			prev_stage=nextNum;
			
			try {
				stage=(Stage)stages[nextNum].newInstance();
			} catch (InstantiationException | IllegalAccessException e1) {
				e1.printStackTrace();
				System.exit(1);
			}
			stage.setPlay(this);
			
			/**
			 * set difficulty
			 */
			if (i<=1) stage.setDifficulty(Stage.EASY);
			else if (i<=5) stage.setDifficulty(Stage.MODERATE);
			else stage.setDifficulty(Stage.HARD);
			
			stage.init();
			while(stage.continuing()){
				long initialTime=System.currentTimeMillis();
				stage.play();
				stage.count++;
				repaint();
				try {
					long delay=1000/fps-(System.currentTimeMillis()-initialTime);
					if(delay>0){
						Thread.sleep(delay);
					}
				} catch (InterruptedException e) {}
			}
			if(stage.stageFailed){
				break;
			}
			gauge=stage.gauge;
		}
	}
	/*class DoubleBufferThread extends Thread{
		boolean draw=false;
		@Override
		public void run() {
			super.run();
			while(true){
				if(draw){
					draw=false;
					stage.draw();
				}
			}
		}
	}*/
	public void paint(Graphics g) {
		try{
			/*if(first){
				first=false;
				t=new DoubleBufferThread();
				t.start();
				buffImage = createImage(f_width, f_height);
				stage.setBuffer((Graphics2D)buffImage.getGraphics());
			}else{
				g.drawImage(anotherImage, 0, 0, this);
				anotherImage=buffImage;
				buffImage = createImage(f_width, f_height);
				stage.setBuffer((Graphics2D)buffImage.getGraphics());
				t.draw=true;
			}*/
			buffImage = createImage(f_width, f_height);
			stage.setBuffer((Graphics2D)buffImage.getGraphics());
			stage.draw();
			g.drawImage(buffImage, 0, 0, this);
		}catch(NullPointerException e){}
	}
	
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			stage.keyUp = true;
			break;
		case KeyEvent.VK_DOWN:
			stage.keyDown = true;
			break;
		case KeyEvent.VK_LEFT:
			stage.keyLeft = true;
			break;
		case KeyEvent.VK_RIGHT:
			stage.keyRight = true;
			break;
		case KeyEvent.VK_SPACE:
			stage.keySpace = true;
			break;
		}
		startPress=true;
	}
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			stage.keyUp = false;
			break;
		case KeyEvent.VK_DOWN:
			stage.keyDown = false;
			break;
		case KeyEvent.VK_LEFT:
			stage.keyLeft = false;
			break;
		case KeyEvent.VK_RIGHT:
			stage.keyRight = false;
			break;
		case KeyEvent.VK_SPACE:
			stage.keySpace = false;
			break;
		}
	}
	public void keyTyped(KeyEvent e) {}
}
