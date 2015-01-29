package reading;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement
public class ContextoDaPalavra {

	@XmlTransient
	public List<ChaveCombinada> chaves = new ArrayList<ChaveCombinada>();

	@XmlValue
	String codigo;

	public ContextoDaPalavra() {

	}

	public ContextoDaPalavra(int codigo) {
		this.codigo = Integer.toString(codigo);
	}
}
