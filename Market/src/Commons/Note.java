package Commons;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Note {

	private int id;
	private String head;
	private String content;
	private int operatorid;
	private String pop;

	private SimpleIntegerProperty SPid;
	private SimpleStringProperty SPhead;
	private SimpleStringProperty SPcontent;
	private SimpleIntegerProperty SPoperatorid;
	private SimpleStringProperty SPpop;
	private SimpleIntegerProperty SPrownumber;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
		this.SPid = new SimpleIntegerProperty(id);
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
		this.SPhead = new SimpleStringProperty(head);
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
		this.SPcontent = new SimpleStringProperty(content);
	}

	public int getOperatorid() {
		return operatorid;
	}

	public void setOperatorid(int operatorid) {
		this.operatorid = operatorid;
		this.SPoperatorid = new SimpleIntegerProperty(operatorid);
	}

	public String getPop() {
		return pop;
	}

	public void setPop(String pop) {
		this.pop = pop;
		this.SPpop = new SimpleStringProperty(pop);
	}

	public int getSPid() {
		return SPid.get();
	}

	public String getSPhead() {
		return SPhead.get();
	}

	public String getSPcontent() {
		return SPcontent.get();
	}

	public String getSPpop() {
		return SPpop.get();
	}

	public int getSPoperatorid() {
		return SPoperatorid.get();
	}

	public int getSPrownumber() {
		return SPrownumber.get();
	}

	public void setSPrownumber(int sPrownumber) {
		this.SPrownumber = new SimpleIntegerProperty(sPrownumber);
	}

}
