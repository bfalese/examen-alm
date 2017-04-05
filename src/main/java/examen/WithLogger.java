package examen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logging interface.
 * 
 * @author bernardo
 */
public interface WithLogger {

	default Logger logger() {
		return LoggerFactory.getLogger(getClass());
	}
}
