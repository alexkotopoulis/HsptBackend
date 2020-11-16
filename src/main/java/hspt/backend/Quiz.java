package hspt.backend;

import java.util.UUID;

public class Quiz {
	String id;
	int numQuestions;
	int numPass;
	int numFail;
	
	public Quiz() {
		UUID uuid = UUID.randomUUID();
		id = uuid.toString();
		numQuestions = 30;
		numPass = 0;
		numFail = 0;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getNumQuestions() {
		return numQuestions;
	}

	public void setNumQuestions(int numQuestions) {
		this.numQuestions = numQuestions;
	}
	
	public int getNumPass() {
		return numPass;
	}
	
	public int getNumFail() {
		return numFail;
	}
}
