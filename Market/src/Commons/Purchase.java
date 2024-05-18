package Commons;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class Purchase {

	private int purchaseidid;
	private int itemid;
	private String itemname;
	private long itemserial;
	private int invoiseid;
	private double value;
	private long cost;
	private String details;

	private SimpleIntegerProperty SPpurchaseid;
	private SimpleIntegerProperty SPitemid;
	private SimpleStringProperty SPitemname;
	private SimpleIntegerProperty SPinvoiseid;
	private SimpleDoubleProperty SPvalue;
	private SimpleLongProperty SPcost;
	private SimpleLongProperty SPitemserial;
	private SimpleStringProperty SPdetails;
	private SimpleIntegerProperty SPrownumber;

	public int getItemid() {
		return itemid;
	}

	public void setItemid(int itemid) {
		this.itemid = itemid;
		this.SPitemid = new SimpleIntegerProperty(itemid);
	}

	public String getItemname() {
		return itemname;
	}

	public void setItemname(String itemname) {
		this.itemname = itemname;
		this.SPitemname = new SimpleStringProperty(itemname);
	}

	public long getItemserial() {
		return itemserial;
	}

	public void setItemserial(long itemserial) {
		this.itemserial = itemserial;
		this.SPitemserial = new SimpleLongProperty(itemserial);
	}

	public int getPurchasseid() {
		return purchaseidid;
	}

	public void setPurchaseid(int purchaseidid) {
		this.purchaseidid = purchaseidid;
		this.SPpurchaseid = new SimpleIntegerProperty(purchaseidid);
	}

	public int getInvoiseid() {
		return invoiseid;
	}

	public void setInvoiseid(int invoiseid) {
		this.invoiseid = invoiseid;
		this.SPinvoiseid = new SimpleIntegerProperty(invoiseid);
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
		this.SPvalue = new SimpleDoubleProperty(value);
	}

	public long getCost() {
		return cost;
	}

	public void setCost(long cost) {
		this.cost = cost;
		this.SPcost = new SimpleLongProperty(cost);
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
		this.SPdetails = new SimpleStringProperty(details);
	}

	public int getSPitemid() {
		return SPitemid.get();
	}

	public String getSPitemname() {
		return SPitemname.get();
	}

	public int getSPpurchaseid() {
		return SPpurchaseid.get();
	}

	public int getSPinvoiseid() {
		return SPinvoiseid.get();
	}

	public String getSPdetails() {
		return SPdetails.get();
	}

	public Long getSPcost() {
		return SPcost.get();
	}

	public Long getSPitemserial() {
		return SPitemserial.get();
	}

	public Double getSPvalue() {
		return SPvalue.get();
	}

	public int getSPrownumber() {
		return SPrownumber.get();
	}

	public void setSPrownumber(int sPrownumber) {
		this.SPrownumber = new SimpleIntegerProperty(sPrownumber);
	}

}
