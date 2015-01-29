package socket;

import java.io.OutputStream;
import java.util.Arrays;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

public class LoggedObjectBase {

	protected Logger logger;
	protected Formatter formatter = new SimpleFormatter();
	protected Level level = Level.ALL;

	public LoggedObjectBase() {
		logger = Logger.getLogger(this.getClass().getName());
	}

	public void setLoggerHandler(OutputStream os) {
		for (Handler h : Arrays.asList(logger.getHandlers())) {
			logger.removeHandler(h);
		}
		addLoggerHandler(os);
	}

	public void addLoggerHandler(OutputStream os) {
		logger.addHandler(new StreamHandler(os, formatter));
		logger.setLevel(level);
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public Formatter getDefaultFormatter() {
		return formatter;
	}

	public void setDefaultFormatter(Formatter formatter) {
		this.formatter = formatter;
	}

	public Level getDefaultLevel() {
		return level;
	}

	public void setDefaultLevel(Level level) {
		this.level = level;
	}
}