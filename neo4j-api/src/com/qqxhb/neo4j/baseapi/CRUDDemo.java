package com.qqxhb.neo4j.baseapi;

import java.io.IOException;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;

public class CRUDDemo {
	private static GraphDatabaseService graphDb;

	public CRUDDemo() throws IOException {
		graphDb = GraphDatabaseServiceHolder.getDataBaseService();
	}

	public void create() throws IOException {
		try (Transaction tx = graphDb.beginTx()) {
			Label label = Label.label("Person");
			Node first = graphDb.createNode(label);
			first.setProperty("name", "first");
			Node second = graphDb.createNode(label);
			second.setProperty("name", "second");

			first.createRelationshipTo(second, RelTypes.FRIEND);
			tx.success();
		}
	}

	public void edit() {
		try (Transaction tx = graphDb.beginTx()) {
			Label label = Label.label("Person");
			Node node = graphDb.findNode(label, "name", "second");

			System.out.println("query node name is " + node.getProperty("name"));

			node.setProperty("name", "new name");
			node.setProperty("sex", "ÄÐ");
			tx.success();
		}
	}

	public void remove() {
		try (Transaction tx = graphDb.beginTx()) {
			Label label = Label.label("Person");
			Node first = graphDb.findNode(label, "name", "first");
			Relationship relationship = first.getSingleRelationship(RelTypes.FRIEND, Direction.OUTGOING);

			System.out.println("delete node name is " + first.getProperty("name")
					+ ", relationship is KNOWS, end node name is " + relationship.getEndNode().getProperty("name"));

			relationship.delete();
			relationship.getEndNode().delete();
			first.delete();

			tx.success();
		}
	}

//	public void shutDown() {
//		GraphDatabase.shutDown();
//	}

	public static void main(final String[] args) throws IOException {
		CRUDDemo knows = new CRUDDemo();
		System.out.println("-------------create:");
		knows.create();
		System.out.println("-------------edit:");
		knows.edit();
		System.out.println("-------------remove:");
		knows.remove();
//		knows.shutDown();
	}

}