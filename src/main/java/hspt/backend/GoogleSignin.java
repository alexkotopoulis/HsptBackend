package hspt.backend;

import java.util.Collections;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Value;


/**
 * Utility Class for Google Signin
 */
public class GoogleSignin {

	
	/**
	 * Verify token from API for valid Google login
	 */
	static HSPTUser verifyToken(String idTokenString, String clientId) throws Exception {
		
		HSPTUser bareUser = null;
		
		

		JsonFactory jsonFactory = new JacksonFactory();;
		HttpTransport transport = new NetHttpTransport(); // UrlFetchTransport.getDefaultInstance();
		
		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
				// Specify the CLIENT_ID of the app that accesses the backend:
				.setAudience(Collections.singletonList(clientId))
				// Or, if multiple clients access the backend:
				// .setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
				.build();

		// (Receive idTokenString by HTTPS POST)

		GoogleIdToken idToken = verifier.verify(idTokenString);
		if (idToken != null) {
			Payload payload = idToken.getPayload();
			
			bareUser = new HSPTUser();

			// Print user identifier
			String userId = payload.getSubject();
			System.out.println("User ID: " + userId);

			// Get profile information from payload
			bareUser.setEmail(payload.getEmail());
			boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
			String name = (String) payload.get("name");
			bareUser.setPictureUrl((String) payload.get("picture"));
			String locale = (String) payload.get("locale");
			bareUser.setLastName((String) payload.get("family_name"));
			bareUser.setFirstName((String) payload.get("given_name"));
			bareUser.setToken(idTokenString);
			// Use or store profile information
			// ...

			return bareUser;

		} else {
			return null;
		}
	}

}

