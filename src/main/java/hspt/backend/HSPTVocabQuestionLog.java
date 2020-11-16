package hspt.backend;

public class HSPTVocabQuestionLog {
	String question;
	String[] answers; // 3 wrong answers
	String givenAnswer;
	int timeSec;
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String[] getAnswers() {
		return answers;
	}
	public void setAnswers(String[] answers) {
		this.answers = answers;
	}
	public String getGivenAnswer() {
		return givenAnswer;
	}
	public void setGivenAnswer(String givenAnswer) {
		this.givenAnswer = givenAnswer;
	}
	public int getTimeSec() {
		return timeSec;
	}
	public void setTimeSec(int timeSec) {
		this.timeSec = timeSec;
	}
	
	
}
