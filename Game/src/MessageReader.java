/**
 * @decription the interpretor of server message
 * @author Haibin Chen
 * @create 2015-5-13
 * @update 2015-5-13
 */
public class MessageReader {

    /**
     * @function get the type of message of meta message from server
     * @param: meta message received from server
     * @return:MessageType message type, if not compatible, return null
     * @create:2015-5-13
     * @update:
     */
    
    public MessageType readType(char msg[]) {
	return readType(String.valueOf(msg));
    }

    public MessageType readType(String message) {
	int endIndex = message.indexOf("/");
	if (endIndex == -1)
	    return null;
	String tag = message.substring(0, endIndex);

	return MessageType.getTypeFromTag(tag);
    }

    /**
     * @function remove the begin tag and end tag of messge;
     * @param: meta message from server
     * @return:String message without tags;
     * @create:2015-5-13
     * @update:
     */
    public String removeTags(String msg, String tag) {
	int index1 = msg.indexOf(tag) + tag.length();
	int index2 = msg.indexOf(tag, index1 + 1) - 1;
	// System.out.println(index1 + "  " + index2);
	if (index1 + 3 < index2 - 1)
	    return msg.substring(index1 + 3, index2 - 1);
	else {
	    return "";
	}
    }

    /**
     * @function update state of all the players through the meta message from
     *           server, and packet it into a new Message
     * @param: ms messge recording the state of all the players msg meta message
     *         received from server
     * @return:Message the updated Message
     * @create:2015-5-13
     * @update:
     */
    public MessageType readContent(Message ms, char msg[]) {
	String strMsg = String.valueOf(msg);
	if (strMsg.contains(MessageType.GAME_OVER.tag))
	    return MessageType.GAME_OVER;
	int next = 0;
	MessageType mt = null;
	while (next >= 0 && next < strMsg.length()) {
	    String message = strMsg.substring(next);

	    MessageType mtTemp = this.readType(message);
	    if (mtTemp != null)
		mt = mtTemp;
	    else {
		break;
	    }
	    String content = this.removeTags(message, mt.tag);

	    int i = 0;

	    String contentSlice[] = content.split("\n");
	    if (mt != null) {
		switch (mt) {
		case SEAT_INFO:
		     ms.numOfActivePlayers = contentSlice.length;
		     break;
		case BLIND:
		    /*
		     * blind/ eol (pid: bet eol)1-2 /blind eol
		     * 
		     * 收到盲注信息，表示新的一局开始， 1，清理消息 2，设置盲注大小 3，复位轮数
		     */
		    ms.clear();
		    ms.active = true;
		    ms.blindPot = Integer.parseInt(contentSlice[0].substring(6)
			    .trim()) * 2;
		    break;
		case HOLD_CARD:
		    /*
		     * hold/ eol color point eol color point eol /hold eol
		     * 收到手牌信息，将手牌信息保存
		     */
		    ms.myCard1 = new Card(contentSlice[0]);
		    ms.myCard2 = new Card(contentSlice[1]);
		    break;
		case INQUIRE:
		    /*
		     * inquire/ eol (pid jetton money bet blind | check | call
		     * |raise | all_in | fold eol)1-8 total pot: num eol
		     * /inquire eol 收到询问消息后，保存该轮的牌局信息
		     */
		    
		    for (; i < contentSlice.length - 1; i++) {
			ActionMsg am = new ActionMsg(contentSlice[i].split(" "));
			am.turnId = ms.turnId;
			ms.globalMsg.add(am);
		    }
		    ms.totalPot = Integer.parseInt(contentSlice[i]
			    .substring(11).trim());
		    System.out.println("");

		    break;
		case FLOG:
		    /*
		     * flop/ eol color point eol color point eol color point eol
		     * /flop eol 收到公牌消息，保存公牌,并更新轮次,转牌，河牌同样处理
		     * trunid = 0:盲注阶段
		     * turnid = 1:公牌阶段
		     * turnid = 2:转牌阶段
		     * turnid = 3:河牌阶段
		     */
		case TURN:
		case RIVER:
		    for (i = 0; i < contentSlice.length; i++) {
			ms.publicCard.add(new Card(contentSlice[i]));
		    }
		    ms.turnId++;
		    break;
		default:
		    break;
		}
		next = readMessage(strMsg, next);
	    }
	}
	return mt;
    }

    public int readMessage(String message, int index) {
	MessageType mt = readType(message.substring(index));
	if (mt != null) {
	    int endTag = message.indexOf(mt.tag, index + 1);
	    return endTag + mt.tag.length() + 2;
	} else {
	    return -1;
	}
    }
    /*
    public static void main(String args[]) {
	String msg = "seat/ \nbutton: 2222 2000 8000 \nsmall blind: 1000 2000 8000 \n/seat";
	// "blind/ \n1000: 50 \n/blind \nhold/ \nHEARTS 3 \nHEARTS 8 \n/hold \ninquire/ \n2222 2000 8000 0 fold \n1000 1950 8000 50 blind \ntotal pot: 50 \n/inquire";
	System.out.println(msg);
	char ms[] = msg.toCharArray();
	MessageReader mr = new MessageReader();
	MessageType mt = mr.readType(ms);
	Message mes = new Message();
	mt = mr.readContent(mes, ms);
	System.out.println(mt.toString());
	String content = mr.removeTags(String.valueOf(ms), mt.tag);
	System.out.println(content);
	String cs[] = content.split("\n");
    }*/

}
