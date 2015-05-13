import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
	private BufferedReader receiver;
	private PrintWriter sender;
	private IRobot rb;

	public game(String serverIp, int serverPort, String clientIp,
			int clientPort, int playerId) {
		this.pId = playerId;

		this.myIp = clientIp;

		this.myPort = clientPort;
		this.serverIp = serverIp;
		this.serverPort = serverPort;
		this.name = "playera";
	}

    /**
     * @function ����������Ϸ���ԵĻ�����
     * @param:
     * @return:void
     * @create:2015-5-13
     * @update:
     */
    public void setRobot(IRobot robot) {
	this.rb = robot;
    }

	public boolean register() {
		boolean con=true;
		if (client == null) {
           con =connectServer();
		}
		if(con){
			// reg: pid pname eol
			String rInfo = "reg: " + this.pId + " " + this.name + " \n";
			sender.write(rInfo);
		}
		return con;
	}

    /**
     * @function ���ӷ�����
     * @param:
     * @return:boolean ���ӳɹ�����true���򷵻�false;
     * @create:2015-5-13
     * @update:
     */
	private boolean connectServer() {
		if (client == null) {
			try {
				client = new Socket(serverIp, serverPort, InetAddress
						.getByName(myIp), myPort);
				receiver = new BufferedReader(new InputStreamReader(client.getInputStream()));
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
		System.out.println("hello world");
	}

    /**
     * @function ���е����˿���Ϸ�����������������
     * @param:
     * @return:void
     * @create:2015-5-13
     * @update:
     */
	private void run() {
		// TODO Auto-generated method stub
		char[] msg_ch = new char[3000];
		Message msgInfo = new Message();
		try {
			while (true) {
				receiver.read(msg_ch); // ����
				String msg_str = String.valueOf(msg_ch);

				if (msg_str.startsWith("seat")) { // �����������Ϣ

				} else if (msg_str.startsWith("game-over")) { // ��Ϸ������Ϣ

				} else if (msg_str.startsWith("blind")) { // äע��Ϣ

				} else if (msg_str.startsWith("inquire")) { // ѯ����Ϣ

				} else if (msg_str.startsWith("flop")) { // ������Ϣ

				} else if (msg_str.startsWith("turn")) { // ת����Ϣ

				} else if (msg_str.startsWith("river")) { // ������Ϣ

				} else if (msg_str.startsWith("showdown")) { // ̯����Ϣ

				} else if (msg_str.startsWith("pot-win")) { // �ʳط�����Ϣ

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

