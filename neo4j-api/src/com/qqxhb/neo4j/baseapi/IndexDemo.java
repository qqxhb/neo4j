package com.qqxhb.neo4j.baseapi;
import java.io.IOException;
import java.util.ArrayList;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.schema.ConstraintDefinition;
import org.neo4j.graphdb.schema.IndexDefinition;
import org.neo4j.graphdb.schema.Schema;

public class IndexDemo {

    public static void main(String[] args) throws IOException {
    	GraphDatabaseService graphDb = GraphDatabaseServiceHolder.getDataBaseService();
        createIndex(graphDb);
        createUser(graphDb);
        updateUser(graphDb, 0);
        updateUser(graphDb, 1);
        updateUser(graphDb, 2);
        findUser(graphDb);
        deleteUser(graphDb);
        dropIndex(graphDb);

        graphDb.shutdown();
    }

    private static void updateUser(GraphDatabaseService graphDb, int id){
        String outstr = "";
        try ( Transaction tx = graphDb.beginTx() )
        {
            Label label = Label.label( "User" );
            String nameToFind = "user" + id;
            for ( Node node : loop(graphDb.findNodes(label, "name", nameToFind)) )
            {
                node.setProperty( "name", "user" + ( id + 1 ) );
                outstr += "Update user name from [" + nameToFind + "] to [" + node.getProperty("name") + "]";
            }
            tx.success();
        }catch (Exception e){
            outstr = "Update error: " + e.getMessage();
        }
        System.out.println(outstr);
    }


    private static void findUser(GraphDatabaseService graphDb){
        Label label = Label.label( "User" );
        try ( Transaction tx = graphDb.beginTx() )
        {
            try ( ResourceIterator<Node> users = graphDb.findNodes(label))
            {
                while ( users.hasNext() )
                {
                    Node node = users.next();
                    System.out.println("find user is " + node.getProperty("name"));
                }
            }
        }
    }

    private static void createUser(GraphDatabaseService graphDb){
        try ( Transaction tx = graphDb.beginTx() )
        {
            Label label = Label.label( "User" );
            for ( int id = 0; id < 3; id++ )
            {
                Node node = graphDb.createNode( label );
                node.setProperty( "name", "user" + id);
                System.out.println( "Create user id=[" + node.getId() + "] name=[" + node.getProperty("name") + "]" );
            }
            tx.success();
        }
    }

    private static void deleteUser(GraphDatabaseService graphDb){
        try ( Transaction tx = graphDb.beginTx() )
        {
            Label label = Label.label( "User" );
            for ( Node node : loop( graphDb.findNodes(label)))
            {
                System.out.println("delete user is " + node.getProperty("name"));
                node.delete();
            }
            tx.success();
        }
    }

    private static void createIndex(GraphDatabaseService graphDb){
        try ( Transaction tx = graphDb.beginTx() )
        {
            Schema schema = graphDb.schema();
            schema.indexFor(Label.label("User")).on("name").create();
            schema.constraintFor(Label.label( "User" )).assertPropertyIsUnique("name").create();
            tx.success();
        }
        System.out.println( "Create unique index");
    }

    private static void dropIndex(GraphDatabaseService graphDb){
        try ( Transaction tx = graphDb.beginTx() )
        {
            Label label = Label.label( "User" );
            for(IndexDefinition indexDefinition : graphDb.schema().getIndexes(label)){
                indexDefinition.drop();
            }
            for(ConstraintDefinition constraintDefinition : graphDb.schema().getConstraints(label)){
                constraintDefinition.drop();

            }
            tx.success();
        }
        System.out.println( "Drop index success." );
    }

    private  static ArrayList<Node> loop( ResourceIterator<Node> users){
        ArrayList<Node> userNodes = new ArrayList<>();
        while ( users.hasNext() )
        {
            userNodes.add( users.next() );
        }
        return userNodes;
    }
}