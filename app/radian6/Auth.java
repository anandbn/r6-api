package radian6;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="auth")
@XmlAccessorType(XmlAccessType.FIELD)
public class Auth {
	private String token;
	
	@XmlElement(name="UserDetails")
	private UserDetails userDetails;
	public Auth(){
		super();
	}

	@Override
	public String toString() {
		return "Auth [token=" + token + ", userDetails=" + userDetails + "]";
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public UserDetails getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(UserDetails userDetails) {
		this.userDetails = userDetails;
	}

}
