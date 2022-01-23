package mike.main;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.*;
import java.awt.event.*;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.text.AttributeSet.ColorAttribute;

import mike.database.SetRankBoard;


public class RacingGame extends JPanel{
	String name;
	String[] horsesname = {"小馬哥","查理","木馬","咚咚","其實是隻驢"};
	int point = 0;
	int guess = -1;
	horse[] horses;
	Timer timer;
	BufferedImage[] horsePic;
	BufferedImage background,people;
	File peoPic;
	File[] montionPic1,montionPic2;
	horseMotion[] montion;
	JLabel pointscord,choice,choicePic,horsename;
	LinkedList<Integer> rank;
	JButton[] guessNum;
	JButton go;
	JPanel down,top;
	view view;
	int finishHorse;	
	ImageIcon[] icon;
	SetRankBoard setpoint;
	public RacingGame(String name){	
		this.name = name;
		pointscord = new JLabel("積分:00");
		pointscord.setFont(pointscord.getFont().deriveFont(30f));
		go = new JButton("跑");
		horsePic = new BufferedImage[5];
		montionPic1 = new File[5];
		montionPic2 = new File[5];
		montion = new horseMotion[5];
		timer = new Timer();
		horses = new horse[5];		
		rank= new LinkedList<>();
		top = new JPanel(new FlowLayout());
		down = new JPanel(new GridLayout(1,5));		
		view = new view();
		guessNum = new JButton[5];
		choice = new JLabel("  你的選擇是     ");
		choicePic = new JLabel();
		horsename =new JLabel();
		top.add(pointscord);top.add(choice);top.add(choicePic);top.add(horsename);
		setpoint = new SetRankBoard(name,3);
		icon = new ImageIcon[5];
		
		top.setBackground(Color.lightGray);
		try {
			peoPic = new File("public/racepeople.png");
			background =ImageIO.read(new File("public/racebk.png"));
			for(int i = 0;i<5;i++) {
				montionPic1[i] = new File("public/horse1-"+(i+1)+".png") ;
				montionPic2[i] = new File("public/horse2-"+(i+1)+".png") ;
				icon[i] =new ImageIcon("public/horseicon"+(i+1)+".png");
				montion[i] = new horseMotion(i);
				horsePic[i] = ImageIO.read(montionPic1[i]);
				horses[i] = new horse((178+(83*i)),i);
				guessNum[i] = new JButton("押注"+(i+1)+"號馬");
				down.add(guessNum[i]);
			}
		}catch(Exception e) {
				System.out.println(e.toString());
		}	
		setLayout(new BorderLayout());
		
		add(top,BorderLayout.NORTH);
		add(down,BorderLayout.SOUTH);
		add(view,BorderLayout.CENTER);
		event();
		top.repaint(getVisibleRect());
	}
	public void event() {
		go.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				racingStart();
				remove(down);
				try {				
				top.remove(go);
				}catch(Exception a) {
					System.out.println(a.toString());
				}
			}			
		});

		guessNum[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				guess = 0;
				choice.setText("  你選擇的是"+(guess+1)+"號馬");
				choicePic.setIcon(icon[guess]);
				horsename.setText(horsesname[guess]);
				top.add(go);
			}			
		});
		guessNum[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				guess = 1;
				choice.setText("  你選擇的是"+(guess+1)+"號馬");
				choicePic.setIcon(icon[guess]);
				horsename.setText(horsesname[guess]);
				top.add(go);
			}			
		});
		guessNum[2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				guess = 2;
				choice.setText("  你選擇的是"+(guess+1)+"號馬");
				choicePic.setIcon(icon[guess]);
				horsename.setText(horsesname[guess]);
				top.add(go);
			}			
		});
		guessNum[3].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				guess = 3;
				choice.setText("  你選擇的是"+(guess+1)+"號馬");
				choicePic.setIcon(icon[guess]);
				horsename.setText(horsesname[guess]);
				top.add(go);
			}			
		});
		guessNum[4].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				guess = 4;
				choice.setText("  你選擇的是"+(guess+1)+"號馬");
				choicePic.setIcon(icon[guess]);
				horsename.setText(horsesname[guess]);
				top.add(go);
			}			
		});
		
	}
	
	public void racingStart() {
		finishHorse = 0;
		try {
			people = ImageIO.read(peoPic);
		}catch(Exception a) {
			System.out.println(a.toString());
		}
		try {
			for(int i = 0;i<5;i++) {
				horses[i].start();
				timer.schedule(montion[i],150,150);
			}
		}catch(Exception e) {
				System.out.println(e.toString());
		}
	}
	public class view extends JPanel {		
		public void paintComponent(Graphics g){
			try {
				for(int i =0;i<5;i++) {			
					g.drawImage(horsePic[i],horses[i].step,horses[i].lane,null);
				}			
			}catch(NullPointerException e) {
				System.out.println(e.toString());				
			}
		}
	}	
	private class horse extends Thread{
		int lane;
		int step;
		int horseNum;
		String s = "";
		
		public horse(int lane,int horseNum) {
			
			this.lane = lane;
			this.horseNum = horseNum;
		}
		
		public void run(){
			
			for(;step<1300;) {
				try {					
					step+=5;
					if(step>1299) {
						rank.add(horseNum);
						finishHorse++;
						stopGame(horseNum);	
						System.out.println(step);
					}
					repaint();					
					Thread.sleep(0+(int)(Math.random()*100));
				} catch (InterruptedException e) {
					break;
				}
			}			
		}

	}
	public class horseMotion extends TimerTask {
		int i =1;
		int horseNum;
		public horseMotion(int horseNum) {
			this.horseNum = horseNum;
			
		}
		public void run() {
			try{
			if(i%2==0) {
				horsePic[horseNum] = ImageIO.read(montionPic1[horseNum]);
			}else {
				horsePic[horseNum]  = ImageIO.read(montionPic2[horseNum]);
			}
			if(horses[horseNum].step%2==0) {
				horses[horseNum].lane++;
				horses[horseNum].lane--;
			}
			
			if(Math.random()*20>19) {
				int i =(int)(Math.random()*5);
				if(horses[i].step<1285) {
					horses[i].step +=15;
				}				
			}				
			i++;			
			}catch(Exception e) {
			System.out.println (e.toString());				
			}
		}
		
	}
	public void stopGame(int horseNum) {
		montion[horseNum].cancel();
		if(finishHorse ==5) {
			allFinish();
			System.out.println("end");
			
		}
	}
	public void allFinish() {
		people = null;
		String rankmsg;
		rankmsg = "      結算\n";
		for(int i = 0;i<5;i++) {
			rankmsg += "第"+(i+1)+"名 . "+horsesname[(rank.get(i))]+"\n";			
		}
		pointjub();
		JOptionPane.showMessageDialog(null,rankmsg);
		int change = JOptionPane.showConfirmDialog(null,"繼續押注 <"+horsesname[guess]+"> ?","choice",JOptionPane.YES_NO_OPTION);
		reset();
		if(change==0){			
			racingStart();	
		}else {	
			add(down,BorderLayout.SOUTH);
			choice.setText("  你的選擇是     ");
			choicePic.setIcon(null);
			horsename.setText("");
			down.repaint();
				
		}
	}
	
	public void pointjub() {
		if(guess ==rank.get(1)){
			point+=100;			
		}else if(guess ==rank.get(0) && point == 0) {
			point += 200;			
		}else if(guess ==rank.get(0)) {
			point *=2;
		}else if(guess ==rank.get(3) && point>50) {
			point -= 50;
		}else if(guess ==rank.get(4)) {
			point /=2;
		}		
		pointscord.setText("積分:"+point);
	}
	
	public void reset() {		
		rank.clear();		
		horses = new horse[5];
		montion = new horseMotion[5];
		for(int i = 0;i<5;i++) {
			montion[i] = new horseMotion(i);
			horses[i] = new horse((178+(83*i)),i);

		}
		timer.cancel();
		timer = new Timer();
	}
	public void paintComponent(Graphics g){		
		g.drawImage( background,1,30,null);
		g.drawImage( people,200,80,null);
		g.drawImage( people,400,80,null);
		g.drawImage( people,600,80,null);

	}
	public void exit() {
		if(setpoint.setBoard(point)) {
			
			JOptionPane.showMessageDialog(null,"本局得到"+point+"分"
					+ "，個人紀錄已更新");
		}else {
			JOptionPane.showMessageDialog(null,"本局得到"+point+"分");
		}
	
		
		timer.cancel();
		timer.purge();
		timer= null;
	}
	

}
