/**
 *@des: set of enum type value
 *@author: Chen Haibin
 *@create:2015.5.10
 *@update:2015.5.11 
 *@version:0.1
 * */


/***
 * 牌的花色
 */
public enum Color {
    SPADES, // 黑桃
    HEARTS, // 红心
    CLUBS, // 梅花
    DIAMONDS; // 方块
}

/***
 * 牌型，由小到大
 */
enum Nut_Hand {
    HIGH_CARD, // 高牌
    ONE_PAIR, // 一对
    TWO_PAIR, // 两对
    THREE_OF_A_KIND, // 三条
    STRAIGHT, // 顺子
    FLUSH, // 同花
    FULL_HOUSE, // 葫芦
    FOUR_OF_A_KIND, // 四条
    STRAIGHT_FLUSH; // 同花顺
}

/***
 *叫牌动作类型
 */
enum Action {
    BLIND, // 盲注
    CHECK, // 让牌
    CALL, // 跟牌
    RAISE, // 加注
    ALL_IN, // 全下
    FOLD; // 弃牌

    /**
     * @function: Transfer the string name of action to corresponding entity of
     *            enum type
     * @param: the name of Action used in message delivered between server and
     *         game
     * @return: return the corresponding Action,if the name are not compatible,
     *          return null
     * @create:2015-5-13
     * @update:2015-5-13
     */
    static Action getActionType(String act) {
	Action[] vs = Action.values();
	for (int i = 0; i < vs.length; i++) {
	    if (act.equalsIgnoreCase(vs[i].toString())) {
		return vs[i];
	    }
	}
	return null;
    }
}

/***
 *发送和接收消息的类型
 */
enum MessageType {
    REG(""), // 注册消息
    SEAT_INFO("seat"), // 座次消息
    GAME_OVER("game-over"), // 游戏结束消息
    BLIND("blind"), // 盲注消息
    HOLD_CARD("hold"), // 手牌消息
    INQUIRE("inquire"), // 询问消息
    ACTION(""), // 行动消息
    FLOG("flop"), // 公牌消息
    TURN("turn"), // 转牌消息
    RIVER("river"), // 和牌消息
    SHOWDOWN("showdown"), // 摊牌消息
    POT_WIN("pot-win"); // 彩池分配消息
    String tag;

    MessageType(String t) {
	this.tag = t;
    }

    static MessageType getTypeFromTag(String tag) {
	for (int i = 0; i < MessageType.values().length; i++) {
	    if (tag.equals(MessageType.values()[i].tag)) {
		return MessageType.values()[i];
	    }
	}
	return null;
    }
}
/**
 * 牌的点数
 * @decription 
 * @author Haibin Chen
 * @create 2015-5-19
 * @update 2015-5-19
 */
enum Point{
    _2("2"),
    _3("3"),
    _4("4"),
    _5("5"),
    _6("6"),
    _7("7"),
    _8("8"),
    _9("9"),
    _10("10"),
    _J("J"),
    _Q("Q"),
    _K("K"),
    _A("A");
    String val;
    Point(String val){
	 this.val = val;
    }
    static Point getFromStr(String val){
	 Point[] ps = Point.values();
	 for(int i=0;i<ps.length;i++){
	     if(val.equals(ps[i].val)){
		 return ps[i];
	     }
	 }
	 return null;
    }
}