package radian6.tagcloud;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;


@XmlRootElement(name="widgetOutput")
@XmlAccessorType(XmlAccessType.FIELD)
public class WidgetOutput {
	@XmlElementWrapper(name="dataitems")
	@XmlElement(name="dataitem")
	@JsonProperty(value="words")
	private List<DataItem> dataitems;
	
	@JsonIgnore
	@XmlElement(name="status")
	private String status;
	
	public WidgetOutput(){
		super();
	}
	public boolean add(DataItem arg0) {
		if(this.dataitems==null){
			dataitems = new ArrayList<DataItem>();
		}
		return dataitems.add(arg0);
	}
	@Override
	public String toString() {
		return "WidgetOutput [dataItems=" + dataitems + ", status=" + status + "]";
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<DataItem> getDataitems() {
		//NEED A MORE EFFICIENT WAY TO REMOVE "null" keys
		List<DataItem> newItems = new ArrayList<DataItem>();
		for(DataItem item:this.dataitems){
			if(item.getKey()!=null){
				newItems.add(item);
			}
		}
		return newItems;
	}
	public void setDataitems(List<DataItem> dataitems) {
		this.dataitems = dataitems;
	}
	
}
