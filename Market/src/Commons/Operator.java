package Commons;

import java.sql.SQLException;

import Dao.OdtDao;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class Operator {

	private OdtDao odtDao = new OdtDao();

	private int operatorid;
	private String name;
	private String family;
	private String mobile;
	private Long mellicode;
	private String username;
	private String password;
	private int accesslevel;

	private SimpleIntegerProperty SPoperatorid;
	private SimpleStringProperty SPname;
	private SimpleStringProperty SPfamily;
	private SimpleStringProperty SPmobile;
	private SimpleLongProperty SPmellicode;
	private SimpleStringProperty SPusername;
	private SimpleStringProperty SPpassword;
	private SimpleIntegerProperty SPrownumber;

	public int getOperatorid() {
		return operatorid;
	}

	public void setOperatorid(int operatorid) {
		this.operatorid = operatorid;
		this.SPoperatorid = new SimpleIntegerProperty(operatorid);
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
		this.SPusername = new SimpleStringProperty(username);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
		this.SPpassword = new SimpleStringProperty(password);
	}

	public int getAccesslevel() {
		return accesslevel;
	}

	public void setAccesslevel(int a) {
		this.accesslevel = a;
	}

	public Long getMellicode() {
		return mellicode;
	}

	public void setMellicode(Long mellicode) {
		this.mellicode = mellicode;
		this.SPmellicode = new SimpleLongProperty(mellicode);
	}

	public int getSPrownumber() {
		return SPrownumber.get();
	}

	public void setSPrownumber(int sPrownumber) {
		this.SPrownumber = new SimpleIntegerProperty(sPrownumber);
	}

	public int getSPoperatorid() {
		return SPoperatorid.get();
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

	public Long getSPmellicode() {
		return SPmellicode.get();
	}

	public String getSPusername() {
		return SPusername.get();
	}

	public String getSPpassword() {
		return SPpassword.get();
	}

	public String getSPaccesslevelString() {

		if (this.accesslevel == 1)
			return "ادمین";
		else if (this.accesslevel == 2)
			return "خرید";
		else if (this.accesslevel == 3)
			return "فروش";
		else if (this.accesslevel == 4)
			return "انبار";
		return "نامعتبر";
	}

	public String getSPstatus() throws ClassNotFoundException, SQLException {
		Odt odt = odtDao.SelectLastByOperatorid(this.operatorid);

		if (odt == null) {
			return "غایب";
		} else if (odt.getEtime() != null) {
			return "غایب";
		} else if (odt.getStime() != null && odt.getEtime() == null) {
			return "حاضر";
		} else
			return "";
	}

}
