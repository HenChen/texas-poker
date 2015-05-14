import java.util.ArrayList;

/**
 * @decription 消息结构体，保存手牌，公共牌以及各个玩家当轮的行动信息
 * @author Haibin Chen
 * @create 2015-5-13
 * @update 2015-5-13 18:45
 */


public class Message {
    Card myCard1; // 手牌1
    Card myCard2; // 手牌2
    ArrayList<Card> publicCard; // 公共牌 包括 转牌，河牌
    ArrayList<ActionMsg> globalMsg; // 当轮全局决策信息
    int totalPot;
    int blindPot; // 盲注
    int turnId; // 当局比赛轮数
    boolean active;// when all in and fold, it is false, otherwise it is true;

    public Message() {
	myCard1 = new Card();
	myCard2 = new Card();
	publicCard = new ArrayList<Card>();
	globalMsg = new ArrayList<ActionMsg>();
	totalPot = 0;
    }

    public void clear() {
	publicCard.clear();
	globalMsg.clear();
	totalPot = 0;
	turnId = 0;
    }
}

/**
 * @decription 纸牌结构体
 * @author Haibin Chen
 * @create 2015-5-13
 * @update 2015-5-13
 */
class Card {
    Color color; // 花色
    String point; // 点数

    public Card() {
    }

    public Card(String cardInfo) {
	String temp[] = cardInfo.split(" ");
	color = Color.valueOf(temp[0].trim());
	point = temp[1].trim();
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
	playerId = Integer.parseInt(temp[0]);
	jetton = Integer.parseInt(temp[1]);
	money = Integer.parseInt(temp[2]);
	bet = Integer.parseInt(temp[3]);
	action = Action.getActionType(temp[4]);
    }
    int playerId; // 玩家Id
    Action action; // 动作
    int jetton; // 手中的赌注
    int money; // 剩余金币
    int bet; // 本手牌累计投注额
    int turnId; // 轮次
}
