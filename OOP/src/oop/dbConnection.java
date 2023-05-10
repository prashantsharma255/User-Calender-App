package oop;

import java.sql.*;

public class dbConnection {
    private static final String oracleURL = "jdbc:oracle:thin:@localhost:1521/xe";
    private static final String user = "SYS as SYSDBA";
    private static final String pass = "oracle";
    
    public Connection connection = null;
    
    public dbConnection(){
        try {
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
        } catch (SQLException e) {
            System.err.println("Unable to load driver.");
        }
        try{
            connection = DriverManager.getConnection(oracleURL,user, pass);
        } catch (SQLException e) {
        	e.printStackTrace();
        }
    }
    
    public void createTables() throws SQLException{
        String user;
        
        user = "CREATE TABLE Users("
                + "user_name varchar2(20) primary key,"
                + "password varchar2(20))";
        
        String event;
        
        event = "CREATE TABLE Event ("
                + "event_id varchar2(10),"
                + "title varchar2(50), "
                + "description varchar2(300), "
                + "event_date varchar2(50),"
                + "start_time varchar2(50),"
                + "end_time varchar2(50), "
                + "user_name varchar2(20),"
                + "event_type varchar2(10), "
                + "progress varchar2(10),"
                + "participant varchar2(50),"
                + "foreign key(user_name) references users(user_name))";
        
        String messages;
        
        messages = "CREATE TABLE MESSAGES("
                + "user_name varchar2(20),"
                + "notification varchar2(200),"
                + "notify_date varchar2(20),"
                + "notify_time varchar2(20))";
        
        Statement statement = connection.createStatement();
        statement.execute(user);
        statement.execute(event);
        statement.execute(messages);
        statement.close();
    }

    public void insertData(String eventId, String title, String description, String eventDate, String start, String end, String user, String type, String progress, String userListString) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO EVENT VALUES (?,?,?,?,?,?,?,?,?,?)");
        statement.setString(1, eventId);
        statement.setString(2, title);
        statement.setString(3, description);
        statement.setString(4, eventDate);
        statement.setString(5, start);
        statement.setString(6, end);
        statement.setString(7, user);
        statement.setString(8, type);
        statement.setString(9, progress);
        statement.setString(10, userListString);
        statement.executeUpdate();
        statement.close();
    }
    
    public ResultSet executeQuery(String sql) throws SQLException{
        Statement statement = connection.createStatement();
        statement.execute(sql);
        ResultSet result = statement.getResultSet();
        return result;
    }
    
    public void insertUserData(String username,String password) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO Users VALUES (?,?)"); 
           statement.setString(1, username);
           statement.setString(2, password);
           statement.executeUpdate();
   }
}