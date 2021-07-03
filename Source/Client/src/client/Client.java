package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.MatteBorder;
import javax.swing.JLabel;
import javax.swing.JList;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import java.awt.FlowLayout;
import javax.swing.ListSelectionModel;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class Client extends JFrame {
	private static final long serialVersionUID = 1L;

	/* --------- Socket client -------------- */
	private String domain;
	private int port;
	private Socket connect;
	private InputStream response;
	private OutputStream request;
	private BufferedReader requestReader;
	private String currentChat;
	private ArrayList<String> online;
	private HashMap<String, JPanel> chatTab;
	/* -------------------------------------- */

	/* --------- Java swing ------------------ */
	private JButton _choice;
	private JPanel contentPane;
	private JButton btnDashboard;
	private JButton btnServer;
	private JPanel profile;
	private JPanel task;
	private JPanel workspace;
	private JLabel lblTitle;
	private JTextField search;
	private JLabel avatar;
	private JLabel lbTest;
	private JButton btnExit;
	private JPanel writting;
	private JPanel contact;
	private JList<String> user;
	private JLabel lblNewLabel;
	private JLabel name;
	private JButton btnSend;
	private JTextArea typing;
	private JPanel serverInfo;
	private JLabel lblNewLabel_1;
	private JTextField textDomain;
	private JLabel lblNewLabel_2;
	private JTextField textPort;
	private JLabel lblNewLabel_3;
	private JScrollPane sp;
	private JButton btnHappy;
	private JButton btnBoring;
	private JButton btnCry;
	private JButton btnSad;
	private JButton btnWow;
	private JButton btnFile;
	private JScrollPane sp2;
	/* ------------------------------------------------------- */

	/**
	 * Create the frame.
	 * 
	 * @throws UnknownHostException
	 * 
	 * @throws IOException
	 */
	public Client(String server, String portSv) throws UnknownHostException, IOException {
		/* Set up */
		this.online = new ArrayList<>();
		this.chatTab = new HashMap<String, JPanel>();
		this.domain = server;
		this.port = Integer.parseInt(portSv);
		this.connect = new Socket(this.domain, this.port);
		this.request = this.connect.getOutputStream();
		this.response = this.connect.getInputStream();
		this.requestReader = new BufferedReader(new InputStreamReader(this.response));

		/* Making GUI by java swing */
		this.view();
	}

	/**
	 * 
	 * @param username
	 * @param password
	 * @return
	 * @throws IOException
	 */
	private boolean signin(String username, String password) throws IOException {
		String content = "signin " + username + " " + password + "\n";
		request.write(content.getBytes());

		String response = requestReader.readLine();
		if (response.equalsIgnoreCase("success")) {
			handle();
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @param username
	 * @param password
	 * @return
	 * @throws IOException
	 */
	private boolean signup(String username, String password) throws IOException {
		String content = "signup " + username + " " + password + "\n";
		request.write(content.getBytes());

		String response = requestReader.readLine();
		if (response.equalsIgnoreCase("success")) {
			signin(username, password);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 */
	private void handle() {
		Thread t = new Thread() {
			@Override
			public void run() {
				try {
					String line;
					while ((line = requestReader.readLine()) != null) {
						String[] data = line.split(" ");

						String flag = data[0];
						if (flag.equals("on")) {
							String username = data[1];
							online.add(username);
							DefaultListModel<String> model = (DefaultListModel<String>) user.getModel();
							model.addElement(username);

							JPanel panel = new JPanel();
							panel.setPreferredSize(new Dimension(350, 300));
							panel.setBackground(Color.white);
							panel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
							chatTab.put(username, panel);
						} else if (flag.equals("off")) {
							String username = data[1];
							online.remove(username);
							DefaultListModel<String> model = (DefaultListModel<String>) user.getModel();
							model.removeElement(username);
						} else if (flag.equals("text")) {
							String sender = data[1];
							String rest = line.substring(line.indexOf(" ")).replaceFirst(" ", "");
							String content = rest.substring(rest.indexOf(" ")).replaceFirst(" ", "");
							JPanel panel = chatTab.get(sender);
							JTextArea row = new JTextArea();
							row.setColumns(24);
							row.setRows(3);
							DateTimeFormatter fm = DateTimeFormatter.ofPattern("HH:mm");
							row.setText(sender + " at " + LocalTime.now().format(fm) + ": " + content);
							panel.add(row);
							validate();
							repaint();
						} else if (flag.equals("emoji")) {
							String sender = data[1];
							String rest = line.substring(line.indexOf(" ")).replaceFirst(" ", "");
							String link = rest.substring(rest.indexOf(" ")).replaceFirst(" ", "");

							JPanel panel = chatTab.get(sender);
							ImageIcon image = new ImageIcon(Client.class.getResource("/icons/" + link + ".png"));
							DateTimeFormatter fm = DateTimeFormatter.ofPattern("HH:mm");
							JLabel row = new JLabel(sender + " at " + LocalTime.now().format(fm) + ": ");
							row.setHorizontalTextPosition(SwingConstants.LEADING);
							row.setAlignmentX(SwingConstants.LEADING);
							row.setPreferredSize(new Dimension(300, 40));
							row.setIcon(image);
							panel.add(row);
							validate();
							repaint();
						}
					}
				} catch (Exception ex) {
					try {
						connect.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		};
		t.start();
	}

	private void view() {
		/* Load setting information */
		this._choice = btnDashboard;

		/* Initial view and event */
		/**
		 * Common view
		 **/
		setBounds(250, 100, 820, 600);
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
		btnDashboard.addActionListener(e -> {
			workspace.removeAll();
			this.dashboard();
			workspace.validate();
			workspace.repaint();
		});
		nav.add(btnDashboard);
		btnServer = new JButton("Server");
		btnServer.setForeground(Color.WHITE);
		btnServer.setFocusPainted(false);
		btnServer.setFont(new Font("AnjaliOldLipi", Font.PLAIN, 16));
		btnServer.setBorder(null);
		btnServer.setBackground(new Color(23, 33, 53));
		btnServer.setBounds(0, 152, 150, 53);
		btnServer.addActionListener(e -> {
			workspace.removeAll();
			workspace.add(new ServerList());
			workspace.validate();
			workspace.repaint();
		});
		nav.add(btnServer);

		/**
		 * The top of the frame: Header
		 */
		JPanel header = new JPanel();
		header.setBackground(new Color(67, 119, 202));
		header.setPreferredSize(new Dimension(70, 70));
		contentPane.add(header, BorderLayout.NORTH);
		header.setLayout(null);

		lblTitle = new JLabel("ZChat");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setForeground(Color.WHITE);
		lblTitle.setFont(new Font("DejaVu Sans Light", Font.PLAIN, 30));
		lblTitle.setBounds(12, 12, 324, 46);
		header.add(lblTitle);

		search = new JTextField();
		search.setFont(new Font("Comic Sans MS", Font.PLAIN, 15));
		search.setText("Search ...");
		search.setBounds(434, 27, 202, 25);
		search.setBorder(null);
		header.add(search);
		search.setColumns(10);

		JLabel dotquestion = new JLabel("");
		dotquestion.setBackground(Color.WHITE);
		dotquestion.setForeground(Color.WHITE);
		dotquestion.setIcon(new ImageIcon(Client.class.getResource("/images/icons8-search-property-32.png")));
		dotquestion.setBounds(654, 12, 48, 46);
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
		avatar.setIcon(new ImageIcon(Client.class.getResource("/images/icons8-male-user-64.png")));
		profile.add(avatar);

		lbTest = new JLabel("Hello @");
		lbTest.setHorizontalAlignment(SwingConstants.CENTER);
		lbTest.setForeground(Color.WHITE);
		lbTest.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
		lbTest.setBounds(0, 86, 142, 22);
		profile.add(lbTest);

		btnExit = new JButton("Exit");
		btnExit.setIcon(new ImageIcon(Client.class.getResource("/images/icons8-safe-out-32.png")));
		btnExit.setForeground(Color.WHITE);
		btnExit.setBackground(new Color(119, 165, 251));
		btnExit.setBounds(210, 45, 78, 31);
		btnExit.setBorder(null);
		btnExit.setFocusPainted(false);
		profile.add(btnExit);
		btnExit.addActionListener(e -> {
			String data = "signout\n";
			try {
				request.write(data.getBytes());
				dispose();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		/* Task zone */
		task = new JPanel();
		task.setBackground(new Color(81, 126, 211));
		task.setPreferredSize(new Dimension(50, 50));
		status.add(task, BorderLayout.CENTER);
		task.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		lblNewLabel = new JLabel("Online");
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		task.add(lblNewLabel);

		user = new JList<String>(new DefaultListModel<String>());
		user.setPreferredSize(new Dimension(250, 100));
		user.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		user.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String client = user.getSelectedValue();
				if (currentChat != null) {
					if (client.equals(currentChat)) {
						return;
					}
				}
				currentChat = client;
				name.setText(client);
				JPanel panel = new JPanel();
				panel.setBackground(Color.WHITE);
				panel.setPreferredSize(new Dimension(350, 300));
				panel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
				if (chatTab.containsKey(currentChat)) {
					panel = chatTab.get(currentChat);
				} else {
					chatTab.put(currentChat, panel);
				}
				sp.setViewportView(panel);
				validate();
				repaint();

			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
			}
		});
		sp2 = new JScrollPane(user);
		task.add(sp2);

		serverInfo = new JPanel();
		serverInfo.setPreferredSize(new Dimension(250, 100));
		task.add(serverInfo);
		serverInfo.setLayout(null);

		lblNewLabel_1 = new JLabel("Domain");
		lblNewLabel_1.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
		lblNewLabel_1.setBounds(12, 27, 70, 33);
		serverInfo.add(lblNewLabel_1);

		textDomain = new JTextField();
		textDomain.setBounds(88, 27, 150, 33);
		textDomain.setText(domain);
		serverInfo.add(textDomain);
		textDomain.setColumns(10);

		lblNewLabel_2 = new JLabel("Port");
		lblNewLabel_2.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
		lblNewLabel_2.setBounds(15, 63, 62, 28);
		serverInfo.add(lblNewLabel_2);

		textPort = new JTextField();
		textPort.setColumns(10);
		textPort.setBounds(88, 71, 150, 19);
		textPort.setText(String.valueOf(port));
		serverInfo.add(textPort);

		lblNewLabel_3 = new JLabel("Server");
		lblNewLabel_3.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		lblNewLabel_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_3.setBounds(83, 0, 70, 28);
		serverInfo.add(lblNewLabel_3);

		/**
		 * Main working space
		 */
		workspace = new JPanel();
		workspace.setBackground(Color.LIGHT_GRAY);
		main.add(workspace, BorderLayout.CENTER);
		workspace.setLayout(new BorderLayout(5, 5));

		this.dashboard();

		/* Set selection buttons */
		ArrayList<JButton> buttons = new ArrayList<JButton>();
		buttons.add(btnDashboard);
		buttons.add(btnServer);

		for (JButton btn : buttons) {
			btn.addMouseListener(new MouseListener() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					_choice = btn;

					ArrayList<JButton> buttons = new ArrayList<JButton>();
					buttons.add(btnDashboard);
					buttons.add(btnServer);
					buttons.add(btnServer);

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
				public void mousePressed(MouseEvent arg0) {
				}

				@Override
				public void mouseReleased(MouseEvent arg0) {
				}
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

	private void dashboard() {
		sp = new JScrollPane();
		sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		workspace.add(sp, BorderLayout.CENTER);

		contact = new JPanel();
		contact.setBackground(Color.WHITE);
		workspace.add(contact, BorderLayout.NORTH);
		contact.setPreferredSize(new Dimension(100, 50));
		contact.setLayout(null);

		name = new JLabel("Username");
		name.setBounds(12, 12, 119, 24);
		name.setFont(new Font("Arial", Font.BOLD, 20));
		contact.add(name);

		JButton btnVoice = new JButton("");
		btnVoice.setIcon(new ImageIcon(Client.class.getResource("/images/icons8-voice-48.png")));
		btnVoice.setForeground(Color.WHITE);
		btnVoice.setFocusPainted(false);
		btnVoice.setBorder(null);
		btnVoice.setBackground(Color.WHITE);
		btnVoice.setBounds(224, 9, 58, 31);
		contact.add(btnVoice);

		JButton btnVideo = new JButton("");
		btnVideo.setIcon(new ImageIcon(Client.class.getResource("/images/icons8-video-48.png")));
		btnVideo.setForeground(Color.WHITE);
		btnVideo.setFocusPainted(false);
		btnVideo.setBorder(null);
		btnVideo.setBackground(Color.WHITE);
		btnVideo.setBounds(294, 9, 48, 31);
		contact.add(btnVideo);

		writting = new JPanel();
		writting.setBackground(Color.WHITE);
		writting.setPreferredSize(new Dimension(100, 100));
		workspace.add(writting, BorderLayout.SOUTH);
		writting.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 12, 271, 46);
		writting.add(scrollPane);

		typing = new JTextArea();
		scrollPane.setViewportView(typing);

		btnHappy = new JButton("");
		btnHappy.setIcon(new ImageIcon(Client.class.getResource("/icons/happy.png")));
		btnHappy.setForeground(Color.WHITE);
		btnHappy.setFocusPainted(false);
		btnHappy.setBorder(null);
		btnHappy.setBackground(Color.WHITE);
		btnHappy.setBounds(22, 69, 46, 31);
		btnHappy.addActionListener(e -> {
			try {
				String data = "emoji " + currentChat + " happy\n";
				ImageIcon image = new ImageIcon(Client.class.getResource("/icons/happy.png"));
				DateTimeFormatter fm = DateTimeFormatter.ofPattern("HH:mm");
				JLabel row = new JLabel("Me at " + LocalTime.now().format(fm) + ": ");
				row.setHorizontalTextPosition(SwingConstants.LEADING);
				row.setAlignmentX(SwingConstants.LEADING);
				row.setPreferredSize(new Dimension(300, 40));
				row.setIcon(image);
				JPanel panel = chatTab.get(currentChat);
				panel.add(row);
				validate();
				repaint();
				request.write(data.getBytes());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		writting.add(btnHappy);

		btnBoring = new JButton("");
		btnBoring.setIcon(new ImageIcon(Client.class.getResource("/icons/boring.png")));
		btnBoring.setForeground(Color.WHITE);
		btnBoring.setFocusPainted(false);
		btnBoring.setBorder(null);
		btnBoring.setBackground(Color.WHITE);
		btnBoring.setBounds(79, 69, 40, 31);
		btnBoring.addActionListener(e -> {
			try {
				String data = "emoji " + currentChat + " boring\n";
				ImageIcon image = new ImageIcon(Client.class.getResource("/icons/boring.png"));
				DateTimeFormatter fm = DateTimeFormatter.ofPattern("HH:mm");
				JLabel row = new JLabel("Me at " + LocalTime.now().format(fm) + ": ");
				row.setHorizontalTextPosition(SwingConstants.LEADING);
				row.setAlignmentX(SwingConstants.LEADING);
				row.setPreferredSize(new Dimension(300, 40));
				row.setIcon(image);
				JPanel panel = chatTab.get(currentChat);
				panel.add(row);
				validate();
				repaint();
				request.write(data.getBytes());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		writting.add(btnBoring);

		btnCry = new JButton("");
		btnCry.setIcon(new ImageIcon(Client.class.getResource("/icons/cry.png")));
		btnCry.setForeground(Color.WHITE);
		btnCry.setFocusPainted(false);
		btnCry.setBorder(null);
		btnCry.setBackground(Color.WHITE);
		btnCry.setBounds(131, 69, 40, 31);
		btnCry.addActionListener(e -> {
			try {
				String data = "emoji " + currentChat + " cry\n";
				ImageIcon image = new ImageIcon(Client.class.getResource("/icons/cry.png"));
				DateTimeFormatter fm = DateTimeFormatter.ofPattern("HH:mm");
				JLabel row = new JLabel("Me at " + LocalTime.now().format(fm) + ": ");
				row.setHorizontalTextPosition(SwingConstants.LEADING);
				row.setAlignmentX(SwingConstants.LEADING);
				row.setPreferredSize(new Dimension(300, 40));
				row.setIcon(image);
				JPanel panel = chatTab.get(currentChat);
				panel.add(row);
				validate();
				repaint();
				request.write(data.getBytes());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		writting.add(btnCry);

		btnSad = new JButton("");
		btnSad.setIcon(new ImageIcon(Client.class.getResource("/icons/sad.png")));
		btnSad.setForeground(Color.WHITE);
		btnSad.setFocusPainted(false);
		btnSad.setBorder(null);
		btnSad.setBackground(Color.WHITE);
		btnSad.setBounds(195, 69, 40, 31);
		btnSad.addActionListener(e -> {
			try {
				String data = "emoji " + currentChat + " sad\n";
				ImageIcon image = new ImageIcon(Client.class.getResource("/icons/sad.png"));
				DateTimeFormatter fm = DateTimeFormatter.ofPattern("HH:mm");
				JLabel row = new JLabel("Me at " + LocalTime.now().format(fm) + ": ");
				row.setHorizontalTextPosition(SwingConstants.LEADING);
				row.setAlignmentX(SwingConstants.LEADING);
				row.setPreferredSize(new Dimension(300, 40));
				row.setIcon(image);
				JPanel panel = chatTab.get(currentChat);
				panel.add(row);
				validate();
				repaint();
				request.write(data.getBytes());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		writting.add(btnSad);

		btnWow = new JButton("");
		btnWow.setIcon(new ImageIcon(Client.class.getResource("/icons/wow.png")));
		btnWow.setForeground(Color.WHITE);
		btnWow.setFocusPainted(false);
		btnWow.setBorder(null);
		btnWow.setBackground(Color.WHITE);
		btnWow.setBounds(247, 69, 40, 31);
		btnWow.addActionListener(e -> {
			try {
				String data = "emoji " + currentChat + " wow\n";
				ImageIcon image = new ImageIcon(Client.class.getResource("/icons/wow.png"));
				DateTimeFormatter fm = DateTimeFormatter.ofPattern("HH:mm");
				JLabel row = new JLabel("Me at " + LocalTime.now().format(fm) + ": ");
				row.setHorizontalTextPosition(SwingConstants.LEADING);
				row.setAlignmentX(SwingConstants.LEADING);
				row.setPreferredSize(new Dimension(300, 40));
				row.setIcon(image);
				JPanel panel = chatTab.get(currentChat);
				panel.add(row);
				validate();
				repaint();
				request.write(data.getBytes());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		writting.add(btnWow);

		btnFile = new JButton("File");
		btnFile.setBounds(295, 43, 70, 25);
		btnFile.addActionListener(e -> {

		});
		writting.add(btnFile);

		btnSend = new JButton("Send");
		btnSend.setBounds(295, 12, 70, 25);
		writting.add(btnSend);
		btnSend.addActionListener(e -> {
			String content = typing.getText();
			try {
				String data = "text " + this.currentChat + " " + content + "\n";
				request.write(data.getBytes());

				JPanel panel = chatTab.get(currentChat);
				JTextArea row = new JTextArea();
				row.setColumns(24);
				row.setRows(3);
				DateTimeFormatter fm = DateTimeFormatter.ofPattern("HH:mm");
				row.setText("Me at " + LocalTime.now().format(fm) + ": " + content);
				panel.add(row);
				validate();
				repaint();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			typing.setText("");
		});

		this.validate();
		this.repaint();
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		LoginForm form = new LoginForm();
		form.setVisible(true);

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				form.getBtnLogin().addActionListener(e -> {
					try {
						String server = form.getDomain().trim();
						String portSv = form.getPort().trim();
						String username = form.getUsername().trim();
						String password = form.getPassword().trim();

						Client view = new Client(server, portSv);

						if (view.signin(username, password)) {
							form.dispose();
							view.setVisible(true);
							// view.receive();
						} else {
							form.setError("Wrong username and password");
						}

					} catch (Exception e1) {
						e1.printStackTrace();
					}
				});

				form.getBtnCancel().addActionListener(e -> {
					try {
						String server = form.getDomain().trim();
						String portSv = form.getPort().trim();
						String username = form.getUsername().trim();
						String password = form.getPassword().trim();

						Client view = new Client(server, portSv);
						if (view.signup(username, password)) {
							form.dispose();
							view.setVisible(true);
						} else {
							form.setError("Username and password are existed");
						}

					} catch (Exception e1) {
						e1.printStackTrace();
					}
				});
			}
		});
	}
}
