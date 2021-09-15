package com.codingapi.springboot.data.permission.sql;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author lorne
 * @since 1.0.0
 */
class SQLTest {

    @Test
    void insert(){
        SQL sql = new SQL(" insert into demo (id, create_time, user_id, name) values (null,?,?,?)");
        sql.deleteKey("user_id");
        System.out.println(sql.getSql());
    }

    @Test
    void update(){
        SQL sql = new SQL("update demo set create_time=?, user_id=?, name=? where id=?");
        sql.deleteKey("create_time");
        System.out.println(sql.getSql());
    }

    @Test
    void delete(){
        SQL sql = new SQL("delete from demo where id=?");
        sql.deleteKey("id");
        System.out.println(sql.getSql());
    }

    @Test
    void select(){
        SQL sql = new SQL("select demo0_.id as id1_0_0_, demo0_.create_time as create_t2_0_0_, demo0_.user_id as user_id3_0_0_, demo0_.name as name4_0_0_ from demo demo0_ where demo0_.id=?");
        sql.deleteKey("demo0_.id");
        System.out.println(sql.getSql());
    }


}
