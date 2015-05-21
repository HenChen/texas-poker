import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @decription TODO
 * @author Zhang Lingmin
 * @create 2015-5-19
 * @update 2015-5-19
 */
public class StatisticsRobot extends SimpleRobot {
    /**
     * key is the product of 5 prime numbers, the value is the rank.card without
     * color
     */
    private Map<Integer, Integer> rankTableNoColor;

    /**
     * key is the product of 5 prime numbers, the value is the rank.card with
     * color
     */
    private Map<Integer, Integer> rankTableColor;

    /**
     * key is the card point,value is the corresponding prime number
     */
    private Map<Integer, Integer> primeTable; // 存储点数对应的指数

    /**
     * Cards set
     */
    private ArrayList<Card> cardsSet;

    public StatisticsRobot() {
	setPrimeTable();
	setRankTable();
	cardsSet = new ArrayList<Card>();
	for (Point p : Point.values()) {
	    for (Color c : Color.values()) {
		cardsSet.add(new Card(c, p));
	    }
	}
	System.out.println("Intialize...");
    }

    public int getCardIndex(Card card) {
	int point = 0;
	int color = 0;
	point = card.point.ordinal();
	color = card.color.ordinal();
	return 4 * point + color;
    }

    /**
     * get the product of five prime number
     * 
     * @param n1
     *            number one
     * @param n2
     *            number two
     * @param n3
     *            number three
     * @param n4
     *            number four
     * @param n5
     *            number five
     * @return
     */
    public int getKey(int n1, int n2, int n3, int n4, int n5) {
	return primeTable.get(n1) * primeTable.get(n2) * primeTable.get(n3)
		* primeTable.get(n4) * primeTable.get(n5);
    }

    public int getKey(int[] arr) {
	return primeTable.get(arr[0]) * primeTable.get(arr[1])
		* primeTable.get(arr[2]) * primeTable.get(arr[3])
		* primeTable.get(arr[4]);
    }

    /**
     * get the rank value
     * 
     * @param key
     *            the product of five prime number
     * @param color
     *            considering color or not
     * @return
     */
    public int getRankValue(int key, boolean color) {
	if (color) {
	    return rankTableColor.get(key);
	} else {
	    return rankTableNoColor.get(key);
	}
    }

    /**
     * 获取牌力，牌的组合为2张，5张，7张
     * 
     * @param cards
     * @return
     */
    public int getStrengthOfCards(ArrayList<Card> cards) {
	Color[] color = new Color[5];
	int[] points = new int[5];
	int StrengthOfCards = 10000;
	if (cards.size() == 2) {
	    int point1 = cards.get(0).point.ordinal();
	    int point2 = cards.get(1).point.ordinal();
	    if (point1 == point2)
		StrengthOfCards = 12 - point1;
	    else if (point1 > point2)
		StrengthOfCards = 90 - (4 * point1 + point2);
	    else
		StrengthOfCards = 90 - (4 * point2 + point1);
	}
	if (cards.size() == 5) {
	    for (int i = 0; i < cards.size(); i++) {
		color[i] = cards.get(i).color;
		points[i] = cards.get(i).point.ordinal() + 2;
	    }
	    StrengthOfCards = getRankValue(getKey(points), isFlush(color));
	}
	if (cards.size() == 6) {
	    int tmp;
	    for (int i = 0; i < 6; i++) {
		ArrayList<Card> tmpCardArr = new ArrayList<Card>();
		tmpCardArr.addAll(cards);
		tmpCardArr.remove(i);
		for (int j = 0; j < tmpCardArr.size(); j++) {
		    color[j] = tmpCardArr.get(j).color;
		    points[j] = tmpCardArr.get(j).point.ordinal() + 2;
		}
		tmp = getRankValue(getKey(points), isFlush(color));
		if (tmp < StrengthOfCards) {
		    StrengthOfCards = tmp;
		}
	    }
	}
	if (cards.size() == 7) {
	    int tmp;
	    for (int i = 0; i < 7; i++) {
		for (int k = i + 1; k < 7; k++) {
		    ArrayList<Card> tmpCardArr = new ArrayList<Card>();
		    for(int m=0;m<cards.size();m++){
			if(m!=i&&m!=k)
			    tmpCardArr.add(cards.get(m));
		    }
		    for (int j = 0; j < tmpCardArr.size(); j++) {
			color[j] = tmpCardArr.get(j).color;
			points[j] = tmpCardArr.get(j).point.ordinal() + 2;
		    }
		    tmp = getRankValue(getKey(points), isFlush(color));
		    if (tmp < StrengthOfCards) {
			StrengthOfCards = tmp;
		    }
		}
	    }
	}
	return StrengthOfCards;
    }
    /**
     * @function TODO
     * @param: test
     * @return:void
     * @create:2015-5-19
     * @update:
    
    public static void main(String args[]){
	Message msg = new Message();
	Card c1 = new Card(Color.DIAMONDS,Point._A);
	Card c2 = new Card(Color.DIAMONDS,Point._K);
	Card c3 = new Card(Color.DIAMONDS,Point._Q);
	Card c4 = new Card(Color.DIAMONDS,Point._J);
	Card c5 = new Card(Color.DIAMONDS,Point._10);
	msg.myCard1 = c1;
	msg.myCard2 = c2;
	msg.publicCard.add(c3);
	msg.publicCard.add(c4);
	msg.publicCard.add(c5);
	IRobot rb = new StatisticsRobot();
	double shenglv = rb.getWinRate(msg);
	System.out.print(shenglv);
    }
    */
    /**
     * 获取胜率
     * 
     * @param msg
     * @return
     */
    @Override
    public double getWinRate(Message msg) {
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
	return (winCount+drawCount) / (winCount + loseCount + drawCount);
    }

    /**
     * the five card is flush or not
     * 
     * @param color
     *            array of colors
     * @return
     */
    public boolean isFlush(Color[] color) {
	boolean flag = true;
	for (int i = 0; i < color.length; i++) {
	    for (int j = i + 1; j < color.length; j++)
		if (color[j].compareTo(color[i]) != 0)
		    flag = false;
	}
	return flag;
    }

    /**
     * set prime table
     */
    public void setPrimeTable() {
	Map<Integer, Integer> pTable = new HashMap<Integer, Integer>();
	int[] primeNumber = { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 49 };
	for (int i = 2; i <= 14; i++) {
	    pTable.put(i, primeNumber[i - 2]);
	}
	this.primeTable = pTable;
    }

    /**
     * generate rank table, initialize
     */
    public void setRankTable() {
	Map<Integer, Integer> rTable = new HashMap<Integer, Integer>();
	int[] arr = { 14, 13, 12, 11, 10 };
	int rankNumber = 6286; // rank number for high card
	int rankNumber2 = 1601; // rank number for straight
	rTable.put(getKey(arr[0], arr[1], arr[2], arr[3], arr[4]), 1600);
	boolean isStraight = false;
	// high card and straight
	while (arr[0] > 6) {
	    arr[4]--;
	    isStraight = false;
	    if (arr[4] == 1) {
		if (arr[3] > 3) {
		    arr[3]--;
		    arr[4] = arr[3] - 1;
		} else {
		    if (arr[2] > 4) {
			arr[2]--;
			arr[3] = arr[2] - 1;
			arr[4] = arr[3] - 1;
		    } else {
			if (arr[1] > 5) {
			    arr[1]--;
			    arr[2] = arr[1] - 1;
			    arr[3] = arr[2] - 1;
			    arr[4] = arr[3] - 1;
			} else {
			    // Straight
			    arr[0]--;
			    arr[1] = arr[0] - 1;
			    arr[2] = arr[1] - 1;
			    arr[3] = arr[2] - 1;
			    arr[4] = arr[3] - 1;
			    isStraight = true;
			}
		    }
		}
	    }
	    if (isStraight) {
		rTable.put(getKey(arr[0], arr[1], arr[2], arr[3], arr[4]),
			rankNumber2);
		rankNumber2++;
	    } else {
		rTable.put(getKey(arr[0], arr[1], arr[2], arr[3], arr[4]),
			rankNumber);
		rankNumber++;
	    }
	}

	// FOUR_OF_A_KIND
	rankNumber = 11;
	for (int i = 14; i >= 2; i--) {
	    for (int j = 14; j >= 2; j--) {
		if (j == i)
		    continue;
		rTable.put(getKey(i, i, i, i, j), rankNumber);
		rankNumber++;
	    }
	}
	// End FOUR_OF_A_KIND

	// FULL_HOUSE
	for (int i = 14; i >= 2; i--) {
	    for (int j = 14; j >= 2; j--) {
		if (j == i)
		    continue;
		rTable.put(getKey(i, i, i, j, j), rankNumber);
		rankNumber++;
	    }
	}
	// End FULL_HOUSE

	// THREE_OF_A_KIND
	rankNumber = 1610;
	for (int i = 14; i >= 2; i--) {
	    for (int j = 14; j >= 3; j--) {
		for (int k = j - 1; k >= 2; k--) {
		    if (j == i || k == i)
			continue;
		    rTable.put(getKey(i, i, i, j, k), rankNumber);
		    rankNumber++;
		}
	    }
	}
	// End THREE_OF_A_KIND

	// TWO_PAIR
	for (int i = 14; i >= 3; i--) {
	    for (int j = i - 1; j >= 2; j--) {
		for (int k = 14; k >= 2; k--) {
		    if (k == i || k == j)
			continue;
		    rTable.put(getKey(i, i, j, j, k), rankNumber);
		    rankNumber++;
		}
	    }
	}
	// End TWO_PAIR
	// ONE_PAIR
	for (int i = 14; i >= 2; i--) {
	    for (int j = 14; j >= 2; j--) {
		for (int k = j - 1; k >= 2; k--) {
		    for (int q = k - 1; q >= 2; q--) {
			if (j == i || k == i || q == i)
			    continue;
			rTable.put(getKey(i, i, j, k, q), rankNumber);
			rankNumber++;
		    }
		}
	    }
	}
	// End ONE_PAIR
	this.rankTableNoColor = rTable;

	Map<Integer, Integer> rTable2 = new HashMap<Integer, Integer>();
	arr[0] = 14;
	arr[1] = 13;
	arr[2] = 12;
	arr[3] = 11;
	arr[4] = 10;
	rTable2.put(getKey(arr[0], arr[1], arr[2], arr[3], arr[4]), 1);
	rankNumber = 323; // rank number for high card
	rankNumber2 = 2; // rank number for straight
	while (arr[0] > 6) {
	    arr[4]--;
	    isStraight = false;
	    if (arr[4] == 1) {
		if (arr[3] > 3) {
		    arr[3]--;
		    arr[4] = arr[3] - 1;
		} else {
		    if (arr[2] > 4) {
			arr[2]--;
			arr[3] = arr[2] - 1;
			arr[4] = arr[3] - 1;
		    } else {
			if (arr[1] > 5) {
			    arr[1]--;
			    arr[2] = arr[1] - 1;
			    arr[3] = arr[2] - 1;
			    arr[4] = arr[3] - 1;
			} else {
			    // Straight
			    arr[0]--;
			    arr[1] = arr[0] - 1;
			    arr[2] = arr[1] - 1;
			    arr[3] = arr[2] - 1;
			    arr[4] = arr[3] - 1;
			    isStraight = true;
			}
		    }
		}
	    }
	    if (isStraight) {
		rTable2.put(getKey(arr[0], arr[1], arr[2], arr[3], arr[4]),
			rankNumber2);
		rankNumber2++;
	    } else {
		rTable2.put(getKey(arr[0], arr[1], arr[2], arr[3], arr[4]),
			rankNumber);
		rankNumber++;
	    }
	}
	this.rankTableColor = rTable2;
    }
    
}
