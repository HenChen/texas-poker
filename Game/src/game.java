import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @decription TODO
 * @author Haibin Chen
 * @create 2015-5-13
 * @update 2015-5-13
 */

public class game {
    private final String name;
    private final int pId;
    private final String myIp;
    private final int myPort;
    private final String serverIp;
    private final int serverPort;
    private Socket client = null;
    private Reader receiver;
    private Writer sender;
    private IRobot rb;
    private int smallBlind; // 小盲注，最低加注的1/2

    public game(String serverIp, int serverPort, String clientIp,
	    int clientPort, int playerId) {
	this.pId = playerId;

	this.myIp = clientIp;

	this.myPort = clientPort;
	this.serverIp = serverIp;
	this.serverPort = serverPort;
	this.name = "playera";
	this.rb = new SimpleRobot();
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
	    // reg: pid pname eol
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
		client = new Socket(serverIp, serverPort, InetAddress
			.getByName(myIp), myPort);
		receiver = new InputStreamReader(client.getInputStream());
		sender = new PrintWriter(client.getOutputStream());
	    } catch (UnknownHostException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
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
	int pId = Integer.parseInt(args[4]);
	game g = new game(sIp, sPort, mIp, mPort, pId);
	boolean con = g.register();

	if (con) {
	    g.run();
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
	char[] msg_ch = new char[1000];
	Message msgInfo = new Message();
	msgInfo.active = true;
	try {
	    while (true) {
		receiver.read(msg_ch); // 接收
		MessageReader mr = new MessageReader();
		msgInfo = mr.readContent(msgInfo, msg_ch);
		MessageType mt = mr.readType(msg_ch);
		if (msgInfo.active && mt == MessageType.INQUIRE) {
		    String action_msg = rb.generateActionMessage(msgInfo);
		    if (action_msg.contains("all_in")
			    || action_msg.contains("fold")) {
			msgInfo.active = false;
		    }
		    sender.write(action_msg);
		    sender.flush();
		} else if (mt == MessageType.GAME_OVER) {
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

    public String getName() {
	return name;
    }

    public int getpId() {
	return pId;
    }
}
