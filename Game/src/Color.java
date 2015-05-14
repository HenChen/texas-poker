/**
 *@des: set of enum type value
 *@author: Chen Haibin
 *@create:2015.5.10
 *@update:2015.5.11 
 *@version:0.1
 * */


/***
 * �ƵĻ�ɫ
 */
public enum Color {
    SPADES, // ����
    HEARTS, // ����
    CLUBS, // ÷��
    DIAMONDS; // ����
}

/***
 * ���ͣ���С����
 */
enum Nut_Hand {
    HIGH_CARD, // ����
    ONE_PAIR, // һ��
    TWO_PAIR, // ����
    THREE_OF_A_KIND, // ����
    STRAIGHT, // ˳��
    FLUSH, // ͬ��
    FULL_HOUSE, // ��«
    FOUR_OF_A_KIND, // ����
    STRAIGHT_FLUSH; // ͬ��˳
}

/***
 *���ƶ�������
 */
enum Action {
    BLIND, // äע
    CHECK, // ����
    CALL, // ����
    RAISE, // ��ע
    ALL_IN, // ȫ��
    FOLD; // ����

    /**
     * @function: Transfer the string name of action to corresponding entity of
     *            enum type
     * @param: the name of Action used in message delivered between server and
     *         game
     * @return: return the corresponding Action,if the name are not compatible,
     *          return null
     * @create:2015-5-13
     * @update:2015-5-13
     */
    static Action getActionType(String act) {
	Action[] vs = Action.values();
	for (int i = 0; i < vs.length; i++) {
	    if (act.equalsIgnoreCase(vs[i].toString())) {
		return vs[i];
	    }
	}
	return null;
    }
}

/***
 *���ͺͽ�����Ϣ������
 */
enum MessageType {
    REG, // ע����Ϣ
    SEAT_INFO, // ������Ϣ
    GAME_OVER, // ��Ϸ������Ϣ
    BLIND, // äע��Ϣ
    HOLD_HARD, // ������Ϣ
    INQUIRE, // ѯ����Ϣ
    ACTION, // �ж���Ϣ
    FLOG, // ������Ϣ
    TURN, // ת����Ϣ
    RIVER, // ������Ϣ
    SHOWDOWN, // ̯����Ϣ
    POT_WIN; // �ʳط�����Ϣ
}
