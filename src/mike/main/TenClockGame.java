package mike.main;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.LinkedList;


import mike.database.SetRankBoard;
import mike.view.MyImagePanel;


public class TenClockGame extends JPanel implements Serializable{
	JButton comfirm,pass,test;
	JTextArea[] playerView = new JTextArea[4];
	JTextArea scoreBoard,playPointArea;
	JPanel center,passSpace,comfirmSpace;
	Poker game;
	PokerPlayer[] player = new PokerPlayer[4];
	MyImagePanel passImage,comfirmImage,cardPic;
	GridLayout gLayout = new  GridLayout(3,3,10,10);
	SetRankBoard setpoint;
	int point;
	int round=1;
	int[] winTimes = {0,0,0,0};
	ComputerTime[] ct = new ComputerTime[3];
	boolean winBonus = false;
	int bonus = 0;
	LinkedList<Integer> bonusNum; 
	public TenClockGame(String name) {
		game = new Poker();
		comfirm = new JButton("要牌");
		pass = new JButton("PASS");
		scoreBoard = new JTextArea();
		playPointArea = new JTextArea();
		
		center = new JPanel(gLayout);
		passSpace = new JPanel();
		comfirmSpace = new JPanel();
		setpoint = new SetRankBoard(name,1);

		test = new JButton("test");
		comfirmImage = new MyImagePanel("public/poker/02.png");
		passImage = new MyImagePanel("public/poker/01.png");
		cardPic = new MyImagePanel("public/poker/p.png");
		
		bonusNum = new LinkedList<>();
		for(int i = 0;i<4 ;i++) {
			playerView[i] = new JTextArea();
			player[i] = new PokerPlayer();
		}
		if(name == null) {
			player[0].setName("玩家");
		}else {
			player[0].setName(name);
		}	
		player[1].setName("阿貓");
		player[2].setName("阿狗");
		player[3].setName("阿呆");
		
		layoutView();	
		game.newGame();
		event();
		
		gameSet();

	}
	
	public void layoutView() {
		setLayout(new BorderLayout());
		add(center,BorderLayout.CENTER);
		comfirmSpace.setLayout(new BorderLayout());
		passSpace.setLayout(new BorderLayout());
		comfirmSpace.add(comfirm,BorderLayout.SOUTH);
		comfirmSpace.add(comfirmImage,BorderLayout.CENTER);
		passSpace.add(pass,BorderLayout.SOUTH);
		passSpace.add(passImage,BorderLayout.CENTER);
		center.add(playPointArea);center.add(scoreBoard);center.add(cardPic);
		center.add(playerView[1]);center.add(playerView[2]);center.add(playerView[3]);
		center.add(passSpace);center.add(playerView[0]);center.add(comfirmSpace);
		comfirmSpace.setVisible(false);
		passSpace.setVisible(false);
		scoreBoard.setText("--------------------------------記分板------------------------------------\n");
		for(int i = 0 ; i<4 ;i++) {
			basicView(player[i],playerView[i]);			
			playerView[i].setFont(playerView[i].getFont().deriveFont(20f)); 
		}
		scoreBoard.setFont(scoreBoard.getFont().deriveFont(20f));
		playPointArea.setFont(playPointArea.getFont().deriveFont(44f));
	}
	
	public void basicView(PokerPlayer player,JTextArea gameview){	
		gameview.setText("                                     "+player.getName()+"\n");
		gameview.append("------------------------------------------------------------------------------\n");				
	}
	
	public void event() {
		comfirm.addActionListener(new ActionListener() {
						
			public void actionPerformed(ActionEvent e) {
				getDeal(player[0],playerView[0]);


			}
			
		});
		pass.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				passDeal();
			}
			
		});	
		
		test.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
			
			}
			
		});	
	}
	
	public void gameSet() {

		comfirmSpace.setVisible(false);
		passSpace.setVisible(false);
		for(int i = 0 ; i<4 ; i++) {
			player[i].count = 0;
		}
		System.out.println(game.getCard().size());
		
		String s = earnPoint(player[0]);
		if(s!=null) {			
			playPointArea.setText("\n           目前點數:\n              "+player[0].getPoint()+"點");
			playerView[0].setText("                                     "+player[0].getName()+"\n"+
			"------------------------------------------------------------------------------\n"+	
			"                                拿到"+s+"\n");
			
			
			
			boolean empty = false;
			for(int i = 1 ;i<4;i++) {
				if(earnPoint(player[i]) ==null) {
					empty = true;
					break;
				}
			}
			if(!empty) {								
				ct[2] = new ComputerTime(player[3],playerView[3],null);			
				ct[1] = new ComputerTime(player[2],playerView[2],ct[2]);	
				ct[0] = new ComputerTime(player[1],playerView[1],ct[1]);					
				try {	
					ct[0].start();
				}catch(Exception e) {
					System.out.println("1. "+e.toString());
				}					
			}

		}		
	}
	private class ComputerTime extends Thread {
		PokerPlayer p;
		JTextArea view;
		ComputerTime next;
		
		
		public ComputerTime(PokerPlayer p , JTextArea view,ComputerTime next) {
			this.p = p;
			this.view = view;
			this.next = next;
		}
		public void run() {
			boolean b = true;
			for(;true;) {
				try {				
					sleep(600);
					if((p.getPoint()<5) && (p.getPoint()!=0) && (p.getPoint()!=10.5) && (p.getPoint()!=14)) {
						sleep(300);
						getDeal(p,view);											
					}else{view.append("\n                                 "+p.getName()+"pass\n");
						if(next!=null) {
							next.start();
							break;
						}else {
							sleep(500);
							JOptionPane.showMessageDialog(null,"你的回合");
							comfirmSpace.setVisible(true);
							passSpace.setVisible(true);
							break;
						}
					
					}
				}catch(Exception e) {
					System.out.println("2. "+e.toString());
					comfirmSpace.setVisible(true);
					passSpace.setVisible(true);
					break;
				}
			}
		}
	}
	

	public String earnPoint(PokerPlayer someone){
	    int i; 	    
	    try {	    
	    	i = game.deal();
	    	someone.earnCard(i);
		    
		    switch(i%13){
	    		case(10):case(11):case(12):
	    			someone.setPoint(someone.getPoint()+0.5);
	    			break;
	    		default:
	    			someone.setPoint(someone.getPoint()+(i%13+1));
	    			break;
	        }
		    
		    
		    if(someone==player[0]) {
		    	if(someone.getPoint()>(double)10.5) {
		        	JOptionPane.showMessageDialog(null,"拿到"+Poker.cardName(i)+"\n爆了");
		        	someone.setPoint(0);
		        	passDeal();
		        	return null;
		    	}else if(someone.getPoint()==(double)10.5) {
		        	JOptionPane.showMessageDialog(null,"拿到"+Poker.cardName(i)+"剛好十點半");
		        	passDeal();        	
		        	return null;
		    	}else if(someone.count == 4) {
        			JOptionPane.showMessageDialog(null,"拿到"+Poker.cardName(i)+"過五關");
        			someone.setPoint(14);
        			passDeal();
        			return null;
		    		
		    	}else {
		    		someone.count++;
		        	return Poker.cardName(i);
		        }		    	
		    }else {
		    	if(someone.getPoint()>(double)10.5){
		        	someone.setPoint(0);
		        	return Poker.cardName(i);
		    	}else if(someone.count == 4 ) {
	        		someone.setPoint(14);
	        		return Poker.cardName(i);
		    	}else {
		    		someone.count++;
		    		return Poker.cardName(i);
		    	}
		    }	        
	    }catch(NullPointerException NE) {
			reStart();
			return null;
	    }    	
	}	
	
	public void getDeal(PokerPlayer someone,JTextArea view) {
		String s =earnPoint(someone);
		if(s!=null) {			
			if( someone == player[0] ) {
				System.out.println("wow");
				playPointArea.setText("\n           目前點數:\n              "+player[0].getPoint()+"點");
				playerView[0].append("                                拿到"+s+"\n");
			}
			else{view.append("                     "+someone.getName()+"要牌         拿到"+s+"\n");}		
		}
	
	}
	public void passDeal() {
		for(int i = 0 ; i<4 ;i++) {
			basicView(player[i],playerView[i]);
		}

		thanSize();
		JOptionPane.showMessageDialog(null,"下一回合");
		gameSet();
	}

	public void thanSize() {
		String playerPoint = "";
		for(int i =0;i<4;i++) {
			playerPoint += player[i].getName()+"點數 : "+playerState(player[i].getPoint())+" . ";
		}		
		JOptionPane.showMessageDialog(null,"結算\n"+playerPoint);

		
		for(int i = 0;i<4;i++) {
			player[i].count = 0;
			for(PokerPlayer p:player) {
				if(player[i].getPoint()>p.getPoint()) {player[i].count++;}
			}
		}
		boolean b= true;
		for(int i = 0;i<4;i++) {
			if(player[i].count==3) {
				setScoredBoard(i);
				b= false;
				break;
			}
		}
		if(b) {setScoredBoard(4);}
		
		for(int i =0 ;i<4;i++) {
			player[i].setPoint(0);
		}
	}
	public void setScoredBoard(int i) {
		String s =""; 
		if(i==4) {
			s = "最高分平手，無人";
		}else {
			s = player[i].getName();
			winTimes[i]++;			
		}
		
		if(i==0) {
			winBonus = true;
		}
		
		if(winBonus) {
			if(i==0) {
				bonus++;
			}else {
				winBonus = false;
				bonus =0;
			}
		}
		
		bonusNum.add(bonus);
		Collections.sort(bonusNum,Collections.reverseOrder());
		System.out.println(bonusNum);
		JOptionPane.showMessageDialog(null,s+"勝利");
		scoreBoard.setText("--------------------------------記分板------------------------------------\n");
		scoreBoard.append("\n\n");
		scoreBoard.append("       -"+player[0].getName()+"-          -"+player[1].getName()+"-         -"+player[2].getName()+"-         -"+player[3].getName()+"-\n");
		for(int ii = 0;ii<4;ii++) {
			scoreBoard.append("       獲得"+winTimes[ii]+"勝");
		}

	}
	public void reStart(){
			round++;	
			JOptionPane.showMessageDialog(null,"牌池已無牌");
			if(round==2) {
				point = winTimes[0]*100;
				int playerbonus = bonusNum.get(0)*200;
				String msg = String.format("勝場:%d局，得分:%d，最高連勝:%d，加分:%d，總分%d",
						winTimes[0],point,bonusNum.get(0),playerbonus,point+playerbonus);
				JOptionPane.showMessageDialog(null,"牌局結束，結算分數\n"+msg);
				point+=playerbonus;
				if(setpoint.setBoard(point)) {
					JOptionPane.showMessageDialog(null,"個人紀錄已更新");
				}
				JOptionPane.showMessageDialog(null,"新牌局開始");
				bonusNum.clear();
				for(int i = 0; i<4;i++) {
					winTimes[i]=0;					
				}
				bonus = 0;
				round =0;
			}
			JOptionPane.showMessageDialog(null,"重新發牌");
			for(int i = 0; i<4;i++) {
				player[i].handCardClear();
				player[i].setPoint(0);
				basicView(player[i], playerView[i]);
			}
			for(int i = 0;i<3;i++) {
				ct[i].interrupt();
			}
			game = new Poker();
			game.newGame();
			gameSet();	


	}
	public void saveGame() {
		
		String saveMesg ="-存檔點- .玩家名稱 : "+player[0].getName()+".\n遊戲狀態\n";
		for(int i = 0;i<4;i++) {
			saveMesg +=player[i].getName()+" : "+winTimes[i]+" 勝 . ";
		}
		saveMesg += "\n當前局數 : 第"+round+"局\n"
				+"存檔時間 : "+DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.MEDIUM).format(new Date());
		JOptionPane.showMessageDialog(null,saveMesg);
		
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(""
					+ "public/gameSave/pokerSaveDetal.txt")));
			byte[] buf;
			writer.write(saveMesg);
			writer.flush();
			writer.close();
			ObjectOutputStream oop = new ObjectOutputStream(
					new FileOutputStream("public/gameSave/poker.save") );
			
			for(int i = 0; i<4;i++) {
				oop.writeObject(player[i]);
				oop.writeObject(playerView[i].getText());
				oop.writeObject(winTimes[i]);
				
			}
			oop.writeObject(playPointArea.getText());
			oop.writeObject(scoreBoard.getText());
			oop.writeObject(game);

			oop.writeObject(round);
			oop.writeObject(bonusNum);
			oop.writeObject(bonus);
			oop.writeObject(winBonus);
			
			oop.flush();
			oop.close();
			
		}catch(Exception e) {
			System.out.println(e.toString());
		}
		
	}
	public void loadGame() throws Exception{
		ObjectInputStream oip = new ObjectInputStream(
			new FileInputStream("public/gameSave/poker.save"));
		for(int i = 0;i<3;i++) {
			ct[i].interrupt();
		}
		for(int i = 0; i<4;i++) {
			player[i] = (PokerPlayer)oip.readObject();
			playerView[i].setText((String) oip.readObject());
			winTimes[i] =(int) oip.readObject();
			
		}
		playPointArea.setText((String) oip.readObject());
		scoreBoard.setText((String) oip.readObject());		
		game = (Poker) oip.readObject();
		
		round = (int) oip.readObject();
		bonusNum = (LinkedList<Integer>) oip.readObject();
		bonus = (int) oip.readObject();
		winBonus = (boolean) oip.readObject();	
		setpoint = new SetRankBoard(player[0].getName(),1);
		oip.close();

		
	}
	public static String playerState(double d) {
		if(d==0) {
			return "爆了";
		}else if(d==14){
			return "過五關";
		}else {
			return ""+d;
		}
		
	}
	public void exit() {
		for(int i = 0 ;i<3;i++) {
			ct[i].interrupt();
		}

	}
}
