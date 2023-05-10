import java.awt.*;
import oop.dbConnection;

import javax.swing.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("serial")
public class userRegister extends javax.swing.JFrame {
	public userRegister() {
		initComponents();
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
            java.util.logging.Logger.getLogger(userRegister.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(userRegister.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(userRegister.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(userRegister.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new userRegister().setVisible(true);
            }
        });
    }

	private void initComponents() {
		jLabel1 = new JLabel();
		user_name_in = new JTextField();
		jLabel2 = new JLabel();
		password_in = new JTextField();
		register_icon = new JLabel();
		jLabel4 = new JLabel();
		register_btn = new JButton();
		label1 = new JLabel();
		log_in_btn2 = new JButton();

		//======== this ========
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("Register Form");
		setBackground(new Color(51, 153, 255));
		var contentPane = getContentPane();

		//---- jLabel1 ----
		jLabel1.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		jLabel1.setIcon(new ImageIcon(getClass().getResource("/images/icons8-username-48.png")));
		jLabel1.setText("User Name ");

		//---- jLabel2 ----
		jLabel2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		jLabel2.setIcon(new ImageIcon(getClass().getResource("/images/icons8-password-64.png")));
		jLabel2.setText("Password ");

		//---- log_in_icon ----
		register_icon.setFont(new Font("Segoe UI", Font.BOLD, 14));
		register_icon.setIcon(new ImageIcon(getClass().getResource("/images/icons8-enter-48.png")));
		register_icon.setText("Sign Up ");

		//---- jLabel4 ----
		jLabel4.setFont(new Font("Segoe UI", Font.BOLD, 18));
		jLabel4.setIcon(new ImageIcon(getClass().getResource("/images/icons8-login-64.png")));
		jLabel4.setText("Sign Up");

		//---- log_in_btn ----
		register_btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
		register_btn.setText("Sign Up");
		register_btn.addActionListener(e -> register_btnActionPerformed(e));
		
		//---- label1 ----
		label1.setText("Already a user?");
		label1.setFont(new Font("Tahoma", Font.PLAIN, 12));

		//---- log_in_btn2 ----
		log_in_btn2.setFont(new Font("Segoe UI", Font.BOLD, 12));
		log_in_btn2.setText("Login");
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
										.addComponent(register_btn, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE)))
								.addGroup(contentPaneLayout.createSequentialGroup()
									.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
										.addComponent(label1, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(jLabel1, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(register_icon, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE))
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
						.addComponent(jLabel2)
						.addComponent(password_in, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
					.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(register_icon, GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE)
						.addComponent(register_btn))
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
					.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(label1)
						.addComponent(log_in_btn2))
					.addContainerGap(43, Short.MAX_VALUE))
		);
		pack();
		setLocationRelativeTo(getOwner());
	}
	
	private void redirect_btnActionPerformed(java.awt.event.ActionEvent evt) {
		userLogin login = new userLogin();
        login.setVisible(true);
        setVisible(false);
    } 
	
	private void register_btnActionPerformed(java.awt.event.ActionEvent evt) {
		dbConnection connection = new dbConnection();
        String userName = user_name_in.getText();
        String pass = password_in.getText();
        
        try {
            connection.insertUserData(userName, pass);
        } catch (SQLException ex) {
            Logger.getLogger(userRegister.class.getName()).log(Level.SEVERE, null, ex);
        } 
        JOptionPane.showMessageDialog(null, "SignUp successful. Now you can login");
        userLogin login = new userLogin();
        login.setVisible(true);
        setVisible(false);
    }
	
	private JLabel jLabel1;
	private JTextField user_name_in;
	private JLabel jLabel2;
	private JTextField password_in;
	private JLabel register_icon;
	private JLabel jLabel4;
	private JButton register_btn;
	private JLabel label1;
	private JButton log_in_btn2;
}
