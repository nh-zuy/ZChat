package client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Color;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.MatteBorder;
import javax.swing.JPasswordField;

public class LoginForm extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	
	private JTextField textUn;
	private JPasswordField textPw;
	private JButton btnLogin;
	private JButton btnSignup;
	private JTextField textPort;
	private JTextField textDomain;
	private JLabel error;

	public JLabel getError() {
		return error;
	}

	public void setError(String text) {
		this.error.setText(text);
	}

	/**
	 * Constructor
	 */
	public LoginForm() {
		this.initialView();
		this.setBanner();
		this.setSignin();
		this.setEventButton();
	}
	
	/**
	 * Inititalize view
	 */
	private void initialView() {
		this.setCloseApp();
		
		setBounds(250, 100, 800, 461);
		contentPane = new JPanel();
		contentPane.setBorder(null);
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
	}
	
	/**
	 * 
	 */
	private void setCloseApp() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
	}
	
	/**
	 * Generating left panel
	 * Greeting, infor, ...
	 */
	private void setBanner() {
		JPanel banner = new JPanel();
		banner.setBackground(new Color(119, 165, 251));
		contentPane.add(banner, BorderLayout.WEST);
		banner.setPreferredSize(new Dimension(350, 100));
		banner.setLayout(null);
		
		JLabel title = new JLabel("Welcome to ZChat");
		title.setFont(new Font("Comic Sans MS", Font.BOLD, 23));
		title.setForeground(Color.WHITE);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setBounds(12, 12, 326, 61);
		banner.add(title);
		
		JLabel background = new JLabel("");
		background.setHorizontalAlignment(SwingConstants.CENTER);
		background.setIcon(new ImageIcon(LoginForm.class.getResource("/images/webp-net-resizeimage.jpg")));
		background.setBounds(0, 64, 350, 239);
		banner.add(background);
		
		JLabel license = new JLabel("*Available on Github nh-zuy");
		license.setHorizontalAlignment(SwingConstants.CENTER);
		license.setForeground(Color.BLACK);
		license.setFont(new Font("Comic Sans MS", Font.ITALIC, 13));
		license.setBounds(125, 328, 225, 34);
		banner.add(license);
		
		JLabel quote = new JLabel("Education is power !");
		quote.setHorizontalAlignment(SwingConstants.CENTER);
		quote.setForeground(Color.BLACK);
		quote.setFont(new Font("Comic Sans MS", Font.ITALIC, 13));
		quote.setBounds(125, 299, 225, 34);
		banner.add(quote);
	}
	
	/**
	 * Inititalize right Sign in view
	 * 
	 * Sign in
	 */
	private void setSignin() {
		JPanel signin = new JPanel();
		signin.setBackground(new Color(119, 165, 251));
		contentPane.add(signin, BorderLayout.CENTER);
		signin.setLayout(null);
		
		JPanel popup = new JPanel();
		popup.setBorder(new RadiusBorder(Color.white, 2, 16, 16));
		popup.setBackground(Color.WHITE);
		popup.setBounds(25, 22, 401, 398);
		signin.add(popup);
		popup.setLayout(null);
		
		textUn = new JTextField();
		textUn.setFont(new Font("Chandas", Font.PLAIN, 16));
		textUn.setColumns(10);
		textUn.setBorder(new MatteBorder(0, 0, 3, 0, (Color) new Color(192, 192, 192)));
		textUn.setBackground(Color.WHITE);
		textUn.setBounds(33, 184, 336, 32);
		popup.add(textUn);
		
		JLabel signinTitle = new JLabel("Sign In");
		signinTitle.setHorizontalAlignment(SwingConstants.CENTER);
		signinTitle.setForeground(new Color(30, 144, 255));
		signinTitle.setFont(new Font("Comic Sans MS", Font.PLAIN, 40));
		signinTitle.setBackground(new Color(23, 119, 242));
		signinTitle.setBounds(3, 0, 401, 63);
		signinTitle.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				signinTitle.setForeground(new Color(30, 144, 254));
				signinTitle.setFont(new Font("Comic Sans MS", Font.ITALIC, 40));
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				signinTitle.setForeground(new Color(30, 144, 255));
				signinTitle.setFont(new Font("Comic Sans MS", Font.PLAIN, 40));
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		popup.add(signinTitle);
		
		JLabel lblUn = new JLabel("Username");
		lblUn.setLabelFor(lblUn);
		lblUn.setForeground(Color.BLACK);
		lblUn.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		lblUn.setBounds(33, 140, 105, 32);
		popup.add(lblUn);
		
		JLabel lblPw = new JLabel("Password");
		lblPw.setForeground(Color.BLACK);
		lblPw.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		lblPw.setBounds(33, 241, 98, 24);
		popup.add(lblPw);
		
		textPw = new JPasswordField();
		lblPw.setLabelFor(textPw);
		textPw.setBounds(33, 277, 336, 37);
		textPw.setBorder(new MatteBorder(0, 0, 3, 0, (Color) Color.LIGHT_GRAY));
		textPw.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		popup.add(textPw);
		
		btnLogin = new JButton("Sign in");
		btnLogin.setForeground(Color.WHITE);
		btnLogin.setFont(new Font("Comic Sans MS", Font.BOLD, 22));
		btnLogin.setFocusPainted(false);
		btnLogin.setBackground(new Color(23, 119, 242));
		btnLogin.setBounds(30, 326, 149, 40);
		btnLogin.setBorder(new SoftBevelBorder(BevelBorder.RAISED, new Color(255, 255, 255), new Color(255, 255, 255), new Color(192, 192, 192), new Color(192, 192, 192)));
		popup.add(btnLogin);
		
		btnSignup = new JButton("Sign up");
		btnSignup.setForeground(Color.WHITE);
		btnSignup.setFont(new Font("Comic Sans MS", Font.PLAIN, 17));
		btnSignup.setFocusPainted(false);
		btnSignup.setBorder(new SoftBevelBorder(BevelBorder.RAISED, new Color(255, 255, 255), new Color(255, 255, 255), new Color(192, 192, 192), new Color(192, 192, 192)));
		btnSignup.setBackground(Color.RED);
		btnSignup.setBounds(220, 326, 149, 40);
		popup.add(btnSignup);
		
		textPort = new JTextField();
		textPort.setText("8080");
		textPort.setFont(new Font("Chandas", Font.PLAIN, 16));
		textPort.setColumns(10);
		textPort.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.LIGHT_GRAY, Color.LIGHT_GRAY, Color.LIGHT_GRAY, Color.LIGHT_GRAY));
		textPort.setBackground(Color.WHITE);
		textPort.setBounds(259, 96, 130, 32);
		popup.add(textPort);
		
		JLabel lblServer = new JLabel("Server");
		lblServer.setForeground(Color.BLACK);
		lblServer.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		lblServer.setBounds(33, 62, 105, 32);
		popup.add(lblServer);
		
		JLabel lblPort = new JLabel("Port");
		lblPort.setForeground(Color.BLACK);
		lblPort.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		lblPort.setBounds(264, 62, 105, 32);
		popup.add(lblPort);
		
		textDomain = new JTextField();
		textDomain.setText("127.0.0.1");
		textDomain.setFont(new Font("Chandas", Font.PLAIN, 16));
		textDomain.setColumns(10);
		textDomain.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.LIGHT_GRAY, Color.LIGHT_GRAY, Color.LIGHT_GRAY, Color.LIGHT_GRAY));
		textDomain.setBackground(Color.WHITE);
		textDomain.setBounds(33, 96, 214, 32);
		popup.add(textDomain);
		
		error = new JLabel("");
		error.setForeground(Color.RED);
		error.setFont(new Font("Comic Sans MS", Font.PLAIN, 15));
		error.setBounds(150, 140, 219, 32);
		popup.add(error);
	}
	
	/**
	 * 
	 */
	private void setEventButton() {
	}
	
	public String getDomain() {
		return textDomain.getText();
	}
	
	public String getPort() {
		return textPort.getText();
	}

	/**
	 * 
	 * @return
	 */
	public String getUsername() {
		return textUn.getText();
	}

	/**
	 * 
	 * @return
	 */
	public String getPassword() {
		return String.copyValueOf(textPw.getPassword());
	}
	
	
	/**
	 * 
	 * @return
	 */
	public JButton getBtnLogin() {
		return btnLogin;
	}

	/**
	 * 
	 * @return
	 */
	public JButton getBtnCancel() {
		return btnSignup;
	}

	/**
	 * 
	 */
	public JPanel getContentPane() {
		return contentPane;
	}
}