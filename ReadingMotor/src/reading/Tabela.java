package reading;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.namespace.QName;

public class Tabela {

	@XmlElement(name="item")
	private static List<ChaveCombinada> todasChaves = new ArrayList<ChaveCombinada>();
	
	public static <T extends Class> void grava(Object objeto, T... clazzes)
			throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(clazzes);
		JAXBElement<T> jaxbElement = new JAXBElement(new QName(objeto
				.getClass().getName()), objeto.getClass(), objeto);
		Marshaller marshaller = context.createMarshaller();
		// JAXBSource sourceA = new JAXBSource(contextA, jaxbElementA);
		marshaller.marshal(jaxbElement, new File("teste.xml"));
	}

	public static void main(String[] args) {
		Tabela tabela = new Tabela();
		tabela.cria("teste", new ContextoDaPalavra(1), new Signo(1));
		try {
			grava(tabela, ArrayList.class, HashMap.class, JaxbRegistry.class,
					Tabela.class, ContextoDaPalavra.class, Signo.class);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		System.out.println("ok");

		// palavra = le();
	}

	private void cria(String palavra, ContextoDaPalavra contextoDaPalavra,
			Signo signo) {
		ChaveCombinada novaChave = new ChaveCombinada();
		Palavra p = new Palavra();
		p.palavra = palavra;
		novaChave.setPalavra(p);
		novaChave.setContexto(contextoDaPalavra);
		novaChave.setSigno(signo);
		p.chaves.add(novaChave);
		contextoDaPalavra.chaves.add(novaChave);
		signo.chaves.add(novaChave);
		todasChaves.add(novaChave);
	}
}
