import java.awt.*;
import java.awt.event.*;
import com.github.lgooddatepicker.components.*;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.TimePicker;
import oop.Meeting;
import oop.Task;
import oop.Calendar;
import oop.dbConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("serial")
public class CalendarApp extends javax.swing.JFrame {
    boolean isMeeting = false;
    Calendar currentUser = new Calendar();
    Task currTask = new Task();
    Meeting currMeeting = new Meeting();
    dbConnection connection = new dbConnection();
    DatePicker meetDate = new com.github.lgooddatepicker.components.DatePicker();
    DatePicker dateOfMeeting = new com.github.lgooddatepicker.components.DatePicker();
    TimePicker meetStart = new com.github.lgooddatepicker.components.TimePicker();
    TimePicker meetEnd = new com.github.lgooddatepicker.components.TimePicker();
    String meetDay;
    String checkedDay = java.time.LocalDate.now().toString();
    ArrayList<String> usersList = new ArrayList<>();
    String currUser;
    String pass;
    String title, description, eventType, eventDate, participant, progress, startTime, endTime, location;
    HashMap<String, String> todayMeetings = new HashMap<String, String>();
    HashSet<String> reminders = new HashSet<>();
    int taskCount = 0;
    int meetingCount = 0;
    
    public CalendarApp() throws SQLException {
        initComponents();
        loadUserData();
        loadUserMessages();
    }                     

    public void setCurrentUser(String user){
        currUser = user;
    }
    
    public void setCurrentPass(String password) {
    	pass = password;
    }

    public void meetingReminder() throws SQLException, ParseException {
        String current_time = java.time.LocalTime.now().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        long cur_time = sdf.parse(current_time).getTime();
        String m_title, m_time;
        for (Map.Entry<String, String> m: todayMeetings.entrySet()){
            m_title = m.getKey().toString();
            m_time = m.getValue().toString();
            long meet = sdf.parse(m_time).getTime();
            long diff = (meet-cur_time)/60000;
            if (0 < diff && diff <15 && !reminders.contains(m_title)){
                reminders.add(m_title);
                JOptionPane.showMessageDialog(null, String.format(" The %s is starting  in %s minutes", m_title, diff));
            }
        }
    }
    
    private void loadUserData() throws SQLException{
        title_label.setText(String.format(" %s Calendar", currUser));
        currentUser.setUserName(currUser);
        todayMeetings.clear();
        reminders.clear();
        ResultSet tasks = currentUser.getTasks(connection, checkedDay);
        DefaultTableModel table = new DefaultTableModel();
        table.addColumn("Title");
        table.addColumn("Description");
        table.addColumn("Type");
        table.addColumn("Event Date");
        table.addColumn("Progress");
        table.addColumn("Start Time");
        table.addColumn("End Time");
        table.addColumn("Participant");

        taskCount = 0;
        while (tasks.next()){
            String taskTitle = tasks.getString("title");
            String taskDesc= tasks.getString("description");
            String taskDate = tasks.getString("event_date");
            String taskState = tasks.getString("progress");
            String taskUsers = tasks.getString("participant");
            table.addRow(new Object[]{taskTitle, taskDesc,"Task",taskDate, taskState,null,null,taskUsers});
            taskCount++;
        }

        ResultSet meetings = currentUser.get_meetings(connection, checkedDay);
        HashSet<String> meets = new HashSet<String>();
        meetingCount = 0;
        while (meetings.next()){
            String meetingTitle = meetings.getString("title");
            String meetingDesc = meetings.getString("description");
            String meetingDate = meetings.getString("event_date");
            String meetingStart = meetings.getString("start_time");
            String meetingEnd = meetings.getString("end_time");
            String meetingMembr = meetings.getString("participant");

            if (!meets.contains(meetingTitle)){
                table.addRow(new Object[]{meetingTitle,meetingDesc,"Meeting", meetingDate,null,meetingStart,meetingEnd,meetingMembr}); 
                meetingCount++;
            }
            meets.add(meetingTitle);
            todayMeetings.put(meetingTitle, meetingStart);
        }
        
        tasks.close();
        meetings.close();
        DefaultTableModel stat = new DefaultTableModel();
        stat_table.setModel(stat);
        stat.addColumn("Type");
        stat.addColumn("Count");
        stat.addRow(new Object[]{"Tasks", String.valueOf(taskCount)});
        stat.addRow(new Object[]{"Meetings", String.valueOf(meetingCount)});
    }
    
    private void loadUserMessages() throws SQLException{
        currentUser.setUserName(currUser);
        ResultSet msgs = currentUser.get_messages(connection,checkedDay);
        DefaultTableModel table = new DefaultTableModel();
        msg_table.setModel(table);
        table.addColumn("Messages");
        table.addColumn("Notification Date");
        table.addColumn("Notification Time");
        while (msgs.next()){
            String msg_content = msgs.getString("notification");
            String msg_date= msgs.getString("notify_date");
            String msg_time= msgs.getString("notify_time");
            table.addRow(new Object[]{msg_content, msg_date, msg_time}); 
        }
        if (msgs != null) 
        	msgs.close();
    }    
    
    private void create_btnActionPerformed(java.awt.event.ActionEvent evt) {
        isMeeting = type_in.getSelectedItem().toString().equals("Meeting"); 
        JTextField eTitle = new JTextField();
        JTextField description = new JTextField();
        JTextField user = new JTextField();
        
        if (isMeeting){
            Object[] meeting_fields = {
                "Title", eTitle,
                "Description", description,
                "Invite User (Separate usrs by ',')", user,
                "Date of meeting",dateOfMeeting,
                "Start Time", meetStart,
                "End Time", meetEnd,
            };
            
            JOptionPane.showConfirmDialog(null, meeting_fields, "Enter the details of the meeting", JOptionPane.OK_CANCEL_OPTION); 

            String start = meetStart.getTimeStringOrEmptyString();
            String end = meetEnd.getTimeStringOrEmptyString();
            if (eTitle.getText().isEmpty() || description.getText().isEmpty() || user.getText().isEmpty()
                    || start.isEmpty() || end.isEmpty()) 
            	JOptionPane.showMessageDialog(null, "Information cannot be empty");
            
            String users = user.getText();
            currMeeting.setTitle(eTitle.getText());
            currMeeting.setUserName(currUser);
            currMeeting.setDescription(description.getText());
            currMeeting.setEventDate(checkedDay);
            currMeeting.setStartTime(start);
            currMeeting.setEndTime(end);
            currMeeting.setType("Meeting");
            
            try {
                try {
                    boolean created = currMeeting.create(connection, users, currUser);
                    if (!created){
                        JOptionPane.showMessageDialog(null, "Failed to create meeting, timing conflict, choose another time.");
                    }
                } catch (ParseException ex) {
                    Logger.getLogger(CalendarApp.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (SQLException ex) {
                Logger.getLogger(CalendarApp.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } else {
            Object[] taskFields = {
                "Title", eTitle,
                "Description", description,
            };
            JOptionPane.showConfirmDialog(null, taskFields, "Enter the task details", JOptionPane.OK_CANCEL_OPTION);
            if (eTitle.getText().isEmpty() || description.getText().isEmpty()) 
            	JOptionPane.showMessageDialog(null, "Enter all the information");
            
            currTask.setTitle(eTitle.getText());
            currTask.setDescription(description.getText());
            currTask.setEventDate(checkedDay);
            currTask.setUserName(currUser);
            currTask.setParticipant(currUser);
            currTask.setType("Task");
            
            try {
                currTask.create(connection);
            } catch (SQLException ex) {
                Logger.getLogger(CalendarApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        try {
            loadDispMsgData();
        } catch (SQLException ex) {
            Logger.getLogger(CalendarApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            loadUserMessages();
        } catch (SQLException ex) {
            Logger.getLogger(CalendarApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
    
    private void displayBtnActionPerformed(java.awt.event.ActionEvent evt) {
        try {
			loadDispMsgData();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }  

    private void logOutBtnMouseClicked(java.awt.event.MouseEvent evt) {
        userLogin login = new userLogin();
        login.setVisible(true);
        setVisible(false);
    }                                        

    private void calendarPanel2PropertyChange(java.beans.PropertyChangeEvent evt) {
        LocalDate selected_day = calendarPanel2.getSelectedDate();
        if (selected_day == null) {
            checkedDay = java.time.LocalDate.now().toString();
            try {
                loadUserMessages();
            } catch (SQLException ex) {
                Logger.getLogger(CalendarApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else{
	        checkedDay = selected_day.toString();
	        try {
	            loadUserMessages();
	        } catch (SQLException ex) {
	            Logger.getLogger(CalendarApp.class.getName()).log(Level.SEVERE, null, ex);
	        }
	    
	        try {
	            loadUserData();
	        } catch (SQLException ex) {
	            Logger.getLogger(CalendarApp.class.getName()).log(Level.SEVERE, null, ex);
	        }
        }
    }
    
    public void loadDispMsgData() throws SQLException {
    	LocalDate selected_day = calendarPanel2.getSelectedDate();
        if (selected_day == null) {
            checkedDay = java.time.LocalDate.now().toString();
        }
        else {
        	checkedDay = selected_day.toString();
        	try {
    			DisplayTable disp = new DisplayTable(currentUser, checkedDay, pass, currUser);
    			setVisible(false);
    			disp.setVisible(true);
    			disp.pack();
    	        disp.setLocationRelativeTo(null);
    		} catch (SQLException e) {
    			e.printStackTrace();
    		}
        }
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CalendarApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CalendarApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CalendarApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CalendarApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(() -> {
            try {
                new CalendarApp().setVisible(true);
            } catch (SQLException ex) {
                Logger.getLogger(CalendarApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }   

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - Savio
		create_btn = new JButton();
		type_in = new JComboBox<>();
		create_type_opt = new JLabel();
		jLabel1 = new JLabel();
		calendarPanel2 = new CalendarPanel();
		jScrollPane1 = new JScrollPane();
		msg_table = new JTable();
		log_out_btn = new JButton();
		jLabel4 = new JLabel();
		jScrollPane4 = new JScrollPane();
		stat_table = new JTable();
		title_label = new JLabel();
		display_btn = new JButton();
		jLabel2 = new JLabel();

		//======== this ========
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("Calendar App");
		var contentPane = getContentPane();

		//---- create_btn ----
		create_btn.setText("Create");
		create_btn.addActionListener(e -> create_btnActionPerformed(e));

		//---- type_in ----
		type_in.setModel(new DefaultComboBoxModel<>(new String[] {
			"Task",
			"Meeting"
		}));

		//---- create_type_opt ----
		create_type_opt.setFont(new Font("Segoe UI Historic", Font.BOLD | Font.ITALIC, 18));
		create_type_opt.setText("Please select a type");
		create_type_opt.setForeground(new Color(0, 153, 153));

		//---- jLabel1 ----
		jLabel1.setFont(new Font("Yu Gothic UI Semibold", Font.BOLD | Font.ITALIC, 18));
		jLabel1.setForeground(new Color(0, 153, 153));
		jLabel1.setText("Notifications (Read only)");

		//---- calendarPanel2 ----
		calendarPanel2.addPropertyChangeListener(e -> calendarPanel2PropertyChange(e));

		//======== jScrollPane1 ========
		{

			//---- msg_table ----
			msg_table.setModel(new DefaultTableModel(
				new Object[][] {
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
				},
				new String[] {
					"Title 1", "Title 2", "Title 3", "Title 4"
				}
			));
			jScrollPane1.setViewportView(msg_table);
		}

		//---- log_out_btn ----
		log_out_btn.setText("LogOut");
		log_out_btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				logOutBtnMouseClicked(e);
			}
		});

		//---- jLabel4 ----
		jLabel4.setFont(new Font("Segoe UI Historic", Font.BOLD | Font.ITALIC, 18));
		jLabel4.setForeground(new Color(0, 153, 153));
		jLabel4.setText("Today's Statistics");

		//======== jScrollPane4 ========
		{

			//---- stat_table ----
			stat_table.setModel(new DefaultTableModel(
				new Object[][] {
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
				},
				new String[] {
					"Title 1", "Title 2", "Title 3", "Title 4"
				}
			));
			jScrollPane4.setViewportView(stat_table);
		}

		//---- title_label ----
		title_label.setFont(new Font("Segoe UI Historic", Font.BOLD | Font.ITALIC, 24));
		title_label.setText("Calendar");

		//---- display_btn ----
		display_btn.setText("Display Tasks/Meetings");
		display_btn.addActionListener(e -> create_btnActionPerformed(e));

		//---- jLabel2 ----
		jLabel2.setFont(new Font("Yu Gothic UI Semibold", Font.BOLD | Font.ITALIC, 18));
		jLabel2.setForeground(new Color(0, 153, 153));
		jLabel2.setText("Select a date from the calendar");

		GroupLayout contentPaneLayout = new GroupLayout(contentPane);
		contentPane.setLayout(contentPaneLayout);
		contentPaneLayout.setHorizontalGroup(
			contentPaneLayout.createParallelGroup()
				.addGroup(contentPaneLayout.createSequentialGroup()
					.addGap(38, 38, 38)
					.addGroup(contentPaneLayout.createParallelGroup()
						.addComponent(jLabel4, GroupLayout.PREFERRED_SIZE, 163, GroupLayout.PREFERRED_SIZE)
						.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
							.addGroup(GroupLayout.Alignment.LEADING, contentPaneLayout.createSequentialGroup()
								.addGroup(contentPaneLayout.createParallelGroup()
									.addComponent(jScrollPane4, GroupLayout.PREFERRED_SIZE, 313, GroupLayout.PREFERRED_SIZE)
									.addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 298, GroupLayout.PREFERRED_SIZE))
								.addGap(41, 41, 41)
								.addGroup(contentPaneLayout.createParallelGroup()
									.addGroup(contentPaneLayout.createSequentialGroup()
										.addComponent(title_label, GroupLayout.PREFERRED_SIZE, 360, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(log_out_btn))
									.addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 298, GroupLayout.PREFERRED_SIZE)
									.addGroup(contentPaneLayout.createSequentialGroup()
										.addComponent(create_type_opt, GroupLayout.PREFERRED_SIZE, 177, GroupLayout.PREFERRED_SIZE)
										.addGap(18, 18, 18)
										.addComponent(type_in, GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE)
										.addGap(18, 18, 18)
										.addComponent(create_btn)
										.addGap(18, 18, 18)
										.addComponent(display_btn))))
							.addGroup(GroupLayout.Alignment.LEADING, contentPaneLayout.createSequentialGroup()
								.addComponent(calendarPanel2, GroupLayout.PREFERRED_SIZE, 297, GroupLayout.PREFERRED_SIZE)
								.addGap(57, 57, 57)
								.addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 774, GroupLayout.PREFERRED_SIZE))))
					.addGap(0, 42, Short.MAX_VALUE))
		);
		contentPaneLayout.setVerticalGroup(
			contentPaneLayout.createParallelGroup()
				.addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
					.addGap(35, 35, 35)
					.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(title_label, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
						.addComponent(log_out_btn))
					.addGap(12, 12, 12)
					.addComponent(jLabel4)
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
					.addGroup(contentPaneLayout.createParallelGroup()
						.addComponent(jScrollPane4, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
						.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							.addComponent(create_type_opt)
							.addComponent(type_in, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(create_btn)
							.addComponent(display_btn)))
					.addGap(18, 18, 18)
					.addGroup(contentPaneLayout.createParallelGroup()
						.addComponent(jLabel1)
						.addComponent(jLabel2))
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
					.addGroup(contentPaneLayout.createParallelGroup()
						.addComponent(calendarPanel2, GroupLayout.PREFERRED_SIZE, 224, GroupLayout.PREFERRED_SIZE)
						.addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 215, GroupLayout.PREFERRED_SIZE))
					.addGap(25, 25, 25))
		);
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - Savio
	private JButton create_btn;
	private JComboBox<String> type_in;
	private JLabel create_type_opt;
	private JLabel jLabel1;
	private CalendarPanel calendarPanel2;
	private JScrollPane jScrollPane1;
	private JTable msg_table;
	private JButton log_out_btn;
	private JLabel jLabel4;
	private JScrollPane jScrollPane4;
	private JTable stat_table;
	private JLabel title_label;
	private JButton display_btn;
	private JLabel jLabel2;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}