import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;


/**
 * @decription TODO
 * @author Haibin Chen
 * @create 2015-5-13
 * @update 2015-5-13
 */

public class game {
    private final String name;
    private final String pId;
    private final String myIp;
    private final int myPort;
    private final String serverIp;
    private final int serverPort;
    private Socket client = null;
    private Reader receiver;
    private Writer sender;
    private IRobot rb;

    public game(String serverIp, int serverPort, String clientIp,
	    int clientPort, String playerId) {
	this.pId = playerId;
	this.myIp = clientIp;
	this.myPort = clientPort;
	this.serverIp = serverIp;
	this.serverPort = serverPort;
	this.name = "playera";
	this.rb = new SimplePredictRobot();
    }

    /**
     * @function 设置生成游戏策略的机器人
     * @param:
     * @return:void
     * @create:2015-5-13
     * @update:
     */
    public void setRobot(IRobot robot) {
	this.rb = robot;
    }

    public boolean register() {
	boolean con = true;
	if (client == null) {
	    con = connectServer();
	}
	if (con) {
	    String rInfo = "reg: " + this.pId + " " + this.name + " \n";
	    try {
		sender.write(rInfo);
		sender.flush();
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		con = false;
		e.printStackTrace();
	    }
	}
	return con;
    }

    /**
     * @function 链接服务器
     * @param:
     * @return:boolean 连接成功返回true否则返回false;
     * @create:2015-5-13
     * @update:
     */
    private boolean connectServer() {
	if (client == null) {
	    try {
		client = new Socket();//(serverIp, serverPort);
		client.setReuseAddress(true);
		if(!client.isBound()){
		    client.bind(new InetSocketAddress(InetAddress.getByName(myIp),myPort));
		}
		client.connect(new InetSocketAddress(InetAddress.getByName(serverIp),serverPort));
		receiver = new InputStreamReader(client.getInputStream());
		sender = new PrintWriter(client.getOutputStream());
	    } catch (UnknownHostException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return false;
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return false;
	    }
	}
	return true;
    }
 
    public static void main(String args[]) {
	System.out.println(args.length);
	if (args.length < 5) {
	    System.out.println((5 - args.length)
		    + " more paramters are expcecting..\n");
	    return;
	}
	String sIp = args[0];
	
	int sPort = Integer.parseInt(args[1]);
	String mIp = args[2];
	int mPort = Integer.parseInt(args[3]);
	String pId = args[4].trim();
	game g = new game(sIp, sPort, mIp, mPort, pId);
	
	boolean con = g.register();

	if (con) {
	    g.run();
	}
    }

    public void clear(char[] buffer) {
	for (int i = 0; i < buffer.length; i++) {
	    buffer[i] = '\0';
	}
    }
    /**
     * @function 进行德州扑克游戏比赛，与服务器交互
     * @param:
     * @return:void
     * @create:2015-5-13
     * @update:
     */
    public void run() {
	// TODO Auto-generated method stub
	char[] buffer = new char[1000];
	Message msgInfo = new Message();
	msgInfo.myPid = this.pId;
	msgInfo.active = true;
	try {
	    while (true) {
		clear(buffer);
		receiver.read(buffer); // 接收
		MessageReader mr = new MessageReader();	
		MessageType mt = mr.readContent(msgInfo, buffer);
		if (msgInfo.active && mt == MessageType.INQUIRE) {
		    String action_msg = generateActionMessage(msgInfo);
		    if (action_msg.contains("all_in")
			    || action_msg.contains("fold")) {
			msgInfo.active = false;
		    }
		    System.out.println("sending to server: " + action_msg);
		    sender.write(action_msg);

		    sender.flush();
		} else if (mt == MessageType.GAME_OVER) {
		    System.out.println(this.pId + " is going to quit....\n");
		    receiver.close();
		    sender.close();
		    client.close();
		  
		    break;
		}
	    }
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

    /**
     * 行动消息：check | call | raise num | all_in | fold eol
     * 
     * @function 生成行动消息
     * @param: IRobot
     * @return:String
     * @create:2015-5-13
     * @update:
     */
    public String generateActionMessage(Message msg) {
	// TODO Auto-generated method stub
	ArrayList<Double> decision = rb.messageHandle(msg);
	for (int i = 1; i < decision.size(); i++) {
	    decision.set(i, decision.get(i) + decision.get(i - 1));
	}
	String actionNames[] = { "check", "call", "raise", "all_in", "fold" };
	String msgStr = "";
	Random dice = new Random();
	double cursor = dice.nextDouble();
	int action = 0;
	for (; action < decision.size(); action++) {
	    if (cursor < decision.get(action)) {
		break;
	    }
	}
        System.out.println("dice: "+cursor+"   action"+action);
	msgStr = msgStr + actionNames[action];
	if (actionNames[action].equals("raise")) {
	    msgStr = msgStr + " " + (msg.blindPot * 2);
	}
	msgStr = msgStr + "\n";

	return msgStr;
    }
    public String getName() {
	return name;
    }

    public String getpId() {
	return pId;
    }
}
