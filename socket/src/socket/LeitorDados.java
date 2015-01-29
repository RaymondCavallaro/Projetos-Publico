package socket;

import java.io.IOException;

public class LeitorDados extends RunOver {

	protected boolean logaLinha;
	protected String line = "";
	protected PortaComunicacao porta;

	@Override
	public void run() {
		try {
			if (readLine() != null) {
				if (logaLinha) {
					logger.info(line);
				}
				processa();
			}
		} catch (IOException e) {
			line = null;
		}
	}

	protected void processa() {
	}

	public String readLine() throws IOException {
		return line = getPorta().getBufferedReader().readLine();
	}

	@Override
	public boolean getRunOverCondition() {
		return line != null;
	}

	public PortaComunicacao getPorta() {
		return porta;
	}

	public void setPorta(PortaComunicacao porta) {
		this.porta = porta;
	}

}