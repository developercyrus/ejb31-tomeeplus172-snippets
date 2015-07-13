package stateful.session.bean.remote.client.console;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.dgc.VMID;
import java.util.Properties;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import stateful.session.bean.AccountRemote;

public class AccountStatefulRemoteCallClientConsole {
    public static void main(String[] args) throws NamingException, UnknownHostException, RemoteException, CreateException {
        
        Properties props = new Properties();
        props.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.openejb.client.RemoteInitialContextFactory");
        props.put(Context.PROVIDER_URL, "http://localhost:8080/tomee/ejb");
        
        Context ctx = new InitialContext(props);
        
        /*
         	EJB 3 remote invocation
			ejb:<app-name>/<module-name>/<distinct-name>/<bean-name>!<fully-qualified-classname-of-the-remote-interface>
			
			<bean-name> can be set by annotation, or by default class name
			
         	reference: https://docs.jboss.org/author/display/AS71/EJB+invocations+from+a+remote+client+using+JNDI?_sscc=t
         */
        
        Object ref = ctx.lookup("java:global/ejb31-tomeeplus172-snippets-ear-0.0.1-SNAPSHOT/ejb31-tomeeplus172-snippets-ejb-0.0.1-SNAPSHOT/stateful.session.bean.Account!stateful.session.bean.AccountRemote");
        AccountRemote h = (AccountRemote)PortableRemoteObject.narrow(ref, AccountRemote.class);

        String hostname = InetAddress.getLocalHost().getHostName();

        VMID vmid = new VMID();
        String result = h.result("get Hello World by remote client console"
        							+ System.getProperty("line.separator") + "at hostname=" + hostname 
        							+ System.getProperty("line.separator") + "at PID=" + getPID()
        							+ System.getProperty("line.separator") + "at VMID=" + vmid.toString());
        System.out.println(result); 
    }
    
	public static long getPID() {
		String processName = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
		return Long.parseLong(processName.split("@")[0]);
	}
}
