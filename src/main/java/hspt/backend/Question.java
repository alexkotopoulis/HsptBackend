package hspt.backend;

import java.util.UUID;

/**
 * Question class for serialization in REST calls
 * @author alex
 *
 */
public class Question {
	protected String id;
	protected int numQuestions;
	
	public Question() {
		UUID uuid = UUID.randomUUID();
		id = uuid.toString();
		numQuestions = 30;
	}
}
