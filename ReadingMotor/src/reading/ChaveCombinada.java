package reading;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class ChaveCombinada {

	private Palavra palavra;

	private ContextoDaPalavra contexto;

	private Signo signo;

	public Palavra getPalavra() {
		return palavra;
	}

	public void setPalavra(Palavra palavra) {
		this.palavra = palavra;
	}

	public ContextoDaPalavra getContexto() {
		return contexto;
	}

	public void setContexto(ContextoDaPalavra contexto) {
		this.contexto = contexto;
	}

	public Signo getSigno() {
		return signo;
	}

	public void setSigno(Signo signo) {
		this.signo = signo;
	}

}
