package data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

class Database{
    public Connection con;
    public Statement statement;
    public Statement noSetStatement;
    public Database(Connection con) throws SQLException{
        statement = con.prepareStatement(" ", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        noSetStatement = con.createStatement();
        this.con = con;
    }
    public List<String> tableNames() throws SQLException{
        List<String> tables = new ArrayList<>();
        Result result = new Result(statement.executeQuery("SHOW TABLES;"));
        while(result.next()){
            tables.add(result.getString(1));
        }
        return tables;
    }
    public Result executeQuery(String query) throws SQLException{
        return new Result(statement.executeQuery(query));
    }
    public int executeUpdate(String query) throws SQLException{
        return noSetStatement.executeUpdate(query);
    }
    public boolean execute(String query) throws SQLException{
        return noSetStatement.execute(query);
    }
}
