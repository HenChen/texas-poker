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
	SPADES,   //����
	HEARTS,   //����
	CLUBS,    //÷��
	DIAMONDS; //����
}

/***
 * ���ͣ���С����
 */
 enum Nut_Hand{
	 HIGH_CARD,      //����
	 ONE_PAIR,       //һ��
	 TWO_PAIR,       //����
	 THREE_OF_A_KIND,//����
	 STRAIGHT,       //˳��
	 FLUSH,          //ͬ��
	 FULL_HOUSE,     //��«
	 FOUR_OF_A_KIND, //����
	 STRAIGHT_FLUSH; //ͬ��˳
 }
 
 /***
  *���ƶ�������
  */
 enum Action{
	 BLIND,  //äע
	 CHECK,  //����
	 CALL,   //����
	 RAISE,  //��ע
	 ALL_IN, //ȫ��
     FOLD;   //����
 }
 /***
  *���ͺͽ�����Ϣ������
  */
 enum MessageType{
	 REG,        //ע����Ϣ
	 SEAT_INFO,  //������Ϣ
	 GAME_OVER,  //��Ϸ������Ϣ
	 BLIND,      //äע��Ϣ
	 HOLD_HARD,  //������Ϣ
	 INQUIRE,    //ѯ����Ϣ
	 ACTION,     //�ж���Ϣ
	 FLOG,       //������Ϣ
	 TURN,       //ת����Ϣ
	 RIVER,      //������Ϣ
	 SHOWDOWN,   //̯����Ϣ
	 POT_WIN;    //�ʳط�����Ϣ
 }

