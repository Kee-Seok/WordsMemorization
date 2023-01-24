package word;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class WordFrame extends JFrame implements Runnable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public static final int SCREEN_WIDTH = screenSize.width * 3 / 7;
	public static final int SCREEN_HEIGHT = screenSize.height * 4 / 7;
	public static int X = SCREEN_WIDTH * 2 / 3;
	public static int Y = SCREEN_HEIGHT * 4 / 14;
	int xGab, yGab;
	static Thread mainThread = new Thread(new WordFrame());
	Container contentPane = getContentPane();
	JPanel mainPanel = new MainPanel();
	JPanel wordPanel = new WordPanel();
	JPanel deletePanel = new DeletePanel();
	static JComboBox<String> comboBox;
	
	Font titleFont, wordFont;
	Font componentFont;
	float wordFontSize = 30f;
	float componentFontSize = 20f;

	
	private static boolean stop;
	
	public static void setStop(boolean stop) {
		WordFrame.stop = stop;
	}
	
	static JComboBox<String> colorCombo;
	ExecWord execFrame;//WordFrame클래스 호출시 객체생성하지 말고 일단 전역변수 선언만 해놓음.
	
	// 단어
	DefaultListModel<String> model;
	JList<String> list;
	JScrollPane scroll;
	// 뜻로
	DefaultListModel<String> model2;
	JList<String> list2;
	JScrollPane scroll2;

	// 단어
	DefaultListModel<String> model3;
	JList<String> list3;
	JScrollPane scroll3;
	// 뜻로
	DefaultListModel<String> model4;
	JList<String> list4;
	JScrollPane scroll4;
	
	public static void main(String[] args) {
		mainThread.start();
	}

	public void run() {
		if(!stop) {
		init();
		add();
		requestFocus();
		}
	}

	public void add() {
		mainPanel.setBounds(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		wordPanel.setBounds(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		deletePanel.setBounds(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		contentPane.add(wordPanel);
		wordPanel.setVisible(false);
		contentPane.add(deletePanel);
		deletePanel.setVisible(false);
		contentPane.add(mainPanel);
		Ex.getWordsFromExcel(model, model2, model3, model4);
		setVisible(true); // 마지막에 프레임 보이도록 함.
		this.requestFocus();
	}

	public void init() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		setLocation(X, Y);
		setLayout(null);
		setUndecorated(true);
		addKeyListener(new KeySetting());
		
	}

	/**
	 * 메뉴창으로 돌아가게 하기 위한 메서드.
	 */
	public void goToMain() {
		wordPanel.setVisible(false);
		deletePanel.setVisible(false);
		mainPanel.setVisible(true);
		System.out.println("메뉴창으로 돌아갑니다.");
		requestFocus();
	}

	/**
	 * @author Shin 단어 입력패널로 넘어가기 위한 메서드
	 */
	public void goToWord() {
		mainPanel.setVisible(false);
		deletePanel.setVisible(false);
		wordPanel.setVisible(true);
		requestFocus();
	}

	/**
	 * 삭제리스트 패널로 넘어가기 위한 메서드
	 */
	public void goToDelete() {
		mainPanel.setVisible(false);
		wordPanel.setVisible(false);
		deletePanel.setVisible(true);
		requestFocus();
	}

	class MainPanel extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public JButton goWordPanelBtn = new JButton("단어추가");
		public JButton goDeletedWordPanelBtn = new JButton("삭제리스트");

		public MainPanel() {
			try {
				componentFont = Font.createFont(Font.TRUETYPE_FONT, new File("./temp/경기천년제목.ttf"));
				componentFont = componentFont.deriveFont(componentFontSize);
				titleFont = Font.createFont(Font.TRUETYPE_FONT, new File("./temp/경기천년제목.ttf"));
				titleFont = titleFont.deriveFont(50f);
				wordFont = Font.createFont(Font.TRUETYPE_FONT, new File("./temp/경기천년제목.ttf"));
				wordFont = wordFont.deriveFont(wordFontSize);
			} catch (FontFormatException | IOException e) {
				e.printStackTrace();
			}
			setLayout(new FlowLayout(FlowLayout.CENTER,500,70));
			goWordPanelBtn.setFont(titleFont);
			goDeletedWordPanelBtn.setFont(titleFont);
			goWordPanelBtn.addActionListener(new ActionSetting());
			goDeletedWordPanelBtn.addActionListener(new ActionSetting());
			add(goWordPanelBtn);
			add(goDeletedWordPanelBtn);
			goWordPanelBtn.setFocusable(false);
			goDeletedWordPanelBtn.setFocusable(false);// JButton은 자동으로 포커싱되기때문에 JFrame에 포커싱시키기 위한 강제 포커스 비활성화
			addMouseListener(new MouseSetting());
			addMouseMotionListener(new MouseSetting());
		}
	}

	/**
	 * 
	 * @author Shin 단어입력을 위한 패널 구현
	 */
	class WordPanel extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		JPanel gridPanel = new JPanel(new GridLayout(1, 2, 20, 20));
		JPanel centerPanel = new JPanel(new BorderLayout()); // JList와 JLabel을 담을 공간
		JPanel centerPanel2 = new JPanel(new BorderLayout()); // JList와 JLabel을 담을 공간
		JPanel southPanel = new JPanel(); // JTextField와 JLabel과 JButton을 담을 공간
		JLabel title = new JLabel("단어입력", SwingConstants.CENTER);

		// 단어
		JLabel wordLb = new JLabel("단어", SwingConstants.CENTER);
		// 뜻로
		JLabel meaningLb = new JLabel("뜻", SwingConstants.LEFT);

		JLabel wordLb2 = new JLabel("단어", SwingConstants.CENTER);
		JTextField wordTf = new JTextField(5);

		JLabel meaningLb2 = new JLabel("뜻", SwingConstants.LEFT);
		JTextField meaningTf = new JTextField(5);

		JButton inputBtn = new JButton("등록");
		JLabel execLb = new JLabel("주기: ",SwingConstants.CENTER);
		JButton execBtn = new JButton("실행");
		JButton goDeleteBtn = new JButton("삭제목록");
		JLabel colorLb = new JLabel("색상",SwingConstants.CENTER);
		public WordPanel() {
			setLayout(new BorderLayout());
			this.init();
			add(title, "North");
			add(gridPanel, "Center");
			add(southPanel, "South");
			inputBtn.setFocusable(false);
			wordTf.requestFocus();
		}

		public void init() {
			wordFontSize = 30f;
			componentFontSize = 20f;
			try {
				componentFont = Font.createFont(Font.TRUETYPE_FONT, new File("./temp/경기천년제목.ttf"));
				componentFont = componentFont.deriveFont(componentFontSize);
				titleFont = Font.createFont(Font.TRUETYPE_FONT, new File("./temp/경기천년제목.ttf"));
				titleFont = titleFont.deriveFont(50f);
				wordFont = Font.createFont(Font.TRUETYPE_FONT, new File("./temp/경기천년제목.ttf"));
				wordFont = wordFont.deriveFont(wordFontSize);
			} catch (FontFormatException | IOException e) {
				e.printStackTrace();
			}
			model = new DefaultListModel<>();
			list = new JList<>(model);
			
			list.setFont(wordFont);
			scroll = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

			model2 = new DefaultListModel<>();
			list2 = new JList<>(model2);
			list2.setFont(wordFont);
			scroll2 = new JScrollPane(list2, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

			wordLb.setFont(componentFont);
			meaningLb.setFont(componentFont);
			wordLb2.setFont(componentFont);
			meaningLb2.setFont(componentFont);
			execLb.setFont(componentFont);
			wordTf.setFont(componentFont);
			meaningTf.setFont(componentFont);
			

			
			title.setFont(titleFont);
			centerPanel.add(wordLb, "West");
			centerPanel.add(scroll, "Center");
			centerPanel2.add(meaningLb, "West");
			centerPanel2.add(scroll2, "Center");
			gridPanel.add(centerPanel);
			gridPanel.add(centerPanel2);

			southPanel.setLayout(new FlowLayout(FlowLayout.CENTER,10,10));
			southPanel.add(wordLb2);
			southPanel.add(wordTf);
			southPanel.add(meaningLb2);
			southPanel.add(meaningTf);
			southPanel.add(inputBtn);
			southPanel.add(execBtn);

			String[] second = {"0.5초","1초", "2초", "3초", "4초"};
	
			comboBox = new JComboBox<>(second);
			comboBox.setFont(componentFont);
			southPanel.add(execLb);
			southPanel.add(comboBox);
			
			comboBox.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if(e.getItem().equals("1초")) {
						ExecWord.sleepTime = 1000;
						System.out.println(ExecWord.sleepTime);
					}else if(e.getItem().equals("2초")) {
						ExecWord.sleepTime = 2000;
						System.out.println(ExecWord.sleepTime);
					}else if(e.getItem().equals("3초")) {
						ExecWord.sleepTime = 3000;
						System.out.println(ExecWord.sleepTime);
					}else if(e.getItem().equals("4초")) {
						ExecWord.sleepTime = 4000;
						System.out.println(ExecWord.sleepTime);
					}else if(e.getItem().equals("0.5초")) {
						ExecWord.sleepTime = 500;
						System.out.println(ExecWord.sleepTime);
					}
				}
			});
			String[] color = {"검정", "빨강", "파랑", "흰색", "분홍", "노랑", "초록", "주황", "자홍"};
			colorCombo = new JComboBox<>(color);
			colorCombo.setFont(componentFont);
			colorLb.setFont(componentFont);
			southPanel.add(colorLb);
			southPanel.add(colorCombo);
			colorCombo.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if(e.getItem().equals(color[0])) {
						ExecWord.fontColor = Color.black;
					}else if(e.getItem().equals(color[1])) {
						ExecWord.fontColor = Color.red;
					}else if(e.getItem().equals(color[2])) {
						ExecWord.fontColor = Color.blue;
					}else if(e.getItem().equals(color[3])) {
						ExecWord.fontColor = Color.white;
					}else if(e.getItem().equals(color[4])) {
						ExecWord.fontColor = Color.pink;
					}else if(e.getItem().equals(color[5])) {
						ExecWord.fontColor = Color.yellow;
					}else if(e.getItem().equals(color[6])) {
						ExecWord.fontColor = Color.GREEN;
					}else if(e.getItem().equals(color[7])) {
						ExecWord.fontColor = Color.orange;
					}else if(e.getItem().equals(color[8])) {
						ExecWord.fontColor = Color.MAGENTA;
					}
				}
			});
			list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
			list2.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
			list.addMouseListener(new MouseSetting());
			list2.addMouseListener(new MouseSetting());
			list.addKeyListener(new KeySetting());
			list2.addKeyListener(new KeySetting());
			inputBtn.addMouseListener(new MouseSetting());
			contentPane.addMouseListener(new MouseSetting());
			addMouseListener(new MouseSetting());
			addMouseMotionListener(new MouseSetting());
			inputBtn.setFont(componentFont);
			execBtn.setFont(componentFont);
			goDeleteBtn.setFont(componentFont);
			// -------------------------단어암기 실행스레드 코딩해야됨.------------------------------
			execBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					execFrame = new ExecWord();
					dispose(); //하나의 프레임만 꺼지게 만드는게 이 메소드임. 잊어버리지 말자.
//					mainThread.stop();
					WordFrame.setStop(true);
					ExecWord.setStop(false);
					Thread t = new Thread(execFrame);
					t.start();
				}
			});
			goDeleteBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					goToDelete();
				}
			});
			southPanel.add(goDeleteBtn);
			// -------------------------------------------------------------------------------
			inputBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (!wordTf.getText().equals("") && !meaningTf.getText().equals("")) {
						input();
					} else {
						JOptionPane.showMessageDialog(new WordPanel(), "단어와 뜻을 작성해주세요.");
					}
				}
			});
			wordTf.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						meaningTf.requestFocus();
					} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
						System.exit(0);
					}
				}
			});
			meaningTf.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						if (!wordTf.getText().equals("") && !meaningTf.getText().equals("")) {
							input();
						} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
							System.exit(0);
						} else {
							JOptionPane.showMessageDialog(new WordPanel(), "단어와 뜻을 작성해주세요.");
						}
					}
				}

			});
		}

		public void input() { // 단어 등록, 엑셀에 리스트에 올라간 단어와 뜻 저장
			model.addElement(wordTf.getText());
			model2.addElement(meaningTf.getText());
			Ex.save(model, model2, model3, model4);
//			Ex.getDeletedWordsFromExcel(model3, model4);
			wordTf.setText("");
			meaningTf.setText("");
			wordTf.requestFocus();
		}
	}
	
	class DeletePanel extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		JPanel gridPanel = new JPanel(new GridLayout(1, 2, 20, 10));
		JPanel centerPanel = new JPanel(new BorderLayout()); // JList와 JLabel을 담을 공간
		JPanel centerPanel2 = new JPanel(new BorderLayout()); // JList와 JLabel을 담을 공간
		JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,20,20)); // JTextField와 JLabel과 JButton을 담을 공간
		JLabel title = new JLabel("삭제된 단어", SwingConstants.CENTER);
		// 단어
		JLabel wordLb = new JLabel("단어", SwingConstants.CENTER);
		JLabel meaningLb = new JLabel("뜻", SwingConstants.CENTER);

		JButton reviveBtn = new JButton("되살리기");
		JButton deleteBtn = new JButton("삭제");
		JButton goWordBtn = new JButton("단어목록");
		int selectedIndex, lastSelectedList; //lastSelectedList가 0이면 단어리스트, 1이면 뜻 리스트가 마지막 선택된거임.

		public DeletePanel() {
			setLayout(new BorderLayout());
			init();
			add(title, "North");
			add(gridPanel, "Center");
			add(southPanel, "South");
			addMouseListener(new MouseSetting());
			addMouseMotionListener(new MouseSetting());
		}

		public void init() {
			model3 = new DefaultListModel<>();
			list3 = new JList<>(model3);
			list3.setFont(wordFont);
			scroll3 = new JScrollPane(list3, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

			model4 = new DefaultListModel<>();
			list4 = new JList<>(model4);
			list4.setFont(wordFont);
			scroll4 = new JScrollPane(list4, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

			wordLb.setFont(componentFont);
			meaningLb.setFont(componentFont);

			reviveBtn.setFont(componentFont);
			deleteBtn.setFont(componentFont);
			goWordBtn.setFont(componentFont);
			title.setFont(titleFont);
			centerPanel.add(wordLb, "West");
			centerPanel.add(scroll3, "Center");
			centerPanel2.add(meaningLb, "West");
			centerPanel2.add(scroll4, "Center");
			gridPanel.add(centerPanel);
			gridPanel.add(centerPanel2);

			southPanel.add(reviveBtn);
			southPanel.add(deleteBtn);
			southPanel.add(goWordBtn);
			goWordBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					goToWord();
				}
			});
			list3.addListSelectionListener(new ListSelectionListener() {

				public void valueChanged(ListSelectionEvent e) {
					selectedIndex = list3.getSelectedIndex();
					lastSelectedList = 0;
					System.out.println(selectedIndex);
				}

			});
			list4.addListSelectionListener(new ListSelectionListener() {

				public void valueChanged(ListSelectionEvent e) {
					selectedIndex = list4.getSelectedIndex();
					lastSelectedList = 1;
					System.out.println(selectedIndex);
				}

			});
			reviveBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(lastSelectedList==1&&list4.getSelectedIndex()!=-1) {
						model.addElement(model3.elementAt(list4.getSelectedIndex()));
						model2.addElement(model4.elementAt(list4.getSelectedIndex()));
						model3.remove(list4.getSelectedIndex());
						model4.remove(list4.getSelectedIndex());
						Ex.save(model,model2,model3,model4);
					}else if(lastSelectedList==0&&list3.getSelectedIndex()!=-1) {
						model.addElement(model3.elementAt(list3.getSelectedIndex()));
						model2.addElement(model4.elementAt(list3.getSelectedIndex()));
						model4.remove(list3.getSelectedIndex());
						model3.remove(list3.getSelectedIndex());
						Ex.save(model,model2,model3,model4);
					}
					list3.clearSelection();
					list4.clearSelection();
				}
			});
			deleteBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(lastSelectedList==1&&list4.getSelectedIndex()!=-1) {
						model3.remove(list4.getSelectedIndex());
						model4.remove(list4.getSelectedIndex());
						Ex.save(model,model2,model3,model4);
						setSelectIndex(model4);
					}else if(lastSelectedList==0&&list3.getSelectedIndex()!=-1) {
						model4.remove(list3.getSelectedIndex());
						model3.remove(list3.getSelectedIndex());
						Ex.save(model,model2,model3,model4);
						setSelectIndex(model3);
					}
				}
			});
			list3.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			list4.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			list3.addMouseListener(new MouseSetting());
			list4.addMouseListener(new MouseSetting());
			list3.addKeyListener(new KeySetting());
			list4.addKeyListener(new KeySetting());
			contentPane.addMouseListener(new MouseSetting());
		}

	}

	class KeySetting extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_ESCAPE:
				System.exit(0);
				break;
			case KeyEvent.VK_BACK_SPACE:
				if (!mainPanel.isVisible()) {
					goToMain();
					requestFocus();
				} else {
					System.out.println("현재 메뉴창입니다.");
				}
				break;
			}
		}
	}

	class ActionSetting implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand() == "단어추가") {
				goToWord();
				System.out.println("단어 추가 패널로 이동");
			} else if (e.getActionCommand() == "삭제리스트") {
				goToDelete();
				System.out.println("삭제리스트 패널로 이동");
			}
		}

	}

	class MouseSetting implements MouseListener, MouseMotionListener {

		public void mouseClicked(MouseEvent e) {
			if (e.getComponent() == list) {
				if (e.getClickCount() == 2) {
					System.out.println("더블클릭 list");
					model3.addElement(model.elementAt(list.getSelectedIndex()));
					model4.addElement(model2.elementAt(list.getSelectedIndex()));
					model2.remove(list.getSelectedIndex()); // DefaultListModel에서 선택된 인덱스의 값을 삭제하면 코드 아랫줄에서는 인덱스가 변경된
															// 값으로 적용되기때문에 코딩 순서를 반드시 잘 맞춰야됨.
					model.remove(list.getSelectedIndex());
					Ex.save(model, model2, model3, model4);
				}
			} else if (e.getComponent() == list2) {
				if (e.getClickCount() == 2) {
					model3.addElement(model.elementAt(list2.getSelectedIndex()));
					model4.addElement(model2.elementAt(list2.getSelectedIndex()));
					model.remove(list2.getSelectedIndex());
					model2.remove(list2.getSelectedIndex());
					Ex.save(model, model2, model3, model4);
				}
			} else if (e.getComponent() == list3) {
				if (e.getClickCount() == 2) {
					model.addElement(model3.getElementAt(list3.getSelectedIndex()));
					model2.addElement(model4.getElementAt(list3.getSelectedIndex()));
					model4.remove(list3.getSelectedIndex());
					model3.remove(list3.getSelectedIndex());
					Ex.save(model, model2, model3, model4);
					setSelectIndex(model3);
				}
			} else if (e.getComponent() == list4) {
				if (e.getClickCount() == 2) {
					model.addElement(model3.getElementAt(list4.getSelectedIndex()));
					model2.addElement(model4.getElementAt(list4.getSelectedIndex()));
					model3.remove(list4.getSelectedIndex());
					model4.remove(list4.getSelectedIndex());
					Ex.save(model, model2, model3, model4);
					setSelectIndex(model4);
		}
			}
			}
		//getLocationOnScreen = 이벤트의 절대좌표, getPoint = 컴포넌트 기준 상대적 좌표
		public void mousePressed(MouseEvent e) {
			xGab = e.getLocationOnScreen().x-X;
			yGab = e.getLocationOnScreen().y-Y;
		}
		public void mouseReleased(MouseEvent e) {

		}
		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {

		}
		public void mouseDragged(MouseEvent e) {
			X = e.getLocationOnScreen().x-xGab;
			Y = e.getLocationOnScreen().y-yGab;
			setLocation(X,Y);
		}
		public void mouseMoved(MouseEvent e) {
			
		}
	}
	
	public void setSelectIndex(DefaultListModel<String> model) {
		if(model.size()>0) {
			list3.setSelectedIndex(0);
			list4.setSelectedIndex(0);
		}
	}
}
