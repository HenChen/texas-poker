import java.util.ArrayList;


/**
 * @decription 最简单的robot，任意条件下，每种行动的概率相等
 * @author Haibin Chen
 * @param <StrategyName>
 * @create 2015-5-13
 * @update 2015-5-13
 */
public class SimpleRobot implements IRobot {
    protected Strategy.StrategyName strategyName = Strategy.StrategyName.CON_LEVEL_1;
    
    public ArrayList<Double> messageHandle(Message msg) {
	chooseStrategy(msg);
	System.out.print(msg.myCard1.color.toString()+"|"+msg.myCard1.point.val+" ");
	System.out.print(msg.myCard2.color.toString()+"|"+msg.myCard2.point.val+" ");
	for(Card card:msg.publicCard){
	    System.out.print(card.color.toString()+"|"+card.point.val+" ");
	}
	System.out.println();
	double strength =getWinRate(msg);
        System.out.println("胜率："+strength);
        strength = refineWinRate(msg,strength);
        System.out.println("修改后的胜率："+strength);
        
	ArrayList<Double> decision = getStrategy()
	    .getDecisionbyStrengh(strength);
	refineDecision(msg,decision);
	System.out.println("修改后的决策："+decision.toString());
	return decision;
    }

    private void refineDecision(Message msg, ArrayList<Double> decision) {
	// TODO Auto-generated method stub
	if(msg.leastCall == 0 ){
	    decision.set(0, decision.get(0)+decision.get(4));
	    decision.set(4, 0.0);
	}
	if(msg.leastCall>=0.6*msg.jetton){ //任何种类的下注和跟注都被视为all_in的条件下
	    //胜率不高
	    if(decision.get(4)>0.2){ //
		decision.set(4, decision.get(0)+decision.get(1)+decision.get(2)+decision.get(4));
		decision.set(0, 0.0);
		decision.set(1, 0.0);
		decision.set(2, 0.0);
	    }
	}
    }

    protected Strategy getStrategy() {
	  return Strategy.instanceOf(strategyName); //默认采用保守策略
    }

    public double getWinRate(Message msg) {
	// TODO Auto-generated method stub
	return 0.5;
    }
    public double refineWinRate(Message msg,double winRate){
	if(msg.turnId == 0)//盲注阶段
	{
	    //如果花色相同，调整胜率
	    if(msg.myCard1.color == msg.myCard2.color)
		winRate = 0.2+ 0.8*winRate;
	    double base = msg.bet*2 >msg.jetton ? msg.bet*2:msg.jetton;
	    double flag = msg.leastCall / base;
	    if(flag<0.2){
          	winRate = 0.3+0.6*winRate;
	    }else if(flag < 0.5){
		winRate = 0.2+0.6*winRate;
	    }else{
		winRate = 0.1+0.6*winRate;
	    }
	}
	if(msg.jetton<100 && msg.money >=2000){
	    winRate = 0.95;
	}
	return winRate;
    }
    protected void chooseStrategy(Message msg){
	   System.out.println("number of active players when choose Strategy:"+msg.numOfActivePlayers);
	   if(msg.numOfActivePlayers<=4)
	       this.strategyName = Strategy.StrategyName.CON_LEVEL_2;
	   else
	       this.strategyName = Strategy.StrategyName.CON_LEVEL_1; 
	   System.out.println("choose the strategy "+this.strategyName.toString());
    }

}
