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
    REG, // 注册消息
    SEAT_INFO, // 座次消息
    GAME_OVER, // 游戏结束消息
    BLIND, // 盲注消息
    HOLD_HARD, // 手牌消息
    INQUIRE, // 询问消息
    ACTION, // 行动消息
    FLOG, // 公牌消息
    TURN, // 转牌消息
    RIVER, // 和牌消息
    SHOWDOWN, // 摊牌消息
    POT_WIN; // 彩池分配消息
}
