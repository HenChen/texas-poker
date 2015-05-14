import java.util.ArrayList;

/**
 * @decription �����˿˻�����
 * @author Haibin Chen
 * @create 2015-5-13
 * @update 2015-5-13 22:43
 */


public interface IRobot {
    /**
     * @function ����Message���ݣ������һ������D(a,b,c,d,e)
     * @param: �ֳ���Ϣ
     * @return:ArrayList<Double> ����decision
     * @create:2015-5-13
     * @update:
     */
    ArrayList<Double> messageHandle(Message msg);

    /**
     * �ж���Ϣ��check | call | raise num | all_in | fold eol
     * @function �����ж���Ϣ
     * @param: IRobot
     * @return:String
     * @create:2015-5-13
     * @update:
     */
    String generateActionMessage(Message msg);
}
