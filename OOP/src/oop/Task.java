package oop;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Task extends Event {
    String progress = "0";

    public void setProgress(String val) {
        progress = val;
    }

    public void update(dbConnection connection, String oldTitle) throws SQLException {
        String sql = String.format("UPDATE event SET title = '%s', description = '%s', event_date = '%s', user_name = '%s', progress = '%s', participant = '%s' WHERE title = '%s' AND user_name = '%s'",
                title, description, eventDate, userName, progress, userName, oldTitle, userName);
        ResultSet result = connection.executeQuery(sql);
        if (result != null) {
        	result.close();
        }        	
    }
}
