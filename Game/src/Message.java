public class Message {
    Card myCard1; //手牌1
    Card myCard2; //手牌2
    Card []publicCard; //公共牌
    int publicCardNum; //公共牌数量
    ActionMsg[] globalMsg; //当轮全局决策信息
    int activePlayerNum; //当前活动玩家数
}
class Card{
	Color color;
	int point;
}
class ActionMsg{
	int playerId;  //玩家Id
	Action action; //动作
	int jetton; //手中的赌注
	int money;  //剩余金币

}



