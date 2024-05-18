package Commons;

import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;
import java.util.GregorianCalendar;

import Calendar.DateConverter;
import Calendar.JalaliCalendar;
import Calendar.JalaliDate;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class Invoise {

	private JalaliCalendar jalaliC = new JalaliCalendar();
	private DateConverter dateconverter = new DateConverter();

	private int invoiseid;
	private Date date;
	private Time time;
	private int customerid;
	private long totalcost;
	private String SaleOrPurchase;

	private SimpleIntegerProperty SPinvoiseid;
	private SimpleIntegerProperty SPcustomerid;
	private SimpleStringProperty SPdate;
	private SimpleStringProperty SPtime;
	private SimpleLongProperty SPtotalcost;
	private SimpleStringProperty SPSaleOrPurchase;
	private SimpleIntegerProperty SPrownumber;

	public int getInvoiseid() {
		return invoiseid;
	}

	public void setInvoiseid(int invoiseid) {
		this.invoiseid = invoiseid;
		this.SPinvoiseid = new SimpleIntegerProperty(invoiseid);
	}

	public int getCustomerid() {
		return customerid;
	}

	public void setCustomerid(int customerid) {
		this.customerid = customerid;
		this.SPcustomerid = new SimpleIntegerProperty(customerid);
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		if (date != null) {
			this.date = date;
			this.SPdate = new SimpleStringProperty(date.toString());
		} else {
			this.date = null;
			this.SPdate = new SimpleStringProperty(null);
		}
	}

	public Time getTime() {
		return this.time;
	}

	public void setTime(Time time) {
		if (time != null) {
			this.time = time;
			this.SPtime = new SimpleStringProperty(time.toString());
		} else {
			this.time = null;
			this.SPtime = new SimpleStringProperty(null);
		}
	}

	public long getTotalcost() {
		return totalcost;
	}

	public void setTotalcost(long totalcost) {
		this.totalcost = totalcost;
		this.SPtotalcost = new SimpleLongProperty(totalcost);
	}

	public String getSaleOrPurchase() {
		return SaleOrPurchase;
	}

	public void setSaleOrPurchase(String SaleOrPurchase) {
		this.SaleOrPurchase = SaleOrPurchase;
		this.SPSaleOrPurchase = new SimpleStringProperty(SaleOrPurchase);
	}

	public int getSPinvoiseid() {
		return SPinvoiseid.get();
	}

	public int getSPcustomerid() {
		return SPcustomerid.get();
	}

	public Date getSPdate() {
		if (SPdate.get() == null)
			return null;
		Date temp = Date.valueOf(SPdate.get());
		return temp;
	}

	public Time getSPtime() {
		if (SPtime.get() == null)
			return null;
		Time temp = Time.valueOf(SPtime.get());
		return temp;
	}

	public Long getSPtotalcost() {
		return SPtotalcost.get();
	}

	public String getSPSaleOrPurchase() {
		return SPSaleOrPurchase.get();
	}

	public int getSPrownumber() {
		return SPrownumber.get();
	}

	public void setSPrownumber(int sPrownumber) {
		this.SPrownumber = new SimpleIntegerProperty(sPrownumber);
	}

	public String getSPJalaliDateString() {
		if (SPdate.get() == null)
			return null;
		Date temp = Date.valueOf(SPdate.get());

		Calendar calendar = new GregorianCalendar();
		calendar.setTime(temp);
		int year = calendar.get(Calendar.YEAR);

		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int month = calendar.get(Calendar.MONTH) + 1;

		dateconverter = new DateConverter();
		JalaliDate jalali = dateconverter.gregorianToJalali(year, month, day);
		return jalali.toString();
	}

	public JalaliDate getSPJalaliDate() {
		if (SPdate.get() == null)
			return null;
		Date temp = Date.valueOf(SPdate.get());

		Calendar calendar = new GregorianCalendar();
		calendar.setTime(temp);
		int year = calendar.get(Calendar.YEAR);

		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int month = calendar.get(Calendar.MONTH) + 1;

		dateconverter = new DateConverter();
		JalaliDate jalali = dateconverter.gregorianToJalali(year, month, day);
		return jalali;
	}

}
