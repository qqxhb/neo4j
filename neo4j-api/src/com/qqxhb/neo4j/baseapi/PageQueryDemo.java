package com.qqxhb.neo4j.baseapi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

public class PageQueryDemo {
	private GraphDatabaseService graphDb;

	public PageQueryDemo() {
		graphDb = GraphDatabaseServiceHolder.getDataBaseService();
	}

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException {
		PageQueryDemo pageData = new PageQueryDemo();
		for (int i = 0; i < 20; i++) {
			pageData.createNode("test" + i);
		}

		// list page
		Map<String, Object> params = new HashMap<>();
		params.put("name", "(?i)test.*");
		params.put("skip", 0 * 10);
		params.put("limit", 10);
		Map<String, Object> page = pageData.findPage(params);
		System.out.println("==page list==");
		for (Map<String, Object> list : (List<Map<String, Object>>) page.get("content")) {
			System.out.println("id=" + list.get("id") + ";name=" + list.get("name") + ";create=" + list.get("create"));
		}
		System.out.println("page total=" + page.get("totalElements"));

		GraphDatabaseServiceHolder.shutDown();
	}

	public Map<String, Object> findPage(Map<String, Object> params) {
		Map<String, Object> page = new HashMap<>();
		try (Transaction tx = graphDb.beginTx()) {
			String query = "MATCH (n) WHERE n.name =~ {name} RETURN n SKIP {skip} LIMIT {limit}";
			Result result = graphDb.execute(query, params);
			Iterator<Node> n_column = result.columnAs("n");

			List<Map<String, Object>> content = new ArrayList<>();
			while (n_column.hasNext()) {
				Node node = n_column.next();

				Map<String, Object> data = new HashMap<>();
				data.put("id", node.getId());
				data.put("name", node.getProperty("name"));
				data.put("create", new Date((Long) node.getProperty("create")));

				content.add(data);
			}

			query = "MATCH (n) WHERE n.name =~ {name} RETURN count(n) as count";
			result = graphDb.execute(query, params);

			if (result.hasNext()) {
				Map<String, Object> row = result.next();
				page.put("totalElements", row.get("count"));
			}

			page.put("content", content);

			tx.close();
		}
		return page;
	}

	public void createNode(String name) {
		try (Transaction tx = graphDb.beginTx()) {
			Node node = graphDb.createNode();
			node.setProperty("name", name);
			node.setProperty("create", new Date().getTime());
			System.out.println("create node id=" + node.getId());
			tx.success();
		}
	}

}