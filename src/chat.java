import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;


public class chat implements javax.jms.MessageListener, ActionListener, WindowListener {
    private javax.jms.Connection con;
    private javax.jms.Session ses;
    private javax.jms.Session sesPub;
    private javax.jms.MessageProducer prod;

    private String name;
    private String password;


    //Login frame fields
    JFrame loginFrame;
    JButton login;
    JTextField userField;
    JTextField passwordField;
    //Chat frame fields
    JFrame chatFrame;
    JTextArea chatArea;
    JTextField chatBox;
    JButton chatButton;

    public SimpleChat()
    {
        loginFrame = new JFrame();

        JPanel user = new JPanel();
        JLabel userLabel = new JLabel("Nazwa: ");
        userField = new JTextField();
        userField.setPreferredSize(new Dimension(100,25));
        user.add(userLabel);
        user.add(userField);

        JPanel password = new JPanel();
        JLabel passwordlabel = new JLabel("Hasło: ");
        passwordField = new JTextField();
        passwordField.setPreferredSize(new Dimension(100,25));
        password.add(passwordlabel);
        password.add(passwordField);

        login = new JButton("Zaloguj Się");
        login.addActionListener(this);

        loginFrame.add(user, BorderLayout.NORTH);
        loginFrame.add(password,BorderLayout.CENTER);
        loginFrame.add(login,BorderLayout.SOUTH);
        loginFrame.setSize(200,150);
        loginFrame.setResizable(false);
        loginFrame.setVisible(true);
        loginFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void setupChatFrame()
    {
        chatFrame = new JFrame();

        JPanel seePanel = new JPanel();
        chatArea = new JTextArea();
        seePanel.setPreferredSize(new Dimension(new Dimension(500,300)));
        chatArea.setEditable(false);
        JScrollPane textScroll = new JScrollPane(chatArea);
        textScroll.setPreferredSize(new Dimension(500,300));
        textScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        seePanel.add(textScroll);

        JPanel sendPanel = new JPanel();
        sendPanel.setLayout(new FlowLayout());
        chatBox = new JTextField();
        chatBox.setPreferredSize(new Dimension(400,25));
        chatBox.addActionListener(this);
        chatButton = new JButton("Wyślij");
        chatButton.setPreferredSize(new Dimension(70,25));
        chatButton.addActionListener(this);
        sendPanel.add(chatBox);
        sendPanel.add(chatButton);

        chatFrame.add(seePanel,BorderLayout.CENTER);
        chatFrame.add(sendPanel,BorderLayout.SOUTH);
        chatFrame.setSize(520,370);
        chatFrame.setVisible(true);
        chatFrame.addWindowListener(this);

        sendMessage(this.name + " właśnie dołączył/a!");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource()==login)
        {
            this.name = userField.getText();
            this.password = passwordField.getText();
            connect();
            loginFrame.dispose();
            setupChatFrame();
        }

        if(e.getSource()== chatButton || e.getSource()== chatBox)
        {
            String text = chatBox.getText();
            if(text == "/exit")
            {
                chatFrame.dispose();
                exit();
            }
            else if (text.length() > 0) {
                sendMessage(this.name + ": " + text);
            }

        }

    }

    @Override
    public void onMessage(Message message) {
        try {
            javax.jms.TextMessage textMessage = (javax.jms.TextMessage) message;
            try {
                String string = textMessage.getText();
                chatArea.append(string+"\n");
            } catch (javax.jms.JMSException jmse) {
                jmse.printStackTrace();
            }
        } catch (java.lang.RuntimeException rte) {
            rte.printStackTrace();
        }
    }

    private void connect()
    {
        javax.jms.ConnectionFactory jmsCF = new ActiveMQConnectionFactory(name, password, Main.host);
        this.name = name;
        try {
            con = jmsCF.createConnection(name, password);
            ses = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
            sesPub = con.createSession(false,Session.AUTO_ACKNOWLEDGE);
            javax.jms.Topic top = sesPub.createTopic(Main.topic);
            javax.jms.MessageConsumer mCons = ses.createConsumer(top);
            mCons.setMessageListener(this);
            prod = sesPub.createProducer(top);
            con.start();
        } catch (javax.jms.JMSException e)
        {
            e.printStackTrace();
            System.err.println("Nie można się połączyć.");
        }
    }

    private void sendMessage(String text)
    {
        try{
            javax.jms.TextMessage msg = sesPub.createTextMessage();
            msg.setText(text);
            prod.send(msg);
            chatBox.setText("");
        } catch (JMSException jmse)
        {
            jmse.printStackTrace();
        }
    }

    private void exit()
    {
        try {
            con.close();
        } catch (javax.jms.JMSException jmse) {
            jmse.printStackTrace();
        }

        System.exit(0);
    }


    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        exit();
        chatFrame.dispose();
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
