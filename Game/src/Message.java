import java.util.ArrayList;

/**
 * @decription ��Ϣ�ṹ�壬�������ƣ��������Լ�������ҵ��ֵ��ж���Ϣ
 * @author Haibin Chen
 * @create 2015-5-13
 * @update 2015-5-13 18:45
 */


public class Message {
    Card myCard1; // ����1
    Card myCard2; // ����2
    ArrayList<Card> publicCard; // ������ ���� ת�ƣ�����
    ArrayList<ActionMsg> globalMsg; // ����ȫ�־�����Ϣ
    int totalPot;
    int blindPot; // äע
    int turnId; // ���ֱ�������
    boolean active;// when all in and fold, it is false, otherwise it is true;

    public Message() {
	myCard1 = new Card();
	myCard2 = new Card();
	publicCard = new ArrayList<Card>();
	globalMsg = new ArrayList<ActionMsg>();
	totalPot = 0;
    }

    public void clear() {
	publicCard.clear();
	globalMsg.clear();
	totalPot = 0;
	turnId = 0;
    }
}

/**
 * @decription ֽ�ƽṹ��
 * @author Haibin Chen
 * @create 2015-5-13
 * @update 2015-5-13
 */
class Card {
    Color color; // ��ɫ
    String point; // ����

    public Card() {
    }

    public Card(String cardInfo) {
	String temp[] = cardInfo.split(" ");
	color = Color.valueOf(temp[0].trim());
	point = temp[1].trim();
    }
}

/**
 * @decription �ж���Ϣ�ṹ��
 * @author Haibin Chen
 * @create 2015-5-13
 * @update 2015-5-13
 */
class ActionMsg {
    public ActionMsg(String[] temp) {
	// TODO Auto-generated constructor stub
	//pid jetton money bet blind | check | call | raise | all_in | fold 
	playerId = Integer.parseInt(temp[0]);
	jetton = Integer.parseInt(temp[1]);
	money = Integer.parseInt(temp[2]);
	bet = Integer.parseInt(temp[3]);
	action = Action.getActionType(temp[4]);
    }
    int playerId; // ���Id
    Action action; // ����
    int jetton; // ���еĶ�ע
    int money; // ʣ����
    int bet; // �������ۼ�Ͷע��
    int turnId; // �ִ�
}
