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
    protected boolean modelStart = false;
    protected double powerLimit = 0.97;

    public ArrayList<Double> messageHandle(Message msg) {
	chooseStrategy(msg);
	System.out.print(msg.myCard1.color.toString() + "|"
		+ msg.myCard1.point.val + " ");
	System.out.print(msg.myCard2.color.toString() + "|"
		+ msg.myCard2.point.val + " ");
	for (Card card : msg.publicCard) {
	    System.out
		    .print(card.color.toString() + "|" + card.point.val + " ");
	}
	System.out.println();
	double strength = getWinRate(msg);
	System.out.println("胜率：" + strength);
	strength = refineWinRate(msg, strength);
	System.out.println("修改后的胜率：" + strength);

	ArrayList<Double> decision = getStrategy().getDecisionbyStrengh(
		strength);
	refineDecision(msg, decision);
	System.out.println("修改后的决策：" + decision.toString());
	return decision;
    }

    private void refineDecision(Message msg, ArrayList<Double> decision) {
	// TODO Auto-generated method stub
	if (msg.leastCall == 0) {
	    decision.set(0, decision.get(0) + decision.get(4));
	    decision.set(4, 0.0);
	}
	if (msg.leastCall >= 0.6 * msg.jetton) { // 任何种类的下注和跟注都被视为all_in的条件下
	    // 胜率不高
	    if (decision.get(4) > 0.2) { //
		decision.set(4, decision.get(0) + decision.get(1)
			+ decision.get(2) + decision.get(4));
		decision.set(0, 0.0);
		decision.set(1, 0.0);
		decision.set(2, 0.0);
	    }
	}
    }

    public double getDebuff(Message msg) {
	double debuff = 0;
	double myWinRate = getWinRate(msg);
	double downLimit = 0.76; // 下界
	double areaArg = 0.6; // 区域参数
	double myConfidence = (myWinRate - downLimit) / areaArg; // 我的自信度与对手比较
	boolean flag = false;
	for (ActionMsg g_msg : msg.globalMsg) {
	    System.out.println("id of g_msg:|" + g_msg.playerId + "|"
		    + "my id:|" + msg.myPid + "|");
	    if (g_msg.turnId != msg.turnId
		    || g_msg.playerId.compareTo(msg.myPid) == 0)
		continue;
	    RivalStrength rs = msg.rivalModel.get(g_msg.playerId);
	    if (rs == null) {
		for (String key : msg.rivalModel.keySet()) {
		    System.out.println(key + ":"
			    + msg.rivalModel.get(key).toString());
		}
		System.out.println("the request id is |" + g_msg.playerId + "|");
	    }
	    if (g_msg.action.equals(Action.ALL_IN)) {
		if (myConfidence < rs.all_in) {
		    powerLimit = 0.99;
		    flag = true;
		}
		debuff += rs.all_in;
	    }
	    if (g_msg.action.equals(Action.RAISE)) {
		if (myConfidence < rs.raise) {
		    powerLimit = 0.99;
		    flag = true;
		}
		debuff += rs.raise;
	    }

	}
	if (!flag) {
	    powerLimit = 0.97;
	}
	return debuff;
    }

    protected Strategy getStrategy() {
	return Strategy.instanceOf(strategyName); // 默认采用保守策略
    }

    public double getWinRate(Message msg) {
	// TODO Auto-generated method stub
	return 0.5;
    }

    public double refineWinRate(Message msg, double winRate) {
	double maxDebuff = 0.4;
	powerLimit = 0.97; // 超越该值的胜率不会受到他信度模型的减益效果

	if (msg.turnId == 0) {// 盲注阶段
	    // 如果花色相同，调整胜率
	    if (msg.myCard1.color == msg.myCard2.color)
		winRate = 0.2 + 0.8 * winRate;
	    double base = msg.bet * 2 > msg.jetton ? msg.bet * 2 : msg.jetton;
	    double flag = msg.leastCall / base;
	    if (flag < 0.2) {
		winRate = 0.3 + 0.6 * winRate;
	    } else if (flag < 0.5) {
		winRate = 0.2 + 0.6 * winRate;
	    } else {
		winRate = 0.1 + 0.6 * winRate;
	    }
	}
	if (msg.money + msg.jetton <= 2000) {
	    this.modelStart = true;
	    //System.out.println("......Model started ......");
	}
	if (this.modelStart && winRate < powerLimit) {
	    double debuff = this.getDebuff(msg);
	    if (debuff > maxDebuff) {
		winRate = winRate - maxDebuff;
	    } else {
		winRate = winRate - debuff;
	    }
	}

	if (msg.jetton < 100 && msg.money >= 2000) {
	    winRate = 0.95;
	}

	return (winRate > 0 ? winRate : 0);
    }

    protected void chooseStrategy(Message msg) {
	System.out.println("number of active players when choose Strategy:"
		+ msg.numOfActivePlayers);
	if (msg.numOfActivePlayers <= 4)
	    this.strategyName = Strategy.StrategyName.CON_LEVEL_2;
	else
	    this.strategyName = Strategy.StrategyName.CON_LEVEL_1;
	System.out.println("choose the strategy "
		+ this.strategyName.toString());
    }

    public static void main(String[] args) {
	String s1 = "1111";
	String s2 = new String(s1);
	System.out.println(s1);
	System.out.println(s2);
	System.out.println(s1 == s2);
	System.out.println(s1.compareTo(s2));
	System.out.println(s1.compareTo("21"));
    }

}
