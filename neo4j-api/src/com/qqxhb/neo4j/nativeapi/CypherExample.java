package com.qqxhb.neo4j.nativeapi;

import java.io.File;
import java.util.HashMap;

import org.neo4j.cypher.internal.javacompat.ExecutionEngine;
import org.neo4j.cypher.internal.javacompat.GraphDatabaseCypherService;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.GraphDatabaseQueryService;
import org.neo4j.kernel.impl.query.QueryExecutionKernelException;

public class CypherExample {
	public static void main(String[] args) {
		GraphDatabaseFactory dbFactory = new GraphDatabaseFactory();
		GraphDatabaseService graph = dbFactory.newEmbeddedDatabase(new File("D:\\neo4j\\data\\databases\\graph.db"));
		GraphDatabaseQueryService db = new GraphDatabaseCypherService(graph);
		ExecutionEngine execEngine = new ExecutionEngine(db, null, null);
		Result execResult;
		try {
			execResult = execEngine.executeQuery("MATCH (java:JAVA) RETURN java", new HashMap<String, Object>(), null);
			String results = execResult.resultAsString();
			System.out.println(results);
		} catch (QueryExecutionKernelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}