import java.util.ArrayList;
import java.util.Arrays;


public class SimplePredictRobot extends StatisticsRobot {
	/**
     * 获取胜率
     * 
     * @param msg
     * @return
     */
	public SimplePredictRobot() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public double getWinRate(Message msg){
		if(msg.publicCard.size()!=3){
			return getWinRate2(msg);
		}
		else{
			double hs = getWinRate2(msg);
			int[] colors = new int[5];
			int[] points = new int[5];
			colors[0] = msg.myCard1.color.ordinal();
			colors[1] = msg.myCard2.color.ordinal();
			points[0] = msg.myCard1.point.ordinal();
			points[1] = msg.myCard2.point.ordinal();
			int count = 2;
			for(Card c:msg.publicCard){
				colors[count] = c.color.ordinal();
				points[count] = c.point.ordinal();
				count++;
			}
			Arrays.sort(colors);
			Arrays.sort(points);
			if(colors[0]==colors[3]||colors[1]==colors[4])
				hs = hs*0.65+0.35;				
			if(isFlushFourCard(points))
				hs = hs*0.9+0.1;
			return hs;
		}
	}
	public boolean isFlushFourCard(int[] points){
		boolean flag = false;
		if(points[3]-points[0]==3&&points[3]!=points[2]&&points[0]!=points[1]&&points[2]!=points[1])
			flag = true;
		if(points[4]-points[1]==3&&points[4]!=points[3]&&points[1]!=points[2]&&points[3]!=points[2])
			flag = true;
		return flag;
	}
    public double getWinRate2(Message msg) {
		ArrayList<Card> excludeCard = new ArrayList<Card>(); // 遍历时排除的牌，包括公共牌，自己的手牌
		ArrayList<Card> opponentCard = new ArrayList<Card>();
		double winCount = 0;
		double loseCount = 0;
		double drawCount = 0;
		Card c1,c2;
		excludeCard.add(cardsSet.get(getCardIndex(msg.myCard1)));
		excludeCard.add(cardsSet.get(getCardIndex(msg.myCard2)));
		double opponentRankValue;
		for (Card c : msg.publicCard) {
		    excludeCard.add(cardsSet.get(getCardIndex(c)));
		    opponentCard.add(cardsSet.get(getCardIndex(c)));
		}
		double myRankValue = getStrengthOfCards(excludeCard);
		for (int i=0;i<cardsSet.size();i++) {
		    for (int j=i+1;j<cardsSet.size();j++) {
			c1 = cardsSet.get(i);
			c2 = cardsSet.get(j);
			if (!excludeCard.contains(c1)
				&& !excludeCard.contains(c2)) {
			    opponentCard.add(c1);
			    opponentCard.add(c2);
			    opponentRankValue = getStrengthOfCards(opponentCard);
			    if (opponentRankValue > myRankValue) {
			    	winCount++;
			    }
			    else if (opponentRankValue == myRankValue) {
			    	drawCount++;
			    } else {
					loseCount++;
			    }
			    opponentCard.remove(opponentCard.size() - 1);
			    opponentCard.remove(opponentCard.size() - 1);
			}
		    }
		}
		return (winCount+drawCount/2) / (winCount + loseCount + drawCount);
    }
    /*
    public static void main(String args[]){
    	Message msg = new Message();
    	Card c1 = new Card(Color.CLUBS,Point._7);
    	Card c2 = new Card(Color.DIAMONDS,Point._5);
    	Card c3 = new Card(Color.DIAMONDS,Point._4);
    	Card c4 = new Card(Color.DIAMONDS,Point._3);
    	Card c5 = new Card(Color.DIAMONDS,Point._2);
//    	Card c6 = new Card(Color.CLUBS,Point._7);
//   	Card c7 = new Card(Color.CLUBS,Point._10);
    	msg.myCard1 = c1;
    	msg.myCard2 = c2;
    	msg.publicCard.add(c3);
    	msg.publicCard.add(c4);
    	msg.publicCard.add(c5);
//    	msg.publicCard.add(c6);
//    	msg.publicCard.add(c7);
    	IRobot rb = new SimplePredictRobot();
    	double shenglv = rb.getWinRate(msg);
    		System.out.print(shenglv);
        }
    */
}
