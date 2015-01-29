package reading;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class JaxbRegistry {

	@XmlElementDecl(name = "palavra")
	JAXBElement<Palavra> createPalavra(Palavra palavra) {
		return new JAXBElement<Palavra>(new QName(palavra.palavra),
				Palavra.class, palavra);
	}
}
