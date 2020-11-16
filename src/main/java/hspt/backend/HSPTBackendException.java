package hspt.backend;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

/**
 * Backend Exception class for generic backend errors
 * @author alex
 *
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class HSPTBackendException extends RuntimeException {
	private static final long serialVersionUID = 4763565038539628217L;

	public HSPTBackendException(String user) {
    	super("Error in Backend: " + user);       
    }
}
