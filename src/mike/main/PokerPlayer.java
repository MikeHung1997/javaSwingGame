package mike.main;

import java.io.Serializable;
import java.util.LinkedList;

public class PokerPlayer implements Serializable{
	private LinkedList<Integer> Handscard;
	private double point = 0; 
	private String name;
	public int count = 0;
	public PokerPlayer() {
		this("玩家");
	}
	public PokerPlayer(String name) {
		Handscard = new LinkedList<>();
		this.name = name;
	}
	public LinkedList<Integer> getHandscard() {
		return Handscard; 
	}
	public double getPoint() {
		return point;
	}
	
	public void setPoint(double point) {
		this.point = point;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public void earnCard(int cardNum) {
		Handscard.add(cardNum);
	}
	
	
	public void handCardClear() {
		Handscard.clear();
	}
	public void setHandscard(LinkedList<Integer> handscard) {
		Handscard = handscard;
	}
	
	
	
}
