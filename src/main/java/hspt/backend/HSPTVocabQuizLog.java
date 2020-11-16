package hspt.backend;

import java.util.ArrayList;
import java.util.List;

public class HSPTVocabQuizLog {
	List<HSPTVocabQuestionLog> questions = new ArrayList<HSPTVocabQuestionLog>(); 
	String timeStart;
	int timeSec;
	
	HSPTUser user;

	public List<HSPTVocabQuestionLog> getQuestions() {
		return questions;
	}

	public void setQuestions(List<HSPTVocabQuestionLog> questions) {
		this.questions = questions;
	}

	public String getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(String timeStart) {
		this.timeStart = timeStart;
	}

	public int getTimeSec() {
		return timeSec;
	}

	public void setTimeSec(int timeSec) {
		this.timeSec = timeSec;
	}

	public HSPTUser getUser() {
		return user;
	}

	public void setUser(HSPTUser user) {
		this.user = user;
	}
	
	
}
