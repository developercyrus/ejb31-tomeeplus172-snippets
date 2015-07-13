package stateful.session.bean;

import javax.ejb.Local;

@Local
public interface AccountLocal {
    public void deposit(double amount);
    public void withdraw(double amount);    
    public String result(String msg);
}

