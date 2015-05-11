import java.net.ServerSocket;
import java.net.Socket;


public class game {
	private final String name;
	private final int pId;
	private final String myIp;
	private final int myPort;
	private final String serverIp;
	private final int serverPort;
	private final ServerSocket server = null;
	private final Socket client = null;

	public game(String serverIp, int serverPort, String clientIp,
			int clientPort, int playerId) {
		this.pId = playerId;
		this.myIp = clientIp;
		this.myPort = clientPort;
		this.serverIp = serverIp;
		this.serverPort = serverPort;
		this.name = "player" + pId;
	}

	public boolean register() {
		return true;
	}

	public static void main(String args[]) {
		System.out.println("hello world");
	}

	public String getName() {
		return name;
	}

	public int getpId() {
		return pId;
	}
}

