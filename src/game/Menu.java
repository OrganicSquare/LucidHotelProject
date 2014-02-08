package game;


import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import communication.FileConnector;
import communication.InternetConnector;
import communication.Util;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class Menu extends JPanel{
	 private static final long serialVersionUID = 1L;
	 
	 
	 private BufferedImage image;	 
	 JLabel usernameLBL = new JLabel("Username");
	 JLabel passwordLBL = new JLabel("Password");
	 JTextField usernameTF = new JTextField();
	 JTextField passwordTF = new JPasswordField();
	 JLabel loginUnsuccessfulLBL = new JLabel("Incorrect username or password");
	 JButton signupBTN = new JButton("Sign up");
	 JLabel loadingGif = null;
	 boolean hasTriedLogin = false;
	 final JPanel loginPane = new JPanel();
	 Timer timerLogin;
	 Timer timerUser;
	 
	public Menu(){		
		// Set up areas
		setBounds(0,0,Main.WIDTH,Main.HEIGHT);
		setLayout(null);
		this.setBackground(new Color(30,30,30));
		
		// Load in graphics
		ImageIcon loginTextField = new ImageIcon("res/Images/loginTextField.png");
		ImageIcon loginButtonImg = new ImageIcon("res/Images/loginButton.png");
		ImageIcon loadingGifImg = new ImageIcon("res/Images/loader.gif");
		try {                
	         image = ImageIO.read(new File("res/Images/loginBackground.png"));
	       } catch (IOException ex) {
	    	   ex.printStackTrace();
	       }
		
		// Generate login pane
		loginPane.setBounds(0,Main.HEIGHT-250,280,250);
		loginPane.setLayout(null);
		loginPane.setOpaque(false);
		add(loginPane);
		
		// Generate signup pane - need to do
		JPanel signupPane = new JPanel();
		signupPane.setBounds((int)((float)Main.WIDTH/2f)-120,(int)((float)Main.HEIGHT/2f)+65,240,30);
		signupPane.setLayout(null);		
		signupPane.setOpaque(false);
		add(signupPane);
		
		
		
		// Generate username label
		usernameLBL.setForeground(new Color(36,36,36));		
		usernameLBL.setBounds(30,5,241,45);
		usernameLBL.setFont(new Font("Arial", Font.PLAIN, 16));		
		loginPane.add(usernameLBL);	
		
		// Generate password label
		passwordLBL.setForeground(new Color(36,36,36));
		passwordLBL.setBounds(30,45,241,85);
		passwordLBL.setFont(new Font("Arial", Font.PLAIN, 16));
		loginPane.add(passwordLBL);
		
		// Generate unsuccessful label
		loginUnsuccessfulLBL.setBounds(50,75,241,85);
		loginUnsuccessfulLBL.setVisible(false);
		loginUnsuccessfulLBL.setForeground(new Color(58,0,0));
		loginPane.add(loginUnsuccessfulLBL);
		// Text Field Capture Key
		JTextField captureKeyTF = new JPasswordField();
		captureKeyTF.setOpaque(false);
		captureKeyTF.setBounds(-10,-10,0,0);
		loginPane.add(captureKeyTF);
		
		// Text Field username
		usernameTF.setBounds(30,5,241,45);
		usernameTF.setForeground(new Color(255,255,255));
		usernameTF.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		usernameTF.setFont(new Font("Arial", Font.PLAIN, 16));
		usernameTF.setOpaque(false);
		usernameTF.addFocusListener(new FocusListener(){
			@Override
            public void focusGained(FocusEvent e) {
				usernameLBL.setVisible(false);
            }
			@Override
            public void focusLost(FocusEvent e) {
				if(usernameTF.getText().length() == 0){
					usernameLBL.setVisible(true);
				}
			}
		});
		usernameTF.addActionListener(new ActionListener(){
			 @Override
		    public void actionPerformed(ActionEvent ae) {
				 tryLogin();	
		    }
		});
		loginPane.add(usernameTF);	
		
		// generate username button backing
		JLabel usernameButtonLBL = new JLabel("", loginTextField, JLabel.LEFT);
		usernameButtonLBL.setBounds(20,5,271,45);
		loginPane.add(usernameButtonLBL);
		
		// text field password
		passwordTF.setBounds(30,45,241,85);
		passwordTF.setForeground(new Color(255,255,255));
		passwordTF.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		passwordTF.setFont(new Font("Arial", Font.PLAIN, 16));
		passwordTF.setOpaque(false);
		passwordTF.addFocusListener(new FocusListener(){
			@Override
            public void focusGained(FocusEvent e) {
				passwordLBL.setVisible(false);
            }
			@Override
            public void focusLost(FocusEvent e) {
				if(passwordTF.getText().length() == 0){
					passwordLBL.setVisible(true);
				}
			}
		});
		passwordTF.addActionListener(new ActionListener(){
			 @Override
		    public void actionPerformed(ActionEvent ae) {
				 tryLogin();	
		    }
		});
		loginPane.add(passwordTF);
		
		// generate password button backing
		JLabel passwordButtonLBL = new JLabel("", loginTextField, JLabel.LEFT);
		passwordButtonLBL.setBounds(20,45,271,85);
		loginPane.add(passwordButtonLBL);
		
		// Generate login button		
		JLabel loginButton = new JLabel("", loginButtonImg, JLabel.LEFT);
		loginButton.setBounds(150,80,270,120);
		loginButton.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e){ 
				tryLogin();				
            }
		});		
		loginPane.add(loginButton);
		
		// Loading Gif 
		loadingGif = new JLabel("", loadingGifImg, JLabel.CENTER);
		loadingGif.setBounds(0,Main.HEIGHT-300,280,250);
		loadingGif.setVisible(false);
		add(loadingGif);
		
		// Try and load details in from file
		Object[] fileReadData = FileConnector.readGameFiles("userDetails.dat");
		if(fileReadData[0] != null && Util.isValid(fileReadData[0].toString()) && Util.isValid(fileReadData[1].toString())){
			usernameTF.setText(fileReadData[0].toString());
			passwordTF.setText(fileReadData[1].toString());
			usernameLBL.setVisible(false);
			passwordLBL.setVisible(false);
		}
		// generate sign up button
		//signupBTN.setBounds(70,5,100,20);
		//signupPane.add(signupBTN);
		//signupBTN.addActionListener(this);
		
		usernameTF.requestFocusInWindow();
	}
	public void tryLogin(){
		if(!hasTriedLogin) {
			// Show loading bar
			loadingGif.setVisible(true);
            loginPane.setVisible(false);
            
			InternetConnector.findUser(usernameTF.getText());				
			asyncCheckLogin(200);
			hasTriedLogin = true;
		}
		
	}
	
	 @Override
	 protected void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        g.drawImage(image, 0, 0, null);           
	  }
	 public void asyncCheckLogin(int seconds) {
		 	timerLogin = new Timer();
		 	timerLogin.schedule(new initialDownloadCompleteLogin(), seconds);
		}
	 class initialDownloadCompleteLogin extends TimerTask {
	        public void run() {
	            if(Util.isValid(InternetConnector.JSONRawMain[1])){	            	
	            	if(InternetConnector.isLoginOK(passwordTF.getText())){	            
	            		InternetConnector.excutePostAsync(new String[]{"target","userName"}, new String[]{"logUserIn",usernameTF.getText()}, 0);
		            	InternetConnector.downloadUserInformation(usernameTF.getText());
		            	asyncCheckUser(100);						
					} else {
						// Show login panel again since user has incorrect details
						loginUnsuccessfulLBL.setVisible(true);
						loadingGif.setVisible(false);
		                loginPane.setVisible(true);
		                hasTriedLogin = false;
					}	            	
	            	timerLogin.cancel(); 
	            	
	            } else {
	            	asyncCheckLogin(200);
	            }
	        }
	    }
	 
	 public void asyncCheckUser(int seconds) {
	        timerUser = new Timer();
	        timerUser.schedule(new initialDownloadCompleteUser(), seconds);
		}
	 class initialDownloadCompleteUser extends TimerTask {
	        public void run() {
	            if(Util.isValid(InternetConnector.JSONRawMain[1])){	            	
	            	// The user is ok to log in
	            	
	            	// Save details to file
	            	
	            	// Create data to write to file
	    			String username = usernameTF.getText();
	    			String password = passwordTF.getText();
	    			Object[] fileData = new Object[]{username,password};
	    			// Write to a file
	    			FileConnector.writeGameFiles("userDetails.dat",fileData);
	            	
	            	// Show GUI
	            	Main.loginSuccessful = true;
	            	Map<String, Object> UserInfoFromNet = InternetConnector.decodeUserInformation();
	            	Main.userInfo = UserInfoFromNet;
	            	Main.lwjglCanvas.setVisible(true);
	            	timerUser.cancel(); 	            	
	            } else {
	            	asyncCheckUser(100);	            	
	            }
	        }
	    }
	 
}