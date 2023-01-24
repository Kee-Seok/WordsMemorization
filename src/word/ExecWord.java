package word;

import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.swing.JFrame;

public class ExecWord extends JFrame implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Container contentPane = getContentPane();
	static int sleepTime = 500;
	String word = "시작";
	int i = 0;
	float fontSize = 150f;
	float watchSize = 200f;
	boolean isStart = true;
	boolean isExec = true;
	boolean canChangableStatus = false;
	@SuppressWarnings("unused")
	private static boolean stop;
	
	public static Font watchFont, wordFont;
	{
		try {
			watchFont = Font.createFont(Font.TRUETYPE_FONT, new File("./temp/DS-DIGIB.ttf"));
			watchFont = watchFont.deriveFont(watchSize);
			wordFont = Font.createFont(Font.TRUETYPE_FONT, new File("./temp/경기천년제목.ttf"));
			wordFont = wordFont.deriveFont(fontSize);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void setStop(boolean stop) {
		ExecWord.stop = stop;
	}
	int x = 0;
	int y = 0;
	int xGab, yGab; // 마우스 좌표와, 출력되는 글자 좌표의 좌표 차이
	static Color fontColor = Color.black;
	public static Thread instance = new Thread(new ExecWord());
	Date date = new Date();
	public void run() {
		setVisible(true);
		Thread t = new Thread() {
			public void run() {
				while(true) {
					try {
					Thread.sleep(1000);
					date = new Date();
					repaint();
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		};
		t.start();
		while (isStart) {
			System.out.println(Thread.currentThread().getName());
			if(isExec) {
			try {
				setBackground(new Color(0, 0, 0, 0));
				Thread.sleep(sleepTime);
				if (Ex.words.size() > 0 && i >= 0) {// words ArrayList타입 변수에 단어와 뜻이 한개 이상씩 저장됐을때만 실행.
					word = Ex.words.get(i);
					repaint();
					i++;
					requestFocus();
					if (i >= Ex.words.size()) {
						i = 0;
						requestFocus();
					}
				} else {
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if(!isExec) {
			try {
			Thread.sleep(sleepTime);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
			
	}
	}

	public ExecWord() {
		init();
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				System.out.println(e.getKeyCode());
				switch (e.getKeyCode()) {
				case KeyEvent.VK_ESCAPE:
					dispose();
//						instance.stop();
					ExecWord.setStop(true);
					WordFrame.setStop(false);
					Thread t = new Thread(new WordFrame());
					t.start();
					isStart = false;
					break;
				case KeyEvent.VK_ADD:
					if(fontSize<170f) {
					fontSize += 10f;
					wordFont = wordFont.deriveFont(fontSize);
					System.out.println(fontSize);
					repaint();
					}
					break;
				case KeyEvent.VK_SUBTRACT:
					if(fontSize>0f) {
					fontSize -= 10f;
					wordFont = wordFont.deriveFont(fontSize);
					repaint();
					}
					break;
				case KeyEvent.VK_SPACE:
					if(isExec) {
						isExec = false;
						canChangableStatus = true;
						i--;
					}else if(!isExec) {
						isExec = true;
						canChangableStatus = false;
					}
					System.out.println(isExec);
					requestFocus();
					break;
				case KeyEvent.VK_LEFT :
					if(canChangableStatus&&i>0) {
					i--;
					word = Ex.words.get(i);
					repaint();
					System.out.println(i);
					}else {
						
					}
					break;
				case KeyEvent.VK_RIGHT:
					if(canChangableStatus&&i>=0) {
				    i++;
					word = Ex.words.get(i);
					repaint();
					}else {
						
					}
					System.out.println(i);
					break;
				}
			}
		});
		addMouseListener(new MouseSetting());
		addMouseMotionListener(new MouseSetting());
	}

	public void init() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(Toolkit.getDefaultToolkit().getScreenSize().width*3, 2000);
		setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2, Toolkit.getDefaultToolkit().getScreenSize().height/5);
		setUndecorated(true);
		setAlwaysOnTop(true); //JFrame이 항상 맨 위에 표시
		setVisible(false);
	}

	public void paint(Graphics g) {
		super.paint(g); // 배경 지우기
		g.setColor(fontColor);
		g.setFont(watchFont);
		g.drawString(date.toString().substring(11,19), 100, 450);
		wordFont = wordFont.deriveFont(fontSize);
		g.setFont(wordFont);
		System.out.println(fontSize);
		g.drawString("·" + word, 100, 250);
	}

	class MouseSetting implements MouseListener, MouseMotionListener {

		public void mouseClicked(MouseEvent e) {

		}

		public void mousePressed(MouseEvent e) {
			xGab = e.getPoint().x;
			yGab = e.getPoint().y;
		}

		public void mouseReleased(MouseEvent e) {

		}

		public void mouseEntered(MouseEvent e) {
			setCursor(new Cursor(Cursor.HAND_CURSOR));
		}

		public void mouseExited(MouseEvent e) {
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}

		public void mouseDragged(MouseEvent e) {
			x = e.getLocationOnScreen().x - xGab;
			y = e.getLocationOnScreen().y - yGab;
			setLocation(x, y);
			repaint();
		}

		public void mouseMoved(MouseEvent e) {

		}

	}
}
