package hspt.backend;

/**
 * Class to collect user statistics in backend
 * @author alex
 *
 */
public class HSPTUser {
	static final int DEFAULT_QUESTIONS=20;
	static final boolean DEFAULT_IS_RANDOM=true;
	static final int DEFAULT_FAIL_PCT=20;
	static final int DEFAULT_EVER_FAIL_PCT=20;
	static final int DEFAULT_RARE_PCT=60;
	static final int DEFAULT_PASS_PCT=20;
	static final String DEFAULT_TESTTYPE="hspt";
	
	String firstName; 
	String lastName; 
	String email; 
	String pictureUrl;
	String password; // Can be empty
	String token;
	int numQuestions;
	int failPct;
	int everFailPct;
	int rarePct;
	int passPct;
	boolean isRandom;
	String testType;  // hspt or sat
	
	public static HSPTUser parseJson(String json) throws Exception{
		return (HSPTUser) Util.jsonToObject(json, HSPTUser.class);
	}
	
	
	public HSPTUser() {
		this.lastName=""; // FIXME
		this.password="";
		this.numQuestions = DEFAULT_QUESTIONS;
		this.failPct = DEFAULT_FAIL_PCT;
		this.everFailPct = DEFAULT_EVER_FAIL_PCT;
		this.rarePct = DEFAULT_RARE_PCT;
		this.passPct = DEFAULT_PASS_PCT;	
		this.isRandom = DEFAULT_IS_RANDOM;	
		this.testType = DEFAULT_TESTTYPE;
	}


	public boolean checkPassword(String password) {
		System.out.println("Checking:"+this.password+","+password);
		return this.password.equals(password);
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getPictureUrl() {
		return pictureUrl;
	}


	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getToken() {
		return token;
	}


	public void setToken(String token) {
		this.token = token;
	}


	public int getNumQuestions() {
		return numQuestions;
	}


	public void setNumQuestions(int numQuestions) {
		this.numQuestions = numQuestions;
	}


	public int getFailPct() {
		return failPct;
	}


	public void setFailPct(int failPct) {
		this.failPct = failPct;
	}


	public int getEverFailPct() {
		return everFailPct;
	}


	public void setEverFailPct(int everFailPct) {
		this.everFailPct = everFailPct;
	}


	public int getRarePct() {
		return rarePct;
	}


	public void setRarePct(int rarePct) {
		this.rarePct = rarePct;
	}


	public int getPassPct() {
		return passPct;
	}


	public void setPassPct(int passPct) {
		this.passPct = passPct;
	}


	public boolean isRandom() {
		return isRandom;
	}


	public void setRandom(boolean isRandom) {
		this.isRandom = isRandom;
	}

	

	public String getTestType() {
		return testType;
	}


	public void setTestType(String testType) {
		this.testType = testType;
	}


	public void update(HSPTUser newUser) {
		//this.firstName = newUser.firstName; 
		//this.lastName = newUser.lastName; 
		//this.email = newUser.email;  
		//this.pictureUrl = newUser.pictureUrl;
		//this.password = newUser.password; // Can be empty
		this.numQuestions = newUser.numQuestions;
		this.failPct = newUser.failPct;
		this.everFailPct = newUser.everFailPct;
		this.rarePct = newUser.rarePct;
		this.passPct = newUser.passPct;
		this.isRandom = newUser.isRandom;
		this.testType = newUser.testType;
	}

	/*
	public String[] toArray() {
		List <String>output = new ArrayList<String>();
		output.add(this.name);
		output.add(this.password);
		output.add(""+this.num_questions);
		output.add(""+this.fail_pct);
		output.add(""+this.ever_fail_pct);
		output.add(""+this.rare_pct);
		output.add(""+this.pass_pct);	
		output.add(""+this.is_random);
		
		return (String[])output.toArray();
		
	}
	*/
	
	
	
}
