package stateful.session.bean.local.client.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.rmi.dgc.VMID;

import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import stateful.session.bean.AccountLocal;

@WebServlet("/AccountStatefulLocalCallServlet")
public class AccountStatefulLocalCallServlet extends HttpServlet  {
	/*	 
	 	@EJB annotation can only be used if the applications are deployed in the same sever instance.
	 	http://stackoverflow.com/a/16518704
	  
		@EJB 
		private AccountLocal h;
	*/
	private static final long serialVersionUID = -828456384345678762L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }   
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();

        try {
            Context ctx = new InitialContext();            
            /*
	         	EJB 3 remote invocation
				ejb:<app-name>/<module-name>/<distinct-name>/<bean-name>!<fully-qualified-classname-of-the-remote-interface>
				
				<bean-name> can be set by annotation, or by default class name
				
	         	reference: https://docs.jboss.org/author/display/AS71/EJB+invocations+from+a+remote+client+using+JNDI?_sscc=t
             */
            
            Object ref = ctx.lookup("java:global/ejb31-tomeeplus172-snippets-ear-0.0.1-SNAPSHOT/ejb31-tomeeplus172-snippets-ejb-0.0.1-SNAPSHOT/stateful.session.bean.Account!stateful.session.bean.AccountLocal");
            AccountLocal h = (AccountLocal)PortableRemoteObject.narrow(ref, AccountLocal.class);

            VMID vmid = new VMID();
            String hostname = InetAddress.getLocalHost().getHostName();   
            String result = h.result("get Hello World by local client servlet"
            		+ System.getProperty("line.separator") + "at hostname=" + hostname 
					+ System.getProperty("line.separator") + "at PID=" + getPID()
					+ System.getProperty("line.separator") + "at VMID=" + vmid.toString());
            
            writer.println("<div>");
            writer.println(result.replaceAll(System.getProperty("line.separator"), "<br />"));
            writer.println("</div>");
            writer.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }    
    
	public long getPID() {
		String processName = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
		return Long.parseLong(processName.split("@")[0]);
	}
}