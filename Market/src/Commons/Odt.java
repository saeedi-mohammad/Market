package Commons;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import Calendar.DateConverter;
import Calendar.JalaliCalendar;
import Calendar.JalaliDate;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Odt {

	private JalaliCalendar jalaliC = new JalaliCalendar();
	private DateConverter dateconverter = new DateConverter();

	private int id;
	private int operatorid;
	private Date date;
	private Time stime;
	private Time etime;
	private String operatorname;
	private String operatorfamily;
	private int InTimeHour;
	private int InTimeMinute;

	private SimpleIntegerProperty SPid;
	private SimpleIntegerProperty SPoperatorid;
	private SimpleStringProperty SPdate;
	private SimpleStringProperty SPstime;
	private SimpleStringProperty SPetime;
	private SimpleStringProperty SPoperatorname;
	private SimpleStringProperty SPoperatorfamily;
	private SimpleIntegerProperty SPrownumber;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
		this.SPid = new SimpleIntegerProperty(id);

	}

	public int getOperatorid() {
		return operatorid;
	}

	public void setOperatorid(int operatorid) {
		this.operatorid = operatorid;
		this.SPoperatorid = new SimpleIntegerProperty(id);
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
		this.SPdate = new SimpleStringProperty(date.toString());
	}

	public Time getStime() {
		return stime;
	}

	public void setStime(Time stime) {
		this.stime = stime;
		if (stime != null)
			this.SPstime = new SimpleStringProperty(stime.toString());
		else
			this.SPstime = new SimpleStringProperty();
	}

	public Time getEtime() {
		return etime;
	}

	public void setEtime(Time etime) {
		this.etime = etime;
		if (etime != null)
			this.SPetime = new SimpleStringProperty(etime.toString());
		else
			this.SPetime = new SimpleStringProperty();
	}

	public String getOperatorname() {
		return operatorname;
	}

	public void setOperatorname(String operatorname) {
		this.operatorname = operatorname;
		this.SPoperatorname = new SimpleStringProperty(operatorname);
	}

	public String getOperatorfamily() {
		return operatorfamily;
	}

	public void setOperatorfamily(String operatorfamily) {
		this.operatorfamily = operatorfamily;
		this.SPoperatorfamily = new SimpleStringProperty(operatorfamily);
	}

	public int getSPid() {
		return SPid.get();
	}

	public int getSPoperatorid() {
		return SPoperatorid.get();
	}

	public Time getSPstime() {
		if (SPstime.get() == null)
			return null;
		Time temp = Time.valueOf(SPstime.get());
		return temp;
	}

	public String getSPdate() {
		return SPdate.get();
	}

	public Time getSPetime() {
		if (SPetime.get() == null)
			return null;
		Time temp = Time.valueOf(SPetime.get());
		return temp;
	}

	public String getSPintimeString() throws ParseException {
		if (SPstime.get() != null && SPetime.get() != null) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

			// Parsing the Time Period
			java.util.Date date1 = simpleDateFormat.parse(SPstime.get());
			java.util.Date date2 = simpleDateFormat.parse(SPetime.get());

			long differenceInMilliSeconds = Math.abs(date2.getTime() - date1.getTime());

			// Calculating the difference in Hours
			long differenceInHours = (differenceInMilliSeconds / (60 * 60 * 1000)) % 24;

			// Calculating the difference in Minutes
			long differenceInMinutes = (differenceInMilliSeconds / (60 * 1000)) % 60;

			// Calculating the difference in Seconds
			long differenceInSeconds = (differenceInMilliSeconds / 1000) % 60;

			return Long.toString(differenceInHours) + ":" + Long.toString(differenceInMinutes) + ":"
					+ Long.toString(differenceInSeconds);
		}

		else
			return null;

	}

	public String getSPoperatorname() {
		return SPoperatorname.get();
	}

	public String getSPoperatorfamily() {
		return SPoperatorfamily.get();
	}

	public String getSPoperatornameANDfamily() {
		return SPoperatorname.get() + " " + SPoperatorfamily.get();
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

	public long getInTimeHour() throws ParseException {
		if (SPstime.get() != null && SPetime.get() != null) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

			// Parsing the Time Period
			java.util.Date date1 = simpleDateFormat.parse(SPstime.get());
			java.util.Date date2 = simpleDateFormat.parse(SPetime.get());

			long differenceInMilliSeconds = Math.abs(date2.getTime() - date1.getTime());

			// Calculating the difference in Hours
			long differenceInHours = (differenceInMilliSeconds / (60 * 60 * 1000)) % 24;

			return differenceInHours;

		}

		else
			return 0;
	}

	public long getInTimeMinute() throws ParseException {
		if (SPstime.get() != null && SPetime.get() != null) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

			// Parsing the Time Period
			java.util.Date date1 = simpleDateFormat.parse(SPstime.get());
			java.util.Date date2 = simpleDateFormat.parse(SPetime.get());

			long differenceInMilliSeconds = Math.abs(date2.getTime() - date1.getTime());

			// Calculating the difference in Minutes
			long differenceInMinutes = (differenceInMilliSeconds / (60 * 1000)) % 60;

			return differenceInMinutes;

		}

		else
			return 0;
	}

}
