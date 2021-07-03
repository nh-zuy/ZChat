package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.JButton;
import javax.swing.border.MatteBorder;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.border.BevelBorder;

public class ServerList extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/* Data model */
	private HashMap<String, String> serverList;
	/* View component */
	private JTable table;
	private JPanel header;
	private JTextField textDomain;
	private JButton btnAdd, btnUpdate, btnDelete;
	private JLabel message;
	private JTextField textPort;

	/**
	 * Create the panel.
	 */
	public ServerList() {
		this.serverList = new HashMap<String, String>();

		setBorder(null);
		setBackground(Color.WHITE);
		setLayout(new BorderLayout(0, 0));

		header = new JPanel();
		header.setBorder(new LineBorder(Color.WHITE, 5));
		header.setBackground(Color.WHITE);
		header.setPreferredSize(new Dimension(100, 300));
		add(header, BorderLayout.NORTH);
		header.setLayout(new BorderLayout(0, 0));

		JPanel form = new JPanel();
		form.setBackground(new Color(119, 165, 251));
		add(form, BorderLayout.CENTER);
		form.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JPanel title_1 = new JPanel();
		title_1.setLayout(null);
		title_1.setPreferredSize(new Dimension(1000, 70));
		title_1.setBackground(new Color(119, 165, 251));
		form.add(title_1);

		JLabel lblNewLabel_1 = new JLabel("Server");
		lblNewLabel_1.setForeground(Color.WHITE);
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setFont(new Font("Comic Sans MS", Font.PLAIN, 35));
		lblNewLabel_1.setBackground(Color.WHITE);
		lblNewLabel_1.setBounds(311, 0, 363, 43);
		title_1.add(lblNewLabel_1);

		message = new JLabel("* Select row to handle data !");
		message.setHorizontalAlignment(SwingConstants.CENTER);
		message.setForeground(Color.BLACK);
		message.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		message.setBounds(376, 38, 229, 32);
		title_1.add(message);

		JPanel input = new JPanel();
		input.setPreferredSize(new Dimension(1000, 400));
		input.setBackground(new Color(119, 165, 251));
		form.add(input);
		input.setLayout(null);

		JPanel forminput_1_2 = new JPanel();
		forminput_1_2.setLayout(null);
		forminput_1_2.setPreferredSize(new Dimension(1000, 50));
		forminput_1_2.setBackground(new Color(119, 165, 251));
		forminput_1_2.setBounds(309, 0, 186, 50);
		input.add(forminput_1_2);

		textDomain = new JTextField();
		textDomain.setText("");
		textDomain.setHorizontalAlignment(SwingConstants.CENTER);
		textDomain.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		textDomain.setColumns(10);
		textDomain.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		textDomain.setBounds(12, 0, 168, 50);
		forminput_1_2.add(textDomain);

		/* Three of button */
		btnAdd = new JButton("Add");
		btnAdd.setBackground(Color.GREEN);
		btnAdd.setFont(new Font("Dialog", Font.BOLD, 16));
		btnAdd.setBounds(334, 62, 86, 50);
		btnAdd.setFocusPainted(false);
		btnAdd.setBorder(new MatteBorder(0, 0, 4, 0, (Color) new Color(0, 0, 0)));
		input.add(btnAdd);

		btnUpdate = new JButton("Update");
		btnUpdate.setForeground(Color.BLACK);
		btnUpdate.setFont(new Font("Dialog", Font.BOLD, 16));
		btnUpdate.setFocusPainted(false);
		btnUpdate.setBorder(new MatteBorder(0, 0, 4, 0, (Color) new Color(0, 0, 0)));
		btnUpdate.setBackground(Color.YELLOW);
		btnUpdate.setBounds(442, 62, 86, 50);
		input.add(btnUpdate);

		btnDelete = new JButton("Delete");
		btnDelete.setForeground(Color.WHITE);
		btnDelete.setFont(new Font("Dialog", Font.BOLD, 16));
		btnDelete.setFocusPainted(false);
		btnDelete.setBorder(new MatteBorder(0, 0, 4, 0, (Color) new Color(0, 0, 0)));
		btnDelete.setBackground(Color.RED);
		btnDelete.setBounds(540, 62, 86, 50);
		input.add(btnDelete);

		JPanel forminput_1_2_1 = new JPanel();
		forminput_1_2_1.setLayout(null);
		forminput_1_2_1.setPreferredSize(new Dimension(1000, 50));
		forminput_1_2_1.setBackground(new Color(119, 165, 251));
		forminput_1_2_1.setBounds(493, 0, 186, 50);
		input.add(forminput_1_2_1);

		JLabel Name = new JLabel("Port");
		Name.setHorizontalAlignment(SwingConstants.CENTER);
		Name.setForeground(Color.RED);
		Name.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		Name.setBounds(0, 2, 59, 50);
		forminput_1_2_1.add(Name);

		textPort = new JTextField();
		textPort.setText("");
		textPort.setHorizontalAlignment(SwingConstants.CENTER);
		textPort.setFont(new Font("Arial", Font.BOLD, 20));
		textPort.setColumns(10);
		textPort.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		textPort.setBounds(59, 12, 114, 26);
		forminput_1_2_1.add(textPort);

		/* Manipulating data */
		this.resetTextField();
		this.setData();
		this.setEventButton();
	}

	/**
	 * Reset data in textfield
	 */
	private void resetTextField() {
		message.setForeground(Color.BLACK);
		message.setText("* Select row to handle data !");
		textDomain.setText("");
		textPort.setText("");

		this.validate();
		this.repaint();
	}

	/**
	 * Show information account
	 */
	private void setData() {
		/* Set data for JTable */
		Vector<Vector<String>> data = new Vector<Vector<String>>();
		Vector<String> row = new Vector<String>();
		Vector<String> headers = new Vector<String>();
		headers.add("Domain");
		headers.add("Port");
		try {
			File f = new File("database/server.txt");
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null) {
				String[] d = line.split(" ");
				String domain = d[0];
				String port = d[1];
				serverList.put(domain, port);

				row.add(domain);
				row.add(port);
				data.add(new Vector<String>(row));
				row.clear();
			}
			fr.close();
			br.close();
		} catch (Exception ex) {
			System.out.println("Loi doc file: " + ex);
		}

		table = new JTable(data, headers);
		table.setRowHeight(28);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.getColumnModel().getColumn(0).setPreferredWidth(21);
		table.getColumnModel().getColumn(1).setPreferredWidth(80);
		table.getTableHeader().setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);

		JScrollPane sp = new JScrollPane(table);
		header.add(sp);
	}

	/**
	 * 
	 */
	private void setEventButton() {
		/* Click button */
		btnAdd.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				String domain = textDomain.getText().trim();
				String port = textPort.getText().trim();

				if (domain.isBlank() || port.isBlank()) {
					JOptionPane.showMessageDialog(new JPanel(), "Please fill all blank!", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else {
					if (serverList.containsKey(domain)) {
						String value = serverList.get(domain);
						if (port.equals(value)) {
							JOptionPane.showMessageDialog(new JPanel(), "Duplicate server!", "Error",
									JOptionPane.ERROR_MESSAGE);
							return;
						}
					}
					
					try {
						File f = new File("database/server.txt");
						FileWriter fw = new FileWriter(f, true);
						fw.write(domain);
						fw.write(" ");
						fw.write(port);
						fw.write("\n");
						fw.close();
						serverList.put(domain, port);
						DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
						String[] row = { domain, port };
						tableModel.addRow(row);
					} catch (IOException ex) {
						System.out.println("Error file" + ex);
					}
				}
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {}
			@Override
			public void mouseExited(MouseEvent arg0) {}
			@Override
			public void mousePressed(MouseEvent arg0) {}
			@Override
			public void mouseReleased(MouseEvent arg0) {}
		});

		/* Click button update */
		btnUpdate.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				String domain = textDomain.getText().trim();
				String port = textPort.getText().trim();

				if (domain.isBlank() || port.isBlank()) {
					JOptionPane.showMessageDialog(new JPanel(), "Please fill all blank!", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else {
					serverList.replace(domain, port);
					DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
					tableModel.setValueAt(domain, table.getSelectedRow(), 0);
					tableModel.setValueAt(port, table.getSelectedRow(), 1);
					try {
						File f = new File("database/server.txt");
						FileWriter fw = new FileWriter(f);
						
						for (@SuppressWarnings("rawtypes") Map.Entry me: serverList.entrySet()) {
							fw.write((String)me.getKey());
							fw.write(" ");
							fw.write((String)me.getValue());
							fw.write("\n");
						}
						fw.close();
					} catch (IOException ex) {
						System.out.println("Error file" + ex);
					}
				}
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {}
			@Override
			public void mouseExited(MouseEvent arg0) {}
			@Override
			public void mousePressed(MouseEvent arg0) {}
			@Override
			public void mouseReleased(MouseEvent arg0) {}
		});

		/* Click button Delete */
		btnDelete.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				String domain = textDomain.getText().trim();
				String port = textPort.getText().trim();

				if (domain.isBlank() || port.isBlank()) {
					JOptionPane.showMessageDialog(new JPanel(), "Please fill all blank!", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else {
					serverList.remove(domain);
					DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
					tableModel.removeRow(table.getSelectedRow());
					try {
						File f = new File("database/server.txt");
						FileWriter fw = new FileWriter(f);
						
						for (@SuppressWarnings("rawtypes") Map.Entry me: serverList.entrySet()) {
							fw.write((String)me.getKey());
							fw.write(" ");
							fw.write((String)me.getValue());
							fw.write("\n");
						}
						fw.close();
					} catch (IOException ex) {
						System.out.println("Error file" + ex);
					}
				}
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {}
			@Override
			public void mouseExited(MouseEvent arg0) {}
			@Override
			public void mousePressed(MouseEvent arg0) {}
			@Override
			public void mouseReleased(MouseEvent arg0) {}
		});

		/* Event for data table */
		table.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				DefaultTableModel tableModel = (DefaultTableModel) table.getModel();

				String domain = tableModel.getValueAt(table.getSelectedRow(), 0).toString();
				String port = tableModel.getValueAt(table.getSelectedRow(), 1).toString();

				textDomain.setText(domain);
				textPort.setText(port);
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {}
			@Override
			public void mouseExited(MouseEvent arg0) {}
			@Override
			public void mousePressed(MouseEvent arg0) {}
			@Override
			public void mouseReleased(MouseEvent arg0) {}
		});
	}
}
