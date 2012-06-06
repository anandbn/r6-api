package radian6;

public class AIHUser {
	private String userKey;
	private String registerDate;
	private String type;

	public AIHUser(){
		super();
	}
	@Override
	public String toString() {
		return "AIHUser [userKey=" + userKey + ", registerDate=" + registerDate
				+ ", type=" + type + "]";
	}
	public String getUserKey() {
		return userKey;
	}
	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}
	public String getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(String registerDate) {
		this.registerDate = registerDate;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
