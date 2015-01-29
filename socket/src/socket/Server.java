package socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends RunOver {

	private ServerSocket server;

	@Override
	public void run() {
		try {
			// conecta
			Socket clientSocket = server.accept();
			logger.fine("nova conexao " + server + " > " + clientSocket);
//			Comm comm = new Comm(clientSocket);
			// broadcast
//			comm.setInputStream(System.in);
			// comunica
//			comm.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			Server server = new Server();
			server.setLoggerHandler(System.out);
			ServerSocket socket = new ServerSocket(8080);
			server.setServer(socket);
			server.getLogger().info("server criado " + socket);
			server.startAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ServerSocket getServer() {
		return server;
	}

	public void setServer(ServerSocket server) {
		this.server = server;
	}
}