package br.com.bradesco.awbhelper.proxycache;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;

import br.com.bradesco.pdc.controller.Controller;
import br.com.bradesco.pdc.controller.ControllerHome;
import br.com.bradesco.pdc.controller.container.ExecuteProcessRequest;
import br.com.bradesco.pdc.controller.container.NewSessionRequest;
import br.com.bradesco.pdc.controller.container.NewSessionResponse;

public class ProxyCache {

	public static void main(String[] args) {
		Properties env = new Properties();
		// env.setProperty("org.omg.CORBA.ORBClass", "com.ibm.CORBA.iiop.ORB");
		// env.setProperty("java.naming.factory.initial",
		// "com.ibm.websphere.naming.WsnInitialContextFactory");
		// env.setProperty("java.naming.provider.url",
		// "iiop://192.168.240.47:2809");
		env.setProperty("java.naming.provider.url",
				"corbaname:iiop:192.168.240.47:2809");
		// env.setProperty("java.naming.provider.url",
		// "corbaname:iiop:192.168.240.47:9810");
		env.setProperty("java.naming.factory.initial",
				"com.sun.jndi.cosnaming.CNCtxFactory");

		try {
			Context ctx = new InitialContext(env);
			ControllerHome controllerHome = (ControllerHome) ctx
					.lookup("cell/clusters/wasclusprd1/EJB/Session/Controller");
//			System.out.println(controllerHome);
			// wasclusprd1
			// ad-fl-ap-002 e ad-fl-ap-003, server: wasasprd1a e wasasprd1b
			// cell/nodes/localhostNode01/servers/server1/EJB/Session/Controller
			// EJB/Session/Local/Connector
			Controller controller = (Controller) controllerHome.create();
//			System.out.println(controller);

//			RepositoryAdministrationHome repoHome = (RepositoryAdministrationHome) ctx
//					.lookup("cell/clusters/wasclusprd1/EJB/Session/RepositoryAdministration");
//			System.out.println(repoHome);

//			RepositoryAdministration repo = (RepositoryAdministration) repoHome.create();
//			System.out.println(repo);

//			System.out.println(repo.getProduct("Intranet"));
//			System.out.println(repo.getProcesses("intr.","Intranet"));

			NewSessionRequest paramNewSessionRequest = new NewSessionRequest();
			paramNewSessionRequest.setLanguage("Português");
			paramNewSessionRequest.setProduct("Intranet");

			NewSessionResponse response = controller.newSession(paramNewSessionRequest);
			System.out.println(response.getSessionId());

			ExecuteProcessRequest paramExecuteProcessRequest = new ExecuteProcessRequest();
			paramExecuteProcessRequest.setProcessName("intr");

//			response = controller.executeProcess(paramExecuteProcessRequest)

//			System.out.println(repo.getProcesses("lege.convivencia.","Intranet"));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
