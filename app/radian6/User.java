package radian6;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

@XmlAccessorType(XmlAccessType.FIELD)
public class User {
	private String userId;
	private String clientId;
	private String displayName;
	private String emailAddress;
	private String timezone;
	private List<String> packages;
	private String userRoleId;
	private String createdDate;
	private String enabled;
	@XmlElementWrapper(name="aihUsers")
	@XmlElement(name="aihUser")
	private List<AIHUser> aihUsers;

	public User(){
		super();
	}
	@Override
	public String toString() {
		return "User [userId=" + userId + ", clientId=" + clientId
				+ ", displayName=" + displayName + ", emailAddress="
				+ emailAddress + ", timezone=" + timezone + ", packages="
				+ packages + ", userRoleId=" + userRoleId + ", createdDate="
				+ createdDate + ", enabled=" + enabled + ", aihUsers="
				+ aihUsers + "]";
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getTimezone() {
		return timezone;
	}
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
	public List<String> getPackages() {
		return packages;
	}
	public void setPackages(List<String> packages) {
		this.packages = packages;
	}
	public String getUserRoleId() {
		return userRoleId;
	}
	public void setUserRoleId(String userRoleId) {
		this.userRoleId = userRoleId;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getEnabled() {
		return enabled;
	}
	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}
	public List<AIHUser> getAihUsers() {
		return aihUsers;
	}
	public void setAihUsers(List<AIHUser> aihUsers) {
		this.aihUsers = aihUsers;
	}
	
}
