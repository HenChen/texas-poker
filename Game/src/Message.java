public class Message {
    Card myCard1; //����1
    Card myCard2; //����2
    Card []publicCard; //������
    int publicCardNum; //����������
    ActionMsg[] globalMsg; //����ȫ�־�����Ϣ
    int activePlayerNum; //��ǰ������
}
class Card{
	Color color; // ��ɫ
	int point; // ����
}
class ActionMsg{
	int playerId;  //���Id
	Action action; //����
	int jetton; //���еĶ�ע
	int money;  //ʣ����

}



