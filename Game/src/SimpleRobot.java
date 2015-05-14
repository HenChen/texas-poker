import java.util.ArrayList;
import java.util.Random;

/**
 * @decription ��򵥵�robot�����������£�ÿ���ж��ĸ������
 * @author Haibin Chen
 * @create 2015-5-13
 * @update 2015-5-13
 */
public class SimpleRobot implements IRobot {

    public ArrayList<Double> messageHandle(Message msg) {
	// TODO Auto-generated method stub
	ArrayList<Double> decision = new ArrayList<Double>();
	decision.add(0.2); // ���Ƶĸ���
	decision.add(0.2); // ��ע�ĸ���
	decision.add(0.2); // ��ע�ĸ���
	decision.add(0.2); // ȫѺ�ĸ���
	decision.add(0.2); // ���Ƶĸ���
	return decision;
    }

    public String generateActionMessage(Message msg) {
	// TODO Auto-generated method stub
	ArrayList<Double> decision = messageHandle(msg);
	for (int i = 1; i < decision.size(); i++) {
	    decision.set(i, decision.get(i) + decision.get(i - 1));
	}
	String actionNames[] = { "action", "call", "raise", "all_in", "fold" };
	String msgStr = "";
	Random dice = new Random();
	double cursor = dice.nextDouble();
	int action = 0;
	for (; action < decision.size(); action++) {
	    if (cursor < decision.get(action)) {
		break;
	    }
	}

	msgStr = msgStr + actionNames[action];
	if (actionNames[action].equals("raise")) {
	    msgStr = msgStr + " " + (msg.blindPot * 2);
	}
	msgStr = msgStr + "\n";

	return msgStr;
    }

}
