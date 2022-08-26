package src;
public class main {

    public static final String host = "tcp://localhost:61616";
    public static final String topic = "test";

    public static void main(String[] args){

        /*Hashtable env = new Hashtable(11);
        env.put(Context.INITIAL_CONTEXT_FACTORY,"org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        env.put(Context.PROVIDER_URL, host);
        env.put("queue.myQueue", "myQueue");

        Context ctx = new InitialContext(env);*/

        
        gui g = gui.getInstance();
    }
}

