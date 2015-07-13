package stateless.session.bean;

import javax.ejb.Remote;

@Remote
public interface AccountRemote {
    public void deposit(double amount);
    public void withdraw(double amount);    
    public String result(String msg);
}

