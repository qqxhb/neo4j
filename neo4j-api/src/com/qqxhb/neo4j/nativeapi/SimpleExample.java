package com.qqxhb.neo4j.nativeapi;

import java.io.File;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class SimpleExample {
	public static void main(String[] args) {
		GraphDatabaseFactory dbFactory = new GraphDatabaseFactory();
		GraphDatabaseService db = dbFactory.newEmbeddedDatabase(new File("D:\\neo4j\\data\\databases\\graph.db"));
		try (Transaction tx = db.beginTx()) {
			Node javaNode = db.createNode(Tutorials.JAVA);
			javaNode.setProperty("TutorialID", "JAVA001");
			javaNode.setProperty("Title", "Learn Java");
			javaNode.setProperty("NoOfChapters", "25");
			javaNode.setProperty("Status", "Completed");

			Node scalaNode = db.createNode(Tutorials.SCALA);
			scalaNode.setProperty("TutorialID", "SCALA001");
			scalaNode.setProperty("Title", "Learn Scala");
			scalaNode.setProperty("NoOfChapters", "20");
			scalaNode.setProperty("Status", "Completed");
			Relationship relationship = javaNode.createRelationshipTo(scalaNode, TutorialRelationships.JVM_LANGIAGES);
			relationship.setProperty("Id", "1234");
			relationship.setProperty("OOPS", "YES");
			relationship.setProperty("FP", "YES");

			Node mysqlNode = db.createNode(Tutorials.MYSQL);
			mysqlNode.setProperty("TutorialID", "MYSQL");
			mysqlNode.setProperty("Title", "Learn MYSQL");
			mysqlNode.setProperty("NoOfChapters", "25");
			mysqlNode.setProperty("Status", "Completed");

			Node neo4jNode = db.createNode(Tutorials.NEO4J);
			neo4jNode.setProperty("TutorialID", "NEO4J");
			neo4jNode.setProperty("Title", "Learn NEO4J");
			neo4jNode.setProperty("NoOfChapters", "20");
			neo4jNode.setProperty("Status", "Completed");

			Relationship relationship1 = mysqlNode.createRelationshipTo(neo4jNode, TutorialRelationships.DATABASE);
			relationship1.setProperty("Id", "456");
			relationship1.setProperty("OOPS", "YES");
			relationship1.setProperty("FP", "YES");
			tx.success();
		}
		System.out.println("Done successfully");
	}
}