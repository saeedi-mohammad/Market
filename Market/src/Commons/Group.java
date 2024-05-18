package Commons;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Group {

	private int id;
	private String name;

	private SimpleIntegerProperty SPid;
	private SimpleStringProperty SPname;
	private SimpleIntegerProperty SPrownumber;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
		this.SPid = new SimpleIntegerProperty(id);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		this.SPname = new SimpleStringProperty(name);
	}

	public int getSPid() {
		return SPid.get();
	}

	public String getSPname() {
		return SPname.get();
	}

	public int getSPrownumber() {
		return SPrownumber.get();
	}

	public void setSPrownumber(int sPrownumber) {
		this.SPrownumber = new SimpleIntegerProperty(sPrownumber);
	}

}
