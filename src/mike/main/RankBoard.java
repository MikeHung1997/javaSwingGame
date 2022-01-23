package mike.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.awt.image.*;
public class RankBoard extends JPanel{
	Properties prop;
	JPanel center,board,searchArea;
	JLabel[] top5; 
	JLabel searchresault;
	JTextField searchBox;
	JButton search;
    BufferedImage background;
    Color transparent;
	public RankBoard (){	
		System.out.println("test");
		prop = new Properties();
		prop.put("user", "root");
		prop.put("password", "root");		
		
		center = new JPanel(new BorderLayout());
		board = new JPanel(new GridLayout(7,1));
		transparent = new Color(0f,0f,0f,0f);
		searchArea = new JPanel(new FlowLayout());
		searchresault = new JLabel("輸入您的名字查詢排行");
		top5 = new JLabel[6];		
		searchBox =new JTextField(20);		
		search = new JButton("搜尋");		
		for(int i = 0;i<6;i++) {
			top5[i] = new JLabel();
		}

		try {
			background = ImageIO.read(new File("public/rankbk.png"));
		}catch(Exception e) {
			System.out.println(e.toString());
		}
		layoutView();
		event();
		getBoard();
		
	}
	public void layoutView() {
		setLayout(new BorderLayout());		
		searchArea.add(searchresault);
		searchArea.add(searchBox);
		searchArea.add(search);
		board.add(new JLabel());
		for(int i = 0;i<6;i++) {
			board.add(top5[i]);
			top5[i].setFont(top5[i].getFont().deriveFont(20f));
		}
		center.add(board,BorderLayout.CENTER);
		center.add(searchArea,BorderLayout.SOUTH);
		
		add(center,BorderLayout.CENTER);
		add(searchArea,BorderLayout.SOUTH);
		JLabel space = new JLabel("  ");
		space.setFont(getFont().deriveFont(30f));
		center.add(space,BorderLayout.NORTH);
		top5[0].setFont(getFont().deriveFont(30f));
		top5[0].setText("                           \n");
		top5[0].setFont(top5[0].getFont().deriveFont(20f));
		board.setBackground(transparent);
		center.setBackground(transparent);
		searchArea.setBackground(transparent);
		searchBox.setBackground(Color.white);
		searchresault.setFont(getFont().deriveFont(20f));
		searchBox.setFont(getFont().deriveFont(20f));
		searchresault.setForeground(Color.BLACK);
	}
	
	public void getBoard() {		
		String scoreBoard = "select name, (game1Point+game2Point+game3Point)total,game1Point ,game2Point,game3Point"
								+ " from gamerank order by total DESC LIMIT 5";		

		try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/eeit36",prop);){
			PreparedStatement tPstmt = connection.prepareStatement(scoreBoard);
			ResultSet tRs = tPstmt.executeQuery();
			int i = 1;
			while(tRs.next()) {
				String name = tRs.getString("name");
				int total = Integer.parseInt(tRs.getString("total"));
				int game1Point = tRs.getInt("game1Point");						
				int game2Point = tRs.getInt("game2Point");						
				int game3Point = tRs.getInt("game3Point");
						

				top5[i].setText(String.format(
						"                	             |%06d|    %05d   /    %05d    /  %05d        |   -%s-",
						total,game1Point,game2Point, game3Point,name));				
				i++;
			}
			tPstmt.close();
		}catch(Exception e) {
			System.out.println(e.toString());
		}
		
	
		
		
	}
	
	public void event() {
		search.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {				
				getData(searchBox.getText());
			}
			
		}
				
				
		);
		
	}
	
	public void getData(String searchname) {
		String userSearch = "select name, (game1Point+game2Point+game3Point)total,game1Point ,game2Point,game3Point"
				+ " from gamerank order by total DESC";		
		try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/eeit36",prop);){
		PreparedStatement sPstmt = connection.prepareStatement(userSearch);
		ResultSet sRs = sPstmt.executeQuery();
		int rank = 0;
		boolean haveData = false;
		
		while(sRs.next()) {
			rank++;				
			String name = sRs.getString("name");
			if(searchname.equals(name)) {
				String total = sRs.getString("total");
				String game1Point = sRs.getString("game1Point");
				String game2Point = sRs.getString("game2Point");
				String game3Point = sRs.getString("game3Point");
				System.out.println(
						String.format("%s 目前排行 %d 總分:%s  十點半:%s 金幣神偷:%s 賭馬:%s "
										,name,rank,total,game1Point,game2Point, game3Point));				
				String res = 
						String.format("-%s-\n"
								+ ".總分:%s\n"
								+ ".十點半:%s 金幣神偷:%s 賭馬:%s\n"
								+ ".目前排行 %d "
						,name,total,game1Point,game2Point, game3Point,rank);
				JOptionPane.showMessageDialog(null,res);				
				haveData = true;
				break;
			}				
		}	
		if(!haveData) {
			JOptionPane.showMessageDialog(null,"查無此人");
		}		
		sPstmt.close();
	}catch(Exception e) {
		System.out.println(e.toString());
	}
		
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		g.drawImage(background,1,1,null);
		
	}

	
}
