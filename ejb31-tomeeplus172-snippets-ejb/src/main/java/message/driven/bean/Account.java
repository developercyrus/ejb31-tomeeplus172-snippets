package message.driven.bean;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.dgc.VMID;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJBException;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.ejb.Stateful;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

@Stateful(name = "message.driven.bean.Account")
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
    @ActivationConfigProperty(propertyName = "destination", propertyValue = "consumingQueue")
})
public class Account implements MessageListener {
	
	private static final long serialVersionUID = 9170778657371910293L;
	private MessageDrivenContext context = null;
	
	public void ejbRemove() throws EJBException {
		this.context = null;		
	}

	public void setMessageDrivenContext(MessageDrivenContext ctx) throws EJBException {
		System.out.println("MessageDrivenContext is set");	
		this.context = ctx;
	}

	public void onMessage(Message message) {
		try {
			/*
	        byte[] by = ((TextMessage) message).getText().getBytes("UTF-8");
	        String text = new String(by,"UTF-8");
	       	*/
			
			/*
			String text = ((TextMessage) message).getText();
			String hostname = InetAddress.getLocalHost().getHostName();
		    VMID vmid = new VMID();   
		    
		    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("c:/tmp/myfile.txt", true)));

			System.out.println(text
					+ System.getProperty("line.separator") + "received via message driven bean onMessage() Impl" 
					+ System.getProperty("line.separator") + "at hostname=" + hostname         		
					+ System.getProperty("line.separator") + "at PID=" + getPID()
					+ System.getProperty("line.separator") + "at VMID=" + vmid.toString());		

			out.println(text
					+ System.getProperty("line.separator") + "received via message driven bean onMessage() Impl" 
					+ System.getProperty("line.separator") + "at hostname=" + hostname         		
					+ System.getProperty("line.separator") + "at PID=" + getPID()
					+ System.getProperty("line.separator") + "at VMID=" + vmid.toString());
		    out.flush();
			*/
			
			ObjectMessage om = (ObjectMessage) message;
			if(om.getObject() instanceof StringMessage) {
				StringMessage s = (StringMessage) om.getObject();
				String text = s.getText();

				String hostname = InetAddress.getLocalHost().getHostName();
			    VMID vmid = new VMID();   
			    
			    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("./mdb.output", true)));				
				out.println(text
						+ System.getProperty("line.separator") + "received via message driven bean onMessage() Impl" 
						+ System.getProperty("line.separator") + "at hostname=" + hostname         		
						+ System.getProperty("line.separator") + "at PID=" + getPID()
						+ System.getProperty("line.separator") + "at VMID=" + vmid.toString());
			    out.flush();			    
			    out.close();
			}
			

		} catch (UnknownHostException e) {
		    e.printStackTrace();
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {			
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
 	public long getPID() {
		String processName = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
		return Long.parseLong(processName.split("@")[0]);
	}
}
