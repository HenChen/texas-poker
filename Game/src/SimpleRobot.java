import java.util.ArrayList;

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
		return null;
	}

}
