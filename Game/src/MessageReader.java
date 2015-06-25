import java.util.ArrayList;
import java.util.Hashtable;


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
    private IRobot gameRobot;
    public void setRobot(IRobot robot){
	this.gameRobot = robot;
    }
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
		     //记录可用赌注
		     String myPid = String.valueOf(ms.myPid);
		     for(i=0;i<contentSlice.length;i++){
			 if(contentSlice[i].contains(": "+myPid)||(contentSlice[i].contains(myPid+" ")&&contentSlice[i].indexOf(myPid)==0)){
			     System.out.println("|"+contentSlice[i]+"|");
			     int bi = contentSlice[i].indexOf(" ",contentSlice[i].indexOf(myPid))+1;
			     int ei = contentSlice[i].indexOf(" ",bi);
			     ms.jetton = Integer.parseInt(contentSlice[i].substring(bi,ei).trim());
			     System.out.println(ms.jetton);
			     int ci = contentSlice[i].indexOf(" ",ei+1);
			     System.out.println(ei+"|"+ci);
			     ms.money = Integer.parseInt(contentSlice[i].substring(ei+1,ci).trim());
			 }else{
			     //初始化rival模型；
			     int idStartIndex = contentSlice[i].indexOf(":")+1;
			     int idEndIndex = contentSlice[i].indexOf(" ",idStartIndex+1);
			     String id = contentSlice[i].substring(idStartIndex,idEndIndex).trim();
			     if(!ms.rivalModel.containsKey(id)){
				 ms.rivalModel.put(id, new RivalStrength());
			     }
			 }
		     }
		     break;
		case BLIND:
		    /*
		     * blind/ eol (pid: bet eol)1-2 /blind eol
		     * 
		     * 收到盲注信息，表示新的一局开始， 1，清理消息 2，设置盲注大小 3，复位轮数
		     */
		    ms.clear();
		    ms.active = true;
		    int si = contentSlice[0].indexOf(":");
		    
		    String blindStr = contentSlice[0].substring(si+1).trim();
		    ms.blindPot = Integer.parseInt(blindStr) * 2;
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
		     System.out.println(strMsg.substring(next));
		    /*
		     * inquire/ eol 
		     * (pid jetton money bet blind | check | call
		     * |raise | all_in | fold eol)1-8 total pot: num eol
		     * /inquire eol 收到询问消息后，保存该轮的牌局信息
		     */
		    
		    for (; i < contentSlice.length - 1; i++) {
			ActionMsg am = new ActionMsg(contentSlice[i].split(" "));
			if(am.playerId.compareTo(ms.myPid)==0){
			    ms.jetton = am.jetton;
			    ms.money = am.money;
			}
			am.turnId = ms.turnId;
			ms.globalMsg.add(am);
		    }
		    ms.totalPot = Integer.parseInt(contentSlice[i]
			    .substring(11).trim());
		    this.getLeastCall(ms); 
		    System.out.println("mybet:"+ms.bet);
		    System.out.println("leastCall:"+ms.leastCall);
		    System.out.println("my jetton"+ms.jetton);
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
		case SHOWDOWN:
		     System.out.println("pot-win/");
		     System.out.println(content);
		     System.out.println("/pot-win");
		     Hashtable<String,ArrayList<Card>> cards = new Hashtable<String,ArrayList<Card>>();
		     for(String s:contentSlice){
			 if(s.contains(":")){
			     int idStartIndex = s.indexOf(":")+1;
			     int idEndIndex = s.indexOf(" ",idStartIndex+1);
			     String id = s.substring(idStartIndex,idEndIndex).trim();
			     
			     ArrayList<Card> handCard = new ArrayList<Card>();
			     int colorEndIndex = s.indexOf(" ",idEndIndex+1);
			     String colorStr = s.substring(idEndIndex,colorEndIndex).trim();
			     Color color1 = Color.valueOf(colorStr);
			     int pointEndIndex = s.indexOf(" ",colorEndIndex+1);
			     String pointStr = s.substring(colorEndIndex,pointEndIndex).trim();
			     Point point1 = Point.getFromStr(pointStr);
			     handCard.add(new Card(color1,point1));
			     
			     colorEndIndex = s.indexOf(" ",pointEndIndex+1);
			     colorStr = s.substring(pointEndIndex,colorEndIndex).trim();
			     Color color2 = Color.valueOf(colorStr);
			     pointEndIndex = s.indexOf(" ",colorEndIndex+1);
			     pointStr = s.substring(colorEndIndex,pointEndIndex).trim();
			     Point point2 = Point.getFromStr(pointStr);
			     handCard.add(new Card(color2,point2));
			     cards.put(id, handCard);
			 }
		     }
		     renewRivalModel(ms,cards);
		default:
		    break;
		}
		next = readMessage(strMsg, next);
	    }
	}
	return mt;
    }
    /**
     * @function 更新对手的debuff值
     * @param: MessageReader
     * @return:void
     * @create:2015-6-12
     * @update:
     */
    private void renewRivalModel(Message ms,Hashtable<String,ArrayList<Card>> cards){
	double raiseStep = 0.02;
	double maxRaise = 0.3;
	double maxAll_in = 0.4;
	double all_inStep = 0.05;
	
	if(cards.size()>0){
	    ArrayList<ActionMsg> actionSeq = ms.getActionSequnceAt(ms.turnId);
	    for(String s:cards.keySet()){
		if(s!=ms.myPid){ 
		    //获取对手的胜率值
		    Message temp = new Message();
		    temp.publicCard = ms.publicCard;
		    temp.myCard1 = cards.get(s).get(0);
		    temp.myCard2 = cards.get(s).get(1);
		    double hisRate = this.gameRobot.getWinRate(temp);
		    for(ActionMsg am:actionSeq){
			 if(am.playerId.compareTo(s)== 0){
			     if(am.action == Action.RAISE && am.playerId.compareTo(ms.myPid)!=0){
				 double debuff = ms.rivalModel.get(s).raise;
				 Range range = expectRate(debuff,am.action);
				 RivalStrength rs = ms.rivalModel.get(s);
				 if(hisRate > range.end){
				      if(rs.raise<maxRaise-raiseStep){
					  rs.raise = rs.raise+ raiseStep;
				      }
				      ms.rivalModel.put(s,rs);
				 }else if (hisRate < range.start){   
				      if(rs.raise>raiseStep){
					  rs.raise = rs.raise - raiseStep;
				      }
				      ms.rivalModel.put(s,rs);
				 }
			     }else if(am.action == Action.ALL_IN){
				 double debuff = ms.rivalModel.get(s).all_in;
				 Range range = expectRate(debuff,am.action);
				 RivalStrength rs = ms.rivalModel.get(s);
				 if(hisRate > range.end){
				      if(rs.all_in < maxAll_in-all_inStep){
					  rs.all_in = rs.all_in+ all_inStep;
				      }
				      ms.rivalModel.put(s,rs);
				 }else if (hisRate < range.start){   
				      if(rs.all_in>all_inStep){
					  rs.all_in = rs.all_in - all_inStep;
				      }
				      ms.rivalModel.put(s,rs);
				 }
			     }
			 }
		    }
		}
	    }
	}
    }
    class Range{
	double start;
	double end;
    }
    private Range expectRate(double debuff,Action action){
	Range rg = new Range();
	double sa=0.3,sb=0.85;
	double ea=0.3,eb=0.88;
	
	rg.start = 0;
	rg.end = 1;
	if(action == Action.ALL_IN|| action ==Action.RAISE){
		rg.start = debuff *sa+sb;
		rg.end = debuff *ea + eb;
	}
	return rg;
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
     /**
      * 计算当前跟注的代价和自己当轮累计下注额度
      * @function TODO
      * @param: Message 
      * @return:void
      * @create:2015-5-23
      * @update:
      */
    private void getLeastCall(Message msg){
         int i =0;
         while(i<msg.globalMsg.size() && msg.globalMsg.get(i).turnId!=msg.turnId){
             i++;
         }
         int maxBet=0,myMaxBet = 0;
         while(i<msg.globalMsg.size()){
             if(msg.globalMsg.get(i).bet>maxBet){
        	 maxBet = msg.globalMsg.get(i).bet;
             }
             if(msg.globalMsg.get(i).playerId.compareTo(msg.myPid)==0 ){
        	 if(msg.globalMsg.get(i).bet>myMaxBet){
        	     myMaxBet = msg.globalMsg.get(i).bet;
        	 }
             }
             i++;
         }
         msg.bet = myMaxBet;
         msg.leastCall = maxBet- myMaxBet;
    }
    
    /*public static void main(String args[]) {
	String msg = "inquire/ \n"+
	"\n4444 2000 8000 0 fold \n"+ 
	"2222 1900 6000 100 call \n"+ 
	"7777 2000 6000 0 fold \n"+ 
	"1111 1900 6000 100 blind \n"+
	"5555 1950 6000 50 blind \n"+
	"total pot: 250 \n"+
	"/inquire \n";
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
