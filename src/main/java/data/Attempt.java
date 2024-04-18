package data;

import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Connection;

enum Run {
    TEST,
    DEFAULT
}

class Attempt {
    private static final Run RUN_TYPE = Run.DEFAULT;

    public static void main(String[] args) {
        try {
            Connection con = establishConnection("own");
            Database database = new Database(con);
            System.out.println(database.getTables());

            if (Run.TEST == RUN_TYPE) {
                long start = System.currentTimeMillis();
                try {
                    runInsertTest(database);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                long end = System.currentTimeMillis();
                System.out.println("Time taken to insert 1000 rows: " + (end - start));
            }
            database.simulateConsole();

        } catch (SQLException sqlExc) {
            sqlExc.printStackTrace();
        }

    }

    protected static void runInsertTest(Database database) throws FileNotFoundException {
        System.out.println("Insert test running");
        BufferedReader nameReader = new BufferedReader(new FileReader("src/test/1000names.txt"));
        BufferedReader lastNameReader = new BufferedReader(new FileReader("src/test/1000lastnames.txt"));
        BufferedReader schoolReader = new BufferedReader(new FileReader("src/test/1000schools.txt"));
        String name, lastName, school;
        try {
            while ((name = nameReader.readLine()) != null && (lastName = lastNameReader.readLine()) != null && (school = schoolReader.readLine()) != null) {
                database.executeUpdate(
                        "INSERT INTO students (name,lastname,school) VALUES (" +
                        stringify(name) + ", " +
                        stringify(lastName) + ", " +
                        stringify(school) + ");"
                );
            }
        } catch (IOException | SQLException exceptions) {
            System.err.println("Error io/sql");
            exceptions.printStackTrace();
        }
        System.out.println("Insert test finished");
    }

    //enclosed with quotation marks instead of apostrophes due to errors in SQL syntax
    public static String stringify(String str) {
        return "\"" + str + "\"";
    }

    protected static Connection establishConnection(final String DATABASE) {
        JSONObject jsonMap = parseJSON("./src/main/resources/config.json");
        final String PASSWORD = jsonMap.getString("password");
        final String USER = jsonMap.getString("user");
        final int PORT = jsonMap.getInt("port");
        final String URL = "jdbc:mysql://localhost:" + PORT + "/" + DATABASE;
        // Since JDBC 4.0 loading using Class.forName() can be omitted.
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("-Established connection-");
        } catch (SQLException throwables) {
            System.err.println("DriverManager.getConnection error");
            throwables.printStackTrace();
        }
        return connection;
    }

    private static JSONObject parseJSON(final String configPath) {
        BufferedReader reader;
        JSONObject jsonMap = null;
        try {
            reader = new BufferedReader(new FileReader(configPath));
            StringBuilder contents = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                contents.append(line);
            }
            jsonMap = new JSONObject(contents.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonMap;
    }
}
