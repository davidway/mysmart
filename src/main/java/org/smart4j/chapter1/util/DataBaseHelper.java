package org.smart4j.chapter1.util;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.chapter1.service.CustomerService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

import static org.smart4j.chapter1.service.CustomerService.LOGGER;

public class DataBaseHelper {
    private static final ThreadLocal<Connection> CONNECTION_HOLDER = new ThreadLocal<Connection>();

    private static final QueryRunner QUERY_RUNNER = new QueryRunner();
    public static final Logger LOGGER = LoggerFactory.getLogger(DataBaseHelper.class);
    private static final String DRIVER;
    private static final String URL;
    private static final String USERNAME;
    private static final String PASSWORD;

    private static final BasicDataSource DATA_SOURCES;


    static{
        Properties conf = PropsUtil.loadProps("config.properties");
        DRIVER = conf.getProperty("jdbc.driver");
        URL = conf.getProperty("jdbc.url");
        USERNAME = conf.getProperty("jdbc.username");
        PASSWORD  = conf.getProperty("jdbc.password");

        DATA_SOURCES = new BasicDataSource();
        DATA_SOURCES.setDriverClassName(DRIVER);
        DATA_SOURCES.setUrl(URL);
        DATA_SOURCES.setUsername(USERNAME);
        DATA_SOURCES.setPassword(PASSWORD);

        try{
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            LOGGER.error("can't not load jdbc driver",e);
        }
    }



    public static void executeSqlFile(String filePath) throws IOException {
        //String file = "sql/update.sql";
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String sql;
        while ( (sql=reader.readLine())!=null){
            DataBaseHelper.executeUpdate(sql);
        }
    }
    public static int executeUpdate(String sql,Object... params){
        int rows=0;
        try{
            Connection conn = getConnection();
            rows = QUERY_RUNNER.update(conn,sql,params);
        }catch(SQLException e){
            LOGGER.error("execute sql failure",e);
            throw new RuntimeException(e);
        }
        return rows;
    }

    public static<T> boolean insertEntity(Class<T> entityClass,Map<String,Object> fieldMap){
        if ( CollectionUtil.isEmpty(fieldMap)){
            LOGGER.error("can't insert entity:filedMap is empty");
            return false;
        }
        String sql=" insert into "+getTableName(entityClass);
        StringBuilder columns = new StringBuilder("(");
        StringBuilder values = new StringBuilder("(");
        for (String fieldName:fieldMap.keySet()){
            columns.append(fieldName).append(",");
            values.append("?,");
        }

        columns.replace(columns.lastIndexOf(","),columns.length(),")");
        values.replace(values.lastIndexOf(","),values.length() ,")");
        sql+= columns+"VALUES "+values;
        Object[] params = fieldMap.values().toArray();

        return executeUpdate(sql,params)==1;
    }


    public static<T> boolean updateEntity(Class<T> entityClass,long id,Map<String,Object> fieldMap){
        if ( CollectionUtil.isEmpty(fieldMap)){
            LOGGER.error("can't insert entity:filedMap is empty");
            return false;
        }
        String sql=" update "+getTableName(entityClass) +" set ";
        StringBuilder columns = new StringBuilder("");
       for (String fieldName:fieldMap.keySet()){
           columns.append(fieldName).append("=? ,");
       }

       sql+= columns.substring(0,columns.lastIndexOf(",")) +" where id = ? ";

       List<Object> paramList = new ArrayList<Object>();
       paramList.addAll(fieldMap.values());
       paramList.add(id);

       Object[] params = paramList.toArray();

       return executeUpdate(sql,params)==1;
    }

    public static<T> boolean deleteEntity(Class<T> entityClass,long id){
        String sql = "DELETE FROM "+ getTableName(entityClass) +" where id=? ";
        return executeUpdate(sql,id)==1;
    }

    private static <T> String getTableName(Class<T> entityClass) {
        return entityClass.getSimpleName();
    }

    //多表查询
    public static List<Map<String,Object>> executeQuery(String sql,Object... params){
        List<Map<String,Object>> result = null;
        try{
            Connection conn = getConnection();
            result = QUERY_RUNNER.query(conn,sql,new MapListHandler(),params);

        }catch(Exception e){
            LOGGER.error("execute sql failure",e);
            throw new RuntimeException(e);
        }
        return result;
    }
    public static<T> T queryEntity(Class<T> entityClass,String sql,Object... params){
        T entity = null;
        try{
            Connection connection = getConnection();
            entity = QUERY_RUNNER.query(connection,sql,new BeanHandler<T>(entityClass),params);

        }catch (SQLException e){
            LOGGER.error("execute sql failure",e);
            throw new RuntimeException(e);
        }
        return entity;
    }

    public static<T> List<T> queryEntityList(Class<T> entityClass,String sql,Object... params){
        List<T> entityList = null;
        try {
            Connection conn = getConnection();
            entityList = QUERY_RUNNER.query(conn,sql,new BeanListHandler<T>(entityClass),params);
        }catch (SQLException e){
            LOGGER.error("query entity list failure",e);
             throw new RuntimeException(e);
        }
        return entityList;
    }

    public static Connection getConnection() {
        Connection conn = CONNECTION_HOLDER.get();
        if ( conn==null){
            try{
                conn = DATA_SOURCES.getConnection();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }finally {
                CONNECTION_HOLDER.set(conn);
            }
        }

        return conn;
    }

    public static void closeConnection() {
       Connection conn = CONNECTION_HOLDER.get();
        if ( conn!=null){
            try{
                conn.close();
            }catch(SQLException e){
                LOGGER.error("close connection failure",e);
                throw new RuntimeException(e);
            }finally{
                CONNECTION_HOLDER.remove();
            }
        }

    }
}
