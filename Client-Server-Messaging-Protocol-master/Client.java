package board;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

public class Client extends javax.swing.JFrame {
	private static final long serialVersionUID = 1L;
	public boolean isConnected = false;
	public Socket socket;

	public Client() {
		initComponents();
	}

	@SuppressWarnings("unchecked")
	private void initComponents() {

		ipAddress = new javax.swing.JTextField();
		jLabel1 = new javax.swing.JLabel();
		portNumber = new javax.swing.JTextField();
		jLabel2 = new javax.swing.JLabel();
		connectButton = new javax.swing.JButton();
		connectButton.setAction(action);
		postButton = new javax.swing.JButton();
		// posts note to the board
		postButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (postXCoordinate.getText().equals("") || postYCoordinate.getText().equals("")
						|| height.getText().equals("") || width.getText().equals("")) {
					// UPDATE DOC TO SAY WHAT FIELDS ARE MANDATORY TO POST A NOTE
					output.setText("Please fill in all mandatory fields \nto post a note.");
				} else if (!isInteger(postXCoordinate.getText()) || !isInteger(postYCoordinate.getText())
						|| !isInteger(height.getText()) || !isInteger(width.getText())) {
					output.setText("Please enter an integers in the \ncoordinate and dimension text fields.");
				} else if (contentTextArea.getText().contains("@@")) {
					output.setText("Please Stop Trying to Break The Code.");
				} else {
					// send post to the server for processing
					try {
						// ADD THE PROPER CODE TO PUT COLOUR INTO THIS STRING
						String colour, content;
						if (textField.getText().equals("")) {
							colour = "default";
						} else {
							colour = textField.getText();
						}
						if (contentTextArea.getText().equals("")) {
							content = "empty";
						} else {
							content = contentTextArea.getText();
						}

						PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
						BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						String post = "post@@" + postXCoordinate.getText() + "@@" + postYCoordinate.getText() + "@@"
								+ height.getText() + "@@" + width.getText() + "@@" + content + "@@" + colour;
						out.println(post);
						output.setText(in.readLine());

					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}

			}
		});
		jLabel3 = new javax.swing.JLabel();
		jLabel4 = new javax.swing.JLabel();
		jLabel5 = new javax.swing.JLabel();
		pinXCoordinate = new javax.swing.JTextField();
		pinYCoordinate = new javax.swing.JTextField();
		jLabel6 = new javax.swing.JLabel();
		jLabel7 = new javax.swing.JLabel();
		pinButton = new javax.swing.JButton();
		pinButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (pinXCoordinate.getText().equals("") || pinYCoordinate.getText().equals("")) {
					output.setText("Please enter an integer in both \ntext fields.");
				} else if (!isInteger(pinXCoordinate.getText()) || !isInteger(pinYCoordinate.getText())) {
					output.setText("Please enter an integer in both \ntext fields.");
				} else {
					// send it to the server and process
					try {
						PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
						BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						String pin = "pin@@" + pinXCoordinate.getText() + "@@" + pinYCoordinate.getText();
						out.println(pin);
						output.setText(in.readLine());

					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}

			}
		});
		disconnectButton = new javax.swing.JButton();
		disconnectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					socket.close();
					output.setText("Disconnected successfully.");
				} catch (IOException e1) {
					output.setText("Disconnect failed.");
				} catch (NullPointerException e2) {
					output.setText("You are currently not connected.");
				}
			}

		});
		unpinButton = new javax.swing.JButton();
		unpinButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (pinXCoordinate.getText().equals("") || pinYCoordinate.getText().equals("")) {
					output.setText("Please enter an integer in both \ntext fields.");
				} else if (!isInteger(pinXCoordinate.getText()) || !isInteger(pinYCoordinate.getText())) {
					output.setText("Please enter an integer in both \ntext fields.");
				} else {
					// send it to the server and process
					try {
						PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
						BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						String pin = "unpin@@" + pinXCoordinate.getText() + "@@" + pinYCoordinate.getText();
						out.println(pin);
						output.setText(in.readLine());

					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}

			}
		});
		jLabel8 = new javax.swing.JLabel();
		jScrollPane2 = new javax.swing.JScrollPane();
		contentTextArea = new javax.swing.JTextArea();
		jLabel9 = new javax.swing.JLabel();
		postYCoordinate = new javax.swing.JTextField();
		jLabel10 = new javax.swing.JLabel();
		jLabel11 = new javax.swing.JLabel();
		postXCoordinate = new javax.swing.JTextField();
		height = new javax.swing.JTextField();
		width = new javax.swing.JTextField();
		jLabel12 = new javax.swing.JLabel();
		jLabel13 = new javax.swing.JLabel();
		jSeparator1 = new javax.swing.JSeparator();
		jLabel14 = new javax.swing.JLabel();
		jScrollPane1 = new javax.swing.JScrollPane();
		output = new javax.swing.JTextArea();
		jLabel15 = new javax.swing.JLabel();
		jLabel15.setFont(new Font("Tahoma", Font.BOLD, 11));
		getPinsButton = new javax.swing.JButton();
		getPinsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// to do this send a string to the server as "getPins"
				// the server then checks for this string
				// and it sends back a string of pins
				try {
					PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
					BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					String message = "getPins@@gg";
					out.println(message);
					String temp = in.readLine();
					if (temp.equals("")) {
						output.setText("No pins are on the board.");
					} else {
						String[] removed = temp.split(",");
						output.setText("Pins on the board:\n");
						for (int i = 0; i < removed.length; i++) {
							output.append(removed[i] + "\n");
						}
					}

				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
		});
		getColour = new javax.swing.JTextField();
		getContains = new javax.swing.JTextField();
		getRefersTo = new javax.swing.JTextField();
		jLabel16 = new javax.swing.JLabel();
		jLabel17 = new javax.swing.JLabel();
		jLabel18 = new javax.swing.JLabel();
		getInfoButton = new javax.swing.JButton();
		getInfoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String message = "get@@";
				String message2 = "";
				String message3 = "";
				String message4 = "";
				String code = "";
				// add the proper get information
				// we have many if statements to reduce whitespace for more easier parsing
				if (!getColour.getText().equals("")) {
					message2 = getColour.getText() + "@@";
					code = code + "c";
				}

				if (!getContains.getText().equals("")) {
					message3 = getContains.getText() + "@@";
					code = code + "d";
				}

				if (!getRefersTo.getText().equals("")) {
					message4 = getRefersTo.getText();
					code = code + "r";
				}

				message = message + code + "@@" + message2 + message3 + message4;

				// send the get message to the server
				try {
					PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
					BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					out.println(message);
					String temp = in.readLine();
					if (temp.equals("")) {
						output.setText("No pins are on the board.");
					} else {
						output.setText("");
						String[] removed = temp.split("@@");
						for (int i = 0; i < removed.length; i++) {
							output.append(removed[i] + "\n");
						}
					}

				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
		});
		clearButton = new javax.swing.JButton();
		clearButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
					BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					String message = "clear";
					out.println(message);
					// to test if string has content or not
					String temp = in.readLine();
					// if string does not have content
					if (temp.equals("")) {
						output.setText("No notes were removed from the board.");
						// if string has content
					} else {
						String[] removed = temp.split("@@");
						output.setText("Notes that were removed:\n");
						for (int i = 0; i < removed.length; i++) {
							output.append(removed[i] + "\n");
						}
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		jLabel1.setText("IP address:");

		jLabel2.setText("Port Number:");

		connectButton.setText("Connect");
		connectButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					connectButtonAction(evt);
				} catch (NumberFormatException e) {
					output.setText("Connection unsuccessful");
				} catch (UnknownHostException e) {
					output.setText("Connection unsuccessful");
				} catch (IOException e) {
					output.setText("Connection unsuccessful");
				}
			}
		});

		postButton.setText("Post");

		jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
		jLabel3.setText("Connect");

		jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
		jLabel4.setText("Post");

		jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
		jLabel5.setText("Pin");

		pinYCoordinate.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				pinYCoordinateAction(evt);
			}
		});

		jLabel6.setText("X Coordinate:");

		jLabel7.setText("Y Coordinate:");

		pinButton.setText("Pin");
		pinButton.setMaximumSize(new java.awt.Dimension(59, 23));
		pinButton.setMinimumSize(new java.awt.Dimension(59, 23));

		disconnectButton.setText("Disconnect");

		unpinButton.setText("Unpin");

		jLabel8.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
		jLabel8.setText("Get Information");

		contentTextArea.setColumns(20);
		contentTextArea.setRows(5);
		contentTextArea.setPreferredSize(new java.awt.Dimension(164, 60));
		jScrollPane2.setViewportView(contentTextArea);

		jLabel9.setText("Post Content:");

		postYCoordinate.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				postYCoordinateAction(evt);
			}
		});

		jLabel10.setText("X Coordinate:");

		jLabel11.setText("Y Coordinate:");

		jLabel12.setText("Height:");

		jLabel13.setText("Width:");

		jLabel14.setText("Post Color:");

		output.setEditable(false);
		output.setColumns(20);
		output.setRows(5);
		jScrollPane1.setViewportView(output);

		jLabel15.setText("Output");

		getPinsButton.setText("Get Pins");

		jLabel16.setText("Colour:");

		jLabel17.setText("Contains:");

		jLabel18.setText("Refers To:");

		getInfoButton.setText("Get Info");

		clearButton.setText("Clear");

		textField = new JTextField();
		textField.setColumns(10);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING)
				.addComponent(jSeparator1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
						GroupLayout.PREFERRED_SIZE)
				.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout
								.createSequentialGroup().addContainerGap()
								.addGroup(layout.createParallelGroup(Alignment.TRAILING)
										.addGroup(layout.createSequentialGroup().addComponent(getPinsButton)
												.addPreferredGap(ComponentPlacement.RELATED).addComponent(getInfoButton)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(
														clearButton, GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
												.addPreferredGap(ComponentPlacement.UNRELATED))
										.addGroup(
												layout.createSequentialGroup().addGap(0, 22, Short.MAX_VALUE)
														.addGroup(layout.createParallelGroup(Alignment.LEADING)
																.addComponent(jLabel18, Alignment.TRAILING)
																.addComponent(jLabel16).addComponent(jLabel17))
														.addGap(18)
														.addGroup(layout.createParallelGroup(Alignment.LEADING, false)
																.addComponent(getColour, GroupLayout.DEFAULT_SIZE, 83,
																		Short.MAX_VALUE)
																.addComponent(getContains).addComponent(getRefersTo))
														.addGap(62)))
								.addComponent(
										jScrollPane1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
								.addGroup(layout.createSequentialGroup().addGroup(layout
										.createParallelGroup(Alignment.LEADING)
										.addGroup(layout.createSequentialGroup().addGap(51).addComponent(jLabel9)
												.addGap(72).addComponent(jLabel4))
										.addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout
												.createParallelGroup(Alignment.TRAILING).addComponent(jLabel15)
												.addGroup(layout.createSequentialGroup().addComponent(jLabel14)
														.addPreferredGap(ComponentPlacement.RELATED)
														.addComponent(textField, GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
														.addGap(80).addComponent(postButton, GroupLayout.PREFERRED_SIZE,
																83, GroupLayout.PREFERRED_SIZE)))))
										.addGap(27, 96, Short.MAX_VALUE)))
						.addContainerGap())
				.addGroup(
						layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup(Alignment.LEADING)
										.addGroup(layout.createSequentialGroup().addGap(58).addComponent(jLabel3))
										.addGroup(layout.createSequentialGroup().addContainerGap()
												.addGroup(layout.createParallelGroup(Alignment.LEADING)
														.addComponent(jLabel1).addComponent(jLabel2))
												.addPreferredGap(ComponentPlacement.UNRELATED)
												.addGroup(layout.createParallelGroup(Alignment.LEADING, false)
														.addComponent(portNumber, GroupLayout.DEFAULT_SIZE, 101,
																Short.MAX_VALUE)
														.addComponent(ipAddress)))
										.addGroup(layout.createSequentialGroup().addGap(2)
												.addComponent(connectButton, GroupLayout.PREFERRED_SIZE, 89,
														GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.UNRELATED)
												.addComponent(disconnectButton))
										.addGroup(layout.createSequentialGroup().addContainerGap()
												.addComponent(jScrollPane2, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
								.addGap(20)
								.addGroup(layout.createParallelGroup(
										Alignment.TRAILING)
										.addGroup(layout.createSequentialGroup().addGap(8).addGroup(layout
												.createParallelGroup(Alignment.LEADING).addComponent(jLabel10)
												.addComponent(jLabel11).addComponent(jLabel12).addComponent(jLabel13))
												.addPreferredGap(ComponentPlacement.RELATED)
												.addGroup(layout.createParallelGroup(Alignment.LEADING)
														.addComponent(postXCoordinate, GroupLayout.PREFERRED_SIZE, 80,
																GroupLayout.PREFERRED_SIZE)
														.addGroup(layout.createParallelGroup(Alignment.TRAILING, false)
																.addComponent(width, Alignment.LEADING).addComponent(
																		height, Alignment.LEADING)
																.addComponent(postYCoordinate, Alignment.LEADING,
																		GroupLayout.PREFERRED_SIZE, 80,
																		GroupLayout.PREFERRED_SIZE)))
												.addGap(0, 58, Short.MAX_VALUE))
										.addGroup(layout.createSequentialGroup()
												.addPreferredGap(ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
												.addGroup(layout.createParallelGroup(Alignment.TRAILING)
														.addGroup(layout.createSequentialGroup().addComponent(jLabel5)
																.addGap(73))
														.addGroup(layout.createSequentialGroup()
																.addGroup(layout.createParallelGroup(Alignment.LEADING)
																		.addComponent(jLabel6).addComponent(jLabel7))
																.addPreferredGap(ComponentPlacement.RELATED)
																.addGroup(layout.createParallelGroup(Alignment.LEADING)
																		.addComponent(pinXCoordinate,
																				GroupLayout.PREFERRED_SIZE, 80,
																				GroupLayout.PREFERRED_SIZE)
																		.addComponent(pinYCoordinate,
																				GroupLayout.PREFERRED_SIZE, 80,
																				GroupLayout.PREFERRED_SIZE)))
														.addGroup(layout.createSequentialGroup()
																.addComponent(pinButton, GroupLayout.PREFERRED_SIZE, 57,
																		GroupLayout.PREFERRED_SIZE)
																.addPreferredGap(ComponentPlacement.UNRELATED)
																.addComponent(unpinButton)))
												.addGap(29))))
				.addGroup(layout.createSequentialGroup().addGap(49).addComponent(jLabel8).addContainerGap(282,
						Short.MAX_VALUE)));
		layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup()
				.addContainerGap()
				.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(jLabel3).addComponent(jLabel5))
				.addGap(7)
				.addGroup(layout.createParallelGroup(Alignment.BASELINE)
						.addComponent(ipAddress, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(jLabel1)
						.addComponent(pinXCoordinate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(jLabel6))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(layout.createParallelGroup(Alignment.BASELINE)
						.addComponent(portNumber, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(jLabel2)
						.addComponent(pinYCoordinate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(jLabel7))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(connectButton)
						.addComponent(pinButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(disconnectButton).addComponent(unpinButton))
				.addGap(17)
				.addComponent(
						jSeparator1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(layout.createParallelGroup(Alignment.TRAILING)
						.addGroup(layout.createSequentialGroup().addComponent(jLabel9).addGap(1))
						.addGroup(layout.createSequentialGroup().addComponent(jLabel4)
								.addPreferredGap(ComponentPlacement.UNRELATED)))
				.addGroup(layout.createParallelGroup(Alignment.LEADING)
						.addComponent(jScrollPane2, GroupLayout.PREFERRED_SIZE, 96, GroupLayout.PREFERRED_SIZE)
						.addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(Alignment.BASELINE)
								.addComponent(postXCoordinate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(jLabel10)).addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(layout.createParallelGroup(Alignment.BASELINE)
										.addComponent(postYCoordinate, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(jLabel11))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(layout.createParallelGroup(Alignment.BASELINE)
										.addComponent(height, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(jLabel12))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(layout.createParallelGroup(Alignment.BASELINE)
										.addComponent(width, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(jLabel13))))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(jLabel14).addComponent(postButton)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE))
				.addGap(15)
				.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(jLabel8).addComponent(jLabel15))
				.addPreferredGap(ComponentPlacement.UNRELATED)
				.addGroup(layout.createParallelGroup(Alignment.LEADING)
						.addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup(Alignment.BASELINE)
										.addComponent(getColour, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(jLabel16))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(layout.createParallelGroup(Alignment.BASELINE)
										.addComponent(getContains, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(jLabel17))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(layout.createParallelGroup(Alignment.BASELINE)
										.addComponent(getRefersTo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(jLabel18))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(getInfoButton)
										.addComponent(getPinsButton).addComponent(clearButton))))
				.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		getContentPane().setLayout(layout);

		pack();
	}

	private void pinYCoordinateAction(java.awt.event.ActionEvent evt) {

	}

	private void connectButtonAction(java.awt.event.ActionEvent evt)
			throws NumberFormatException, UnknownHostException, IOException {
		if (socket == null) {
			String serverAddress = ipAddress.getText();
			String port = portNumber.getText();
			socket = new Socket(serverAddress, Integer.parseInt(port));
			isConnected = socket.isConnected();
			if (isConnected) {
				output.setText("You are connected to the server.");
			}
		} else {
			output.setText("You have already connected to the \nserver.");
		}
	}

	private void postYCoordinateAction(java.awt.event.ActionEvent evt) {

	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}

		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Client().setVisible(true);
			}
		});
	}

	public boolean isInteger(String num) {
		try {
			Integer.parseInt(num);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private javax.swing.JButton connectButton;
	private javax.swing.JButton pinButton;
	private javax.swing.JButton postButton;
	private javax.swing.JButton disconnectButton;
	private javax.swing.JButton unpinButton;
	private javax.swing.JButton getPinsButton;
	private javax.swing.JButton getInfoButton;
	private javax.swing.JButton clearButton;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel10;
	private javax.swing.JLabel jLabel11;
	private javax.swing.JLabel jLabel12;
	private javax.swing.JLabel jLabel13;
	private javax.swing.JLabel jLabel14;
	private javax.swing.JLabel jLabel15;
	private javax.swing.JLabel jLabel16;
	private javax.swing.JLabel jLabel17;
	private javax.swing.JLabel jLabel18;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JLabel jLabel6;
	private javax.swing.JLabel jLabel7;
	private javax.swing.JLabel jLabel8;
	private javax.swing.JLabel jLabel9;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JSeparator jSeparator1;
	private javax.swing.JTextArea output;
	private javax.swing.JTextArea contentTextArea;
	private javax.swing.JTextField pinXCoordinate;
	private javax.swing.JTextField getContains;
	private javax.swing.JTextField getRefersTo;
	private javax.swing.JTextField ipAddress;
	private javax.swing.JTextField portNumber;
	private javax.swing.JTextField pinYCoordinate;
	private javax.swing.JTextField postYCoordinate;
	private javax.swing.JTextField postXCoordinate;
	private javax.swing.JTextField height;
	private javax.swing.JTextField width;
	private javax.swing.JTextField getColour;
	private final Action action = new SwingAction();
	private JTextField textField;

	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "SwingAction");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
		}
	}
}