package Commons;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class Item {

	private int id;
	private String name;
	private long serial;
	private double weight;
	private int number;
	private long PurchasePrice;
	private long RetailPrice;
	private long WholesalePrice;
	private int groupid;
	private String groupname;

	private SimpleIntegerProperty SPid;
	private SimpleIntegerProperty SPgroupid;
	private SimpleIntegerProperty SPnumber;
	private SimpleStringProperty SPname;
	private SimpleStringProperty SPgroupname;
	private SimpleLongProperty SPPurchasePrice;
	private SimpleLongProperty SPRetailPrice;
	private SimpleLongProperty SPWholesalePrice;
	private SimpleLongProperty SPserial;
	private SimpleDoubleProperty SPweight;
	private SimpleIntegerProperty SPrownumber;

	public Item(Item item) {

		setId(item.getId());
		setName(item.getName());
		setSerial(item.getSerial());
		setWeight(item.getWeight());
		setNumber(item.getNumber());
		setPurchasePrice(item.getPurchasePrice());
		setRetailPrice(item.getRetailPrice());
		setWholesalePrice(item.getWholesalePrice());
		setGroupid(item.getGroupid());
		setGroupname(item.getGroupname());
		setSPrownumber(item.getSPrownumber());
	}

	public Item() {

	}

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

	public long getSerial() {
		return serial;
	}

	public void setSerial(long serial) {
		this.serial = serial;
		this.SPserial = new SimpleLongProperty(serial);
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
		this.SPweight = new SimpleDoubleProperty(weight);
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
		this.SPnumber = new SimpleIntegerProperty(number);
	}

	public long getPurchasePrice() {
		return PurchasePrice;
	}

	public void setPurchasePrice(long purchasePrice) {
		PurchasePrice = purchasePrice;
		this.SPPurchasePrice = new SimpleLongProperty(purchasePrice);
	}

	public long getRetailPrice() {
		return RetailPrice;
	}

	public void setRetailPrice(long retailPrice) {
		RetailPrice = retailPrice;
		this.SPRetailPrice = new SimpleLongProperty(retailPrice);
	}

	public long getWholesalePrice() {
		return WholesalePrice;
	}

	public void setWholesalePrice(long wholesalePrice) {
		WholesalePrice = wholesalePrice;
		this.SPWholesalePrice = new SimpleLongProperty(wholesalePrice);
	}

	public int getGroupid() {
		return groupid;
	}

	public void setGroupid(int groupid) {
		this.groupid = groupid;
		this.SPgroupid = new SimpleIntegerProperty(groupid);
	}

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
		this.SPgroupname = new SimpleStringProperty(groupname);
	}

	public int getSPid() {
		return SPid.get();
	}

	public int getSPgroupid() {
		return SPgroupid.get();
	}

	public int getSPnumber() {
		return SPnumber.get();
	}

	public String getSPname() {
		return SPname.get();
	}

	public String getSPgroupname() {
		return SPgroupname.get();
	}

	public Long getSPPurchasePrice() {
		return SPPurchasePrice.get();
	}

	public Long getSPRetailPrice() {
		return SPRetailPrice.get();
	}

	public Long getSPWholesalePrice() {
		return SPWholesalePrice.get();
	}

	public Long getSPserial() {
		return SPserial.get();
	}

	public Double getSPweight() {
		return SPweight.get();
	}

	public int getSPrownumber() {
		return SPrownumber.get();
	}

	public void setSPrownumber(int sPrownumber) {
		this.SPrownumber = new SimpleIntegerProperty(sPrownumber);
	}

	public Double getSPinventory() {
		if (this.number < 0)

			return getSPweight();

		else
			return (double) getSPnumber();

	}

}
