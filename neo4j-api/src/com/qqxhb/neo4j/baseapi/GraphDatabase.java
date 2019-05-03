package com.qqxhb.neo4j.baseapi;

import java.io.File;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class GraphDatabase {
	private static final File DB_PATH = new File("target/neo4j-db");
	private static GraphDatabaseService graphDb;

	/**
	 * 获取数据库服务
	 * 
	 * @return
	 */
	public static GraphDatabaseService getDataBaseService() {
		if (graphDb == null) {
			synchronized (GraphDatabase.class) {
				if (graphDb == null) {
					graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
					registerShutdownHook(graphDb);
				}
			}
		}
		return graphDb;
	}

	public static void shutDown() {
		System.out.println("Shutting down database ...");
		graphDb.shutdown();
	}

	/**
	 * 关闭数据库钩子
	 * 
	 * @param graphDb
	 */
	private static void registerShutdownHook(final GraphDatabaseService graphDb) {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				graphDb.shutdown();
			}
		});
	}
}
