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
        
	ArrayList<Double> decision = getStrategy()
	    .getDecisionbyStrengh(strength);
	return decision;
    }

    protected Strategy getStrategy() {
	  return Strategy.instanceOf(strategyName); //默认采用保守策略
    }

    public double getWinRate(Message msg) {
	// TODO Auto-generated method stub
	return 0.5;
    }
    protected void chooseStrategy(Message msg){
	   System.out.println("number of active players when choose Strategy:"+msg.numOfActivePlayers);
	   if(msg.numOfActivePlayers<3)
	       this.strategyName = Strategy.StrategyName.RADICAL_LEVEL_2;
	   else if(msg.numOfActivePlayers<4)
	       this.strategyName = Strategy.StrategyName.RADICAL_LEVEL_1;
	   else if(msg.numOfActivePlayers<5)
	       this.strategyName = Strategy.StrategyName.NORMAL;
	   else if(msg.numOfActivePlayers<6)
	       this.strategyName = Strategy.StrategyName.CON_LEVEL_2;
	   else if(msg.numOfActivePlayers<7)
	       this.strategyName = Strategy.StrategyName.CON_LEVEL_1;  
	   System.out.println("choose the strategy "+this.strategyName.toString());
    }


   
   
   



}
