package com.hardik.hardiklogin;

import java.awt.print.Book;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import javax.management.Query;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;

@RestController
@CrossOrigin
public class loginapi {

	@PostMapping("/login")
	public String login(@RequestBody userModel user) throws IOException, FirebaseAuthException {
		if (user.getUsername() == null || user.getPassword() == null) {
			System.out.println("no details given");
			return ("Unable to receive body data");
		} else {
			System.out.println("received body data");
		}

		MongoClient mongoClient = MongoConnection.getConnection();
		MongoDatabase db = mongoClient.getDatabase("demo");
		MongoCollection<Document> collection = db.getCollection("users");
		/* Finding user in Database by matching user-name */

		MongoCursor<Document> cursor = collection.find((eq("username", user.getUsername()))).iterator();
		/* If the user is not Found */
		//System.out.println("HELP"+cursor.next());
		while (!cursor.hasNext()) {
			return "No User Found ...!";
		}
		Document doc=cursor.next();
		Object hpassword=doc.get("password");//receivinh hashed password from db here
		 int strength = 10; // work factor of bcrypt
		 String checkpassword = (String) hpassword;//changing datatype of password received from db
	     BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	     String encodedPassword=passwordEncoder.encode(checkpassword);
	     System.out.println("Encoded password is " + encodedPassword);		
	     String password2 = user.getPassword(); //receiving the entered password here
			Boolean isPasswordMatch = passwordEncoder.matches(password2, checkpassword);
			System.out.println("Password : " + password2 + "           isPasswordMatch    : " + isPasswordMatch);
			if(isPasswordMatch==true)  
			{
				System.out.println("Hello " + user.getUsername() + " Login Success..!");
				FirebaseOptions options = FirebaseOptions.builder()
				    .setCredentials(GoogleCredentials.getApplicationDefault())
				    .setDatabaseUrl("https://<DATABASE_NAME>.firebaseio.com/")
				    .build();
				if(FirebaseApp.getApps().isEmpty()) { //<--- check with this line
	                FirebaseApp.initializeApp(options);
	                }
				System.out.print(FirebaseApp.getApps());
				System.out.println("id=="+doc.get("id"));
				String uid = (String)doc.get("id");
				Map<String, Object> additionalClaims = new HashMap<String, Object>();
				additionalClaims.put("premiumAccount", true);

				String customToken = FirebaseAuth.getInstance()
				    .createCustomToken(uid, additionalClaims);
				// Send token back to client
				
			}
				return "working";
		
	}

}