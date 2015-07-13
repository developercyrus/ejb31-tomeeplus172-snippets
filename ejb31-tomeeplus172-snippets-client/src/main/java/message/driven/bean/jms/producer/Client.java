package message.driven.bean.jms.producer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.dgc.VMID;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;

import message.driven.bean.StringMessage;

public class Client {
	public static void main(String args[]) throws Exception {		
		Properties props = new Properties();
        /*
         	tomee currently (already said in year 2012) DOES NOT support remote access JNDI to JMS resources. a workaround is to add a system properties in the client 
            by calling
                "-DResource/javax.jms.ConnectionFactory=connectionfactory:org.apache.activemq.ActiveMQConnectionFactory:tcp://localhost:61616"
            or programmatically 
                "System.setProperty("Resource/javax.jms.ConnectionFactory", "connectionfactory:org.apache.activemq.ActiveMQConnectionFactory:tcp://localhost:61616");"
            reference:
                http://tomee-openejb.979440.n4.nabble.com/Remote-JNDI-access-to-JMS-resources-td4656767.html
         */		
		System.setProperty("Resource/javax.jms.ConnectionFactory", "connectionfactory:org.apache.activemq.ActiveMQConnectionFactory:tcp://localhost:61616");
        
		props.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.openejb.client.RemoteInitialContextFactory");
		props.put(Context.PROVIDER_URL, "http://localhost:8080/tomee/ejb");
		
        Context ctx = new InitialContext(props);
        ConnectionFactory qcf = (ConnectionFactory) ctx.lookup("MyJmsConnectionFactory");
        Connection conn = qcf.createConnection();
        Queue queue = (Queue) ctx.lookup("consumingQueue");
        Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageProducer producer = session.createProducer(queue); 
        
        String hostname = InetAddress.getLocalHost().getHostName();
        VMID vmid = new VMID();           
        
        String text = "sent by MDB client console"
					+ System.getProperty("line.separator") + "at hostname=" + hostname 
					+ System.getProperty("line.separator") + "at PID=" + getPID()
					+ System.getProperty("line.separator") + "at VMID=" + vmid.toString();
        String msg = new String(text.getBytes(), "UTF-8");
                
        StringMessage sm = new StringMessage();
        sm.setText(msg);
        ObjectMessage om = session.createObjectMessage();
        om.setObject(sm);
        producer.send(om);
        
        /*
        TextMessage tm = session.createTextMessage();
        tm.setText(text);
        producer.send(tm);
		*/

        producer.close();
        session.close();
        conn.close();
	}
	
    public static long getPID() {
		String processName = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
		return Long.parseLong(processName.split("@")[0]);
	}
}
