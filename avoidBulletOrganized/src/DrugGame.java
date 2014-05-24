
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.JFrame;

final public class DrugGame {
	public static void main(String[] args) {
		new Play().play();
	}
}

@SuppressWarnings("serial")
final class Play extends JFrame implements KeyListener{
	int fps=50;
	int f_width;
	int f_height;
	int f_xpos;
	int f_ypos;
	double gauge;
	
	private Image buffImage; // �������۸������ѹ���
	private Random rand;
	
	@SuppressWarnings("rawtypes")
	private Class[] stages={/*AvoidBulletStage.class,*/ManagingGradeStage.class};//����� Stages ����Ʈ
	private Stage stage;
	// ���� ���� ��
	
	public Play() {		
		f_width=800;
		f_height=600;
		gauge=100.0;
		rand=new Random();
		setTitle("avoidBullet");
		setSize(f_width, f_height);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		// �������� �����쿡 ǥ�õɶ� ��ġ�� �����ϱ� ����
		// ���� ������� �ػ� ���� �޾ƿɴϴ�.

		f_xpos = (int) (screen.getWidth() / 2 - f_width / 2);
		f_ypos = (int) (screen.getHeight() / 2 - f_height / 2);
		// �������� ����� ȭ�� ���߾ӿ� ��ġ��Ű�� ���� ��ǥ ���� ����մϴ�.

		setLocation(f_xpos, f_ypos);// �������� ȭ�鿡 ��ġ
		setResizable(false); // �������� ũ�⸦ ���Ƿ� ������ϰ� ����
		setVisible(true); // �������� ���� ���̰� ��

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// �����ӿ�������X ��ư��Ŭ�������α׷�����
		addKeyListener(this);
		// Ű�����Է�ó���޾Ƶ��̱�
	}

	public void play() {
		for(int i=0;i<stages.length;i++){
			int nextNum=rand.nextInt(stages.length);
			try {
				stage=(Stage)stages[nextNum].newInstance();
			} catch (InstantiationException | IllegalAccessException e1) {
				e1.printStackTrace();
				System.exit(1);
			}
			stage.setPlay(this);
			stage.setDifficulty(Stage.MODERATE);
			stage.init();
			while(stage.continuing()){
				stage.play();
				stage.count++;
				repaint();
				try {
					Thread.sleep(1000/fps);
				} catch (InterruptedException e) {}
			}
			if(stage.stageFailed){
				break;
			}
		}
	}
	public void paint(Graphics g) {
		try{
			buffImage = createImage(f_width, f_height);
			stage.buffg = buffImage.getGraphics();
			update(g);
		}catch(NullPointerException e){}
	}
	public void update(Graphics g) {
		stage.draw();
		g.drawImage(buffImage, 0, 0, this);
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