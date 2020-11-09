package com.hardik.hardiklogin;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoClientException;
import com.mongodb.MongoConfigurationException;
import com.mongodb.client.MongoClients;

public class MongoConnection {

	private static MongoClient mongoClient = null;

	public static MongoClient getConnection() {
		if (mongoClient != null)
			return mongoClient;
		MongoClientURI uri = new MongoClientURI(
				"//paste your connection url here");
		mongoClient = new MongoClient(uri);

		return mongoClient;
		

	}

}
