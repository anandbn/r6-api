package radian6;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class UserDetails {
	@XmlElement(name="user")
	private User user;
	private String avatar;

	private List<String> packages;


	public UserDetails(){
		super();
	}
	@Override
	public String toString() {
		return "UserDetails [user=" + user + ", avatar=" + avatar
				+ ", packages=" + packages + "]";
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public List<String> getPackages() {
		return packages;
	}
	public void setPackages(List<String> packages) {
		this.packages = packages;
	}
}
