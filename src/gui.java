package src;

import javax.swing.*;
import javax.jms.JMSException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;



public class gui implements ActionListener, WindowListener {

        //Login frame fields
        JFrame loginFrame;
        JButton login;
        JTextField userField;
        //JTextField passwordField;

        //Chat frame fields
        JFrame chatFrame;
        JTextArea chatArea;
        JTextField chatBox;
        JButton chatButton;

        private static chat ch;
        private static gui instance;
    
        private gui()
        {
            ch = new chat();

            loginFrame = new JFrame();
    
            JPanel user = new JPanel();
            JLabel userLabel = new JLabel("Nazwa: ");
            userField = new JTextField();
            userField.setPreferredSize(new Dimension(100,25));
            user.add(userLabel);
            user.add(userField);
    
            //JPanel password = new JPanel();
            //JLabel passwordlabel = new JLabel("Hasło: ");
            //passwordField = new JTextField();
            //passwordField.setPreferredSize(new Dimension(100,25));
            //password.add(passwordlabel);
            //password.add(passwordField);
    
            login = new JButton("Zaloguj Się");
            login.addActionListener(this);
    
            loginFrame.add(user, BorderLayout.NORTH);
            //loginFrame.add(password,BorderLayout.CENTER);
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
    
            ch.sendMessage(ch.getName() + " właśnie dołączył/a!");
        }
    
        @Override
        public void actionPerformed(ActionEvent e) {
    
            if(e.getSource()==login)
            {
                try
                {
                    ch.setName(userField.getText());
                    //ch.password = passwordField.getText();
                    ch.connect(); 
                } catch (JMSException ex)
                {
                    errorWindow("Nie udało się połączyć.");
                    ex.printStackTrace();
                    return;
                }
                loginFrame.dispose();
                setupChatFrame();
            }
    
            if(e.getSource()== chatButton || e.getSource()== chatBox)
            {
                String text = chatBox.getText();
                if(text == "/exit")
                {
                    chatFrame.dispose();
                    ch.exit();
                }
                else if (text.length() > 0) {
                    ch.sendMessage(ch.getName() + ": " + text);
                    chatBox.setText("");
                }
    
            }
    
        }

        public static gui getInstance()
        {
            if(instance == null)
            {
                instance = new gui();
            }
            return instance;
        }

        private void errorWindow(String s)
        {
            JFrame frame = new JFrame();
            JLabel err = new JLabel(s);
            JButton ok = new JButton("Ok");
            ok.addActionListener(e -> frame.dispose());
            frame.add(err, BorderLayout.CENTER);
            frame.add(ok,BorderLayout.SOUTH);
            frame.pack();
            frame.setResizable(false);
            frame.setVisible(true);
        }
        

        @Override
        public void windowOpened(WindowEvent e) {

        }

        @Override
        public void windowClosing(WindowEvent e) {
            ch.exit();
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
