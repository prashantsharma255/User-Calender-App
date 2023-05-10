import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;
import javax.swing.table.*;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.TimePicker;

import oop.Meeting;
import oop.Task;
import oop.Calendar;
import oop.dbConnection;

@SuppressWarnings("serial")
public class DisplayTable extends javax.swing.JFrame {
    dbConnection connection = new dbConnection();
    static String checked;
    static Calendar currUser;
    static String user;
    static String pass;
    String title, description, eventType, eventDate, participant, progress, startTime, endTime;
    DatePicker meetDate = new com.github.lgooddatepicker.components.DatePicker();
    TimePicker meetStart = new com.github.lgooddatepicker.components.TimePicker();
    TimePicker meetEnd = new com.github.lgooddatepicker.components.TimePicker();
    String meetDay;
    Task currTask = new Task();
    Meeting currMeeting = new Meeting();
    String currentUser;
    //String checked_day = java.time.LocalDate.now().toString();
    HashMap<String, String> meetingsToday = new HashMap<String, String>();
    HashSet<String> reminders = new HashSet<>();
    
    public DisplayTable(Calendar current_user,String checked_day,String password,String user_name) throws SQLException  {
        checked = checked_day;
        currUser = current_user;
        pass = password;
        user = user_name;
        initComponents();
        loadCalendarData();
    }
    
    private void initComponents() {
		jLabel1 = new JLabel();
		jScrollPane1 = new JScrollPane();
		table1 = new JTable();
		button1 = new JButton();

		//======== this ========
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("Display Table");
		var contentPane = getContentPane();

		//---- jLabel1 ----
		jLabel1.setText("Tasks and Meetings");
		jLabel1.setFont(new Font("Tahoma", Font.BOLD, 14));

		//======== jScrollPane1 ========
		{

			//---- table1 ----
			table1.setModel(new DefaultTableModel(
				new Object[][] {
					{null, null, null, null, null, null, null, null, null},
					{null, null, null, null, null, null, null, null, null},
					{null, null, null, null, null, null, null, null, null},
					{null, null, null, null, null, null, null, null, null},
					{null, null, null, null, null, null, null, null, null},
					{null, null, null, null, null, null, null, null, null},
				},
				new String[] {
					"Title", "Description", "Type", "Event Date", "Progress", "Start Time", "End Time", "Participants", "Options"
				}
			));
			table1.setFont(new Font("Segoe UI", Font.PLAIN, 10));
			jScrollPane1.setViewportView(table1);
		}

		//---- button1 ----
		button1.setText("Back to Main Page");
		button1.addActionListener(e -> {
			back(e);
			button1(e);
		});

		GroupLayout contentPaneLayout = new GroupLayout(contentPane);
		contentPane.setLayout(contentPaneLayout);
		contentPaneLayout.setHorizontalGroup(
			contentPaneLayout.createParallelGroup()
				.addGroup(contentPaneLayout.createSequentialGroup()
					.addContainerGap(26, Short.MAX_VALUE)
					.addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 808, GroupLayout.PREFERRED_SIZE)
					.addGap(24, 24, 24))
				.addGroup(contentPaneLayout.createSequentialGroup()
					.addGroup(contentPaneLayout.createParallelGroup()
						.addGroup(contentPaneLayout.createSequentialGroup()
							.addGap(349, 349, 349)
							.addComponent(jLabel1))
						.addGroup(contentPaneLayout.createSequentialGroup()
							.addGap(405, 405, 405)
							.addComponent(button1)))
					.addContainerGap(367, Short.MAX_VALUE))
		);
		contentPaneLayout.setVerticalGroup(
			contentPaneLayout.createParallelGroup()
				.addGroup(contentPaneLayout.createSequentialGroup()
					.addGap(34, 34, 34)
					.addComponent(jLabel1)
					.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
					.addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 156, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
					.addComponent(button1)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		pack();
		setLocationRelativeTo(getOwner());
    }// </editor-fold>//GEN-END:initComponents

	private void back(ActionEvent e) {
		
	}

	private void button1(ActionEvent e) {
		String psw = pass;
	    try {
            CalendarApp obj = new CalendarApp();
            obj.setCurrentUser(user);
            obj.setCurrentPass(psw);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        obj.meetingReminder();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }, 0, 5000);
            obj.setVisible(true);
            setVisible(false);
	    } catch (SQLException ex) {
	        Logger.getLogger(userLogin.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}
	
    void loadCalendarData() throws SQLException{
        ResultSet tasks = currUser.getTasks(connection, checked);
        DefaultTableModel table = new DefaultTableModel();
        table1.setModel(table);
        table.addColumn("Title");
        table.addColumn("Description");
        table.addColumn("Type");
        table.addColumn("Event Date");
        table.addColumn("Progress");
        table.addColumn("Start Time");
        table.addColumn("End Time");
        table.addColumn("Participant");
        table.addColumn("Edit");
        table.addColumn("Delete");
        
        table1.getColumnModel().getColumn(8).setCellRenderer(new ButtonRendererEdit());
        table1.getColumnModel().getColumn(8).setCellEditor(new ButtonEditorEdit(new JTextField()));
        
        table1.getColumnModel().getColumn(9).setCellRenderer(new ButtonRendererDelete());
        table1.getColumnModel().getColumn(9).setCellEditor(new ButtonEditorDelete(new JTextField()));
        
        while (tasks.next()){
            String taskTitle = tasks.getString("title");
            String taskDesc= tasks.getString("description");
            String taskDate = tasks.getString("event_date");
            String taskState = tasks.getString("progress");
            String taskUsers = tasks.getString("participant");
            table.addRow(new Object[]{taskTitle, taskDesc, "Task", taskDate, taskState, null, null, taskUsers});
        }

        ResultSet meetings = currUser.get_meetings(connection, checked);
        HashSet<String> meets = new HashSet<String>();
        while (meetings.next()){
            String meetingTitle = meetings.getString("title");
            String meetingDesc = meetings.getString("description");
            String meetingDate = meetings.getString("event_date");
            String meetingStart = meetings.getString("start_time");
            String meetingEnd = meetings.getString("end_time");
            String meetingMembr = meetings.getString("participant");

            if (!meets.contains(meetingTitle)){
                table.addRow(new Object[]{meetingTitle,meetingDesc,"Meeting", meetingDate,null,meetingStart,meetingEnd,meetingMembr}); 
            }
            meets.add(meetingTitle);
        }
        tasks.close();
        meetings.close();
    }
    
    static class ButtonRendererEdit extends JButton implements  TableCellRenderer{
    	public ButtonRendererEdit() {
    		setOpaque(true);
    	}
    	@Override
    	public Component getTableCellRendererComponent(JTable table, Object obj,
    		boolean selected, boolean focused, int row, int col) {
    		setText("Edit");
    		return this;
    	}
    }
    
    static class ButtonRendererDelete extends JButton implements  TableCellRenderer{
    	public ButtonRendererDelete() {
    		setOpaque(true);
    	}
    	@Override
    	public Component getTableCellRendererComponent(JTable table, Object obj,
    		boolean selected, boolean focused, int row, int col) {
    		setText("Delete");
    		return this;
    	}
    }
    
    class ButtonEditorEdit extends DefaultCellEditor{
    	protected JButton btn;
    	private String lbl;
    	public ButtonEditorEdit(JTextField txt) {
    		super(txt);
    		btn=new JButton();
    		btn.setOpaque(true);
    		btn.addActionListener(new ActionListener() {
    			@Override
    			public void actionPerformed(ActionEvent e) {
    				editBtnMouseClicked();
    			}
    		});
    	}
    	@Override
    	public Component getTableCellEditorComponent(JTable table, Object obj, boolean selected, int row, int col) {
    		lbl = "Edit";
    		btn.setText(lbl);
    		return btn;
    	}
    }
    
    class ButtonEditorDelete extends DefaultCellEditor{
    	protected JButton btn;
    	private String lbl;
    	public ButtonEditorDelete(JTextField txt) {
    		super(txt);
    		btn=new JButton();
    		btn.setOpaque(true);
    		btn.addActionListener(new ActionListener() {
    			@Override
    			public void actionPerformed(ActionEvent e) {
    				try {
						delete_btnMouseClicked();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
    			}
    		});
    	}
    	@Override
    	public Component getTableCellEditorComponent(JTable table, Object obj, boolean selected, int row, int col) {
    		lbl = "Delete";
    		btn.setText(lbl);
    		return btn;
    	}
    }
    
    private void editBtnMouseClicked() {
        boolean meetingChanged = table1.getValueAt(table1.getSelectedRow(),2).toString().equals("Meeting");
        String oldTitle, oldParticipant;
        oldTitle = table1.getValueAt(table1.getSelectedRow(),0).toString();
        description = table1.getValueAt(table1.getSelectedRow(),1).toString();
        eventType = table1.getValueAt(table1.getSelectedRow(),2).toString();
        eventDate = table1.getValueAt(table1.getSelectedRow(),3).toString();
        oldParticipant = table1.getValueAt(table1.getSelectedRow(),7).toString();

        var titleCB = new JCheckBox();
        var descpCB = new JCheckBox();
        var userCB = new JCheckBox();
        var progressCB = new JCheckBox();
        JTextField eTitle = new JTextField();
        JTextField descTF = new JTextField();
        JTextField userTF = new JTextField();
        JTextField progressTF = new JTextField();
        eTitle.setText(oldTitle);
        descTF.setText(description);
        userTF.setText(oldParticipant);
        meetDate.setDate(LocalDate.parse(eventDate));

        if (meetingChanged){
            startTime = table1.getValueAt(table1.getSelectedRow(),5).toString();
            endTime = table1.getValueAt(table1.getSelectedRow(),6).toString();
            meetStart.setText(startTime);
            meetEnd.setText(endTime);
            Object[] changes_fields = {
            	titleCB,"Title", eTitle,
            	descpCB,"Description", descTF,
            	userCB,"Invite User (Separate usrs by ',')", userTF,
               	"Meeting Date", meetDate,
               	"Start Time", meetStart,
               	"End Time", meetEnd
            };
            
            JOptionPane.showConfirmDialog(null, changes_fields, "change the event", JOptionPane.OK_CANCEL_OPTION);

            startTime = table1.getValueAt(table1.getSelectedRow(),5).toString();
            endTime = table1.getValueAt(table1.getSelectedRow(),6).toString();
            meetDay = meetDate.getDateStringOrEmptyString();
            String start = meetStart.getTimeStringOrEmptyString();
            String end = meetEnd.getTimeStringOrEmptyString();

            if (titleCB.isSelected()) {
                title = eTitle.getText();
            } else{
                title = oldTitle;
            }
            
            if (descpCB.isSelected()) 
            	description = descTF.getText();
            
            if (userCB.isSelected()) {
                participant = userTF.getText();
            } else{
                participant = oldParticipant;
            }
            
            if (!meetDay.isEmpty()) 
            	eventDate = meetDay;
            
            if (! start.isEmpty()) 
            	startTime = start;
            
            if (! end.isEmpty()) 
            	endTime = end;

            currMeeting.setTitle(title);
            currMeeting.setUserName(user);
            currMeeting.setDescription(description);
            currMeeting.setEventDate(eventDate);
            currMeeting.setStartTime(startTime);
            currMeeting.setEndTime(endTime);
            currMeeting.setParticipant(participant);
            currMeeting.setType("Meeting");

            try {
                currMeeting.update(connection, oldParticipant, oldTitle, user);
            } catch (SQLException ex) {
                Logger.getLogger(CalendarApp.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParseException ex) {
                Logger.getLogger(CalendarApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else{
        	Object[] changes_fields = {
                titleCB,"Title", eTitle,
                descpCB, "Description", descTF,
                progressCB,
                "Progress (range 0-100)", progressTF,
                "Meeting Date", meetDate
            };
        	
            JOptionPane.showConfirmDialog(null, changes_fields, "change the event", JOptionPane.OK_CANCEL_OPTION);
            progress = table1.getValueAt(table1.getSelectedRow(),4).toString();
            meetDay = meetDate.getDateStringOrEmptyString();
            
            if (titleCB.isSelected()) {
                title = eTitle.getText();
            }else{
                title = oldTitle;
            }
            
            if (descpCB.isSelected()) 
            	description = descTF.getText();
            
            if (progressCB.isSelected()) 
            	progress = progressTF.getText();
            
            if (!meetDay.isEmpty()) 
            	eventDate = meetDay;

            currTask.setTitle(title);
            currTask.setDescription(description);
            currTask.setEventDate(checked);
            currTask.setUserName(user);
            currTask.setProgress(progress);
            currTask.setType("Task");

            try {
                currTask.update(connection, oldTitle);
            } catch (SQLException ex) {
                Logger.getLogger(DisplayTable.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            loadCalendarData();
        } catch (SQLException ex) {
            Logger.getLogger(DisplayTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void delete_btnMouseClicked() throws SQLException {
        boolean deleteMeeting = table1.getValueAt(table1.getSelectedRow(),2).toString().equals("Meeting");
        title = table1.getValueAt(table1.getSelectedRow(), 0).toString();
        participant = table1.getValueAt(table1.getSelectedRow(),7).toString();
        if (deleteMeeting){
            currMeeting.setTitle(title);
            currMeeting.setParticipant(participant);
            currMeeting.generateUsersList();
            currMeeting.setUserName(user);
            currMeeting.delete(connection);
            for (String user: currMeeting.getUsersList()){
                currMeeting.setUserName(user);
                currMeeting.delete(connection);
            }
        } else{
            currTask.setUserName(user);
            currTask.setTitle(title);
            currTask.setType("Task");
            try {
                currTask.delete(connection);
            } catch (SQLException ex) {
                Logger.getLogger(DisplayTable.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            loadCalendarData();
        } catch (SQLException ex) {
            Logger.getLogger(DisplayTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DisplayTable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DisplayTable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DisplayTable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DisplayTable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {  
					new DisplayTable(currUser, checked, pass, user).setVisible(true);
				} catch (SQLException e) {
					e.printStackTrace();
				}
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - Savio
	private JLabel jLabel1;
	private JScrollPane jScrollPane1;
	private JTable table1;
	private JButton button1;
    // End of variables declaration//GEN-END:variables
}
