package mike.main;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import mike.view.BeginView;
import mike.view.MyImagePanel;
import mike.view.PokerView;
import mike.main.PoliceGame;
import mike.view.PoliceView;
import mike.main.RacingGame;
import mike.view.RacingView;
import mike.main.RankBoard;
import mike.main.TenClockGame;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;




public class Main extends JFrame{
	JButton newGame,exitgame,right,left;
	JPanel top,down,center,space;
	String playerName;
	TenClockGame game;	
	PoliceGame policeGame; 
	RacingGame raceGame;
	
	BorderLayout bLayout;
	RankBoard rank;
	int pointer = 1;
	
	public Main() {
		super("我的遊戲場");
		newGame= new JButton("開始遊戲");
		exitgame = new JButton("離開");
		top = new JPanel(new FlowLayout());
		down = new JPanel(new FlowLayout());
		right = new JButton(">");
		left = new JButton("<");
		center = new JPanel(new BorderLayout());
		bLayout = new BorderLayout();
		
		
		setVisible(true);
		setSize(900,500);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		BeginView bv = new BeginView();  
		center.add(bv,BorderLayout.CENTER);
				
		center.add(down,BorderLayout.SOUTH);
		space = new JPanel();
        setResizable(false);
        
		firstLayoutView();
		event();		

	}
	
	public void event() {
		newGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {				
				gameStart();
			}
			
		});
		exitgame.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int i= JOptionPane.showConfirmDialog(null,"確認離開遊戲?","exit",JOptionPane.YES_NO_OPTION);
				System.out.println(i);
				if(i==0) {
					exitEvent();
				}								
			}
			
			
			
		});

		left.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if(pointer==5) {
					add(right,BorderLayout.EAST);
					repaint();
					pointer--;
				}else if(pointer ==2) {
					pointer--;
					remove(left);
					repaint();					

				}else if( pointer>2){
					
					add(left,BorderLayout.WEST);
					pointer--;
					repaint();
				}
				choiceLayout(pointer);
			}
			
		});
		right.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if(pointer == 1) {
					remove(space);					
					add(left,BorderLayout.WEST);
					setSize(901,500);
					repaint();
					pointer++;
				}else if (pointer == 4) {
					remove(right);
					repaint();
					pointer++;

				}else if(pointer <4){					
					pointer++;
					repaint();
				}
				choiceLayout(pointer);
				
			}
			
		});
	}
	public void firstLayoutView() {
		setLocation(new Point(450,300));
		setSize(901,500);
		setLayout(bLayout);
		add(center,BorderLayout.CENTER);
		add(space,BorderLayout.WEST);
		add(right,BorderLayout.EAST);
		add(down,BorderLayout.SOUTH);

	}	
	public void choiceLayout(int i) {
		center.removeAll();
		down.removeAll();
		try {
			switch(i) {	
				case 1 :
					center.add(new BeginView(),BorderLayout.CENTER);
					repaint();
					setSize(901,500);
					
					break;
				case 2 :
					setSize(900,500);
					center.add(new PokerView(),BorderLayout.CENTER);
					down.add(newGame);
					repaint();
					break;
				case 3:
					setSize(901,500);
					center.add(new PoliceView(),BorderLayout.CENTER);
					down.add(newGame);
					repaint();
					break;
				case 4:					
					setSize(900,500);
					center.add(new RacingView(),BorderLayout.CENTER);
					down.add(newGame);
					repaint();
					break;
		
				case 5:
					rank = new RankBoard();
					setSize(901,500);
					center.add(rank,BorderLayout.CENTER);
					repaint();
					break;			
			}
		}catch(NullPointerException e) {
			System.out.println(e.toString());
		}
	}
	
	
	public void gameStart() {
		if(pointer !=2) {
			playerName = JOptionPane.showInputDialog(null,"輸入玩家名稱","玩家");
			if(playerName.isEmpty()){
				playerName = "玩家";
			}
		}
		createGame();
		gameLayoutView();
		setLocation(new Point(200,160));		
	}

	public void gameLayoutView() {
		setSize(1500,800);
		remove(center);
		remove(left);
		remove(right);
		down.remove(newGame);
		top.add(exitgame);
		add(top,BorderLayout.NORTH);
		if(pointer == 2) {
			add(game,BorderLayout.CENTER);
		}else if(pointer == 3) {
			add(policeGame,BorderLayout.CENTER);
		}else if(pointer ==4) {
			add(raceGame,BorderLayout.CENTER);				
		}		
		repaint();		
	}
	
	public void createGame() {
		if(pointer ==2) {
			try {
			checkLoad();
			}catch(Exception e){
				playerName = JOptionPane.showInputDialog(null,"輸入玩家名稱","玩家");
				if(playerName.isEmpty()){
					playerName = "玩家";
				}
				game = new TenClockGame(playerName);
			}
		}else if(pointer == 3) {
			policeGame = new PoliceGame(playerName);
		}else if(pointer == 4) {
			raceGame = new RacingGame(playerName);
		}
	}
	
	public void checkLoad() throws FileNotFoundException,IOException{		
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(""
				+ "public/gameSave/pokerSaveDetal.txt")));				
			String s;
			String msg = "";
			while((s= reader.readLine())!=null) {
				msg+=s+"\n";								
			};
			int b = JOptionPane.showConfirmDialog(null,  "找尋到前次紀錄，是否載入?\n\n"+msg);
			if(b == 0) {
				System.out.println("yes");
				try {
					game = new TenClockGame(playerName);
					game.loadGame();
				}catch(Exception e) {
					System.out.println(e.toString());
					JOptionPane.showMessageDialog(null,"載入錯誤，開啟新局");
					playerName = JOptionPane.showInputDialog(null,"輸入玩家名稱","玩家");
					if(playerName.isEmpty()){
						playerName = "玩家";
					}
					game = new TenClockGame(playerName);
				}
			}else {
				playerName = JOptionPane.showInputDialog(null,"輸入玩家名稱","玩家");
				if(playerName.isEmpty()){
					playerName = "玩家";
				}			
				game = new TenClockGame(playerName);
			}
	}
	
	
	public void exitEvent() {		
		switch(pointer) {
		 	case 2:
		 		game1Save();
		 		remove(game);
		 		game.exit();
		 		break;
		 	case 3:
		 		remove(policeGame);
		 		policeGame.exit();		 		
		 		break;
		 	case 4:
		 		remove(raceGame);
		 		System.out.println("test");
		 		raceGame.exit();
		 		break;
		 	
		}
		pointer =1;		
		top.remove(exitgame);
		center.removeAll();
		center.add(new BeginView(),BorderLayout.CENTER);
		add(top,BorderLayout.NORTH);
		firstLayoutView();		
	}
	public void game1Save() {
		int b = JOptionPane.showConfirmDialog(null, "是否存檔?");
		if(b == 0) {
			game.saveGame();
		}	
		
	}	
	public static void main(String[] args) {
		new Main();
		
	}

}
