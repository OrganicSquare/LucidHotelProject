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
import communication.Timers;
import entities.Entites;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class Menu extends JPanel{
	 private static final long serialVersionUID = 1L;
	 
	 
	 private BufferedImage image;	 
	 JLabel usernameLBL = new JLabel("Username");
	 JLabel passwordLBL = new JLabel("Password");
	 static JTextField usernameTF = new JTextField();
	 static JTextField passwordTF = new JPasswordField();
	 static JLabel loginUnsuccessfulLBL = new JLabel("Incorrect username or password");
	 JButton signupBTN = new JButton("Sign up");
	 static JLabel loadingGif = null;
	 static boolean hasTriedLogin = false;
	 final static JPanel loginPane = new JPanel();
	 Timer timerLogin;
	 Timer timerUser;
	 
	 // Two key Flags that determine when the loading is complete
	 public static boolean hasDetailsDownloaded = false;
	 public static boolean hasModelsLoaded = false;
	 	// When both flags are true then the main.java is loaded in
	 
	 
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

		// Start Flag checking
    	Timers.initiateTimer4(2000,Timers.timerThread4, 4);
		
		// Start Model Loading checking
    	Timers.initiateTimer3(200,Timers.timerThread3, 3);
		
		
		
		
	}
	public void tryLogin(){
		if(!hasTriedLogin) {
			// Show loading bar
			loadingGif.setVisible(true);
            loginPane.setVisible(false);
            
			InternetConnector.findUser(usernameTF.getText());				
			
        	Timers.initiateTimer1(100,Timers.timerThread1, 1);
			
			hasTriedLogin = true;
		}
		
	}
	
	 @Override
	 protected void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        g.drawImage(image, 0, 0, null);           
	  }
	 
	 
	 // TIMER CLASSES
	 public static boolean checkUserLoginIsOk(){ // CODE 1
		 if(Util.isValid(InternetConnector.JSONRawMain[1])){	            	
         	if(InternetConnector.isLoginOK(passwordTF.getText())){	            
         		InternetConnector.excutePostAsync(new String[]{"target","userName"}, new String[]{"logUserIn",usernameTF.getText()}, 0);
	            	InternetConnector.downloadUserInformation(usernameTF.getText());
	            	
	            	// Details ok, start second operations
	            	Timers.initiateTime2(100,Timers.timerThread2, 2);						
				} else {
					// Show login panel again since user has incorrect details
					loginUnsuccessfulLBL.setVisible(true);
					loadingGif.setVisible(false);
	                loginPane.setVisible(true);
	                hasTriedLogin = false;
				}	            	
         	return true;
         } else {
         	return false;
         }
	 }
	 static public boolean checkUserDetailsDownloaded(){ // CODE 2
		 if(Util.isValid(InternetConnector.JSONRawMain[1])){        
			 System.out.println("Downloaded all data");
         	// Save details to file
	            	// Create data to write to file
	    			String username = usernameTF.getText();
	    			String password = passwordTF.getText();
	    			Object[] fileData = new Object[]{username,password};
	    			// Write to a file
	    			FileConnector.writeGameFiles("userDetails.dat",fileData);
	    			
	    		// Decode the users information
	    			Map<String, Object> UserInfoFromNet = InternetConnector.decodeUserInformation();
	            	Main.userInfo = UserInfoFromNet;
	            // Initiate the creation of the players
	            	Main.initiateOtherUserInfo(); 
	            	Main.initUserInfo(Entites.standingPlayer);
	    			
	            // Update flags
	            	hasDetailsDownloaded = true;
         	
         	return true;	            	
         } else {
         	return false;	            	
         }
	 }
	 static public void checkModelsHaveLoaded(){ // Code 3
		 if(Entites.hasEntitesFinished){
			 hasModelsLoaded = true;
			 System.out.println("Models finished");
		 } else {
			 Timers.initiateTimer3(200,Timers.timerThread3, 3);
		 }
	 }
	 static public void checkAndRunAllFlagsAccepted(){ // Code 4
		 if(hasModelsLoaded && hasDetailsDownloaded){
			 
			 System.out.println("Ready to play game");
			 // Loading is complete 
			 // Show GUI
				 Main.lwjglCanvas.setVisible(true);
				 Main.loginSuccessful = true;
				 
				 
			// This is menu's end state. Everything is full loaded and ready - now the menu.java opens
		 } else {
			 Timers.initiateTimer4(200,Timers.timerThread4, 4);
		 }
	 }
	 
}