package mike.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

public class SetRankBoard {
	private String name;
	private int point;
	private int gameType;
	public SetRankBoard(String name,int gameType) {
		this.name = name;
		this.gameType = gameType;		
	}
	public Boolean setBoard(int point){
		boolean newScored = false;
		this.point = point;
		Properties prop = new Properties();
		prop.put("user", "root");
		prop.put("password", "root");
		String game;
		switch(gameType) {
			case 1:
				game = "game1Point";
				break;
			case 2:
				game = "game2Point";
				break;
			case 3:
				game = "game3Point";
				break;
			default:
				game = "err";
				System.out.println(game);
				break;
		}
		
		
		
		String checksql = "select "+game+" from gamerank where Name =?";		
		String updatesql = "update gamerank set "+game+" = ? where Name = ?";	
	   	String insertsql = "insert into gamerank (Name , "+game+") values (?,?)";
	   	boolean Update = false;
		try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/eeit36",prop);){
			
			PreparedStatement cPstmt = connection.prepareStatement(checksql);
			cPstmt.setString(1, name);
			ResultSet cRs =  cPstmt.executeQuery();
			int check;
			while(cRs.next()) {
				check =	cRs.getInt(game);
				if(point>check) {
					PreparedStatement uPstmt = connection.prepareStatement(updatesql);
					uPstmt.setInt(1, point);
					uPstmt.setString(2, name);
					uPstmt.execute();
					uPstmt.close();
					Update = true;
					newScored = true;
					break;
				}else {	
					Update = true;
					break;
				}				
			}	
			if(!Update) {
				PreparedStatement iPstmt = connection.prepareStatement(insertsql);
				iPstmt.setString(1,name);
				iPstmt.setInt(2,point);				
				iPstmt.execute();
				iPstmt.close();
				newScored = true;
				Update = true;
			}
			cPstmt.close();
				
			return newScored;
			
		}catch(Exception e) {
			System.out.println(e.toString());
			return Update;
		}
	}

}
