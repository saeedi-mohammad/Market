package Controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
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
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class TransactionController {

	private ObservableList<Invoise> invoises = FXCollections.observableArrayList();

	private DateConverter dateconverter = new DateConverter();

	private ArrayList<String> day1 = new ArrayList<String>();
	private ArrayList<String> month1 = new ArrayList<String>();
	private ArrayList<String> year1 = new ArrayList<String>();

	private ArrayList<String> day2 = new ArrayList<String>();
	private ArrayList<String> month2 = new ArrayList<String>();
	private ArrayList<String> year2 = new ArrayList<String>();

	private InvoiseDao invoiseDao = new InvoiseDao();

	@FXML
	private Button BackButton;

	@FXML
	private ComboBox<String> DayCombo1;

	@FXML
	private ComboBox<String> DayCombo2;

	@FXML
	private TableView<Invoise> InvoiseTable;

	@FXML
	private ComboBox<String> MonthCombo1;

	@FXML
	private ComboBox<String> MonthCombo2;

	@FXML
	private Button PrintButton;

	@FXML
	private Button SearchButton;

	@FXML
	private Button ShowDetailsButton;

	@FXML
	private TableColumn<Invoise, String> SorP;

	@FXML
	private ComboBox<String> YearCombo1;

	@FXML
	private ComboBox<String> YearCombo2;

	@FXML
	private TableColumn<Invoise, Integer> customerid;

	@FXML
	private TableColumn<Invoise, Date> date;

	@FXML
	private TableColumn<Invoise, Integer> invoiseid;

	@FXML
	private TableColumn<Invoise, Integer> row;

	@FXML
	private TableColumn<Invoise, Time> time;

	@FXML
	private TableColumn<Invoise, Long> totalCost;

	@FXML
	void ConfirmDays1(ActionEvent event) {

		if (MonthCombo1.getSelectionModel().getSelectedIndex() > 5) {
			day1.remove("31");
			DayCombo1.getItems().clear();
			DayCombo1.getItems().addAll(day1);
		}

		else {
			if (day1.size() == 30) {
				day1.add("31");
				DayCombo1.getItems().clear();
				DayCombo1.getItems().addAll(day1);
			}
		}
	}

	@FXML
	void ConfirmDays2(ActionEvent event) {

		if (MonthCombo2.getSelectionModel().getSelectedIndex() > 5) {
			day2.remove("31");
			DayCombo2.getItems().clear();
			DayCombo2.getItems().addAll(day2);
		}

		else {
			if (day2.size() == 30) {
				day2.add("31");
				DayCombo2.getItems().clear();
				DayCombo2.getItems().addAll(day2);
			}
		}
	}

	@FXML
	void Search(ActionEvent event) throws ClassNotFoundException, SQLException {

		if (YearCombo1.getSelectionModel().getSelectedItem() == null
				|| YearCombo2.getSelectionModel().getSelectedItem() == null
				|| MonthCombo1.getSelectionModel().getSelectedItem() == null
				|| MonthCombo2.getSelectionModel().getSelectedItem() == null
				|| DayCombo1.getSelectionModel().getSelectedItem() == null
				|| DayCombo2.getSelectionModel().getSelectedItem() == null) {

			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("خطا");
			alert.setHeaderText(null);
			alert.setContentText("تمامی فیلد ها میبایستی انتخاب شوند.");
			alert.show();

		} else {

			int year1 = Integer.parseInt(YearCombo1.getSelectionModel().getSelectedItem());
			int month1 = MonthCombo1.getSelectionModel().getSelectedIndex() + 1;
			int day1 = Integer.parseInt(DayCombo1.getSelectionModel().getSelectedItem());

			int year2 = Integer.parseInt(YearCombo2.getSelectionModel().getSelectedItem());
			int month2 = MonthCombo2.getSelectionModel().getSelectedIndex() + 1;
			int day2 = Integer.parseInt(DayCombo2.getSelectionModel().getSelectedItem());

			LocalDate ldate1 = dateconverter.jalaliToGregorian(year1, month1, day1);
			Date date1 = convertLDtoD(ldate1);

			LocalDate ldate2 = dateconverter.jalaliToGregorian(year2, month2, day2);
			Date date2 = convertLDtoD(ldate2);

			ResultSet rs = invoiseDao.SelectByBetween(date1, date2);
			SetRstoList(rs);
			InvoiseTable.setItems(invoises);
		}
	}

	@FXML
	void ShowDetails(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {

		if (InvoiseTable.getSelectionModel().getSelectedItems().isEmpty()) {

			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("خطا");
			alert.setHeaderText(null);
			alert.setContentText("ابتدا فاکتور مورد نظر را انتخاب کنید.");
			alert.show();
		}

		else {
			FXMLLoader fxmlLoader = new FXMLLoader();
			Parent root;
			if (InvoiseTable.getSelectionModel().getSelectedItem().getSaleOrPurchase().equals("فروش")) {
				root = fxmlLoader.load(getClass().getResource("/View/ShowSaleInvoiseDetails.fxml").openStream());
				ShowSaleInvoiseDetailsController ssidc = (ShowSaleInvoiseDetailsController) fxmlLoader.getController();
				ssidc.OnStart(InvoiseTable.getSelectionModel().getSelectedItem());
			} else {
				root = fxmlLoader.load(getClass().getResource("/View/ShowPurchaseInvoiseDetails.fxml").openStream());
				ShowPurchaseInvoiseDetailsController spidc = (ShowPurchaseInvoiseDetailsController) fxmlLoader
						.getController();
				spidc.OnStart(InvoiseTable.getSelectionModel().getSelectedItem());
			}

			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setTitle("  جزئیات فاکتور");

			stage.setScene(scene);
			stage.show();
		}
	}

	@FXML
	void back(ActionEvent event) {

		Stage currentstage = (Stage) BackButton.getScene().getWindow();
		currentstage.close();
	}

	@FXML
	void print(ActionEvent event) {

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

	void WrightToFile(File file) {

		try {
			@SuppressWarnings("resource")
			XSSFWorkbook workbook = new XSSFWorkbook();

			XSSFSheet sheet = workbook.createSheet("sheet1");// creating a blank sheet
			int rownum = 0;

			Row row0 = sheet.createRow(rownum++);
			CreateExelColumns(row0);

			for (Invoise invoise : invoises) {
				Row row = sheet.createRow(rownum++);
				CreateExelList(invoise, row);
			}

			/////////////// print option
			FileOutputStream out = new FileOutputStream(file); // path
			workbook.write(out);
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void CreateExelColumns(Row row0) {

		Cell cell = row0.createCell(0);
		cell.setCellValue("کد فاکتور");

		cell = row0.createCell(1);
		cell.setCellValue("نوع فاکتور");

		cell = row0.createCell(2);
		cell.setCellValue("کد مشتری");

		cell = row0.createCell(3);
		cell.setCellValue("تاریخ");

		cell = row0.createCell(4);
		cell.setCellValue("زمان");

		cell = row0.createCell(5);
		cell.setCellValue("هزینه کل آیتم");
	}

	private void CreateExelList(Invoise invoise, Row row) // creating cells for each row
	{
		Cell cell = row.createCell(0);
		cell.setCellValue(invoise.getInvoiseid());

		cell = row.createCell(1);
		cell.setCellValue(invoise.getSaleOrPurchase());

		cell = row.createCell(2);
		cell.setCellValue(invoise.getCustomerid());

		cell = row.createCell(3);
		cell.setCellValue(invoise.getSPJalaliDateString());

		cell = row.createCell(4);
		cell.setCellValue(invoise.getTime().toString());

		cell = row.createCell(5);
		cell.setCellValue(invoise.getTotalcost());
	}

	public void OnStart() throws SQLException, ClassNotFoundException {

		row.setCellValueFactory(new PropertyValueFactory<Invoise, Integer>("SPrownumber"));
		invoiseid.setCellValueFactory(new PropertyValueFactory<Invoise, Integer>("SPinvoiseid"));
		customerid.setCellValueFactory(new PropertyValueFactory<Invoise, Integer>("SPcustomerid"));
		SorP.setCellValueFactory(new PropertyValueFactory<Invoise, String>("SPSaleOrPurchase"));
		date.setCellValueFactory(new PropertyValueFactory<Invoise, Date>("SPJalaliDate"));
		time.setCellValueFactory(new PropertyValueFactory<Invoise, Time>("SPtime"));
		totalCost.setCellValueFactory(new PropertyValueFactory<Invoise, Long>("SPtotalcost"));

		SetDates(year1, month1, day1, YearCombo1, MonthCombo1, DayCombo1);
		SetDates(year2, month2, day2, YearCombo2, MonthCombo2, DayCombo2);

		YearCombo1.getSelectionModel().select("1400");
		YearCombo2.getSelectionModel().select("1400");
		MonthCombo1.getSelectionModel().select("فروردین");
		MonthCombo2.getSelectionModel().select("مهر");
		DayCombo1.getSelectionModel().select("1");
		DayCombo2.getSelectionModel().select("1");
	}

	public ObservableList<Invoise> GetAllInvoise() throws SQLException, ClassNotFoundException {

		ResultSet rs = invoiseDao.GetAll();
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

			invoises.add(temp);
		}
		return invoises;
	}

	public void UpdateTable() throws ClassNotFoundException, SQLException {
		invoises.clear();
		GetAllInvoise();
		InvoiseTable.getItems().setAll(invoises);
	}

	public void SetDates(ArrayList<String> year, ArrayList<String> month, ArrayList<String> day, ComboBox<String> yearC,
			ComboBox<String> monthC, ComboBox<String> dayC) {
		year.add("1400");
		year.add("1401");
		yearC.getItems().addAll(year);

		month.add("فروردین");
		month.add("اردیبهشت");
		month.add("خرداد");
		month.add("تیر");
		month.add("مرداد");
		month.add("شهریور");
		month.add("مهر");
		month.add("آبان");
		month.add("آذر");
		month.add("دی");
		month.add("بهمن");
		month.add("اسفند");
		monthC.getItems().addAll(month);

		for (int x = 1; x <= 31; x++)
			day.add(Integer.toString(x));
		dayC.getItems().addAll(day);
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

	public Date convertLDtoD(LocalDate ldate) {

		// default time zone
		ZoneId defaultZoneId = ZoneId.systemDefault();

		// local date + atStartOfDay() + default time zone + toInstant() = Date
		java.util.Date date = Date.from(ldate.atStartOfDay(defaultZoneId).toInstant());

		java.sql.Date sDate = new java.sql.Date(date.getTime());
		return sDate;
	}
}
