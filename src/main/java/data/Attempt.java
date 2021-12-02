package data;
import java.sql.*;


class Attempt{
    public static void main(String[] args) {
        try{
            Connection con = establishConnection("own");
            System.out.println("-Gotten connection-" );

            Database database = null;
            database = new Database(con);
            System.out.println(database.tableNames());
            Result typesTable = database.executeQuery("select * from types;");
            System.out.println("Rows: "  + typesTable.rows());
            System.out.println("Printing set");
            typesTable.printSet(10);
            System.out.println("---------------------------------------------------------SPLITTING-LINE ---------------------");
            typesTable.printSet(10);
            System.out.println("---------------------------------------------------------SPLITTING-LINE ---------------------");
            Result participantsTable = database.executeQuery("select * from participants;");
            participantsTable.printSet(11);
            System.out.println("---------------------------------------------------------SPLITTING-LINE ---------------------");
            participantsTable.printSet(11);

        }catch (SQLException sqlExc){
            sqlExc.printStackTrace();
        }

    }
    private static Connection establishConnection(final String DATABASE){
        final String URL = "jdbc:mysql://localhost:3306/" + DATABASE;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            //Class.forName("com.mysql.jdbc.Driver"); is deprecated
        }catch (ClassNotFoundException e){
            System.err.println("Class.forName error");
            e.printStackTrace();
        }
        Connection connection = null;
        try{
            connection = DriverManager.getConnection(URL,"root","1234");
        }catch (SQLException throwables){
            System.err.println("DriverManager.getConnection error");
            throwables.printStackTrace();
        }
        return connection;
    }


}
