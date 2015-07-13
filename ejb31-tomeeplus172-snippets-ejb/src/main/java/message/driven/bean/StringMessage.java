package message.driven.bean;

import java.io.Serializable;

public class StringMessage implements Serializable {
	private static final long serialVersionUID = 4816136249902615441L;
	
	private String Text;

	public String getText() {
		return Text;
	}

	public void setText(String text) {
		Text = text;
	}

}
