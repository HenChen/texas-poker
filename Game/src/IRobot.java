import java.util.ArrayList;

/**
 * @decription 
 *   ����Messge���ݣ������һ������D(a,b,c,d,e) 
 *   a���Ƶĸ��� 
 *   b��ע�ĸ��� 
 *   c��ע�ĸ��� 
 *   dȫѺ�ĸ���
 *   e���Ƶĸ��� 
 *   a+b+c+d+e=1 
 * @author Haibin Chen
 * @create 2015-5-13
 * @update 2015-5-13
 */
public interface IRobot {
	ArrayList<Double> messageHandle(Message msg);
}
