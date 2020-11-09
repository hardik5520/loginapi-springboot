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
				"mongodb://hardikuser:shiro123@cluster0-shard-00-00.n3g8u.mongodb.net:27017,cluster0-shard-00-01.n3g8u.mongodb.net:27017,cluster0-shard-00-02.n3g8u.mongodb.net:27017/test?connectTimeoutMS=300000&replicaSet=atlas-x0o1fm-shard-0&ssl=true&authSource=admin");
		mongoClient = new MongoClient(uri);

		return mongoClient;
		

	}

}