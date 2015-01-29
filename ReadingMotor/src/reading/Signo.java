package reading;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement
public class Signo {

	@XmlTransient
	public List<ChaveCombinada> chaves = new ArrayList<ChaveCombinada>();

	@XmlValue
	String codigo;

	public Signo() {

	}

	public Signo(int codigo) {
		this.codigo = Integer.toString(codigo);
	}

}
