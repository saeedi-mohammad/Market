package Controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Calendar.DateConverter;
import Commons.Invoise;
import Dao.InvoiseDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ShowReportsController {

	private ObservableList<Invoise> invoises = FXCollections.observableArrayList();

	private ArrayList<String> type = new ArrayList<String>();
	private ArrayList<String> day = new ArrayList<String>();
	private ArrayList<String> month = new ArrayList<String>();
	private ArrayList<String> year = new ArrayList<String>();

	private Long totalcost = (long) 0;
	private Long totalpurchasecost = (long) 0;
	private Long totalsalecost = (long) 0;
	private Long profit = (long) 0;

	private DateConverter dateconverter = new DateConverter();

	private InvoiseDao invoiseDao = new InvoiseDao();

	private String typetemp;

	@FXML
	private Button BackButton;

	@FXML
	private Label DateL;

	@FXML
	private ComboBox<String> TypeCombo;

	@FXML
	private ComboBox<String> YearCombo;

	@FXML
	private ComboBox<String> DayCombo;

	@FXML
	private ComboBox<String> MonthCombo;

	@FXML
	private Button PrintButton;

	@FXML
	private Label PurchaseTransactionL;

	@FXML
	private Label SaleTransactionL;

	@FXML
	private Button ShowDetailsButton;

	@FXML
	private Label TotalProfitL;

	@FXML
	private Label TotalTransactionL;

	@FXML
	private GridPane DateGridPane;

	@FXML
	void print(ActionEvent event) {

		if (DateL.getText() == null)
			return;

		Stage stage = new Stage();

		FileChooser fileChooser = new FileChooser();

		// Set extension filter for text files
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel file (*.xlsx)", "*.xlsx");
		fileChooser.getExtensionFilters().add(extFilter);

		// Show save file dialog
		File file = fileChooser.showSaveDialog(stage);

		if (file != null) {
			WrightToFile(file);
		}
	}

	private void WrightToFile(File file) {

		try {
			@SuppressWarnings("resource")
			XSSFWorkbook workbook = new XSSFWorkbook();

			XSSFSheet sheet = workbook.createSheet("sheet1");// creating a blank sheet

			Row rown_1 = sheet.createRow(1);
			Cell cel_1 = rown_1.createCell(0);
			cel_1.setCellValue("تاریخ گزارش");

			cel_1 = rown_1.createCell(1);
			cel_1.setCellValue("مبلغ کل تراکنش ها");

			cel_1 = rown_1.createCell(2);
			cel_1.setCellValue("سهم خرید");

			cel_1 = rown_1.createCell(3);
			cel_1.setCellValue("سهم فروش");

			cel_1 = rown_1.createCell(4);
			cel_1.setCellValue("سود خالص");

			Row rown = sheet.createRow(2);
			Cell celln = rown.createCell(0);
			celln.setCellValue(DateL.getText());

			celln = rown.createCell(1);
			celln.setCellValue(TotalTransactionL.getText());

			celln = rown.createCell(2);
			celln.setCellValue(PurchaseTransactionL.getText());

			celln = rown.createCell(3);
			celln.setCellValue(SaleTransactionL.getText());

			celln = rown.createCell(4);
			celln.setCellValue(TotalProfitL.getText());

			FileOutputStream out = new FileOutputStream(file); // file name with path
			workbook.write(out);
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	void ShowDetails(ActionEvent event) throws ClassNotFoundException, SQLException {

		this.totalcost = (long) 0;
		this.totalsalecost = (long) 0;
		this.totalpurchasecost = (long) 0;
		this.profit = (long) 0;

		if (typetemp.equals("سالیانه")) {
			int year = Integer.parseInt(YearCombo.getSelectionModel().getSelectedItem());
			GetReportByYear(year);
		} else if (typetemp.equals("ماهیانه")) {
			int year = Integer.parseInt(YearCombo.getSelectionModel().getSelectedItem());
			int month = MonthCombo.getSelectionModel().getSelectedIndex() + 1;
			GetReportByMonth(year, month);

		} else {
			int year = Integer.parseInt(YearCombo.getSelectionModel().getSelectedItem());
			int month = MonthCombo.getSelectionModel().getSelectedIndex() + 1;
			int day = Integer.parseInt(DayCombo.getSelectionModel().getSelectedItem());
			GetReportByDay(year, month, day);
		}
	}

	@FXML
	void GrideVisible(ActionEvent event) {

		DateGridPane.setDisable(false);
		typetemp = TypeCombo.getSelectionModel().getSelectedItem();
		if (typetemp.equals("سالیانه")) {
			DayCombo.setDisable(true);
			MonthCombo.setDisable(true);
			YearCombo.setDisable(false);
		}

		else if (typetemp.equals("ماهیانه")) {
			DayCombo.setDisable(true);
			MonthCombo.setDisable(false);
			YearCombo.setDisable(false);
		}

		else {
			DayCombo.setDisable(false);
			MonthCombo.setDisable(false);
			YearCombo.setDisable(false);
		}

	}

	@FXML
	void ConfirmDays(ActionEvent event) {
		String temp;
		if (DayCombo.isDisable())
			temp = "1";
		else
			temp = new String(DayCombo.getSelectionModel().getSelectedItem());

		if (MonthCombo.getSelectionModel().getSelectedIndex() > 5) {
			day.remove("31");
			DayCombo.getItems().clear();
			DayCombo.getItems().addAll(day);
			if (!temp.equals("31"))
				DayCombo.getSelectionModel().select(temp);
			else
				DayCombo.getSelectionModel().select("1");

		}

		else {
			if (day.size() == 30) {
				day.add("31");
				DayCombo.getItems().clear();
				DayCombo.getItems().addAll(day);
				DayCombo.getSelectionModel().select(temp);
			}
		}
	}

	@FXML
	void back(ActionEvent event) {

		Stage currentstage = (Stage) BackButton.getScene().getWindow();
		currentstage.close();
	}

	public void OnStart() {

		DateL.setText("");
		PurchaseTransactionL.setText("");
		SaleTransactionL.setText("");
		TotalProfitL.setText("");
		TotalTransactionL.setText("");

		this.type.add("روزانه");
		this.type.add("ماهیانه");
		this.type.add("سالیانه");
		this.TypeCombo.getItems().addAll(type);

		this.year.add("1400");
		this.year.add("1401");
		this.YearCombo.getItems().addAll(year);

		this.month.add("فروردین");
		this.month.add("اردیبهشت");
		this.month.add("خرداد");
		this.month.add("تیر");
		this.month.add("مرداد");
		this.month.add("شهریور");
		this.month.add("مهر");
		this.month.add("آبان");
		this.month.add("آذر");
		this.month.add("دی");
		this.month.add("بهمن");
		this.month.add("اسفند");
		this.MonthCombo.getItems().addAll(month);

		for (int x = 1; x <= 31; x++)
			this.day.add(Integer.toString(x));
		this.DayCombo.getItems().addAll(day);

		this.DateGridPane.setDisable(true);
	}

	private void GetReportByDay(int year, int month, int day) throws ClassNotFoundException, SQLException {

		LocalDate ldate = dateconverter.jalaliToGregorian(year, month, day);
		Date date = convertLDtoD(ldate);

		ResultSet rs = invoiseDao.SelectByDate(date);

		SetRstoList(rs);
		SetToFields();

		DateL.setText(year + "/" + month + "/" + day);

	}

	private void GetReportByMonth(int year, int month) throws ClassNotFoundException, SQLException {

		LocalDate ldate1 = dateconverter.jalaliToGregorian(year, month, 1);
		Date date1 = convertLDtoD(ldate1);

		LocalDate ldate2 = dateconverter.jalaliToGregorian(year, month, day.size());
		Date date2 = convertLDtoD(ldate2);

		ResultSet rs = invoiseDao.SelectByBetween(date1, date2);

		SetRstoList(rs);
		SetToFields();

		DateL.setText("از " + year + "/" + month + "/" + 1 + "تا " + year + "/" + month + "/" + (day.size()));

	}

	private void GetReportByYear(int year) throws SQLException, ClassNotFoundException {

		LocalDate ldate1 = dateconverter.jalaliToGregorian(year, 1, 1);
		Date date1 = convertLDtoD(ldate1);

		LocalDate ldate2 = dateconverter.jalaliToGregorian(year, 12, day.size());
		Date date2 = convertLDtoD(ldate2);

		ResultSet rs = invoiseDao.SelectByBetween(date1, date2);

		SetRstoList(rs);
		SetToFields();

		DateL.setText("از " + year + "/" + 1 + "/" + 1 + "تا " + year + "/" + 12 + "/" + 30);

	}

	private void SetRstoList(ResultSet rs) throws SQLException {

		invoises.clear();
		int i = 1;
		while (rs.next()) {
			Invoise temp = new Invoise();
			temp.setInvoiseid(rs.getInt("id"));
			temp.setCustomerid(rs.getInt("customerid"));
			temp.setDate(rs.getDate("date"));
			temp.setTime(rs.getTime("time"));
			temp.setSaleOrPurchase(rs.getString("saleorpurchase"));
			temp.setTotalcost(rs.getLong("totalcost"));
			temp.setSPrownumber(i++);

			this.invoises.add(temp);

		}
	}

	private void SetToFields() {
		for (int x = 0; x < invoises.size(); x++) {
			Invoise temp = invoises.get(x);
			this.totalcost += temp.getTotalcost();
			if (temp.getSaleOrPurchase().equals("فروش")) {
				this.totalsalecost += temp.getTotalcost();
				this.profit += temp.getTotalcost();
			} else {
				this.totalpurchasecost += temp.getTotalcost();
				this.profit -= temp.getTotalcost();
			}
		}

		this.TotalTransactionL.setText(Long.toString(this.totalcost));
		this.SaleTransactionL.setText(Long.toString(this.totalsalecost));
		this.PurchaseTransactionL.setText(Long.toString(this.totalpurchasecost));
		this.TotalProfitL.setText(Long.toString(this.profit));
	}

	public Date convertLDtoD(LocalDate ldate) {

		// default time zone
		ZoneId defaultZoneId = ZoneId.systemDefault();

		// local date + atStartOfDay() + default time zone + toInstant() = Date
		java.util.Date date = Date.from(ldate.atStartOfDay(defaultZoneId).toInstant());

		java.sql.Date sDate = new java.sql.Date(date.getTime());
		return sDate;
	}
}
