package hspt.backend;

public class HSPTVocabQuestionFeedback {
	Word answer;
	int choice;	
	
	public int getChoice() {
		return choice;
	}
	public void setChoice(int choice) {
		this.choice = choice;
	}
	public int getCorrectChoice() {
		return correctChoice;
	}
	public void setCorrectChoice(int correctChoice) {
		this.correctChoice = correctChoice;
	}
	int correctChoice;
}
