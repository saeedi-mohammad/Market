package Commons;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class Sale {

	private int saleid;
	private int itemid;
	private int invoiseid;
	private String itemname;
	private long itemserial;
	private double itemvalue;
	private long itemprice;
	private long salecost;
	private int itemgroupid;
	private String itemgroupname;
	private String retailORwholesale;

	private SimpleIntegerProperty SPsaleid;
	private SimpleIntegerProperty SPitemid;
	private SimpleIntegerProperty SPinvoiseid;
	private SimpleIntegerProperty SPitemgroupid;
	private SimpleStringProperty SPitemname;
	private SimpleStringProperty SPitemgroupname;
	private SimpleDoubleProperty SPitemvalue;
	private SimpleLongProperty SPitemprice;
	private SimpleLongProperty SPsalecost;
	private SimpleLongProperty SPitemserial;
	private SimpleStringProperty SPretailORwholesale;
	private SimpleIntegerProperty SPrownumber;

	public int getItemid() {
		return itemid;
	}

	public void setItemid(int itemid) {
		this.itemid = itemid;
		this.SPitemid = new SimpleIntegerProperty(itemid);
	}

	public int getSaleid() {
		return saleid;
	}

	public void setSaleid(int saleid) {
		this.saleid = saleid;
		this.SPsaleid = new SimpleIntegerProperty(saleid);
	}

	public int getInvoiseid() {
		return invoiseid;
	}

	public void setInvoiseid(int invoiseid) {
		this.invoiseid = invoiseid;
		this.SPinvoiseid = new SimpleIntegerProperty(invoiseid);
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

	public double getItemvalue() {
		return itemvalue;
	}

	public void setItemvalue(double itemvalue) {
		this.itemvalue = itemvalue;
		this.SPitemvalue = new SimpleDoubleProperty(itemvalue);
	}

	public long getItemprice() {
		return itemprice;
	}

	public void setItemprice(long itemprice) {
		this.itemprice = itemprice;
		this.SPitemprice = new SimpleLongProperty(itemprice);
	}

	public long getSalecost() {
		return salecost;
	}

	public void setSalecost(long salecost) {
		this.salecost = salecost;
		this.SPsalecost = new SimpleLongProperty(salecost);
	}

	public int getItemgroupid() {
		return itemgroupid;
	}

	public void setItemgroupid(int itemgroupid) {
		this.itemgroupid = itemgroupid;
		this.SPitemgroupid = new SimpleIntegerProperty(itemgroupid);
	}

	public String getItemgroupname() {
		return itemgroupname;
	}

	public void setItemgroupname(String itemgroupname) {
		this.itemgroupname = itemgroupname;
		this.SPitemgroupname = new SimpleStringProperty(itemgroupname);
	}

	public String getRetailORwholesale() {
		return retailORwholesale;
	}

	public void setRetailORwholesale(String retailORwholesale) {
		this.retailORwholesale = retailORwholesale;
		this.SPretailORwholesale = new SimpleStringProperty(retailORwholesale);
	}

	public int getSPitemid() {
		return SPitemid.get();
	}

	public int getSPsaleid() {
		return SPsaleid.get();
	}

	public int getSPinvoiseid() {
		return SPinvoiseid.get();
	}

	public int getSPitemgroupid() {
		return SPitemgroupid.get();
	}

	public String getSPretailORwholesale() {
		return SPretailORwholesale.get();
	}

	public String getSPitemname() {
		return SPitemname.get();
	}

	public String getSPgroupname() {
		return SPitemgroupname.get();
	}

	public Long getSPitemprice() {
		return SPitemprice.get();
	}

	public Long getSPsalecost() {
		return SPsalecost.get();
	}

	public Long getSPitemserial() {
		return SPitemserial.get();
	}

	public Double getSPitemvalue() {
		return SPitemvalue.get();
	}

	public int getSPrownumber() {
		return SPrownumber.get();
	}

	public void setSPrownumber(int sPrownumber) {
		this.SPrownumber = new SimpleIntegerProperty(sPrownumber);
	}

}
