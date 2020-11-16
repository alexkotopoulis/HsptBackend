package hspt.backend;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

/**
 * Quiz object with a list of questions and responses
 * @author alex
 *
 */
public class HSPTVocabQuiz extends Quiz {
	// static String filename="./hspt.csv";
	static final String BASE_PATH = "/tmp/hspt";

	static final int NUM_ANSWERS = 4;

	String propertyHome;

	List<Word> failedWordsQuiz;
	List<Word> words;
	Random rnd = new Random();
	Properties props = new Properties();
	int everFailPct;
	int failPct;
	int rarePct;
	int passPct;
	boolean isRandom;
	
	HSPTVocabQuizLog quizLog;
	HSPTVocabQuestionLog questionLog;
	HSPTUser user;
	
	List<Word> failedWords = new ArrayList<Word>(); // words for fail > pass
	List<Word> passedWords = new ArrayList<Word>(); // words for pass > 0, pass >= fail
	List<Word> rareWords = new ArrayList<Word>(); // words for pass >= fail and pass <= avgPass
	int zeroCountWords = 0;
	int count = 0;
	Word currentWord;

	List<Word> questions;
	int numCorrect = 0;
	HSPTVocabQuestion currentQuestion;
	long questionStartMs;
	long quizStartMs;

	public int getEverFailPct() {
		return everFailPct;
	}

	public int getFailPct() {
		return failPct;
	}

	public int getRarePct() {
		return rarePct;
	}

	public int getPassPct() {
		return passPct;
	}

	public int getTotalPass() {
		return passedWords.size();
	}

	public int getTotalFail() {
		return failedWords.size();
	}

	public int getTotalRare() {
		return rareWords.size();
	}

	public HSPTVocabQuiz(HSPTUser user) throws Exception {
		this.user = user;
		init();
		numCorrect = 0;
		Set<Word> questionsSet = getUniqueWords(numQuestions);
		questions = new ArrayList<Word>(questionsSet);
		quizLog = new HSPTVocabQuizLog();
		
		
	}

	public HSPTVocabQuestion nextQuestion() {
		System.out.println("nextQuestion:"+questionLog+", Object:"+this);
				
        questionStartMs = System.currentTimeMillis();
		if (quizStartMs==0) {
			Date date = Calendar.getInstance().getTime();  
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			quizLog.timeStart  = dateFormat.format(date);  
			quizStartMs = questionStartMs;
		}
		
		HSPTVocabQuestion q = new HSPTVocabQuestion();
		q.correctAnswer = rnd.nextInt(NUM_ANSWERS);

		currentWord = questions.get(count);

		q.numFail = currentWord.fail;
		q.numPass = currentWord.pass;

		Set<Word> wrongAs = getUniqueOthers(NUM_ANSWERS - 1, words, currentWord);
		/*
		 * int cat = w.getCategory(); if (0==cat) System.out.print(ANSI_RED); else if
		 * (1==cat) System.out.print(ANSI_BLUE); else System.out.print(ANSI_GREEN);
		 * System.out.println("\nQuestion "+count+":"); System.out.print(ANSI_RESET);
		 */
		System.out.println(currentWord.word);

		List<Word> wrongAList = new ArrayList<Word>(wrongAs);

		int wrongACount = 0;
		for (int aCount = 0; aCount < NUM_ANSWERS; aCount++) {
			System.out.print("(" + (aCount + 1) + ") ");
			if (aCount == q.correctAnswer)
				q.answers[aCount] = currentWord;
			else {
				Word wrongW = wrongAList.get(wrongACount);
				wrongACount++;
				q.answers[aCount] = wrongW;
			}
		}

		currentQuestion = q;
		q.index = count;
		
		System.out.println("New questionLog");
		questionLog = new HSPTVocabQuestionLog();
		questionLog.question = currentWord.id;
		questionLog.answers = new String[NUM_ANSWERS - 1];
		int i=0;
		for (Word wrongWord: wrongAs) {
			questionLog.answers[i] = wrongWord.id;
			i++;
		}
		
		return q;
	}

	public HSPTVocabQuestionFeedback answerQuestion(int choice) throws Exception {
		System.out.println("answerQuestion:"+questionLog+", Object:"+this);
		HSPTVocabQuestionFeedback f = new HSPTVocabQuestionFeedback();
		
		long questionEndMs = System.currentTimeMillis();
		long questionTimeMs =  questionEndMs - questionStartMs;
		questionLog.timeSec = (int)(questionTimeMs / 1000);
		questionLog.givenAnswer = currentQuestion.answers[choice].id;
		quizLog.questions.add(questionLog);
		
		f.choice = choice;
		f.correctChoice = currentQuestion.correctAnswer;
		f.answer = currentQuestion.answers[currentQuestion.correctAnswer];

		count++;
		if (f.choice == f.correctChoice) {
			currentWord.pass++;
			numPass++;
		} else {
			currentWord.fail++;
			numFail++;
			failedWordsQuiz.add(currentWord);
		}

		calcWordLists();

		// System.out.println("Failed words:"+failedWords.size()+"\nRare
		// words:"+rareWords.size()+"\nPassed words:"+passedWords.size()+"\nNever used
		// words:"+this.zeroCountWords);

		if (count >= numQuestions) {
			long quizTimeMs = questionEndMs - quizStartMs;
			quizLog.timeSec = (int)(quizTimeMs / 1000);
			quizLog.user = this.user;
			finishTest();
		}

		return f;
	}

	public void finishTest() throws Exception {
		System.out.println("Test finished");
		writeCSV();
		writeQuizLog();
	}

	public void init() throws Exception {

		propertyHome = BASE_PATH; // "C:/temp"; // System.getenv("HSPT_HOME");
		String newHome = System.getenv("HSPT_BACKEND_BASE_PATH");
		if (null != newHome)
			propertyHome = newHome;
		try {
			props.load(new FileInputStream(propertyHome + "/hspt.properties"));
		} catch (FileNotFoundException fex) {
			System.err.println("(No property file)");
		}

		numQuestions = user.numQuestions;
		everFailPct = user.everFailPct;
		failPct = user.failPct;
		rarePct = user.rarePct;
		passPct = user.passPct;
		isRandom = user.isRandom;

		String masterFilename = propertyHome + "/"+user.testType+"_master.csv";
		List<String[]> masterList = Util.readCSV(new FileInputStream(masterFilename));
		LinkedHashMap<String, Word> userWords = new LinkedHashMap<String, Word>();

		for (String[] input : masterList) {
			userWords.put(input[0], new Word(input));
		}

		String userFilename = getFileName(user);
		try {
			List<String[]> userList = Util.readCSV(new FileInputStream(userFilename));
			for (String[] input : userList) {
				Word word = userWords.get(input[0]);
				if (null != word) {
					word.pass = Integer.parseInt(input[2]);
					word.fail = Integer.parseInt(input[3]);
				} else {
					System.err.println("Word from user " + user + " not found:" + input[0] + "," + input[1]);
				}
			}

		} catch (FileNotFoundException fex) {
			System.out.println("Create new stats for user " + user.email);
		}

		words = new ArrayList<Word>(userWords.values());
		failedWordsQuiz = new ArrayList<Word>();
		System.out.println("After new failWords:"+failedWordsQuiz.size());

	}

	public void writeCSV() throws Exception {
		List<String[]> outputList = new ArrayList<String[]>();
		for (Word word : words) {
			outputList.add(word.toUserStatsArray());
		}
		String userFilename = getFileName(user);
		Util.writeCSV(userFilename, outputList);
	}
	
	public String getFileName(HSPTUser user) {
		return propertyHome + "/"+user.testType+"_words_" + user.email + ".csv";
	}


	/*
	 * public Set<Integer> getUniqueList(int resultSize, int totalSize) { return
	 * getUniqueList(resultSize, totalSize, -1); }
	 * 
	 * public Set<Integer> getUniqueList(int resultSize, int totalSize, int exclude)
	 * {
	 * 
	 * 
	 * Set<Integer> result = new HashSet<Integer> ();
	 * 
	 * for (int i=0; i<resultSize; i++) { int num; do { num =
	 * rnd.nextInt(totalSize); } while (result.contains(num) || (exclude > -1 && num
	 * == exclude )); result.add(num); }
	 * 
	 * return result;
	 * 
	 * }
	 */

	public Set<Word> getUniqueOthers(int resultSize, List<Word> words, Word exclude) {

		Set<Word> result = new HashSet<Word>();

		for (int i = 0; i < resultSize; i++) {
			int num;
			Word otherWord;
			do {
				num = rnd.nextInt(words.size());
				otherWord = words.get(num);
			} while (result.contains(otherWord) || exclude.equals(otherWord));
			result.add(otherWord);
		}

		return result;

	}

	public void calcWordLists() {
		failedWords = new ArrayList<Word>(); // words for fail > pass
		passedWords = new ArrayList<Word>(); // words for pass > 0, pass >= fail
		rareWords = new ArrayList<Word>(); // words for pass >= fail and pass <= avgPass

		// First loop: Find average for passed words
		int totalPass = 0;
		int countPass = 0;
		int avgPass = 0;

		for (Word word : words) {
			if (word.pass >= word.fail) {
				avgPass += word.pass;
				countPass++;
			}
		}
		avgPass = totalPass / countPass;

		Word.avgPass = avgPass;
		zeroCountWords = 0;
		// Second loop: distribute words
		for (Word word : words) {
			if (word.pass < word.fail)
				failedWords.add(word);
			else if (word.pass > avgPass)
				passedWords.add(word);
			else
				rareWords.add(word);

			if (word.pass == 0 && word.fail == 0)
				zeroCountWords++;

		}

	}

	public Set<Word> getUniqueWords(int resultSize) {
		// System.out.println("Fail:"+failedWords.size()+", Rare:"+rareWords.size()+",
		// Pass:"+passedWords.size());

		calcWordLists();
		Set<Word> result = new HashSet<Word>();

		for (int i = 0; i < resultSize; i++) {
			Word newWord;
			do {
				if (isRandom) {
					newWord = getRandomWord(this.words);
				} else {
					int category = rnd.nextInt(100); // 0-3: failed; 4-7: rare; 8-9: passed
					if (category < failPct - 1 && failedWords.size() > 0) {
						newWord = getRandomWord(failedWords);
						// System.out.println("Failed:"+newWord);
					} else if (category < failPct + rarePct - 1 && rareWords.size() > 0) {
						newWord = getRandomWord(rareWords);
						// System.out.println("Rare:"+newWord);
					} else if (passedWords.size() > 0) {
						newWord = getRandomWord(passedWords);
						// System.out.println("Passed:"+newWord);
					} else {
						newWord = null;
					}
				}
			} while (newWord == null || result.contains(newWord));
			// System.out.println("Picked:"+newWord);
			result.add(newWord);
		}

		return result;

	}

	public Word getRandomWord(List<Word> wordList) {
		int listSize = wordList.size();
		int num = rnd.nextInt(listSize);
		return wordList.get(num);
	}

	@Override
	public String toString() {
		return "Quiz{" + "id=" + getId() + '}';
	}	
	
	public String getLogFileName(HSPTUser user) {
		Date date = Calendar.getInstance().getTime();  
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");  
		String strDate = dateFormat.format(date);  
		return propertyHome + "/"+user.testType+"_log_" + user.email + "_"+strDate+".csv";
	}
	
	public void writeQuizLog() throws Exception {		
		System.out.println("writeQuizLog");
		String fileName = getLogFileName(user);
		System.out.println("File name:" + fileName);
		Util.writeJson(fileName, quizLog);
	}

}
