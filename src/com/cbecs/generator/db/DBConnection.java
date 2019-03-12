package com.cbecs.generator.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cbecs.generator.entity.DbTable;
import com.cbecs.generator.util.CommonUtils;

public class DBConnection
{

    private static Logger logger = LoggerFactory.getLogger(DBConnection.class);
    private static String url = "jdbc:mysql://{0}:{1}/{2}?useUnicode=true&characterEncoding={3}";
    private static String dnurl = "";
    private static String port = "3306";
    private static String username = "";
    private static String password = "";
    @SuppressWarnings("unused")
    private static String sections = "mysql_conn_info";

    private Connection conn = null;

    public DBConnection setDbInfo(String ip, String dnname, String encode, String userName, String pwd)
    {
        dnurl = CommonUtils.format(url, ip, port, dnname, encode);
        username = userName;
        password = pwd;
        return this;
    }

    public DBConnection setDbInfo(String sections2, String ip, String port, String dnname, String encode, String userName, String pwd)
    {
        sections = sections2;
        dnurl = CommonUtils.format(url, ip, port, dnname, encode);
        username = userName;
        password = pwd;
        return this;
    }

    public Connection getConnection()
    {
        try
        {
            // 加载MySql的驱动类
            Class.forName("com.mysql.jdbc.Driver");
            // 连接MySql数据库，用户名和密码都是root
            DriverManager.setLoginTimeout(3);
            conn = DriverManager.getConnection(dnurl, username, password);
        }
        catch (Exception e)
        {
            logger.error("找不到驱动程序类 ，加载驱动失败！");
        }
        return conn;
    }

    public Statement getStatement()
    {
        Statement stat = null;
        try
        {
            conn = getConnection();
            stat = conn.createStatement();
        }
        catch (Exception e)
        {
            logger.error("创建Statement失败！", e);
        }
        return stat;
    }

    public PreparedStatement getPreparedStatement(String sql)
    {
        PreparedStatement stat = null;
        try
        {
            conn = getConnection();
            if (conn != null)
            {
                stat = conn.prepareStatement(sql);
            }            
        }
        catch (Exception e)
        {
            logger.error("创建PreparedStatement失败！", e);
        }
        return stat;
    }

    public List<DbTable> getTableNameList() throws SQLException
    {
        try
        {
            conn = getConnection();
        }
        catch (Exception e)
        {
            logger.error("创建oracle连接失败！", e);
        }
        DatabaseMetaData dbmd = conn.getMetaData();
        // 访问当前用户ANDATABASE下的所有表
        ResultSet rs = dbmd.getTables("null", username.toUpperCase(), "%", new String[]
        { "TABLE" });
        List<DbTable> tableList = new ArrayList<DbTable>();
        DbTable table = null;
        while (rs.next())
        {
            table = new DbTable();
            ResultSet rsclon = dbmd.getColumns(null, "%", rs.getString("TABLE_NAME"), "%");
            while (rsclon.next())
            {
                table.setTableSchema("orcl").setTableName(rs.getString("table_name")).setTableComment(rsclon.getString("COLUMN_NAME"));
                tableList.add(table);
            }
        }
        return tableList;
    }

    public DBConnection close(ResultSet rs, PreparedStatement stmt)
    {
        close(rs, stmt, conn);
        return this;
    }

    public DBConnection close(ResultSet rs, PreparedStatement stmt, Connection conn)
    {
        if (rs != null)
        { // 关闭记录集
            try
            {
                rs.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        if (stmt != null)
        { // 关闭声明
            try
            {
                stmt.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        if (conn != null)
        { // 关闭连接对象
            try
            {
                conn.close();
                conn = null;
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        return this;
    }

    public DBConnection close(ResultSet rs, Statement stmt)
    {
        close(rs, stmt, conn);
        return this;
    }

    public DBConnection close(ResultSet rs, Statement stmt, Connection conn)
    {
        if (rs != null)
        { // 关闭记录集
            try
            {
                rs.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        if (stmt != null)
        { // 关闭声明
            try
            {
                stmt.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        if (conn != null)
        { // 关闭连接对象
            try
            {
                conn.close();
                conn = null;
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        return this;
    }

}
