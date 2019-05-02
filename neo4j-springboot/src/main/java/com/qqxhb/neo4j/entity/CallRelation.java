package com.qqxhb.neo4j.entity;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

@RelationshipEntity(type = "call")
public class CallRelation {

    public CallRelation() {
        this.name = "call";
    }


    public CallRelation(SGNode start, SGNode end, Long count) {
        this();
        this.startNode = start;
        this.endNode = end;
        this.count = count;
    }

    /**
     * Neo4j会分配的ID（节点唯一标识 当前类中有效）
     */
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 定义关系的起始节点 == StartNode
     */

    @StartNode
    private SGNode startNode;

    /**
     * 定义关系的终止节点 == EndNode
     */

    @EndNode
    private SGNode endNode;


    /**
     * 定义关系的属性
     */

    @Property(name = "count")
    private Long count;

    public SGNode getStartNode() {
        return startNode;
    }

    public void setStartNode(SGNode startNode) {
        this.startNode = startNode;
    }

    public SGNode getEndNode() {
        return endNode;
    }

    public void setEndNode(SGNode endNode) {
        this.endNode = endNode;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}