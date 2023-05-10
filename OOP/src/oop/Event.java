package oop;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Event {

    String eventId;
    String title;
    String description;
    String eventDate;
    String participant;
    String userName;
    String type;
    ArrayList<String> userList = new ArrayList<>();;

    Event(){
    }

    public void setUserName(String user){
        userName = user;
    }

    public void setDescription(String desc){
        description = desc;
    }

    public void setTitle(String eTitle){
        title = eTitle;
    }

    public void setEventDate(String eDate){
        eventDate = eDate;
    }
    
    public void setParticipant(String userListString){
        participant = userListString;
    }
    
    public void setType(String eType){
        type = eType;
    }

    public void generateUsersList(){
        userList.clear();
        String[] users = participant.split("\\s*,\\s*");
        userList.addAll(Arrays.asList(users));
    }
    
    public ArrayList<String> getUsersList(){
        return userList;
    }

    public void create(dbConnection connection) throws SQLException {
        connection.insertData(eventId, title, description, eventDate, "random", "random", userName, type, "0", participant);
    }

    public void update(dbConnection connection, String oldTitle) throws SQLException {
        String sql = String.format("UPDATE event SET title = '%s', description = '%s', event_date = '%s', user_name = '%s',  participant = '%s' WHERE title = '%s' AND user_name = '%s'",
                eventId, title, description,eventDate, userName, userName, oldTitle, userName);
        ResultSet result = connection.executeQuery(sql);
        if (result != null) 
        	result.close();
    }
    
    public void delete(dbConnection connection) throws SQLException{
        String sql = String.format("DELETE FROM EVENT WHERE user_name = '%s' AND title = '%s' AND event_type ='%s' ",userName, title, type);
        ResultSet result = connection.executeQuery(sql);
        if (result != null) 
        	result.close();
    }
    
}