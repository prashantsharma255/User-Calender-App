package oop;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Meeting extends Event {
    String startTime;
    String endTime;
    List<String> oldMemList = new ArrayList<>();

    public void setStartTime(String start){
        startTime = start;
    }
    
    public void setEndTime(String end){
        endTime = end;
    }
    
    public boolean create(dbConnection connection, String usersListString, String currUser) throws SQLException, ParseException{		//changed
        if (!checkAvailability(usersListString, connection)) 
        	return false;
        setParticipant(usersListString);
        generateUsersList();
        String deliverMessage = String.format("Meeting %s has been created.", title);
        connection.insertData(eventId, title, description, eventDate, startTime, endTime, currUser, "Meeting", null, participant);
        for (String user : userList){
        	if(!user.equals(currUser)) {
        		userName = user;
                connection.insertData(eventId, title, description, eventDate, startTime, endTime, userName, "Meeting", null, participant);
                notifyListener(user, connection, deliverMessage);
        	}
        }
        return true;
    }
    
    public void getMemberList(String usr){
        String[] users = usr.split("\\s*,\\s*");
        oldMemList.clear();
        oldMemList.addAll(Arrays.asList(users));
    }
    
    public boolean update(dbConnection connection, String oldParticipant, String oldTitle, String currUser) throws SQLException, ParseException{
        String sql = String.format("title = '%s', description = '%s', event_date = '%s', participant = '%s', start_time = '%s', end_time = '%s'",
                title, description, eventDate, participant, startTime,endTime);
        String updateQuery = "UPDATE event SET " + sql + String.format(" WHERE title = '%s' AND event_type = 'Meeting' AND event_date = '%s'",
                oldTitle, eventDate);
        String message = String.format("%s has changed. Updated meeting: title: %s,description: %s,event_date: %s,participant: %s,start_time: %s,end_time: %s", 
        		oldTitle,title, description, eventDate, participant, startTime, endTime);
        String updateSql ;
        generateUsersList();
        if (participant.equals(oldParticipant)){
        	updateSql = updateQuery + String.format(" AND user_name = '%s'", currUser);
        	connection.executeQuery(updateSql);
            notifyListener(currUser, connection, message);
            for (String u : userList){
                userName = u;
                updateSql = updateQuery + String.format(" AND user_name = '%s'", u);
                connection.executeQuery(updateSql);
                notifyListener(u, connection, message);
            }
        } else {
            if (!checkAvailability(participant, connection)) {
            	return false;
            }	
            getMemberList(oldParticipant);
            if (oldMemList.contains(currUser)){
                connection.executeQuery(updateQuery);
                notifyListener(currUser, connection, message);
            }else{
                connection.insertData(eventId, title, description, eventDate, startTime, endTime, currUser, "Meeting", null, participant);
                notifyListener(currUser, connection, message);
            }
            for (String u : userList){
                userName = u;
                if (oldMemList.contains(u)){
                    connection.executeQuery(updateQuery);
                    notifyListener(u, connection, message);
                }else{
                    connection.insertData(eventId, title, description, eventDate, startTime, endTime, userName, "Meeting", null, participant);
                    notifyListener(u, connection, message);
                }
            }
            for (String u : oldMemList){
                userName = u;
                if (!userList.contains(u)){
                    delete(connection);
                }
            }
        }
        return true;
    }
    
    public void notifyListener(String user, dbConnection connection, String updateMessage) throws SQLException{
        String date = java.time.LocalDate.now().toString();
        String time = java.time.LocalTime.now().toString();
        String deliveryMessage = String.format("INSERT INTO messages values('%s', '%s', '%s', '%s' )", user, updateMessage, date, time);
        connection.executeQuery(deliveryMessage);
    }
    
    public void delete(dbConnection connection) throws SQLException{
        String deliveryMessage = String.format(" %s has been cancelled", title);
        String sql = String.format("DELETE FROM EVENT WHERE user_name = '%s' AND title = '%s' AND event_type = 'Meeting'", userName, title);
        ResultSet result = connection.executeQuery(sql);
        notifyListener(userName, connection, deliveryMessage);
        if (result != null) {
        	result.close();
        }
    }
    
    @SuppressWarnings("unused")
	public boolean checkAvailability(String usersList, dbConnection connection) throws SQLException, ParseException{
        String[] users = usersList.split("\\s*,\\s*");
        long s1, e1;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        long s2 = simpleDateFormat.parse(startTime).getTime();
        long e2= simpleDateFormat.parse(endTime).getTime();
        for (String u : users){
            String sql = String.format("SELECT start_time, end_time FROM EVENT WHERE event_type ='Meeting' AND user_name = '%s' AND event_date = '%s'", userName, eventDate);
            ResultSet result = connection.executeQuery(sql);
            if (result != null){
                while (result.next()){
                    s1 = simpleDateFormat.parse(result.getString("start_time")).getTime();
                    e1 = simpleDateFormat.parse(result.getString("end_time")).getTime();
                    if (! (e1 <= s2 || s1>=e2)) 
                    	return false;
                }
                result.close();
            }
        }
        return true;
    }
}
