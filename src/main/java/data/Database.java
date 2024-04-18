package data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Database {
    public Connection con;
    public Statement statement;

    public Database(Connection con) throws SQLException {
        statement = con.prepareStatement(" ", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        this.con = con;
    }

    public List<String> getTables() throws SQLException {
        List<String> tables = new ArrayList<>();
        Result result = executeQuery("SHOW TABLES;");
        while (result.next()) {
            tables.add(result.getString(1));
        }
        return tables;
    }

    //produces a set
    public Result executeQuery(String query) throws SQLException {
        return new Result(statement.executeQuery(query));
    }

    //generally does not produce sets (data manipulation) INSERT, UPDATE or DELETE..
    public int executeUpdate(String query) throws SQLException {
        return statement.executeUpdate(query);
    }

    //true if ResultSet is returned, false if update is made
    public boolean execute(String query) throws SQLException {
        return statement.execute(query);
    }

    public Result getResultSet() throws SQLException {
        return new Result(statement.getResultSet());
    }

    public void simulateConsole() {
        Scanner scan = new Scanner(System.in);
        String input;
        while ((input = scan.nextLine()) != null && !input.equals("exit")) {
            if (input.length() == 0) continue;
            try {
                if (this.execute(input)) {
                    this.getResultSet().printSet(40);
                } else {
                    System.out.println("Query produced no result set and executed successfully");
                }
            } catch (SQLException sqlException) {
                System.err.println("Invalid query");
            }
        }
    }
}
