package data;
import java.sql.*;
import java.util.Scanner;


class Attempt{
    public static void main(String[] args) {

        try{
            Connection con = establishConnection("own");
            System.out.println("-Gotten connection-" );
            Database database = new Database(con);
            System.out.println(database.tableNames());
            database.executeUpdate("UPDATE frisk set id = 5 where age = 13;");
            Scanner scan = new Scanner(System.in);
            String input = null;
            while(input == null || !input.equals("exit")){
                input = scan.nextLine();
                try{
                    database.executeQuery(input).printSet(15);
                }catch (SQLException sqlException){
                    System.err.println("Invalid query");
                    sqlException.printStackTrace();

                }
            }

            Result typesTable = database.executeQuery("select * from types;");
            System.out.println("Rows: "  + typesTable.rows());
            typesTable.printSet(10);
            splittingLine();
            Result participantsTable = database.executeQuery("select * from participants;");
            participantsTable.printSet(11);

        }catch (SQLException sqlExc){
            sqlExc.printStackTrace();
        }

    }
    private static void splittingLine(){
        System.out.println("------------------------------------------------------------------------------SPLITTING-LINE ---------------------");
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
