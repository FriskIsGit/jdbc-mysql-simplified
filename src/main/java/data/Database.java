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
    public Database(Connection con) throws SQLException{
        statement = con.createStatement();
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
}
