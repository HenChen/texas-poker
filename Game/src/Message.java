import java.util.ArrayList;
import java.util.Hashtable;
/**
 * @decription 消息结构体，保存手牌，公共牌以及各个玩家当轮的行动信息
 * @author Haibin Chen
 * @create 2015-5-13
 * @update 2015-5-19 18:45
 */


public class Message {
    Card myCard1; // 手牌1
    Card myCard2; // 手牌2
    ArrayList<Card> publicCard; // 公共牌 包括 转牌，河牌
    ArrayList<ActionMsg> globalMsg; // 当轮全局决策信息
    Hashtable<String,RivalStrength> rivalModel;
    int totalPot;
    int blindPot; // 盲注
    int turnId;   // 当局比赛轮数
    int numOfActivePlayers;
    String myPid;
    int jetton;
    int bet;
    int leastCall;
    boolean active;// when all in and fold, it is false, otherwise it is true;
    int money;

    public Message() {
	myCard1 = new Card();
	myCard2 = new Card();
	publicCard = new ArrayList<Card>();
	globalMsg = new ArrayList<ActionMsg>();
	rivalModel = new Hashtable<String,RivalStrength>();
	totalPot = 0;
    }

    public void clear() {
	publicCard.clear();
	globalMsg.clear();
	totalPot = 0;
	turnId = 0;
    }
    public ArrayList<ActionMsg> getActionSequnceAt(int turnId){
	ArrayList<ActionMsg> as = new ArrayList<ActionMsg>();
	for(ActionMsg ms:globalMsg){
	    if(ms.turnId == turnId){
		as.add(ms);
	    }
	}
	return as;
    }
}

/**
 * @decription 纸牌结构体
 * @author Haibin Chen
 * @create 2015-5-13
 * @update 2015-5-19
 */
class Card {
    Color color; // 花色
    Point point; // 点数

    public Card() {
    }
    public Card(Color color,Point point){
	this.color = color;
	this.point = point;
    }
    public Card(String cardInfo) {
	String temp[] = cardInfo.split(" ");
	color = Color.valueOf(temp[0].trim());
	point = Point.getFromStr(temp[1].trim());
    }
}

/**
 * @decription 行动消息结构体
 * @author Haibin Chen
 * @create 2015-5-13
 * @update 2015-5-13
 */
class ActionMsg {
    public ActionMsg(String[] temp) {
	// TODO Auto-generated constructor stub
	//pid jetton money bet blind | check | call | raise | all_in | fold 
	playerId = temp[0].trim();
	jetton = Integer.parseInt(temp[1]);
	money = Integer.parseInt(temp[2]);
	bet = Integer.parseInt(temp[3]);
	action = Action.getActionType(temp[4]);
    }
    String playerId; // 玩家Id
    Action action;   // 动作
    int jetton;      // 手中的赌注
    int money;       // 剩余金币
    int bet;         // 本手牌累计投注额
    int turnId;      // 轮次
}
