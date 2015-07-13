package stateless.session.bean;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.dgc.VMID;

import javax.ejb.Stateless;

@Stateless(name = "stateless.session.bean.Account")
public class Account implements AccountLocal, AccountRemote {
    private double balance = 0;
    
    public void deposit(double amount) {
        balance += amount;
    }
    
    public void withdraw(double amount) {
        balance += amount;
    }
    
    public String result(String message) {
        String hostname = null;
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        VMID vmid = new VMID();       
        return message 
        		+ System.getProperty("line.separator") + "received via stateless session bean result() Impl" 
        		+ System.getProperty("line.separator") + "at hostname=" + hostname         		
        		+ System.getProperty("line.separator") + "at PID=" + getPID()
        		+ System.getProperty("line.separator") + "at VMID=" + vmid.toString()
        		+ System.getProperty("line.separator") + "the balance is " + balance;
    }
    
    public long getPID() {
		String processName = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
		return Long.parseLong(processName.split("@")[0]);
	}
}
