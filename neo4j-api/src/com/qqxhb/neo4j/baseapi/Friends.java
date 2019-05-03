package com.qqxhb.neo4j.baseapi;

import java.io.IOException;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.graphdb.traversal.Traverser;

public class Friends {

	private GraphDatabaseService graphDb;
	private long homeNodeId;

	public static void main(String[] args) throws IOException {
		Friends matrix = new Friends();
		matrix.setUp();
		System.out.println(matrix.printFriends());
		System.out.println(matrix.printWorkers());
		matrix.shutdown();
	}

	public void setUp() throws IOException {
		graphDb = GraphDatabase.getDataBaseService();
		createNodespace();
	}

	public void shutdown() {
		graphDb.shutdown();
	}

	public void createNodespace() {
		try (Transaction tx = graphDb.beginTx()) {
			Node home = graphDb.createNode();
			homeNodeId = home.getId();

			Node user = graphDb.createNode();
			user.setProperty("name", "User");

			home.createRelationshipTo(user, RelTypes.HOME_NODE);

			Node one = graphDb.createNode();
			one.setProperty("name", "One");

			user.createRelationshipTo(one, RelTypes.FRIEND);

			Node first = graphDb.createNode();
			first.setProperty("name", "First");

			user.createRelationshipTo(first, RelTypes.FRIEND);
			first.createRelationshipTo(one, RelTypes.FRIEND);

			Node two = graphDb.createNode();
			two.setProperty("name", "Two");

			one.createRelationshipTo(two, RelTypes.FRIEND);
			first.createRelationshipTo(two, RelTypes.FRIEND);

			Node three = graphDb.createNode();
			three.setProperty("name", "Three");

			two.createRelationshipTo(three, RelTypes.FRIEND);

			Node four = graphDb.createNode();
			four.setProperty("name", "Four");

			three.createRelationshipTo(four, RelTypes.COLLEAGUE);

			tx.success();
		}
	}

	private Node getEndNode(Long id) {
		return graphDb.getNodeById(id).getSingleRelationship(RelTypes.HOME_NODE, Direction.OUTGOING).getEndNode();
	}

	public String printFriends() {
		try (Transaction tx = graphDb.beginTx()) {
			Node neoNode = getEndNode(homeNodeId);
			int numberOfFriends = 0;
			String output = neoNode.getProperty("name") + "'s friends:\n";
			Traverser friendsTraverser = getFriends(neoNode);
			for (Path friendPath : friendsTraverser) {
				output += "At depth " + friendPath.length() + " => " + friendPath.endNode().getProperty("name") + "\n";
				numberOfFriends++;
			}
			output += "Number of friends found: " + numberOfFriends + "\n";
			return output;
		}
	}

	// 广度优先
	private Traverser getFriends(final Node person) {
		TraversalDescription td = graphDb.traversalDescription().breadthFirst()
				.relationships(RelTypes.FRIEND, Direction.OUTGOING).evaluator(Evaluators.excludeStartPosition());
		return td.traverse(person);
	}

	// 深度优先
	private Traverser getFriendsDepth(final Node person) {
		TraversalDescription td = graphDb.traversalDescription().depthFirst()
				.relationships(RelTypes.FRIEND, Direction.OUTGOING).evaluator(Evaluators.excludeStartPosition());
		return td.traverse(person);
	}

	public String printWorkers() {
		try (Transaction tx = graphDb.beginTx()) {
			String output = "colleagues:\n";
			int number = 0;
			Traverser traverser = findWorker(getEndNode(homeNodeId));
			for (Path hackerPath : traverser) {
				output += "At depth " + hackerPath.length() + " => " + hackerPath.endNode().getProperty("name") + "\n";
				number++;
			}
			output += "Number of colleagues found: " + number + "\n";
			return output;
		}
	}

	private Traverser findWorker(final Node startNode) {
		TraversalDescription td = graphDb.traversalDescription().depthFirst()
				.relationships(RelTypes.COLLEAGUE, Direction.OUTGOING)
				.relationships(RelTypes.FRIEND, Direction.OUTGOING)
				.evaluator(Evaluators.includeWhereLastRelationshipTypeIs(RelTypes.COLLEAGUE));
		return td.traverse(startNode);
	}

}