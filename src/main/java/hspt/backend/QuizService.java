package hspt.backend;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;


/**
 * QuizService Class
 * Central REST controller for Quiz application
 * @author alex
 *
 */

@RestController
@RequestMapping(value = "/api/quiz")
public class QuizService {
	private static final int TESTDELAY = 0;
	
	@Value("${google.client.id}")
	String CLIENT_ID;
	
	@Value("${spring.profiles.active}")
	static String profiles_active;
	
	@Value("${spring.application.name}")
    String springAppName;

	List<Word> words;
	HSPTUsers users;
	private Map<String, HSPTUser> tokenCache = new HashMap<String, HSPTUser>();
	private Map<String, HSPTVocabQuiz> quizList;

	public QuizService() throws Exception {
		System.out.println("QuizService ctor");
		quizList = new HashMap<String, HSPTVocabQuiz>();
		users = new HSPTUsers();
	}
	
	/**
	 * Returns information about the current user
	 * @param token
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public HSPTUser getUser(@RequestHeader(value = "Authorization") String token) throws Exception {
		System.out.println("getUser");
		Thread.sleep(TESTDELAY);
		HSPTUser user = checkToken(token);
		return user;
	}

	/**
	 * Changes user information
	 * @param newUser
	 * @param token
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/user", method = RequestMethod.PUT)
	public HSPTUser putUser(@RequestBody HSPTUser newUser, @RequestHeader(value = "Authorization") String token)
			throws Exception {
		System.out.println("putUser");
		Thread.sleep(TESTDELAY);
		HSPTUser user = checkToken(token);
		user.update(newUser);
		users.updateUser(user);
		return user;
	}

    /**
     * Returns quiz based on a given ID 
     * @param id
     * @param token
     * @return
     * @throws Exception
     */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public HSPTVocabQuiz getQuiz(@PathVariable String id, @RequestHeader(value = "Authorization") String token)
			throws Exception {
		System.out.println("getQuiz");
		Thread.sleep(TESTDELAY);
		checkToken(token);
		return quizList.get(id);
	}

	/** 
	 * Starts new quiz
	 * @param newUser
	 * @param token
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public HSPTVocabQuiz postQuiz(@RequestBody HSPTUser newUser, @RequestHeader(value = "Authorization") String token)
			throws Exception {
		System.out.println("postQuiz");

		Thread.sleep(TESTDELAY);
		// HSPTUser user = checkToken(token);  // checked in putUser
		putUser(newUser, token);
		HSPTVocabQuiz myQuiz = new HSPTVocabQuiz(newUser);
		quizList.put(myQuiz.getId(), myQuiz);
		return myQuiz;
	}

	/**
	 * Gets new question form a given quiz
	 * @param id
	 * @param token
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/{id}/q", method = RequestMethod.GET)
	public HSPTVocabQuestion getNextQuestion(@PathVariable String id,
			@RequestHeader(value = "Authorization") String token) throws Exception {
		System.out.println("getNextQuestion");
		Thread.sleep(TESTDELAY);
		checkToken(token);
		HSPTVocabQuiz myQuiz = quizList.get(id);
		if (null == myQuiz)
			throw new HSPTBackendException("Unknown Quiz ID");
		HSPTVocabQuestion nextQ = myQuiz.nextQuestion();
		return nextQ;
	}

	/**
	 * Gets information about the failed words of the given quiz
	 * @param id
	 * @param token
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/{id}/failedwords", method = RequestMethod.GET)
	public List<Word> getFailedWords(@PathVariable String id, @RequestHeader(value = "Authorization") String token)
			throws Exception {
		System.out.println("getFailedWords");
		Thread.sleep(TESTDELAY);
		checkToken(token);
		HSPTVocabQuiz myQuiz = quizList.get(id);
		if (null == myQuiz)
			throw new HSPTBackendException("Unknown Quiz ID");
		System.out.println("Size failedWords:" + myQuiz.failedWordsQuiz.size());

		return myQuiz.failedWordsQuiz;
	}

	/**
	 * Answer a question on a given quiz
	 * @param id
	 * @param answer
	 * @param token
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/{id}/q", method = RequestMethod.PUT)
	public HSPTVocabQuestionFeedback answerQuestion(@PathVariable String id, @RequestBody HSPTVocabAnswer answer,
			@RequestHeader(value = "Authorization") String token) throws Exception {
		System.out.println("answerQuestion");
		Thread.sleep(TESTDELAY);
		checkToken(token);
		HSPTVocabQuiz myQuiz = quizList.get(id);
		if (null == myQuiz)
			throw new HSPTBackendException("Unknown Quiz ID");		
		HSPTVocabQuestionFeedback fb = myQuiz.answerQuestion(answer.choice);
		return fb;
	}

	/**
	 * Check token for a given user
	 * Check in cache, otherwise use GoogleSignin
	 * @param token
	 * @return
	 * @throws Exception
	 */
	public HSPTUser checkToken(String token) throws Exception {
		System.out.println("checkToken:" + token);

		HSPTUser cacheUser = tokenCache.get(token);
		if (null != cacheUser) {
			System.out.println("token cache hit:" + token);
			return cacheUser;
		}

		HSPTUser bareUser = GoogleSignin.verifyToken(token, CLIENT_ID);
		if (null != bareUser) {
			System.out.println("Verify Token:" + token);
			HSPTUser user = users.checkUser(bareUser, false);
			tokenCache.put(token, user);
			return user;
		} else {
			throw new HSPTLoginException(token);
		}
	}
}
