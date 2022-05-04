import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.*;
import java.awt.event.KeyListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.awt.event.KeyEvent;

public class Main {

// Vẽ giao diện Java Swing

	static JFrame frame;
	static JPanel panel;
	static JPanel chatbot;
	static JCanvas canvas;
	static JButton NewGameButton;
	static JButton UndoButton;
	static JButton AboutButton;
	static JLabel LevelLabel;
	static JLabel ColorXLabel;
	static JLabel ColorOLabel;
	static JLabel whoFirstLabel;
	static JLabel RepresentLabel;
	static JLabel TableSizeLabel;
	static JComboBox LevelBox;
	static JComboBox ColorXBox;
	static JComboBox ColorOBox;
	static JComboBox whoFirstBox;
	static JComboBox RepresentBox;
	static JComboBox TableSizeBox;
	static JTextField ScoreText;
	static JTextArea dialog = new JTextArea(9, 27);
	static JTextArea input = new JTextArea(3, 27);

	static JEventQueue Events;

	static Border lineBoder = BorderFactory.createLineBorder(Color.BLACK);
	static TitledBorder titlePanel = BorderFactory.createTitledBorder(lineBoder, "ĐIỀU KHIỂN");
	static TitledBorder titlePanel1 = BorderFactory.createTitledBorder(lineBoder, "CHATBOT");

// ChatBot
	static JScrollPane scroll = new JScrollPane(dialog, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

	static String[][] chatBot = {
			// Time - mai chua tim dc cach hien thi Time /dell hieu kieu gi
//			{"hom nay la ngay nao","may gio roi"},
//			
			// hello
			{ "xin chao", "chao", "hi", "chao ban" }, { "chao ban, toi là AI sieu thong minh!" },
			// question greetings
			{ "khoe khong", "ban khoe khong", "khoe khong nao", "khoe chu" }, { "Toi khoe!" },
			// how to play
			{ "game nay choi the nao", "how to play this game" },
			{ "Ban co the chon X, O " + "roi danh vao bang, \n" + "neu du 5 X hoac O tren 1 "
					+ "hang thi ban se thang !" },
			// thanks
			{ "cam on nha", "thanks", "thank You" }, { "khong co gi, chuc ban may man!" },
			// nothing
			{ "lam sao de choi co gioi" }, { "Ban len google hoc nhe!" },
			// tri tue nhan tao
			{ "toi muon gioi mon tri tue nhan tao" }, { "ban hay hoc them tu nhieu nguon nhe!" },
			// bye
			{ "bye", "chao ban nhe" }, { "Hen gap lai!" },
			// default
			{ "Toi chua hoc tu nay - I am learning  !" } };

// Giao dien tiep theo

	static int thicknessX = 4;
	static int thicknessO = 4;
	static int marginTableCell = 3;
	static int marginTable = 10;
	static int sizeTable = 600;
	static int widthCanvas = sizeTable + 2 * marginTable + 30;
	static int widthPanel = 340;
	static int widthFrame = widthCanvas + widthPanel + 30;
	static int height = widthCanvas;
	static int widthButton = 120;
	static int heightButton = 30;
	static int marginButton = 50;
	static int whoFirstBox_width = 200;
	static int ScoreText_width = 250;
	static int xx = 125;

	static Color Default_colorX = Color.red;
	static Color Default_colorO = Color.green;
	static Color Default_colorTable = Color.black;
	static Color colorX = Default_colorX;
	static Color colorO = Default_colorO;
	static Color colorTable = Default_colorTable;
	static Color colorButtonA = Color.yellow;
	static Color colorFrame = new Color(198, 226, 255);

	// Jlabel
	static String RepresentData[] = { "Bạn là X", "Bạn là O" };
	static String TableSizeData[] = { "Mặc định", "3 x 3", "5 x 5" };

	static int MaxN = 30;
	static int Default_N = 20;
	static int whoFirst = -1;
	static boolean UserX = true;
	static boolean hasAI = true;
	static int LengthWin;
	static int nUserWin = 0;
	static int nComputerWin = 0;

// Biểu diễn bảng giao diện

	static int N = Default_N;
	static int nSteps;
	static int x[] = new int[MaxN * MaxN];
	static int y[] = new int[MaxN * MaxN];
	static boolean used[][] = new boolean[MaxN][MaxN];

	public static void InitGUI(){

		
		frame = new JFrame();
		frame.setTitle("Caro and ChatBot");
		frame.setSize(widthFrame, height);
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		canvas = new JCanvas();
		canvas.setBounds(0, 0, widthCanvas, height);
		frame.add(canvas);
		
		panel = new JPanel();
		panel.setBounds(widthCanvas, 0, widthPanel, height/2);
		titlePanel.setTitleJustification(TitledBorder.RIGHT);
		panel.setBorder(titlePanel);
		panel.setLayout(null);
		panel.setBackground(colorFrame);
		frame.add(panel);
		
		
// set newgame, undo, size bảng, bảng kết quả 
		
		int ButtonPositionX = widthCanvas + (widthPanel - widthButton) / 2;
		NewGameButton = new JButton("Game mới");
		NewGameButton.setBounds(ButtonPositionX, marginButton, widthButton, heightButton);
		NewGameButton.setBackground(colorButtonA);
		panel.add(NewGameButton);
		
		UndoButton = new JButton("Đi lại");
		UndoButton.setBounds(ButtonPositionX, 2 * marginButton, widthButton, heightButton);
		UndoButton.setBackground(colorButtonA);
		panel.add(UndoButton);
		
		int LabelPositionX = widthCanvas + marginButton;
		int BoxPositionX = widthCanvas + (widthPanel - whoFirstBox_width) / 2;
	
		TableSizeLabel = new JLabel("Bảng chơi:");
		TableSizeLabel.setBounds(LabelPositionX, 3 * marginButton, widthButton, heightButton);
		TableSizeLabel.setOpaque(true);
		TableSizeLabel.setBackground(colorFrame);
		panel.add(TableSizeLabel);
		
		TableSizeBox = new JComboBox(TableSizeData);
		TableSizeBox.setBounds(LabelPositionX + widthButton, 3 * marginButton, widthButton, heightButton);
		TableSizeBox.setBackground(colorButtonA);
		panel.add(TableSizeBox);
					
		RepresentLabel = new JLabel("Người chơi:");
		RepresentLabel.setBounds(LabelPositionX, 4 * marginButton, widthButton, heightButton);
		RepresentLabel.setOpaque(true);
		RepresentLabel.setBackground(colorFrame);
		panel.add(RepresentLabel);
		
		RepresentBox = new JComboBox(RepresentData);
		RepresentBox.setBounds(LabelPositionX + widthButton, 4 * marginButton, widthButton, heightButton);
		RepresentBox.setBackground(colorButtonA);
		panel.add(RepresentBox);
		// xu li du lieu khi nguoi dung nhap vao 
		chatbot = new JPanel();
		chatbot.setBounds(LabelPositionX -50, 6 * marginButton, (widthButton* 2)+(widthButton-50) , height/3);
		dialog.setEditable(false);
		input.addKeyListener(new KeyListener() {
		    public void keyPressed(KeyEvent e){
		        if(e.getKeyCode()==KeyEvent.VK_ENTER){
		            input.setEditable(false);
		            
		            String quote=input.getText();
		            input.setText("");
		            addText("You : "+quote);
		            quote.trim();
		            while(
		                    quote.charAt(quote.length()-1)=='!' ||
		                            quote.charAt(quote.length()-1)=='.' ||
		                            quote.charAt(quote.length()-1)=='?'
		            ){
		                quote=quote.substring(0,quote.length()-1);
		            }
		            quote.trim();
		           
		            byte response=0;

		            int j=0;
		            
		            while(response==0){
		                if(inArray(quote.toLowerCase(),chatBot[j*2])){
		                    response=2;
		                    int r=(int)Math.floor(Math.random()*chatBot[(j*2)+1].length);
		                    addText("\nAI : "+chatBot[(j*2)+1][r]);
		                }
		                j++;
		                if(j*2==chatBot.length-1 && response==0){
		                    response=1;
		                }
		            }

		            //-----default--------------
		            if(response==1){
		                int r=(int)Math.floor(Math.random()*chatBot[chatBot.length-1].length);
		                addText("\nAI : "+chatBot[chatBot.length-1][r]);
		            }
		         
		            addText("\n");
		        }
		    }

		    public void keyReleased(KeyEvent e){
		        if(e.getKeyCode()==KeyEvent.VK_ENTER){
		            input.setEditable(true);
		        }
		    }
//	        public String timNgay() {
//		    	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
//		    	 LocalDateTime now = LocalDateTime.now();  
//		    	 return dtf.format(now).toString(); 
//	    	
//		    }
		    

		    public void keyTyped(KeyEvent e){}
		    public void addText(String str){
		        dialog.setText(dialog.getText()+str);
		    }

		    public boolean inArray(String in,String[] str){
		        boolean match=false;
		        for(int i=0;i<str.length;i++){
		            if(str[i].equals(in)){
		                match=true;
		            }
		        }
		        return match;
		    }
		});   
		chatbot.add(scroll);
		chatbot.add(input);
		panel.add(chatbot);
		
		int TextPositionX = widthCanvas + (widthPanel - ScoreText_width) / 2;
		ScoreText = new JTextField();
		ScoreText.setBounds(TextPositionX, 11 * marginButton, ScoreText_width, heightButton);
		ScoreText.setEditable(false);
		ScoreText.setHorizontalAlignment(JTextField.CENTER);
		String bxh = "Bảng điểm: Bạn 0 - 0 Máy";
		ScoreText.setText(bxh);
		ScoreText.setBackground(colorButtonA);
		panel.add(ScoreText);
		
		frame.setVisible(true);
	}

	public static void InitEventListener() {
		Events = new JEventQueue();
		Events.listenTo(canvas, "canvas");
		Events.listenTo(NewGameButton, "NewGame");
		Events.listenTo(UndoButton, "Undo");
		Events.listenTo(TableSizeBox, "TableSize");
		Events.listenTo(RepresentBox, "Represent");
	}

	public static void clearTable() {
		canvas.setBackground(panel.getBackground());
	}

	// vẽ bảng

	public static void drawTable() {
		int lengthCell = sizeTable / N;
		int x1 = marginTable;
		int x2 = marginTable + sizeTable;
		int y1 = marginTable;
		int y2 = marginTable + sizeTable;
		canvas.setColor(colorTable);
		for (int i = 0; i <= N; i++) {
			canvas.drawLine(x1, y1 + i * lengthCell, x2, y1 + i * lengthCell);
			canvas.drawLine(x1 + i * lengthCell, y1, x1 + i * lengthCell, y2);
		}
	}

	public static void drawX(int tableX, int tableY) {
		int lengthCell = sizeTable / N;
		int x1 = marginTable + tableX * lengthCell;
		int y1 = marginTable + tableY * lengthCell;
		int x2 = x1 + lengthCell;
		int y2 = y1 + lengthCell;
		x1 += marginTableCell;
		y1 += marginTableCell;
		x2 -= marginTableCell;
		y2 -= marginTableCell;
		canvas.setColor(colorX);
		for (int i = 0; i <= thicknessX; i++) {
			canvas.drawLine(x1, y1 + i, x2 - i, y2);
			canvas.drawLine(x1 + i, y1, x2, y2 - i);
			canvas.drawLine(x1, y2 - i, x2 - i, y1);
			canvas.drawLine(x1 + i, y2, x2, y1 + i);
		}
	}

	public static void drawO(int tableX, int tableY) {
		int lengthCell = sizeTable / N;
		int x = marginTable + tableX * lengthCell + marginTableCell;
		int y = marginTable + tableY * lengthCell + marginTableCell;
		int diameter = lengthCell - 2 * marginTableCell;
		canvas.setColor(colorO);
		for (int i = 0; i <= thicknessO; i++)
			canvas.drawOval(x + i, y + i, diameter - 2 * i, diameter - 2 * i);
	}

	public static void reDrawX() {
		for (int i = 0; i < nSteps; i++)
			if (whoFirst == 1) {
				if ((i % 2 == 0) && (!UserX))
					drawX(x[i], y[i]);
				if ((i % 2 == 1) && (UserX))
					drawX(x[i], y[i]);
			} else {
				if ((i % 2 == 0) && (UserX))
					drawX(x[i], y[i]);
				if ((i % 2 == 1) && (!UserX))
					drawX(x[i], y[i]);
			}
	}

	public static void reDrawO() {
		boolean UserO = false;
		if (!UserX)
			UserO = true;

		for (int i = 0; i < nSteps; i++)
			if (whoFirst == 1) {
				if ((i % 2 == 0) && (!UserO))
					drawO(x[i], y[i]);
				if ((i % 2 == 1) && (UserO))
					drawO(x[i], y[i]);
			} else {
				if ((i % 2 == 0) && (UserO))
					drawO(x[i], y[i]);
				if ((i % 2 == 1) && (!UserO))
					drawO(x[i], y[i]);
			}
	}

	public static void clearCell(int tableX, int tableY) {
		int lengthCell = sizeTable / N;
		int x1 = marginTable + tableX * lengthCell;
		int y1 = marginTable + tableY * lengthCell;
		int length = lengthCell - 2;
		canvas.setColor(panel.getBackground());
		canvas.fillRect(x1 + 1, y1 + 1, length, length);
	}

	public static void reDrawXO() {
		for (int i = 0; i < nSteps; i++)
			clearCell(x[i], y[i]);
		reDrawX();
		reDrawO();
	}

	public static void UpdateMove(int nextMoveX, int nextMoveY) {
		used[nextMoveX][nextMoveY] = true;
		x[nSteps] = nextMoveX;
		y[nSteps] = nextMoveY;
		nSteps++;
		if (UserX)
			drawO(nextMoveX, nextMoveY);
		else
			drawX(nextMoveX, nextMoveY);
	}

	public static int getTableSize(int index) {
		if (index == 0)
			return Default_N;
		if (index == 1)
			return 3;
		if (index == 2)
			return 5;
		return 30;
	}

	public static void DeleteMove(int MoveX, int MoveY) {
		used[MoveX][MoveY] = false;
		clearCell(MoveX, MoveY);
		nSteps--;
	}

	public static void UndoMove() {
		if (nSteps == 0)
			return;
		if (nSteps == 1) {
			JOptionPane.showMessageDialog(frame, "Bạn không thể đi lại!", "Notice", JOptionPane.ERROR_MESSAGE);
			return;
		}
		DeleteMove(x[nSteps - 1], y[nSteps - 1]);
		DeleteMove(x[nSteps - 1], y[nSteps - 1]);
	}

	/*
	 * Check người thắng và lưu kết quả
	 */
	public static boolean CheckFinalState() {
		int result = AI.CheckWinner(N, nSteps, x, y, whoFirst, LengthWin);
		if (result != 0) {
			if (result == 1) {
				JOptionPane.showMessageDialog(frame, "Máy chiến thắng!!!");
				nComputerWin++;
			} else {
				JOptionPane.showMessageDialog(frame, "Bạn chiến thắng!!!");
				nUserWin++;
			}
			ScoreText.setText("Bảng điểm: " + "Bạn " + Integer.toString(nUserWin) + " - "
					+ Integer.toString(nComputerWin) + " Máy");
			return true;
		}
		if (nSteps == N * N) {
			JOptionPane.showMessageDialog(frame, "Bạn và máy hòa!");
			return true;
		}
		return false;
	}

// Chay game	

	public static void GamePlaying() {
		int nextMoveX, nextMoveY;

		clearTable();
		drawTable();

		nSteps = 0;
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				used[i][j] = false;

		LengthWin = Math.min(5, N);

		if (whoFirst == 1) {
			AI.findNextMove(N, nSteps, x, y, whoFirst, hasAI);
			nextMoveX = AI.getNextMoveX();
			nextMoveY = AI.getNextMoveY();
			UpdateMove(nextMoveX, nextMoveY);
		}

		while (true) {
			EventObject AnEvent = Events.waitEvent();

			if (Events.isMouseEvent(AnEvent))
				if (Events.isMousePressed(AnEvent)) {
					int MouseX = Events.getMouseX(AnEvent);
					int MouseY = Events.getMouseY(AnEvent);
					if ((MouseX > marginTable) && (MouseX < marginTable + sizeTable))
						if ((MouseY > marginTable) && (MouseY < marginTable + sizeTable)) {
							int lengthCell = sizeTable / N;
							int TableX = (MouseX - marginTable) / lengthCell;
							int TableY = (MouseY - marginTable) / lengthCell;

							if (!used[TableX][TableY]) {
								used[TableX][TableY] = true;
								x[nSteps] = TableX;
								y[nSteps] = TableY;
								nSteps++;
								if (UserX)
									drawX(TableX, TableY);
								else
									drawO(TableX, TableY);

								if (CheckFinalState()) {
									GamePlaying();
									return;
								}

								// dự đoán bước di chuyển tiếp theo

								AI.findNextMove(N, nSteps, x, y, whoFirst, hasAI);
								nextMoveX = AI.getNextMoveX(); // bước tiếp theo của X
								nextMoveY = AI.getNextMoveY(); // bước tiếp theo của Y
								UpdateMove(nextMoveX, nextMoveY);

								if (CheckFinalState()) {
									GamePlaying();
									return;
								}
							}
						}
				}

			String name = Events.getName(AnEvent);

			// Khởi tạo 1 game mới

			if (name.equals("NewGame")) {
				GamePlaying();
				return;
			}

			// Quay lại bước trước

			if (name.equals("Undo")) {
				UndoMove();
				continue;
			}

			if (name.equals("Represent")) {
				int index = RepresentBox.getSelectedIndex();
				if (index == 0) {
					if (!UserX) {
						UserX = true;
						reDrawXO();
					}
				} else if (UserX) {
					UserX = false;
					reDrawXO();
				}
				continue;
			}

			if (name.equals("TableSize")) {
				int n = getTableSize(TableSizeBox.getSelectedIndex());
				if (n != N) {
					N = n;
					GamePlaying();
					return;
				}
				continue;
			}
		}
	}

	public static void main(String args[]) {
		InitGUI();
		InitEventListener();
		GamePlaying();
	}

}
