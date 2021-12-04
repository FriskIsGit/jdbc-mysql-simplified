package data;
import java.io.*;
import java.sql.*;

import static java.lang.System.err;
import static java.lang.System.out;

class Attempt{
    public static void main(String[] args)  {
        try{

            Connection con = establishConnection("own");
            Database database = new Database(con);
            out.println(database.tableNames());
            database.simulateConsole();
            long start = System.currentTimeMillis();
            try{
                runInsertTest(database);
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
            long end = System.currentTimeMillis();
            out.println("Time taken to insert 1000 rows: " + (end-start));
            database.simulateConsole();


        }catch (SQLException sqlExc){
            sqlExc.printStackTrace();
        }

    }

    private static void runInsertTest(Database database) throws FileNotFoundException{
        out.println("Insert test running");
        BufferedReader nameReader = new BufferedReader(new FileReader("src/test/1000names.txt"));
        BufferedReader lastNameReader = new BufferedReader(new FileReader("src/test/1000lastnames.txt"));
        BufferedReader schoolReader = new BufferedReader(new FileReader("src/test/1000schools.txt"));
        String name,lastName,school;
        try{
            while ((name = nameReader.readLine()) != null && (lastName = lastNameReader.readLine()) != null && (school = schoolReader.readLine()) != null){
                database.executeUpdate("INSERT INTO students (name,lastname,school) VALUES (" + stringify(name) + ", " + stringify(lastName) + ", " + stringify(school) + ");");
            }
        }
        catch(IOException | SQLException exceptions){
            err.println("Error io/sql");
            exceptions.printStackTrace();
        }
        out.println("Insert test finished");
    }
    //enclosed with quotation marks instead of apostrophes due to errors in SQL syntax
    public static String stringify(String str){
        return "\"" + str + "\"";
    }

    private static void splittingLine(){
        out.println("------------------------------------------------------------------------------------------------------------------------");
    }
    protected static Connection establishConnection(final String DATABASE){
        final String URL = "jdbc:mysql://localhost:3306/" + DATABASE;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            //Class.forName("com.mysql.jdbc.Driver"); is deprecated but works
        }catch (ClassNotFoundException e){
            err.println("Class.forName error");
            e.printStackTrace();
        }
        Connection connection = null;
        try{
            connection = DriverManager.getConnection(URL,"root","1234");
            out.println("-Established connection-");
        }catch (SQLException throwables){
            err.println("DriverManager.getConnection error");
            throwables.printStackTrace();
        }
        return connection;
    }


}
