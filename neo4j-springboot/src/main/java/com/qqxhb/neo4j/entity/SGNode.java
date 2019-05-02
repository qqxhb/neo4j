package com.qqxhb.neo4j.entity;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
public class SGNode {
    private Long count;
    private Long error;
    private Double max;
    private Double min;

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

    @Relationship(type = "call")
    private List<CallRelation> calls;

    public SGNode() {
        count = 0L;
        error = 0L;
        max = 0.0;
        min = 0.0;
        calls = new ArrayList<>();
    }

    public SGNode(String name) {
        this();
        this.name = name;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getError() {
        return error;
    }

    public void setError(Long error) {
        this.error = error;
    }

    public Double getMax() {
        return max;
    }

    public void setMax(Double max) {
        this.max = max;
    }

    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }

    public List<CallRelation> getCalls() {
        return calls;
    }

    public void setCalls(List<CallRelation> calls) {
        this.calls = calls;
    }

    public void addCalls(SGNode node, Long count) {
        CallRelation relation = new CallRelation(this, node, count);
        this.calls.add(relation);
    }
}