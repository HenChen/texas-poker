/**
 * @decription ��Ϣ�ṹ�壬�������ƣ��������Լ�������ҵ��ֵ��ж���Ϣ
 * @author Haibin Chen
 * @create 2015-5-13
 * @update 2015-5-13
 */

public class Message {
    Card myCard1; //����1
    Card myCard2; //����2
    Card []publicCard; //������
    int publicCardNum; //����������
    ActionMsg[] globalMsg; //����ȫ�־�����Ϣ
    int activePlayerNum; //��ǰ������
}

/**
 * @decription ֽ�ƽṹ��
 * @author Haibin Chen
 * @create 2015-5-13
 * @update 2015-5-13
 */
class Card{
	Color color; // ��ɫ
	int point; // ����
}

/**
 * @decription �ж���Ϣ�ṹ��
 * @author Haibin Chen
 * @create 2015-5-13
 * @update 2015-5-13
 */
class ActionMsg{
	int playerId;  //���Id
	Action action; //����
	int jetton; //���еĶ�ע
	int money;  //ʣ����

}



