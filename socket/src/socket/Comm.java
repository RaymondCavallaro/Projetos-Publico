package socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;

public class Comm extends LeitorDados {

	private LeitorDados leitor;
	private PortaComunicacao portaRemota;

	private void init(PortaComunicacao portaRemota,
			PortaComunicacao portaInterface) throws IOException {
		leitor = new LeitorDados();
		leitor.setPorta(portaRemota);
		setPorta(portaInterface);
		this.portaRemota = portaRemota;
	}

	protected void processa() {
	}

	@Override
	public String readLine() throws IOException {
		// Le interface
		super.readLine();
		// Escreve remoto
		portaRemota.getPrintWriter().println(line);
		return line;
	}

	@Override
	public Thread start() {
		leitor.start();
		return super.start();
	}

	public static void main(String[] args) {
		try {
			URL url = new URL("http://189.33.165.175:8081");
			SocketAddress addr = new InetSocketAddress("proxyjb.planum.corp",
					3128);
			Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);
			url.openConnection(proxy);
			PortaComunicacao portaRemota = new PortaComunicacao();
			portaRemota.setIs(url.openStream());
			PortaComunicacao portaInterface = new PortaComunicacao();
			portaInterface.setIs(System.in);
			Comm comm = new Comm();
			comm.setLoggerHandler(System.out);
			comm.init(portaRemota, portaInterface);
			comm.startAndWait();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}