package hspt.backend;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

/**
 * Exception class for errors during login
 * @author alex
 *
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class HSPTLoginException extends RuntimeException {
    public HSPTLoginException(String user) {
    	super("Invalid Login: " + user);       
    }
}
