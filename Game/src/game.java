import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


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
	private Robot rb;

	public game(String serverIp, int serverPort, String clientIp,
			int clientPort, int playerId) {
		this.pId = playerId;

		this.myIp = clientIp;

		this.myPort = clientPort;
		this.serverIp = serverIp;
		this.serverPort = serverPort;
		this.name = "playera";
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

	private void run() {
		// TODO Auto-generated method stub
		char[] msg = null;
		try {
			receiver.read(msg);
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

