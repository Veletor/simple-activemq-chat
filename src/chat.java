package src;

import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;



public class chat implements javax.jms.MessageListener{
    private javax.jms.Connection con;
    private javax.jms.Session ses;
    private javax.jms.Session sesPub;
    private javax.jms.MessageProducer prod;

    private String name;
    private String password;



    @Override
    public void onMessage(Message message) {
        try {
            javax.jms.TextMessage textMessage = (javax.jms.TextMessage) message;
            try {
                String string = textMessage.getText();
                gui.getInstance().chatArea.append(string+"\n");
            } catch (javax.jms.JMSException jmse) {
                jmse.printStackTrace();
            }
        } catch (java.lang.RuntimeException rte) {
            rte.printStackTrace();
        }
    }

    void connect() throws JMSException
    {
        javax.jms.ConnectionFactory jmsCF = new ActiveMQConnectionFactory(name, password, main.host);
            con = jmsCF.createConnection(name, password);
            ses = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
            sesPub = con.createSession(false,Session.AUTO_ACKNOWLEDGE);
            javax.jms.Topic top = sesPub.createTopic(main.topic);
            javax.jms.MessageConsumer mCons = ses.createConsumer(top);
            mCons.setMessageListener(this);
            prod = sesPub.createProducer(top);
            con.start();
    }

    void sendMessage(String text)
    {
        try{
            javax.jms.TextMessage msg = sesPub.createTextMessage();
            msg.setText(text);
            prod.send(msg);
        } catch (JMSException jmse)
        {
            jmse.printStackTrace();
        }
    }

    void exit()
    {
        try {
            con.close();
        } catch (javax.jms.JMSException jmse) {
            jmse.printStackTrace();
        }

        System.exit(0);
    }

    public void setName(String s)
    {
        this.name = s;
    }

    public String getName()
    {
        return this.name;
    }
}
