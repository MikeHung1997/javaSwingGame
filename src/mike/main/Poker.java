package mike.main;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;


public class Poker implements Serializable{
	private LinkedList<Integer> card;
	public final static int type_Clasic = 52;
	public final static int type_Joker = 54;
	private int cardQuantity; 
	
	public Poker() {
		this(52);
	}
	
	public Poker(int type) {
		card = new LinkedList<>();
		if(type==52||type==54) {
			for(int i = 0;i<(type);i++) {
				card.add(i);
				this.cardQuantity = type;
			}
		}else {
			for(int i = 0;i<52;i++) {
				card.add(i);
				this.cardQuantity = 52;
			}
		}			
	}
	
	public void newGame() {
		if(card.size()<cardQuantity) {
			card.clear();
			for(int i = 0;i<(cardQuantity);i++) {
				card.add(i);
			}
		}

		shuffle();
	}
	
	//發牌
	public int deal() {	
		return card.poll();
	}
	
	public void shuffle() {
		Collections.shuffle(card);		
	}
	
	
	public LinkedList<Integer> getCard() {
		return card;
	}

	public void setCard(LinkedList<Integer> card) {
		this.card = card;
	}

	public static String cardName(int cardNums) {
		if(cardNums==52||cardNums==53) {
			return "鬼牌";
		}else {
			String cardColor , cardNumber;
			cardNumber = ((cardNums%13)+1)+"";
			switch(cardNums/13) {
				case(0):
					cardColor = "黑桃";
					break;
				case(1):
					cardColor = "紅心";
					break;
				case(2):
					cardColor = "方塊";
					break;
				case(3):
					cardColor = "梅花";
					break;
				default:
					cardColor = "";
			}
			
			return cardColor + cardNumber;
		}

	}
}
