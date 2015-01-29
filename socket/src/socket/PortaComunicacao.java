package socket;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

public class PortaComunicacao extends LoggedObjectBase {

	protected Object recurso;
	protected InputStream is;
	protected OutputStream os;

	public BufferedReader getBufferedReader() {
		return new BufferedReader(new InputStreamReader(is));
	}

	public PrintWriter getPrintWriter() {
		return new PrintWriter(os, true);
	}

	public InputStream getIs() {
		return is;
	}

	public void setIs(InputStream is) {
		this.is = is;
	}

	public OutputStream getOs() {
		return os;
	}

	public void setOs(OutputStream os) {
		this.os = os;
	}

	@SuppressWarnings("unchecked")
	public <Recurso> Recurso getRecurso() {
		return (Recurso) recurso;
	}

	public void setRecurso(Object recurso) {
		this.recurso = recurso;
	}

}