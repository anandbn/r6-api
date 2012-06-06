package radian6.tagcloud;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

@XmlAccessorType(XmlAccessType.PROPERTY)
public class DataItem {
	@JsonProperty(value="text")
	private String key;
	@JsonIgnore
	private int cloudSize;
	@JsonProperty(value="size")
	private int value;
	@JsonIgnore
	private Boolean isSphinxLimit;
	
	public DataItem(){
		super();
	}
	@Override
	public String toString() {
		return "DataItem [key=" + key + ", cloudSize=" + cloudSize + ", value="
				+ value + ", isSphinxLimit=" + isSphinxLimit + "]";
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key.replace("\"", "");
	}
	public int getCloudSize() {
		return cloudSize;
	}
	public void setCloudSize(int cloudSize) {
		this.cloudSize = cloudSize;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public Boolean getIsSphinxLimit() {
		return isSphinxLimit;
	}
	public void setIsSphinxLimit(Boolean isSphinxLimit) {
		this.isSphinxLimit = isSphinxLimit;
	}
	
	
}
