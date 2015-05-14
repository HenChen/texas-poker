
/**
 * @decription the interpretor of server message
 * @author Haibin Chen
 * @create 2015-5-13
 * @update 2015-5-13
 */


public class MessageReader {
    private final String SEAT_TAG = "seat";
    private final String GAME_OVER_TAG = "game-over";
    private final String BLIND_TAG = "blind/";
    private final String HOLD_TAG = "hold";
    private final String INQUIRE_TAG = "inquire";
    private final String FLOP_TAG = "flop";
    private final String RIVER_TAG = "river";
    private final String SHOW_TAG = "showdown";
    private final String POT_TAG = "pot-win";

    /**
     * @function get the type of message of meta message from server
     * @param: meta message received from server
     * @return:MessageType message type, if not compatible, return null
     * @create:2015-5-13
     * @update:
     */
    public MessageType readType(char msg[]) {
	String mstemp = String.valueOf(msg);
	if (mstemp.startsWith(SEAT_TAG)) {
	    return MessageType.SEAT_INFO;
	} else if (mstemp.startsWith(GAME_OVER_TAG)) {
	    return MessageType.GAME_OVER;
	} else if (mstemp.startsWith(this.BLIND_TAG)) {
	    return MessageType.BLIND;
	} else if (mstemp.startsWith(this.HOLD_TAG)) {
	    return MessageType.HOLD_HARD;
	} else if (mstemp.startsWith(this.INQUIRE_TAG)) {
	    return MessageType.INQUIRE;
	} else if (mstemp.startsWith(this.FLOP_TAG)) {
	    return MessageType.FLOG;
	} else if (mstemp.startsWith(this.RIVER_TAG)) {
	    return MessageType.RIVER;
	} else if (mstemp.startsWith(this.SHOW_TAG)) {
	    return MessageType.SHOWDOWN;
	} else if (mstemp.startsWith(this.POT_TAG)) {
	    return MessageType.POT_WIN;
	}
	return null;
    }

    /**
     * @function remove the begin tag and end tag of messge;
     * @param: meta message from server
     * @return:String message without tags;
     * @create:2015-5-13
     * @update:
     */
    public String removeTags(char msg[]) {
	String mstemp = String.valueOf(msg);
	int index1 = mstemp.indexOf("/");
	int index2 = mstemp.indexOf("/", index1 + 1);
	return mstemp.substring(index1 + 3, index2 - 1);
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
    public Message readContent(Message ms, char msg[]) {
	MessageType mt = this.readType(msg);
	Message newMs = ms;
	String content = this.removeTags(msg);
	int i = 0;
	String contentSlice[] = content.split("\n");
	if (mt != null) {
	    switch (mt) {
	    case BLIND:
		/*
		 * blind/ eol 
		 * (pid: bet eol)1-2 
		 * /blind eol
		 * 
		 * 收到盲注信息，表示新的一局开始， 1，清理消息 2，设置盲注大小 3，复位轮数
		 */
		ms.clear();
		ms.active = true;
		ms.blindPot = Integer.parseInt(contentSlice[0].substring(6)
			.trim()) * 2;
		break;
	    case HOLD_HARD:
		/*
		 * hold/ eol 
		 * color point eol 
		 * color point eol 
		 * /hold eol
		 * 收到手牌信息，将手牌信息保存
		 */
		ms.myCard1 = new Card(contentSlice[0]);
		ms.myCard2 = new Card(contentSlice[1]);
		break;
	    case INQUIRE:
		/*
		 * inquire/ eol 
		 * (pid jetton money bet blind | check | call |raise | all_in | fold eol)1-8 
		 * total pot: num eol 
		 * /inquire eol 
		 * 收到询问消息后，保存该轮的牌局信息
		 */
		 
		for (; i < contentSlice.length - 1; i++) {
		    ActionMsg am = new ActionMsg(contentSlice[i].split(" "));
		    am.turnId = ms.turnId;
		    ms.globalMsg.add(am);
		}
		ms.totalPot = Integer.parseInt(contentSlice[i].substring(11)
			.trim());

		break;
	    case FLOG:
		/*
		 * flop/ eol 
		 * color point eol 
		 * color point eol 
		 * color point eol
		 * /flop eol 收到公牌消息，保存公牌,并更新轮次,转牌，河牌同样处理
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
	}
	return ms;
    }

    /*
    public static void main(String args[]) {
	String msg = "inquire/ \n1001 500 4000 234 call \n1002 400 3000 250 raise \n1004 500 4000 500 all_in \ntotal pot: 1234 \n/inquire \n        ";
	System.out.println(msg);
	char ms[] = msg.toCharArray();
	MessageReader mr = new MessageReader();
	MessageType mt = mr.readType(ms);
	Message mes = new Message();
	Message mes1 = mr.readContent(mes, ms);

	System.out.println(mt.toString());
	String content = mr.removeTags(ms);
	System.out.println(content);
	String cs[] = content.split("\n");
    }*/

}
