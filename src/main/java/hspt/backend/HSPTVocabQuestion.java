package hspt.backend;

/**
 * Question in Quiz
 * @author alex
 *
 */
public class HSPTVocabQuestion extends Question {
    int correctAnswer;
	Word [] answers;
	int index;
	int numFail;
	int numPass;
	
	HSPTVocabQuestion() {
		answers = new Word [HSPTVocabQuiz.NUM_ANSWERS];	
	}
	
    public String getWord() {
    	return answers[correctAnswer].word;
    }
    
    public int getIndex() {
    	return index;
    }
    
    public String[] getAnswers() {
    	String [] result = new String [HSPTVocabQuiz.NUM_ANSWERS];
    	int count = 0;
        for (Word answer: answers) {
        	result[count++] = answer.meaning;
        }
        
        return result;
    }
    
    public int getNumFail() {
    	return numFail;
    	
    }
    
    public int getNumPass() {
    	return numPass;
    	
    }
    
        
}
