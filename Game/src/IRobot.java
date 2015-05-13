import java.util.ArrayList;

/**
 * @decription 
 *   根据Messge内容，构造出一个决策D(a,b,c,d,e) 
 *   a让牌的概率 
 *   b跟注的概率 
 *   c加注的概率 
 *   d全押的概率
 *   e弃牌的概率 
 *   a+b+c+d+e=1 
 * @author Haibin Chen
 * @create 2015-5-13
 * @update 2015-5-13
 */
public interface IRobot {
	ArrayList<Double> messageHandle(Message msg);
}
