package com.borisskurikhin.reg;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.swing.Action;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Main extends JFrame implements ActionListener {

	/*
	 * Computer Club Registration Program Written by Boris Skurikhin [August
	 * 2015] In Java 7 & Java's Mail API
	 */

	JButton submit;
	JTextField field = new JTextField("Enter your e-mail address!", 30);
	JLabel label = new JLabel("Register for 2015-2016 TSS Computer Club!", JTextField.CENTER);
	GridBagConstraints constraints = new GridBagConstraints();
	List<String> repeats = new ArrayList<String>();
	static String message_body = "Insert Body Here";

	public Main(String windowTitle) {
		super(windowTitle);
		setLayout(new GridBagLayout());
		setSize(500, 300);
		
		getContentPane().setBackground(new Color(44, 62, 80));
		repaint();

		submit = new JButton("Submit!");

		submit.addActionListener(this);

		constraints.gridx = 30;
		constraints.insets = new Insets(10, 0, 0, 0);

		label.setFont(new Font("Lato", Font.BOLD, 20));
		label.setForeground(new Color(236, 240, 241));

		field.setHorizontalAlignment(JTextField.CENTER);
		field.setFont(new Font("Lato", Font.PLAIN, 16));
		field.setBackground(new Color(52, 73, 94));
		field.setForeground(new Color(236, 240, 241));

		add(label, constraints);
		add(field, constraints);
		add(submit, constraints);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setAlwaysOnTop(true);
		
		setVisible(true);
		setLocationRelativeTo(null);
	}

	public static void main(String[] args) {
		new Main("Register");
	}

	public static void sendMail(String address) {
		try {
			Properties props = new Properties();
			props.put("mail.smtp.host", "smtp.gmail.com"); //using gmail ofcourse
			props.put("mail.smtp.auth", "true");
			props.put("mail.debug", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.port", "465");
			props.put("mail.smtp.socketFactory.port", "465");
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.socketFactory.fallback", "false");
			Session mailSession = Session.getInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication("your@address.com", "password");
				}
			});

			mailSession.setDebug(false);
			Message msg = new MimeMessage(mailSession);
			msg.setFrom(new InternetAddress("your@address.com"));
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(address));
			msg.setSentDate(new Date());
			msg.setSubject("Thank you for registering for Thornhil Secondary Computer Club 2015-2016!\n\n");
			msg.setText(message_body);
			Transport.send(msg);
		} catch (Exception E) {
			System.out.println("Hmm, something went wrong.");
			System.err.println(E);
		}
	}

	public void actionPerformed(ActionEvent e) {
		/*
		 * Little tricky algorithm to memorize people who registered for the site
		 * add uniuqe address to the list, and then when a keyword address is typed
		 * change the body of the message to everyone's e-mail address
		*/
		
		if (field.getText().trim().contains("@") && !repeats.contains(field.getText().trim())) {
			if(field.getText().trim().equals("your@address.com")){
				String newString = "Here's a list of everyone who registered:\n\n";
				for(String s : repeats) newString += s + "\n";
				message_body = newString;
			}
			sendMail(field.getText().trim());
			repeats.add(field.getText().trim());
			message_body = "Insert Body Here";
		}
		field.setText("Enter your e-mail address!");
	}
}
