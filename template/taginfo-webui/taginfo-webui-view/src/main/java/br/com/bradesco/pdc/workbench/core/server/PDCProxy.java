/*     */ package br.com.bradesco.pdc.workbench.core.server;

/*     */
import java.util.Properties;

/*     */
import javax.naming.InitialContext;
/*     */
import javax.naming.NamingException;

/*     */
/*     */public class PDCProxy
/*     */{
	/*     */private static final String SUN_INITIAL_CONTEXT_FACTORY = "com.sun.jndi.cosnaming.CNCtxFactory";
	/*     */private static final String JBOSS_INITIAL_CONTEXT_FACTORY = "org.jnp.interfaces.NamingContextFactory";
	/*     */private static final String JBOSS_URL_PKG_PREFIXES = "org.jboss.naming";

	/*     */
	/*     */public PDCProxy() {
	}

	/*     */
	/*     */protected Object lookup(String server, String port, String node,
			String serverName, String clusterName, boolean isCluster,
			boolean isWas, String ejbName) throws NamingException
	/*     */
	/*     */{
		/* 52 */Object object = null;
		/* 53 */InitialContext ic = null;
		/*     */
		/*     */try
		/*     */{
			/* 57 */Properties properties = new Properties();
			/* 58 */properties.setProperty("java.naming.provider.url",
					getUrlProvider(server, port, isWas));
			/* 59 */if (isWas) {
				/* 60 */properties.setProperty("java.naming.factory.initial",
				/* 61 */"com.sun.jndi.cosnaming.CNCtxFactory");
				/*     */} else {
				/* 63 */properties.setProperty("java.naming.factory.initial",
				/* 64 */"org.jnp.interfaces.NamingContextFactory");
				/* 65 */properties.setProperty("java.naming.factory.url.pkgs",
						"org.jboss.naming");
				/*     */}
			/*     */
			/* 68 */ic = new InitialContext(properties);
			/*     */
			/*     */
			/* 71 */object =
			/* 72 */ic.lookup(getJNDIName(node, serverName, clusterName,
			/* 73 */isCluster, isWas, ejbName));
			/*     */} finally {
			/*     */try {
				/* 80 */ic.close();
				/*     */} catch (Throwable th) {
				/* 82 */th.printStackTrace();
				/*     */}
			/*     */}
		/*     */
		/* 86 */return object;
		/*     */}
	/*     */
	/*     */protected String getJNDIName(String node, String serverName,
			String clusterName, boolean isCluster, boolean isWas, String name)
	/*     */{
		/* 110 */StringBuffer sbf = new StringBuffer();
		/* 111 */if (isWas) {
			/* 112 */sbf.append("cell/");
			/* 113 */if (isCluster) {
				/* 114 */sbf.append("clusters/");
				/* 115 */sbf.append(clusterName);
				/*     */} else {
				/* 117 */sbf.append("nodes/");
				/* 118 */sbf.append(node);
				/* 119 */sbf.append("/servers/");
				/* 120 */sbf.append(serverName);
				/*     */}
			/*     */} else {
			/* 123 */int index = name.indexOf("/");
			/* 124 */if (index == 0) {
				/* 125 */name = name.substring(index + 1);
				/*     */}
			/*     */}
		/* 128 */sbf.append(name);
		/*     */
		/* 130 */return sbf.toString();
		/*     */}

	/*     */
	/*     */
	/*     */
	/*     */
	/*     */
	/*     */
	/*     */
	/*     */
	/*     */
	/*     */
	/*     */
	/*     */protected String getUrlProvider(String server, String port,
			boolean isWas)
	/*     */{
		/* 145 */String url = null;
		/* 146 */if (isWas) {
			/* 147 */url = "corbaname:iiop:" + server + ":" + port;
			/*     */} else {
			/* 149 */url = server + ":" + port + "/java.channel";
			/*     */}
		/* 151 */return url;
		/*     */}
	/*     */
}

/*
 * Location:
 * C:\development\workspaces\lege\local\br.com.bradesco.pdc.workbench.core_2.2.0
 * Qualified Name: br.com.bradesco.pdc.workbench.core.server.PDCProxy Java Class
 * Version: 5 (49.0) JD-Core Version: 0.7.0.1
 */