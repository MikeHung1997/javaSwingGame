package mike.main;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.MouseInputAdapter;

import mike.database.SetRankBoard;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class PoliceGame extends JPanel{
	private int point,coinGetX,coinGetY;
	private String name;
	private Timer timer;
	private LinkedList<bonusPolicesTask> bonuspolices;
	private policesTask police; 
	private int width, height;
	private BufferedImage policePic,thiefPic,coinPic,background,gameover,coinGet;
	private File gameoverPic,coinGetPic;
	private JButton start;
	private thiefMan thief;
	private coin coin;
	private JLabel scoreboard;
	private SetRankBoard setpoint;
	
	public PoliceGame(String name) {
		scoreboard = new JLabel("積分:\n"+000);
		scoreboard.setFont(scoreboard.getFont().deriveFont(30f));
		scoreboard.setForeground(Color.white);
		setpoint = new SetRankBoard(name,2);
		add(scoreboard);
		width = getWidth(); height = getHeight();
		try {			
			coinGetPic = new File("public/COINGET.png");			
			gameoverPic = new File("public/gameover.png");
			background = ImageIO.read(new File("public/policebk.jpg"));
			policePic = ImageIO.read(new File("public/police1.png"));
			thiefPic = ImageIO.read(new File("public/thief.png"));
			coinPic = ImageIO.read(new File("public/coin.png"));
		} catch (IOException e) {
			System.out.println(e.toString());
		} 		
		thief = new thiefMan(100,400);
		coin = new coin();
		bonuspolices = new LinkedList<>();
		police = new policesTask(1300,262);
		timer = new Timer();
		timer.schedule(new Refresh(), 0, 16);
		
		
		timer.schedule(coin, 0, 2000);
		timer.schedule(police, 0, 80);
		addMouseListener(new MyMouseAdapter());
		TimerTask makebonusPolice = new TimerTask() {
			public void run() {
				bonuspolices.add(new bonusPolicesTask(1300,262)) ;
				timer.schedule(bonuspolices.get(bonuspolices.size()-1), 0, 100);
			
			}
		};
				
		timer.schedule(makebonusPolice, 30000, 60000);
		timer.schedule(thief, 0, 100);

	
			
	}

	private class policesTask extends TimerTask {
		int x, y, dx, dy;
		public policesTask(int x, int y) {
			this.x = x; this.y = y;
			dx = 8;
			dy = 8;		
		}
		@Override
		public void run() {

			
			if (x<11||x +100>= width||(Math.random()*50)>49 ) {
				dx *= -1;
			}else if ((Math.random()*30)>29){
				dx += 2;
			}else if ((Math.random()*50)>49){
				dx --;
			}
			
			if (y<261||y+120 >= height||(Math.random()*50)>49) {
				dy *= -1;
			}else if ((Math.random()*50)>49){
				dx += 2;
			}else if ((Math.random()*50)>49){
				dx --;
			}
			
			x += dx;
			y += dy;


				
		}
		
	}
	private class bonusPolicesTask extends TimerTask {
		int x, y, dx, dy;
		int randX,randY;
		public bonusPolicesTask(int x, int y) {
			this.x = x; this.y = y;
			dx = 5;
			dy = 5;	
			randX=(int)(Math.random()*40)-20;
			randY=(int)(Math.random()*40)-20;
			
		}
		@Override
		public void run() {			
			
			
			

			if ((Math.random()*100)>95 ) {
				dx = (thief.x-x+randX)/20;				
				dy = (thief.y-y+randX)/20;

			}
			if (x<11||x +100>= width||(Math.random()*50)>49 ) {
				dx *= -1;
			}
			if (y<261||y+120 >= height||(Math.random()*50)>49 ) {
				dy *= -1;
			}
			x += dx;
			y += dy;


				
		}
		
	}
	private class Refresh extends TimerTask {
		@Override
		public void run() {
			
			if((thief.x > coin.x-35&&thief.x<coin.x+35)&&(thief.y>coin.y-35&&thief.y<coin.y+35)){
				coinGetX = coin.x;coinGetY = coin.y;
				new getcoin(coin.x,coin.y).start();
			}
			
			if((thief.x > police.x-35&&thief.x<police.x+35)&&(thief.y>police.y-35&&thief.y<police.y+35)){
				try {
					gameover = ImageIO.read(gameoverPic);
					}catch(Exception e ) {
						System.out.println(e.toString());
					}
				gameover();
			}

			if(bonuspolices.size()>0) {
				for(bonusPolicesTask pol : bonuspolices) {
					if((thief.x > pol.x-35&&thief.x<pol.x+35)&&(thief.y>pol.y-35&&thief.y<pol.y+35)){
						try {
						gameover = ImageIO.read(gameoverPic);
						}catch(Exception e ) {
							System.out.println(e.toString());
						}
						gameover();
					}
				}
			}

			repaint();		
		}
	}
		
	private class MyMouseAdapter extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {			
			
			thief.moveX =e.getX();
			thief.moveY = e.getY();

		}					
	}
		
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			width = getWidth(); height = getHeight();
			g.drawImage(background,1,1,null);
			g.drawImage(thiefPic,thief.x,thief.y,null);
			g.drawImage(coinPic,coin.x,coin.y,null);
			g.drawImage(policePic, police.x, police.y, null);
			g.drawImage(gameover,400,30,null);
			
			g.drawImage(coinGet,coinGetX,coinGetY,null);
			
			if(bonuspolices.size()>0) {
				for(bonusPolicesTask pol : bonuspolices) {
					g.drawImage(policePic, pol.x, pol.y, null);
				}
			}
			
			
		}
		private class coin extends TimerTask{
			int x,y;
			public coin() {
				x = getX(width,thief.x);
				y = getY(height,thief.y);
			}
			public void run() {
				x = getX(width,thief.x);
				y = getY(height,thief.y);
			}
			public static int getX(int width ,int thief) {
				int i;
				do {
				i = (int)(Math.random()*(1200));
				}while(i==thief);
				return i;
			}
			public static int getY(int height ,int thief){
				int i;
				do {
					int preY;
					do{
					preY = (int)(Math.random()*500);
					}while(preY<300);
					i = preY;
					}while(i==thief);
				return i;
				
			}
		}
		
		
		private class thiefMan extends TimerTask{
			int x,y;
			int moveX,moveY;
			public thiefMan(int x, int y) {
				this.x = x; this.y = y;	
				moveX = x; moveY = y;
				
			}
			public void run() {				
				x += (moveX-x)/5;
				y += (moveY-y)/5;
				if(y<300) {
					y = 300;
				}

				if(y+100>height) {
					y = height-100;
				}
				moveX--;
				moveY--;
			}
			
		}			
		
	public void gameover() {
		boolean newScore = setpoint.setBoard(point);				
		thief.x = 100;thief.y = 400;
		police.x=1300;police.y=262;	
		timer.cancel();
		timer.purge();
		bonuspolices.clear();
		
		
		thief = new thiefMan(100,400);
		timer = new Timer();
		police = new policesTask(1300,262);
		coin = new coin();
		scoreboard.setText("積分:"+000);
		TimerTask makebonusPolice = new TimerTask() {
			public void run() {
					
				bonuspolices.add(new bonusPolicesTask(1300,262)) ;
				timer.schedule(bonuspolices.get(bonuspolices.size()-1), 0, 100);
					
				}
		};
			
		timer.schedule(new Refresh(), 0, 16);

		timer.schedule(makebonusPolice, 30000, 60000);
		TimerTask t =new TimerTask() {
				public void run() {
					if(newScore ) {
						JOptionPane.showMessageDialog(null,"本局獲得"+point+"分，"+"個人紀錄已更新，重新開始遊戲");				
					}else{
						JOptionPane.showMessageDialog(null,"本局獲得"+point+"分，"+"，重新開始遊戲");
					}	
					point = 0;
					gameover = null;
				}
		};
		
		timer.schedule(t,500);
		timer.schedule(coin, 700, 2000);
		timer.schedule(police, 700, 80);
		timer.schedule(thief, 700, 100);

		
	}

	public void exit() {
		timer.cancel();
		timer.purge();
		timer = null;
	}
	
	public class getcoin extends Thread{
		int x,y;
		public getcoin(int x,int y){
			this.x = x;
			this.y = y;
		}
		public void run(){
			try {
			point +=100;
			scoreboard.setText("積分:"+point);			
			coinPic = null;
			coin.x=1;
			coin.y=1;
			coin.cancel();
			coinGet = ImageIO.read(coinGetPic);
			sleep(200);
			coinPic = ImageIO.read(new File("public/coin.png"));
			coin = new coin();
			timer.schedule(coin, 700, 2000);			

			coinGet = null;
			}catch(Exception e) {
				System.out.println(e.toString());
			}
		}
	}
}
