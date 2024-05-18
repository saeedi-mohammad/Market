package Commons;

import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

import Calendar.DateConverter;
import Calendar.JalaliCalendar;
import Calendar.JalaliDate;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Customer {

	private JalaliCalendar jalaliC = new JalaliCalendar();
	private DateConverter dateconverter = new DateConverter();

	private int id;
	private String name;
	private String family;
	private String mobile;
	private int point;
	private Date lastPurchaseDate;
	private SimpleIntegerProperty SPid;
	private SimpleStringProperty SPname;
	private SimpleStringProperty SPfamily;
	private SimpleStringProperty SPmobile;
	private SimpleIntegerProperty SPpoint;
	private SimpleStringProperty SPlastPurchaseDate;
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

	public String getFamily() {
		return family;
	}

	public void setFamily(String family) {
		this.family = family;
		this.SPfamily = new SimpleStringProperty(family);
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
		this.SPmobile = new SimpleStringProperty(mobile);
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
		this.SPpoint = new SimpleIntegerProperty(point);
	}

	public Date getlastPurchaseDate() {
		return this.lastPurchaseDate;
	}

	public void setLastPurchaseDate(Date date) {
		if (date != null) {
			this.lastPurchaseDate = date;
			this.SPlastPurchaseDate = new SimpleStringProperty(date.toString());
		} else {
			this.lastPurchaseDate = null;
			this.SPlastPurchaseDate = new SimpleStringProperty(null);
		}
	}

	public int getSPid() {
		return SPid.get();
	}

	public String getSPname() {
		return SPname.get();
	}

	public String getSPfamily() {
		return SPfamily.get();
	}

	public String getSPmobile() {
		return SPmobile.get();
	}

	public int getSPpoint() {
		return SPpoint.get();
	}

	public int getSPrownumber() {
		return SPrownumber.get();
	}

	public void setSPrownumber(int sPrownumber) {
		this.SPrownumber = new SimpleIntegerProperty(sPrownumber);
	}

	public Date getSPlastPurchaseDate() {
		if (SPlastPurchaseDate.get() == null)
			return null;
		Date temp = Date.valueOf(SPlastPurchaseDate.get());
		return temp;
	}

	public String getSPlastPurchaseJalaliDateString() {
		if (SPlastPurchaseDate.get() == null)
			return null;
		Date temp = Date.valueOf(SPlastPurchaseDate.get());

		Calendar calendar = new GregorianCalendar();
		calendar.setTime(temp);
		int year = calendar.get(Calendar.YEAR);

		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int month = calendar.get(Calendar.MONTH) + 1;

		dateconverter = new DateConverter();
		JalaliDate jalali = dateconverter.gregorianToJalali(year, month, day);
		return jalali.toString();
	}

	public JalaliDate getSPlastPurchaseJalaliDate() {
		if (SPlastPurchaseDate.get() == null)
			return null;
		Date temp = Date.valueOf(SPlastPurchaseDate.get());

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
