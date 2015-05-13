import java.util.ArrayList;

/**
 * @decription 最简单的robot，任意条件下，每种行动的概率相等
 * @author Haibin Chen
 * @create 2015-5-13
 * @update 2015-5-13
 */
public class SimpleRobot implements IRobot {

	public ArrayList<Double> messageHandle(Message msg) {
		// TODO Auto-generated method stub
		ArrayList<Double> decision = new ArrayList<Double>();
		decision.add(0.2); // 让牌的概率
		decision.add(0.2); // 跟注的概率
		decision.add(0.2); // 加注的概率
		decision.add(0.2); // 全押的概率
		decision.add(0.2); // 弃牌的概率
		return null;
	}

}
