/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package oop;

import java.sql.*;
public class Calendar {
	
    private String userName;
    private String password;

    public Calendar(){
    }
    
    public void setUserName(String name){
        userName = name;
    }
    
    public String getUser_name() {
		return userName;
	}

//	public void setUser_name(String user_name) {
//		this.userName = user_name;
//	}

	public ResultSet getTasks(dbConnection connection, String selectedDay) throws SQLException{
        String sql = String.format("SELECT * FROM EVENT WHERE user_name = '%s' AND event_type = 'Task' AND event_date = '%s' ", 
        		userName, selectedDay);
        ResultSet res = connection.executeQuery(sql);
        return res;
    }
    public ResultSet get_meetings(dbConnection connection, String selectedDay) throws SQLException{
        String sql = String.format("SELECT * FROM EVENT WHERE user_name = '%s' AND event_type = 'Meeting' AND event_date = '%s' ", 
        		userName, selectedDay);
        ResultSet result = connection.executeQuery(sql);
        return result;
    }
    
    public ResultSet get_messages(dbConnection connection, String selectedDay) throws SQLException{
        String sql = String.format("SELECT * FROM MESSAGES WHERE user_name = '%s' AND notify_date = '%s' ", 
        		userName, selectedDay);
        ResultSet result = connection.executeQuery(sql);
        return result;
    }
    
}
