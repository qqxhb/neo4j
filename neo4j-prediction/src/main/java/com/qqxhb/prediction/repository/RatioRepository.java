package com.qqxhb.prediction.repository;

import java.util.Map;
import java.util.Set;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.qqxhb.prediction.domain.Team;

@Repository
public interface RatioRepository extends Neo4jRepository<Team,Long> {
    Team findByName(String name);

    //���NBA������
    @Query("MATCH (t:Team)-[r:WIN]->(p:Playoff) WHERE p.year = {year} " +
            "RETURN t.name AS name, t.code AS code, p.year AS year, p.round AS round, r.win AS win")
    Set<Map<String,Object>> findHistory(@Param("year") String year);

    //һ����ӵ���ʷ������
    @Query("MATCH (t:Team {name: {name}})-[w:WIN]->(p:Playoff)<-[l:WIN]-() " +
            "RETURN ID(t) AS id, ID(w) as wid, ID(l) as lid, t.name AS name, t.code AS code, w.win AS win, l.win AS loss," +
            "p.year AS year, p.round AS round ORDER BY p.year SKIP {skip} LIMIT {limit}")
    Set<Map<String,Object>> findHistoryByTeamName(@Param("name") String name, @Param("skip") Integer  skip, @Param("limit") Integer  limit);

    //һ����ӵ���ʷ����������
    @Query("MATCH (t:Team {name: {name}})-[w:WIN]->(p:Playoff)<-[l:WIN]-() " +
            "RETURN COUNT(t) AS count")
    Integer findHistoryByTeamNameCount(@Param("name") String nam);

    //����ʤ��
    @Query("MATCH (t:Team)-[w:WIN]->(:Playoff)<-[l:WIN]-() " +
            "RETURN t.name AS team, SUM(w.win) AS wins, SUM(l.win) AS losses," +
            "(SUM(w.win)*1.0 / (SUM(w.win)+ SUM(l.win))) AS percentage " +
            "ORDER BY SUM(w.win) DESC SKIP {skip} LIMIT {limit}")
    Set<Map<String,Object>> findPercentage(@Param("skip") Integer  skip, @Param("limit") Integer  limit);

    //ʤ������
    @Query("MATCH (t:Team)-[w:WIN]->(:Playoff)<-[l:WIN]-() WITH DISTINCT t AS p " +
            "RETURN  COUNT(p) AS count")
    Integer findPercentageCount();

    //������ǰ����
    @Query("MATCH (t1:Team {name: {t1}})-[r1:WIN]->(p:Playoff)<-[r2:WIN]-(t2:Team {name:{t2}}) " +
            "RETURN t1.name as t1, r1.win as r1, p.year as year, p.round as round, r2.win as r2, t2.name as t2")
    Set<Map<String,Object>> findHaveMet(@Param("t1") String t1, @Param("t2") String t2);

    //������Ӯ
    @Query("MATCH (t1:Team {name: {t1}})-[r1:WIN]->(p:Playoff)<-[r2:WIN]-(t2:Team {name:{t2}}) " +
            "RETURN p.year AS year, r1.win AS win, r2.win AS loss " +
            "ORDER BY p.year DESC")
    Set<Map<String,Object>> findWinAndLoss(@Param("t1") String t1, @Param("t2") String t2);

    //��������ƽ��
    @Query("MATCH (t1:Team {name:{t1}}),(t2:Team {name:{t2}}),\n" +
            "p = AllshortestPaths((t1)-[*..14]-(t2))\n" +
            "RETURN p")
    Set<Map<String,Object>> findNeverMet(@Param("t1") String t1, @Param("t2") String t2);

    //Ӯ���Ƚ�
    @Query("MATCH (t1:Team {name:{t1}}),(t2:Team {name:{t2}}),\n" +
            "p = AllshortestPaths((t1)-[r:WIN*..14]-(t2))\n" +
            "WITH r,p,extract(r IN relationships(p)| r.win ) AS paths\n" +
            "RETURN paths")
    Set<Map<String,Object>> findNeverMetPaths(@Param("t1") String t1, @Param("t2") String t2);

    //ƽ����Ӯ
    @Query("MATCH p= AllShortestPaths((t1:Team {name: {t1}})-[:WIN*0..14]-(t2:Team {name:{t2}})) " +
            "WITH extract(r IN relationships(p)| r.win) AS RArray, LENGTH(p)-1 AS s " +
            "RETURN AVG(REDUCE(x = 0, a IN [i IN range(0,s) WHERE i % 2 = 0 | RArray[i] ] | x + a)) " +
            "- AVG(REDUCE(x = 0, a IN [i IN range(0,s) WHERE i % 2 <> 0 | RArray[i] ] | x + a)) AS NET_WIN")
   float findAvgNetWin(@Param("t1") String t1, @Param("t2") String t2);

}
