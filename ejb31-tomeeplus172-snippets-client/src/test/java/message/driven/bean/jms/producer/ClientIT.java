package message.driven.bean.jms.producer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ClientIT {	
    @Test
    public void test1() throws Exception {
    	Client.main(null);     
    	Thread.sleep(2000);

    	String filePathString = new File(".").getAbsolutePath();
    	filePathString = filePathString.substring(0, filePathString.length()-2)+ "/target/apache-tomee/mdb.output";
    	System.out.println(filePathString);    	
    	File f = new File(filePathString);    	
    	boolean actual = (f.exists() && !f.isDirectory());     
    	BufferedReader br = new BufferedReader(new FileReader(f));
	    String line;
	    while ((line = br.readLine()) != null) {
	       System.out.println(line);
	    }
	    br.close();
	    
    	assertTrue(actual);
	}

	
}