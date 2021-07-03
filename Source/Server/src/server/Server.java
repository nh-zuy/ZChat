package server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;
import javax.swing.JLabel;
import javax.swing.JList;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;

public class Server extends JFrame {
	private static final long serialVersionUID = 1L;

	/* --------- Socket server -------------- */
	public String domain;
	public int port;
	public ArrayList<Handler> online;
	public ServerSocket server;
	public HashMap<String, String> account;
	/* -------------------------------------- */

	/* --------- Java swing ------------------ */
	private JButton _choice;
	private JPanel contentPane;
	private JButton btnDashboard;
	private JPanel profile;
	private JPanel task;
	private JPanel workspace;
	private JLabel lblTitle;
	private JTextField search;
	private JLabel avatar;
	private JLabel lblHiAdmin;
	private JButton btnExit;
	private JTextField textDomain;
	private JTextField textPort;
	private JButton btnStart;
	private JScrollPane scrollPane;
	private JList<String> user;
	private JLabel statusServer;

	/**
	 * Create the frame.
	 * 
	 * @throws IOException
	 */
	public Server() throws IOException {
		/* Making GUI by java swing */
		this.view();

		/* Load all acount */
		this.account = new HashMap<String, String>();
		try {
			File f = new File("data/account.txt");
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null) {
				String[] data = line.split(" ");
				String username = data[0];
				String password = data[1];

				this.account.put(username, password);
			}
			fr.close();
			br.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	private boolean authenticate(String username, String password) {
		if (this.account.containsKey(username)) {
			if (this.account.containsValue(password)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 
	 * @author zuy
	 *
	 */
	public class Handler extends Thread {
		private Socket connect;
		private String username;
		private OutputStream response;

		/**
		 * 
		 * @param client
		 */
		public Handler(Socket client) {
			this.connect = client;
		}

		@Override
		public void run() {
			try {
				InputStream request = connect.getInputStream();
				this.response = connect.getOutputStream();

				BufferedReader reader = new BufferedReader(new InputStreamReader(request));
				String line;
				while ((line = reader.readLine()) != null) {
					String[] data = line.split(" ");
					DefaultListModel<String> model = (DefaultListModel<String>) user.getModel();

					String flag = data[0];
					if (flag.equals("signin")) {
						String username = data[1];
						String password = data[2];

						boolean isLogin = authenticate(username, password);
						if (isLogin) {
							String msg = "success\n";
							response.write(msg.getBytes());
							this.username = username;
							model.addElement(username);
							System.out.println("User logged in succesfully: " + username);

							// send current user all other online logins
							String onlineMsg = "on " + username + "\n";
							for (Handler client : online) {
								if (client.username != null) {
									if (!username.equals(client.username)) {
										String msg2 = "on " + client.username + "\n";
										try {
											response.write(msg2.getBytes());
											client.response.write(onlineMsg.getBytes());
										} catch (Exception ex) {
											ex.printStackTrace();
										}
									}
								}
							}
						} else {
							String msg = "fail\n";
							response.write(msg.getBytes());
						}
					} else if (flag.equals("text")) {
						String recipient = data[1];
						String rest = line.substring(line.indexOf(" ")).replaceFirst(" ", "");
						String content = rest.substring(rest.indexOf(" ")).replaceFirst(" ", "");

						ArrayList<Handler> user = online;
						for (Handler client : user) {
							if (recipient.equalsIgnoreCase(client.username)) {
								String msg = "text " + username + " " + content + "\n";
								try {
									client.response.write(msg.getBytes());
									break;
								} catch (Exception ex) {
									ex.printStackTrace();
								}
							}
						}
					} else if (flag.equals("signup")) {
						String username = data[1];
						String password = data[2];

						boolean isExisted = authenticate(username, password);
						if (isExisted) {
							String msg = "fail\n";
							response.write(msg.getBytes());
						} else {
							try {
								File f = new File("data/account.txt");
								FileWriter fw = new FileWriter(f, true);
								fw.write(username);
								fw.write(" ");
								fw.write(password);
								fw.write("\n");
								fw.close();
								account.put(username, password);
							} catch (IOException ex) {
								ex.printStackTrace();
							}
							String msg = "success\n";
							response.write(msg.getBytes());
						}
					} else if (flag.equals("emoji")) {
						String recipient = data[1];
						String rest = line.substring(line.indexOf(" ")).replaceFirst(" ", "");
						String link = rest.substring(rest.indexOf(" ")).replaceFirst(" ", "");

						ArrayList<Handler> user = online;
						for (Handler client : user) {
							if (recipient.equalsIgnoreCase(client.username)) {
								String msg = "emoji " + username + " " + link + "\n";
								try {
									client.response.write(msg.getBytes());
									break;
								} catch (Exception ex) {
									ex.printStackTrace();
								}
							}
						}
					} else if (flag.equals("signout")) {
						online.remove(this);
						String msg = "off " + username + "\n";
						model.removeElement(username);
						for (Handler client : online) {
							if (client != null) {
								if (!username.equals(client.username)) {
									try {
										client.response.write(msg.getBytes());
									} catch (Exception ex) {
										ex.printStackTrace();
									}
								}
							}
						}
						break;
					}
				}
				connect.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void view() {
		/* Load setting information */
		this._choice = btnDashboard;

		/* Initial view and event */
		/**
		 * Common view
		 **/
		setBounds(250, 100, 900, 530);
		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());
		setContentPane(contentPane);

		/**
		 * Navigation left bar
		 **/
		JPanel nav = new JPanel();
		nav.setPreferredSize(new Dimension(150, 100));
		nav.setBackground(new Color(23, 33, 53));
		contentPane.add(nav, BorderLayout.WEST);
		nav.setLayout(null);
		btnDashboard = new JButton("Dashboard");
		btnDashboard.setBounds(0, 86, 150, 53);
		btnDashboard.setFocusPainted(false);
		btnDashboard.setForeground(Color.WHITE);
		btnDashboard.setBackground(new Color(39, 55, 80));
		btnDashboard.setBorder(new MatteBorder(0, 5, 0, 0, new Color(233, 229, 238)));
		btnDashboard.setFont(new Font("AnjaliOldLipi", Font.BOLD, 16));
		nav.add(btnDashboard);

		/**
		 * The top of the frame: Header
		 */
		JPanel header = new JPanel();
		header.setBackground(new Color(67, 119, 202));
		header.setPreferredSize(new Dimension(70, 70));
		contentPane.add(header, BorderLayout.NORTH);
		header.setLayout(null);

		lblTitle = new JLabel("ZChat server");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setForeground(Color.WHITE);
		lblTitle.setFont(new Font("DejaVu Sans Light", Font.PLAIN, 30));
		lblTitle.setBounds(12, 12, 324, 46);
		header.add(lblTitle);

		search = new JTextField();
		search.setFont(new Font("Comic Sans MS", Font.PLAIN, 15));
		search.setText("Search ...");
		search.setBounds(462, 27, 202, 25);
		search.setBorder(null);
		header.add(search);
		search.setColumns(10);

		JLabel dotquestion = new JLabel("");
		dotquestion.setBackground(Color.WHITE);
		dotquestion.setForeground(Color.WHITE);
		dotquestion.setIcon(new ImageIcon(Server.class.getResource("/images/icons8-search-property-32.png")));
		dotquestion.setBounds(682, 16, 48, 46);
		header.add(dotquestion);

		/**
		 * Main content
		 */
		JPanel main = new JPanel();
		contentPane.add(main, BorderLayout.CENTER);
		main.setLayout(new BorderLayout(0, 0));

		/* Status zone */
		JPanel status = new JPanel();
		status.setPreferredSize(new Dimension(300, 300));
		main.add(status, BorderLayout.WEST);
		status.setLayout(new BorderLayout(0, 0));

		profile = new JPanel();
		profile.setBackground(new Color(119, 165, 251));
		profile.setPreferredSize(new Dimension(120, 120));
		status.add(profile, BorderLayout.NORTH);
		profile.setLayout(null);

		avatar = new JLabel("");
		avatar.setHorizontalAlignment(SwingConstants.CENTER);
		avatar.setBounds(12, 22, 103, 65);
		avatar.setIcon(new ImageIcon(Server.class.getResource("/images/icons8-male-user-64.png")));
		profile.add(avatar);

		lblHiAdmin = new JLabel("Hello, admin @");
		lblHiAdmin.setHorizontalAlignment(SwingConstants.CENTER);
		lblHiAdmin.setForeground(Color.WHITE);
		lblHiAdmin.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
		lblHiAdmin.setBounds(0, 86, 142, 22);
		profile.add(lblHiAdmin);

		btnExit = new JButton("Exit");
		btnExit.setIcon(new ImageIcon(Server.class.getResource("/images/icons8-safe-out-32.png")));
		btnExit.setForeground(Color.WHITE);
		btnExit.setBackground(new Color(119, 165, 251));
		btnExit.setBounds(210, 45, 78, 31);
		btnExit.setBorder(null);
		btnExit.setFocusPainted(false);
		profile.add(btnExit);

		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(192, 32, 6, 55);
		profile.add(panel);

		/* Task zone */
		task = new JPanel();
		task.setBackground(new Color(81, 126, 211));
		task.setPreferredSize(new Dimension(50, 50));
		status.add(task, BorderLayout.CENTER);
		task.setLayout(null);

		user = new JList<String>(new DefaultListModel<String>());
		scrollPane = new JScrollPane(user);
		scrollPane.setBounds(12, 49, 276, 253);
		task.add(scrollPane);

		JLabel lblNewLabel_1 = new JLabel("Online");
		lblNewLabel_1.setForeground(Color.WHITE);
		lblNewLabel_1.setFont(new Font("Comic Sans MS", Font.PLAIN, 29));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(0, 12, 300, 25);
		task.add(lblNewLabel_1);

		/**
		 * Main working space
		 */
		workspace = new JPanel();
		main.add(workspace, BorderLayout.CENTER);
		workspace.setLayout(new BorderLayout(5, 5));

		JLabel lblNewLabel = new JLabel("Configuration");
		lblNewLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 40));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		workspace.add(lblNewLabel, BorderLayout.NORTH);

		JPanel panel_2 = new JPanel();
		panel_2.setBackground(Color.WHITE);
		workspace.add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(null);

		JLabel lblDomain = new JLabel("Domain");
		lblDomain.setFont(new Font("Dialog", Font.PLAIN, 20));
		lblDomain.setBounds(28, 21, 88, 46);
		panel_2.add(lblDomain);

		textDomain = new JTextField();
		textDomain.setText("127.0.0.1");
		textDomain.setBounds(127, 24, 269, 46);
		panel_2.add(textDomain);
		textDomain.setColumns(10);

		JLabel lblPort = new JLabel("Port");
		lblPort.setFont(new Font("Dialog", Font.PLAIN, 20));
		lblPort.setBounds(28, 85, 88, 46);
		panel_2.add(lblPort);

		textPort = new JTextField();
		textPort.setText("8080");
		textPort.setColumns(10);
		textPort.setBounds(127, 82, 269, 46);
		panel_2.add(textPort);

		btnStart = new JButton("Start");
		btnStart.setBounds(92, 159, 117, 25);
		panel_2.add(btnStart);

		JButton btnEnd = new JButton("End");
		btnEnd.setForeground(Color.WHITE);
		btnEnd.setBackground(Color.RED);
		btnEnd.setBounds(243, 159, 117, 25);
		panel_2.add(btnEnd);

		statusServer = new JLabel("");
		statusServer.setForeground(Color.GREEN);
		statusServer.setBounds(127, 132, 193, 15);
		panel_2.add(statusServer);

		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (server != null) {
					return;
				}
				statusServer.setText("Server started ...");
				Thread t = new Thread() {
					public void run() {
						domain = textDomain.getText().trim();
						port = Integer.parseInt(textPort.getText().trim());
						online = new ArrayList<Handler>();
						try {
							server = new ServerSocket(port);
							System.out.println("[*] Server started ...");

							while (true) {
								Socket connect = server.accept();

								Handler client = new Handler(connect);
								online.add(client);
								client.start();
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				};
				t.start();
			}
		});

		btnEnd.addActionListener(e -> {
			try {
				if (!server.isClosed()) {
					server.close();
				}
				statusServer.setText("Server ended ...");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		this.validate();
		this.repaint();

		/* Set selection buttons */
		ArrayList<JButton> buttons = new ArrayList<JButton>();
		buttons.add(btnDashboard);

		for (JButton btn : buttons) {
			btn.addMouseListener(new MouseListener() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					_choice = btn;

					ArrayList<JButton> buttons = new ArrayList<JButton>();
					buttons.add(btnDashboard);

					for (JButton btn : buttons) {
						if (_choice == btn) {
							btn.setBackground(new Color(39, 55, 80));
							btn.setBorder(new MatteBorder(0, 5, 0, 0, new Color(233, 229, 238)));
							btn.setFont(new Font("AnjaliOldLipi", Font.BOLD, 16));
						} else {
							btn.setBackground(new Color(23, 33, 53));
							btn.setBorder(null);
							btn.setFont(new Font("AnjaliOldLipi", Font.PLAIN, 16));
						}
					}
				}
				@Override
				public void mouseEntered(MouseEvent arg0) {
					// TODO Auto-generated method stub
					if (_choice == btn) {
						return;
					}

					btn.setBackground(new Color(39, 55, 80));
					btn.setBorder(new MatteBorder(0, 5, 0, 0, new Color(233, 229, 238)));
					btn.setFont(new Font("AnjaliOldLipi", Font.BOLD, 16));
				}
				@Override
				public void mouseExited(MouseEvent arg0) {
					// TODO Auto-generated method stub
					if (_choice == btn) {
						return;
					}

					btn.setBackground(new Color(23, 33, 53));
					btn.setBorder(null);
					btn.setFont(new Font("AnjaliOldLipi", Font.PLAIN, 16));
				}
				@Override
				public void mousePressed(MouseEvent arg0) {}
				@Override
				public void mouseReleased(MouseEvent arg0) {}
			});
		}

		/* Set action close app */
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(0);
			}
		});
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Server _view = new Server();
		_view.setVisible(true);

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					_view.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
