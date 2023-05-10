import java.awt.*;
import java.awt.event.*;
import oop.dbConnection;

import javax.swing.*;
import java.sql.*;
import java.text.ParseException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("serial")
public class userLogin extends javax.swing.JFrame {
	
	dbConnection connection = new dbConnection();
	String userName;
	
    public userLogin() {
        initComponents();
    }                      
        
    private boolean userVerification(String user_name, String password) throws SQLException{
        String sql = String.format("SELECT password FROM users WHERE user_name = '%s' ",user_name);
        ResultSet res = connection.executeQuery(sql);
        if (res == null) return false;
        	res.next();
        String pass = res.getString("password");
        return pass.equals(password);
    }
    
    private void log_in_btnActionPerformed(java.awt.event.ActionEvent evt) {
        userName = user_name_in.getText();
        String psw = password_in.getText();
	    try {
	        if (userVerification(userName, psw)){
	            CalendarApp obj = new CalendarApp();
	            obj.setCurrentUser(userName);
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
	        } else {
	            JOptionPane.showMessageDialog(null, "Login failed, try again.");
	        }
	    } catch (SQLException ex) {
	        Logger.getLogger(userLogin.class.getName()).log(Level.SEVERE, null, ex);
	    }
    }    
    
    private void redirect_btnActionPerformed(java.awt.event.ActionEvent evt) {
    	userRegister register = new userRegister();
        register.setVisible(true);
        setVisible(false);
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
            java.util.logging.Logger.getLogger(userLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(userLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(userLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(userLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new userLogin().setVisible(true);
            }
        });
    }               

//	private void initComponents() {
//		jLabel1 = new JLabel();
//		user_name_in = new JTextField();
//		jLabel2 = new JLabel();
//		password_in = new JTextField();
//		log_in_icon = new JLabel();
//		jLabel4 = new JLabel();
//		log_in_btn = new JButton();
//		label1 = new JLabel();
//		log_in_btn2 = new JButton();
//
//		//======== this ========
//		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//		setTitle("Login Form");
//		setBackground(new Color(51, 153, 255));
//		var contentPane = getContentPane();
//
//		//---- jLabel1 ----
//		jLabel1.setFont(new Font("Segoe UI", Font.PLAIN, 14));
//		jLabel1.setIcon(new ImageIcon(getClass().getResource("/images/icons8-username-48.png")));
//		jLabel1.setText("User Name ");
//
//		//---- jLabel2 ----
//		jLabel2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
//		jLabel2.setIcon(new ImageIcon(getClass().getResource("/images/icons8-password-64.png")));
//		jLabel2.setText("Password ");
//
//		//---- log_in_icon ----
//		log_in_icon.setFont(new Font("Segoe UI", Font.BOLD, 14));
//		log_in_icon.setIcon(new ImageIcon(getClass().getResource("/images/icons8-enter-48.png")));
//		log_in_icon.setText("Login");
//
//		//---- jLabel4 ----
//		jLabel4.setFont(new Font("Segoe UI", Font.BOLD, 18));
//		jLabel4.setIcon(new ImageIcon(getClass().getResource("/images/icons8-login-64.png")));
//		jLabel4.setText("Login");
//
//		//---- log_in_btn ----
//		log_in_btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
//		log_in_btn.setText("Login");
//		log_in_btn.addActionListener(e -> log_in_btnActionPerformed(e));
//
//		//---- label1 ----
//		label1.setText("Are you a new user?");
//		label1.setFont(new Font("Tahoma", Font.PLAIN, 12));
//
//		//---- log_in_btn2 ----
//		log_in_btn2.setFont(new Font("Segoe UI", Font.BOLD, 12));
//		log_in_btn2.setText("Sign Up");
//		log_in_btn2.addActionListener(e -> redirect_btnActionPerformed(e));
//
//		GroupLayout contentPaneLayout = new GroupLayout(contentPane);
//		contentPane.setLayout(contentPaneLayout);
//		contentPaneLayout.setHorizontalGroup(
//			contentPaneLayout.createParallelGroup()
//				.addGroup(contentPaneLayout.createSequentialGroup()
//					.addGroup(contentPaneLayout.createParallelGroup()
//						.addGroup(contentPaneLayout.createSequentialGroup()
//							.addGap(86, 86, 86)
//							.addGroup(contentPaneLayout.createParallelGroup()
//								.addGroup(contentPaneLayout.createSequentialGroup()
//									.addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 153, GroupLayout.PREFERRED_SIZE)
//									.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
//									.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
//										.addComponent(user_name_in, GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
//										.addComponent(password_in)
//										.addComponent(log_in_btn, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE)))
//								.addGroup(contentPaneLayout.createSequentialGroup()
//									.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
//										.addComponent(label1, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//										.addComponent(jLabel1, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//										.addComponent(log_in_icon, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE))
//									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
//									.addComponent(log_in_btn2))))
//						.addGroup(contentPaneLayout.createSequentialGroup()
//							.addGap(142, 142, 142)
//							.addComponent(jLabel4, GroupLayout.PREFERRED_SIZE, 229, GroupLayout.PREFERRED_SIZE)))
//					.addContainerGap(85, Short.MAX_VALUE))
//		);
//		contentPaneLayout.setVerticalGroup(
//			contentPaneLayout.createParallelGroup()
//				.addGroup(contentPaneLayout.createSequentialGroup()
//					.addGap(70, 70, 70)
//					.addComponent(jLabel4)
//					.addGap(37, 37, 37)
//					.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
//						.addComponent(jLabel1)
//						.addComponent(user_name_in, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
//					.addGap(29, 29, 29)
//					.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
//						.addComponent(password_in, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
//						.addComponent(jLabel2))
//					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
//					.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
//						.addComponent(log_in_icon, GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE)
//						.addComponent(log_in_btn))
//					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
//					.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
//						.addComponent(label1)
//						.addComponent(log_in_btn2))
//					.addContainerGap(43, Short.MAX_VALUE))
//		);
//		pack();
//		setLocationRelativeTo(getOwner());
//	}
//	
////	private JLabel jLabel1;
////	private JTextField user_name_in;
////	private JLabel jLabel2;
////	private JTextField password_in;
////	private JLabel log_in_icon;
////	private JLabel jLabel4;
////	private JButton log_in_btn;
////	private JLabel label1;
////	private JButton log_in_btn2;

	private void log_in_btnMouseClicked(MouseEvent e) {
		// TODO add your code here
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - Savio
		jLabel1 = new JLabel();
		user_name_in = new JTextField();
		jLabel2 = new JLabel();
		password_in = new JTextField();
		log_in_icon = new JLabel();
		jLabel4 = new JLabel();
		log_in_btn = new JButton();
		label1 = new JLabel();
		log_in_btn2 = new JButton();

		//======== this ========
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("Login Form");
		setBackground(new Color(51, 153, 255));
		var contentPane = getContentPane();

		//---- jLabel1 ----
		jLabel1.setFont(new Font("Segoe UI Historic", Font.BOLD, 14));
		jLabel1.setIcon(new ImageIcon(getClass().getResource("/images/icons8-username-48.png")));
		jLabel1.setText("User Name ");

		//---- jLabel2 ----
		jLabel2.setFont(new Font("Segoe UI Historic", Font.BOLD, 14));
		jLabel2.setIcon(new ImageIcon(getClass().getResource("/images/icons8-password-64.png")));
		jLabel2.setText("Password ");

		//---- log_in_icon ----
		log_in_icon.setFont(new Font("Segoe UI", Font.BOLD, 14));
		log_in_icon.setIcon(new ImageIcon(getClass().getResource("/images/icons8-enter-48.png")));
		log_in_icon.setText("Login");

		//---- jLabel4 ----
		jLabel4.setFont(new Font("Segoe UI Historic", Font.BOLD, 18));
		jLabel4.setIcon(new ImageIcon(getClass().getResource("/images/icons8-login-64.png")));
		jLabel4.setText("Login");

		//---- log_in_btn ----
		log_in_btn.setFont(new Font("Segoe UI Historic", Font.BOLD, 16));
		log_in_btn.setText("Login");
		log_in_btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				log_in_btnMouseClicked(e);
			}
		});
		log_in_btn.addActionListener(e -> log_in_btnActionPerformed(e));

		//---- label1 ----
		label1.setText("Are you a new user?");
		label1.setFont(new Font("Segoe UI Historic", Font.ITALIC, 14));

		//---- log_in_btn2 ----
		log_in_btn2.setFont(new Font("Segoe UI Historic", Font.BOLD, 16));
		log_in_btn2.setText("Sign Up");
		log_in_btn2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				log_in_btnMouseClicked(e);
			}
		});
		log_in_btn2.addActionListener(e -> redirect_btnActionPerformed(e));

		GroupLayout contentPaneLayout = new GroupLayout(contentPane);
		contentPane.setLayout(contentPaneLayout);
		contentPaneLayout.setHorizontalGroup(
			contentPaneLayout.createParallelGroup()
				.addGroup(contentPaneLayout.createSequentialGroup()
					.addGroup(contentPaneLayout.createParallelGroup()
						.addGroup(contentPaneLayout.createSequentialGroup()
							.addGap(86, 86, 86)
							.addGroup(contentPaneLayout.createParallelGroup()
								.addGroup(contentPaneLayout.createSequentialGroup()
									.addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 153, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
									.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
										.addComponent(user_name_in, GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
										.addComponent(password_in)
										.addComponent(log_in_btn, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE)))
								.addGroup(contentPaneLayout.createSequentialGroup()
									.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
										.addComponent(label1, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(jLabel1, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(log_in_icon, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addComponent(log_in_btn2))))
						.addGroup(contentPaneLayout.createSequentialGroup()
							.addGap(142, 142, 142)
							.addComponent(jLabel4, GroupLayout.PREFERRED_SIZE, 229, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(85, Short.MAX_VALUE))
		);
		contentPaneLayout.setVerticalGroup(
			contentPaneLayout.createParallelGroup()
				.addGroup(contentPaneLayout.createSequentialGroup()
					.addGap(70, 70, 70)
					.addComponent(jLabel4)
					.addGap(37, 37, 37)
					.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(jLabel1)
						.addComponent(user_name_in, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(29, 29, 29)
					.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(password_in, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(jLabel2))
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
					.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(log_in_icon, GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE)
						.addComponent(log_in_btn))
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
					.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(label1)
						.addComponent(log_in_btn2))
					.addContainerGap(43, Short.MAX_VALUE))
		);
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - Savio
	private JLabel jLabel1;
	private JTextField user_name_in;
	private JLabel jLabel2;
	private JTextField password_in;
	private JLabel log_in_icon;
	private JLabel jLabel4;
	private JButton log_in_btn;
	private JLabel label1;
	private JButton log_in_btn2;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}