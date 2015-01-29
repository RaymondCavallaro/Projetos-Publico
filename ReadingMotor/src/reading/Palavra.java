package reading;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement
public class Palavra {

	@XmlTransient
	public List<ChaveCombinada> chaves = new ArrayList<ChaveCombinada>();

	@XmlValue
	public String palavra;

	public Palavra() {

	}

}
