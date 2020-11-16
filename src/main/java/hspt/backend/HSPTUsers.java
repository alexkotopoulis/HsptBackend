package hspt.backend;


import java.util.HashMap;
import java.util.Map;


/**
 * Class for list of all users
 * @author alex
 *
 */

public class HSPTUsers {
	
	Map<String,HSPTUser> users = new HashMap<String,HSPTUser>();
	String fileSuffix;
	
	public HSPTUsers () throws Exception{
	  String propertyHome = HSPTVocabQuiz.BASE_PATH; //"C:/temp"; // System.getenv("HSPT_HOME");
	  String newHome = System.getenv("HSPT_BACKEND_BASE_PATH");
	  if (null != newHome)
		  propertyHome = newHome;	
	  fileSuffix = propertyHome;
	}
	
	public HSPTUser getUser(String name) {
		return users.get(name);
	}
	
	public HSPTUser checkUser(HSPTUser bareUser) throws Exception {
		return checkUser(bareUser, true);
		
	}
	public HSPTUser checkUser(HSPTUser bareUser, boolean useCache) throws Exception {
		System.out.println("Checking:"+bareUser.getEmail());
		
		HSPTUser user = getUser(bareUser.getEmail());
	
		if (useCache && user != null) { 	
			System.out.println("User found in cache:"+user);
		} else {
			user = readUser(bareUser.getEmail());
			if (user != null) {
				System.out.println("User found in file:"+user);
			} else {
			    user = bareUser;
			} 
		}
		user.setToken(bareUser.getToken());
		updateUser(user);
		return user;
			
	}
	
	public HSPTUser readUser(String email) throws Exception{
		  String usersFilename = getFileName(email);
		  
		  try {
			  HSPTUser newUser = (HSPTUser)Util.readJson(usersFilename, HSPTUser.class);
			  return newUser;
		  } catch (Exception ex) {
			  return null;
		  }
	}
	
	public void updateUser(HSPTUser user) throws Exception {
		String usersFilename = getFileName(user.email);
		users.put(user.email, user);
		System.out.println("Write user:"+usersFilename);
		Util.writeJson(usersFilename, user);
	}
	
	public String getFileName(String name) {
		return fileSuffix + "/hspt_user_" + name + ".csv";
	}
	

}
