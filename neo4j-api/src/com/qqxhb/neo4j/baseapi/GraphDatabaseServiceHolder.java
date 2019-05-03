package com.qqxhb.neo4j.baseapi;

import java.io.File;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class GraphDatabaseServiceHolder {
	private static final File DB_PATH = new File("D:\\neo4j\\data\\databases\\graph.db");
	private static GraphDatabaseService graphDb;

	/**
	 * ��ȡ���ݿ����
	 * 
	 * @return
	 */
	public static GraphDatabaseService getDataBaseService() {
		if (graphDb == null) {
			synchronized (GraphDatabaseServiceHolder.class) {
				if (graphDb == null) {
					graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
					registerShutdownHook(graphDb);
				}
			}
		}
		return graphDb;
	}

	public static void shutDown() {
		{
			try {
				if (graphDb != null) {
					graphDb.shutdown();
				}
			} finally {
				graphDb = null;
			}
		}
	}

	/**
	 * �ر����ݿ⹳��
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
