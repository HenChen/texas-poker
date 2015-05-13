/**
 * @decription 消息结构体，保存手牌，公共牌以及各个玩家当轮的行动信息
 * @author Haibin Chen
 * @create 2015-5-13
 * @update 2015-5-13
 */

public class Message {
    Card myCard1; //手牌1
    Card myCard2; //手牌2
    Card []publicCard; //公共牌
    int publicCardNum; //公共牌数量
    ActionMsg[] globalMsg; //当轮全局决策信息
    int activePlayerNum; //当前活动玩家数
}

/**
 * @decription 纸牌结构体
 * @author Haibin Chen
 * @create 2015-5-13
 * @update 2015-5-13
 */
class Card{
	Color color; // 花色
	int point; // 点数
}

/**
 * @decription 行动消息结构体
 * @author Haibin Chen
 * @create 2015-5-13
 * @update 2015-5-13
 */
class ActionMsg{
	int playerId;  //玩家Id
	Action action; //动作
	int jetton; //手中的赌注
	int money;  //剩余金币

}



