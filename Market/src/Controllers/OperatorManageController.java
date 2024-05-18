package Controllers;

import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Optional;

import Calendar.DateConverter;
import Commons.Odt;
import Commons.Operator;
import Dao.OdtDao;
import Dao.OperatorDao;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class OperatorManageController {

	private OperatorDao operatorDao = new OperatorDao();
	private OdtDao odtDao = new OdtDao();
	private HomePageController hpc;

	private ObservableList<Odt> odts = FXCollections.observableArrayList();
	private ObservableList<Operator> operators = FXCollections.observableArrayList();
	private ObservableList<Operator> MfilterdOperators = FXCollections.observableArrayList();
	private ObservableList<Operator> PAfilterdOperators = FXCollections.observableArrayList();
	private ObservableList<Operator> RfilterdOperators = FXCollections.observableArrayList();

	private DateConverter dateconverter = new DateConverter();

	private ArrayList<String> month1 = new ArrayList<String>();
	private ArrayList<String> year1 = new ArrayList<String>();

	private ArrayList<String> month2 = new ArrayList<String>();
	private ArrayList<String> year2 = new ArrayList<String>();

	private Operator tempOperator = new Operator();

	@FXML
	private Label ApsentHourLable;

	@FXML
	private Button DeleteOperatorButton;

	@FXML
	private Button EditOperatorButton;

	@FXML
	private TableColumn<Odt, Time> EtimeOdt;

	@FXML
	private TableView<Operator> ManageTable;

	@FXML
	private TableColumn<Operator, Long> MelliCodeM;

	@FXML
	private ComboBox<String> MonthComboPA;

	@FXML
	private ComboBox<String> MonthComboR;

	@FXML
	private Label ApsentDaysLable;

	@FXML
	private Button NewOperatorButton;

	@FXML
	private TableView<Operator> PAtable;

	@FXML
	private TableView<Operator> RTable;

	@FXML
	private Button SetEndButton;

	@FXML
	private Button SetStartButton;

	@FXML
	private Button ShowodtPATableButton;

	@FXML
	private Button ShowRFieldButton;

	@FXML
	private TableColumn<Odt, Time> StimeOdt;

	@FXML
	private ComboBox<String> YearComboPA;

	@FXML
	private ComboBox<String> YearComboR;

	@FXML
	private TableColumn<Operator, String> accesslevelM;

	@FXML
	private TableColumn<Operator, String> accesslevelPA;

	@FXML
	private TableColumn<Operator, String> accesslevelR;

	@FXML
	private TableColumn<Odt, Date> dateOdt;

	@FXML
	private TableColumn<Operator, String> familyM;

	@FXML
	private TableColumn<Operator, String> familyPA;

	@FXML
	private TableColumn<Operator, String> familyR;

	@FXML
	private TableColumn<Odt, Integer> idOdt;

	@FXML
	private TableColumn<Odt, String> inTimeOdt;

	@FXML
	private TableColumn<Operator, String> mobileM;

	@FXML
	private TableColumn<Operator, String> nameM;

	@FXML
	private TableColumn<Operator, String> namePA;

	@FXML
	private TableColumn<Operator, String> nameR;

	@FXML
	private TableView<Odt> odtTablePA;

	@FXML
	private Label presentHourLable;

	@FXML
	private TableColumn<Operator, Integer> rowM;

	@FXML
	private TableColumn<Odt, Integer> rowOdt;

	@FXML
	private TableColumn<Operator, Integer> rowPA;

	@FXML
	private TableColumn<Operator, Integer> rowR;

	@FXML
	private TextField searchM;

	@FXML
	private TextField searchPA;

	@FXML
	private TextField searchR;

	@FXML
	private TableColumn<Operator, String> usernameM;

	@FXML
	private TableColumn<Operator, String> statusPA;

	@FXML
	private TableColumn<Operator, String> usernameR;

	@FXML
	void CreateNewOperator(ActionEvent event) throws IOException {

		FXMLLoader fxmlLoader = new FXMLLoader();
		Parent root = fxmlLoader.load(getClass().getResource("/View/NewOperator.fxml").openStream());

		NewOperatorController noc = (NewOperatorController) fxmlLoader.getController();
		noc.OnStart(this);

		Stage stage = new Stage();
		Scene scene = new Scene(root);
		stage.setTitle("  افزودن اپراتور جدید");

		stage.setScene(scene);
		stage.show();
	}

	@FXML
	void DeleteOperator(ActionEvent event) throws ClassNotFoundException, SQLException {

		if (ManageTable.getSelectionModel().getSelectedItems().isEmpty()) {

			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("خطا");
			alert.setHeaderText(null);
			alert.setContentText("ابتدا اپراتور مورد نظر را انتخاب کنید.");
			alert.show();
		}

		else {

			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("هشدار");
			alert.setHeaderText(null);
			alert.setContentText(
					"در صورت حذف اپراتور تمامی اطلاعات ورود و خروج و تمامی دادداشت های وی نیز پاک خواهد شد، آیا مطمعن هستید؟");
			ButtonType byes = new ButtonType("بله");
			ButtonType bno = new ButtonType("خیر");
			alert.getButtonTypes().clear();
			alert.getButtonTypes().add(byes);
			alert.getButtonTypes().add(bno);

			Optional<ButtonType> option = alert.showAndWait();
			if (option.get() == byes) {
				operatorDao.DeleteOperator(ManageTable.getSelectionModel().getSelectedItem());

				Alert a = new Alert(AlertType.INFORMATION);
				a.setTitle("اپراتور حذف شده");
				a.setHeaderText(null);
				a.setContentText("اپراتور مورد نظر با موفقیت حذف شد.");
				a.show();

				UpdateMTable();
				UpdatePATable();
				UpdateRTable();
			}

			else if (option.get() == null || option.get() == bno)
				return;
		}
	}

	@FXML
	void EditOperator(ActionEvent event) throws IOException {

		if (ManageTable.getSelectionModel().getSelectedItems().isEmpty()) {
			Alert a = new Alert(AlertType.ERROR);
			a.setTitle("خطا");
			a.setHeaderText(null);
			a.setContentText("ابتدا اپراتور مورد نظر را از جدول انتخاب کنید.");
			a.show();

		} else {

			FXMLLoader fxmlLoader = new FXMLLoader();
			Parent root = fxmlLoader.load(getClass().getResource("/View/EditOperator.fxml").openStream());

			EditOperatorController eoc = (EditOperatorController) fxmlLoader.getController();
			eoc.OnStart(this, ManageTable.getSelectionModel().getSelectedItem());

			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setTitle("  افزودن اپراتور جدید");

			stage.setScene(scene);
			stage.show();
		}
	}

	@FXML
	void SetEndTime(ActionEvent event) throws ClassNotFoundException, SQLException {

		if (PAtable.getSelectionModel().getSelectedItems().isEmpty()) {
			Alert a = new Alert(AlertType.ERROR);
			a.setTitle("خطا");
			a.setHeaderText(null);
			a.setContentText("ابتدا اپراتور مورد نظر را از جدول انتخاب کنید.");
			a.show();

		} else {

			if (PAtable.getSelectionModel().getSelectedItem().getSPstatus().equals("غایب")) {

				Alert a = new Alert(AlertType.ERROR);
				a.setTitle("خطا");
				a.setHeaderText(null);
				a.setContentText("اپراتور انتخاب شده غایب است.");
				a.show();

			} else {

				java.util.Date utilDate = new java.util.Date();
				Odt odt = odtDao.SelectLastByOperatorid(PAtable.getSelectionModel().getSelectedItem().getOperatorid());
				odt.setEtime(new java.sql.Time(utilDate.getTime()));

				odtDao.UpdateRecord(odt);
				UpdatePATable();
				this.hpc.UpdateOnorOff();

				Alert a = new Alert(AlertType.INFORMATION);
				a.setTitle("ثبت خروج");
				a.setHeaderText(null);
				a.setContentText("خروج اپراتور مورد نظر ثبت شد.");
				a.show();
			}
		}
	}

	@FXML
	void SetStartTime(ActionEvent event) throws ClassNotFoundException, SQLException {

		if (PAtable.getSelectionModel().getSelectedItems().isEmpty()) {
			Alert a = new Alert(AlertType.ERROR);
			a.setTitle("خطا");
			a.setHeaderText(null);
			a.setContentText("ابتدا اپراتور مورد نظر را از جدول انتخاب کنید.");
			a.show();
		} else {

			if (PAtable.getSelectionModel().getSelectedItem().getSPstatus().equals("حاضر")) {

				Alert a = new Alert(AlertType.ERROR);
				a.setTitle("خطا");
				a.setHeaderText(null);
				a.setContentText("اپراتور انتخاب شده حاضر است.");
				a.show();

			} else {

				java.util.Date utilDate = new java.util.Date();
				Odt odt = new Odt();
				Operator tempoperator = PAtable.getSelectionModel().getSelectedItem();
				odt.setOperatorid(tempoperator.getOperatorid());
				odt.setDate(new java.sql.Date(utilDate.getTime()));
				odt.setStime(new java.sql.Time(utilDate.getTime()));

				odtDao.AddNewRecourd(odt);
				UpdatePATable();

				this.hpc.UpdateOnorOff();

				Alert a = new Alert(AlertType.INFORMATION);
				a.setTitle("ثبت ورود");
				a.setHeaderText(null);
				a.setContentText("ورود اپراتور مورد نظر ثبت شد.");
				a.show();
			}
		}
	}

	@FXML
	void ShowodtPATable(ActionEvent event) throws ClassNotFoundException, SQLException {

		if (PAtable.getSelectionModel().getSelectedItems().isEmpty()) {
			Alert a = new Alert(AlertType.ERROR);
			a.setTitle("خطا");
			a.setHeaderText(null);
			a.setContentText("ابتدا اپراتور مورد نظر را از جدول انتخاب کنید.");
			a.show();

		} else {

			this.tempOperator = PAtable.getSelectionModel().getSelectedItem();

			int year = Integer.parseInt(YearComboPA.getSelectionModel().getSelectedItem());
			int month = MonthComboPA.getSelectionModel().getSelectedIndex() + 1;

			LocalDate ldate1 = dateconverter.jalaliToGregorian(year, month, 1);
			Date date1 = convertLDtoD(ldate1);

			int a = 0;
			if (month <= 6)
				a = 31;
			else
				a = 30;

			LocalDate ldate2 = dateconverter.jalaliToGregorian(year, month, a);
			Date date2 = convertLDtoD(ldate2);

			odts.clear();
			GetAllOdtsByOperatoridBetweendates(date1, date2);
			odtTablePA.setItems(odts);
		}
	}

	@FXML
	void ShowRFieldButton(ActionEvent event) throws ClassNotFoundException, SQLException, ParseException {

		if (RTable.getSelectionModel().getSelectedItems().isEmpty()) {
			Alert a = new Alert(AlertType.ERROR);
			a.setTitle("خطا");
			a.setHeaderText(null);
			a.setContentText("ابتدا اپراتور مورد نظر را از جدول انتخاب کنید.");
			a.show();

		} else {

			this.tempOperator = RTable.getSelectionModel().getSelectedItem();

			int apsentdays = 0;

			int year = Integer.parseInt(YearComboR.getSelectionModel().getSelectedItem());
			int month = MonthComboR.getSelectionModel().getSelectedIndex() + 1;

			LocalDate ldate1 = dateconverter.jalaliToGregorian(year, month, 1);
			Date date1 = convertLDtoD(ldate1);

			int a = 0;
			if (month <= 6)
				a = 31;
			else
				a = 30;

			LocalDate ldate2 = dateconverter.jalaliToGregorian(year, month, a);
			Date date2 = convertLDtoD(ldate2);

			odts.clear();
			GetAllOdtsByOperatoridBetweendates(date1, date2);

			for (int x = 1; x <= a; x++) {
				LocalDate ltempdate = dateconverter.jalaliToGregorian(year, month, x);
				Date tempdate = convertLDtoD(ltempdate);
				boolean flag = false;
				for (int y = 0; y < this.odts.size(); y++) {

					if (this.odts.get(y).getDate().equals(tempdate)) {

						flag = true;
						break;
					}
				}
				if (!flag)
					apsentdays++;
			}
			ApsentDaysLable.setText(Integer.toString(apsentdays));

			long inhours = 0;
			long inminutes = 0;
			for (int x = 0; x < this.odts.size(); x++) {
				inhours += this.odts.get(x).getInTimeHour();
				inminutes += this.odts.get(x).getInTimeMinute();
			}
			inhours += inminutes / 60;
			inminutes = inminutes % 60;
			presentHourLable.setText(Long.toString(inhours) + " : " + Long.toString(inminutes));

			int totalmonthhourse = a * 8;
			long apsenthours = totalmonthhourse - inhours;
			apsenthours -= (apsentdays * 8);
			long apsentminutes = 0;

			if (inminutes > 0) {
				apsenthours--;
				apsentminutes = 60 - inminutes;

			}
			if (apsenthours < 0) {
				ApsentHourLable.setText("0");

			} else {

				ApsentHourLable.setText(Long.toString(apsenthours) + " : " + Long.toString(apsentminutes));
			}
		}
	}

	public void OnStart(HomePageController hpc) throws ClassNotFoundException, SQLException {

		this.hpc = hpc;

		rowM.setCellValueFactory(new PropertyValueFactory<Operator, Integer>("SPrownumber"));
		nameM.setCellValueFactory(new PropertyValueFactory<Operator, String>("SPname"));
		familyM.setCellValueFactory(new PropertyValueFactory<Operator, String>("SPfamily"));
		MelliCodeM.setCellValueFactory(new PropertyValueFactory<Operator, Long>("SPmellicode"));
		mobileM.setCellValueFactory(new PropertyValueFactory<Operator, String>("SPmobile"));
		usernameM.setCellValueFactory(new PropertyValueFactory<Operator, String>("SPusername"));
		accesslevelM.setCellValueFactory(new PropertyValueFactory<Operator, String>("SPaccesslevelString"));

		rowPA.setCellValueFactory(new PropertyValueFactory<Operator, Integer>("SPrownumber"));
		namePA.setCellValueFactory(new PropertyValueFactory<Operator, String>("SPname"));
		familyPA.setCellValueFactory(new PropertyValueFactory<Operator, String>("SPfamily"));
		statusPA.setCellValueFactory(new PropertyValueFactory<Operator, String>("SPstatus"));
		accesslevelPA.setCellValueFactory(new PropertyValueFactory<Operator, String>("SPaccesslevelString"));

		rowR.setCellValueFactory(new PropertyValueFactory<Operator, Integer>("SPrownumber"));
		nameR.setCellValueFactory(new PropertyValueFactory<Operator, String>("SPname"));
		familyR.setCellValueFactory(new PropertyValueFactory<Operator, String>("SPfamily"));
		usernameR.setCellValueFactory(new PropertyValueFactory<Operator, String>("SPusername"));
		accesslevelR.setCellValueFactory(new PropertyValueFactory<Operator, String>("SPaccesslevelString"));

		rowOdt.setCellValueFactory(new PropertyValueFactory<Odt, Integer>("SPrownumber"));
		idOdt.setCellValueFactory(new PropertyValueFactory<Odt, Integer>("SPid"));
		dateOdt.setCellValueFactory(new PropertyValueFactory<Odt, Date>("SPJalaliDateString"));
		StimeOdt.setCellValueFactory(new PropertyValueFactory<Odt, Time>("SPstime"));
		EtimeOdt.setCellValueFactory(new PropertyValueFactory<Odt, Time>("SPetime"));
		inTimeOdt.setCellValueFactory(new PropertyValueFactory<Odt, String>("SPintimeString"));

		SetDates(year1, month1, YearComboPA, MonthComboPA);
		SetDates(year2, month2, YearComboR, MonthComboR);

		YearComboPA.getSelectionModel().select("1400");
		YearComboR.getSelectionModel().select("1400");
		MonthComboPA.getSelectionModel().select("فروردین");
		MonthComboR.getSelectionModel().select("فروردین");

		searchM.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				updateMFilteredData();
			}
		});

		UpdateMTable();
		operators.addListener(new ListChangeListener<Operator>() {

			@Override
			public void onChanged(Change<? extends Operator> arg0) {

			}

		});

		searchPA.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				updatePAFilteredData();
			}
		});

		UpdatePATable();
		operators.addListener(new ListChangeListener<Operator>() {

			@Override
			public void onChanged(Change<? extends Operator> arg0) {

			}

		});

		searchR.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				updateRFilteredData();
			}
		});

		UpdateRTable();
		operators.addListener(new ListChangeListener<Operator>() {

			@Override
			public void onChanged(Change<? extends Operator> arg0) {

			}
		});
	}

	private void updateMFilteredData() {
		MfilterdOperators.clear();

		for (Operator p : operators) {
			if (MmatchesFilter(p)) {
				MfilterdOperators.add(p);
			}
		}
		// Must re-sort table after items changed
		reapplyMTableSortOrder();
	}

	private void updatePAFilteredData() {
		PAfilterdOperators.clear();

		for (Operator p : operators) {
			if (PAmatchesFilter(p)) {
				PAfilterdOperators.add(p);
			}
		}
		// Must re-sort table after items changed
		reapplyPATableSortOrder();
	}

	private void updateRFilteredData() {
		RfilterdOperators.clear();

		for (Operator p : operators) {
			if (RmatchesFilter(p)) {
				RfilterdOperators.add(p);
			}
		}
		// Must re-sort table after items changed
		reapplyRTableSortOrder();
	}

	private boolean MmatchesFilter(Operator operator) {
		String filterString = searchM.getText();
		if (filterString == null || filterString.isEmpty()) {
			// No filter --> Add all.
			return true;
		}

		String lowerCaseFilterString = filterString.toLowerCase();

		if (operator.getName().toLowerCase().indexOf(lowerCaseFilterString) != -1)
			return true;
		else if (operator.getFamily().toLowerCase().indexOf(lowerCaseFilterString) != -1)
			return true;
		else if (operator.getMobile().toLowerCase().indexOf(lowerCaseFilterString) != -1)
			return true;

		return false; // Does not match
	}

	private boolean PAmatchesFilter(Operator operator) {
		String filterString = searchPA.getText();
		if (filterString == null || filterString.isEmpty()) {
			// No filter --> Add all.
			return true;
		}

		String lowerCaseFilterString = filterString.toLowerCase();

		if (operator.getName().toLowerCase().indexOf(lowerCaseFilterString) != -1)
			return true;
		else if (operator.getFamily().toLowerCase().indexOf(lowerCaseFilterString) != -1)
			return true;
		else if (operator.getMobile().toLowerCase().indexOf(lowerCaseFilterString) != -1)
			return true;

		return false; // Does not match
	}

	private boolean RmatchesFilter(Operator operator) {
		String filterString = searchR.getText();
		if (filterString == null || filterString.isEmpty()) {
			// No filter --> Add all.
			return true;
		}

		String lowerCaseFilterString = filterString.toLowerCase();

		if (operator.getName().toLowerCase().indexOf(lowerCaseFilterString) != -1)
			return true;
		else if (operator.getFamily().toLowerCase().indexOf(lowerCaseFilterString) != -1)
			return true;
		else if (operator.getMobile().toLowerCase().indexOf(lowerCaseFilterString) != -1)
			return true;

		return false; // Does not match
	}

	private void reapplyMTableSortOrder() {
		ArrayList<TableColumn<Operator, ?>> sortOrder = new ArrayList<>(ManageTable.getSortOrder());
		ManageTable.getSortOrder().clear();
		ManageTable.getSortOrder().addAll(sortOrder);
	}

	private void reapplyPATableSortOrder() {
		ArrayList<TableColumn<Operator, ?>> sortOrder = new ArrayList<>(PAtable.getSortOrder());
		PAtable.getSortOrder().clear();
		PAtable.getSortOrder().addAll(sortOrder);
	}

	private void reapplyRTableSortOrder() {
		ArrayList<TableColumn<Operator, ?>> sortOrder = new ArrayList<>(RTable.getSortOrder());
		RTable.getSortOrder().clear();
		RTable.getSortOrder().addAll(sortOrder);
	}

	public void SetDates(ArrayList<String> year, ArrayList<String> month, ComboBox<String> yearC,
			ComboBox<String> monthC) {
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
	}

	private void GetAllOperators() throws ClassNotFoundException, SQLException {

		ResultSet rs = operatorDao.GetAllOperators();
		int i = 1;

		while (rs.next()) {
			Operator temp = new Operator();
			temp.setOperatorid(rs.getInt("id"));
			temp.setName(rs.getString("name"));
			temp.setFamily(rs.getString("family"));
			temp.setMobile(rs.getString("mobile"));
			temp.setMellicode(rs.getLong("mellicode"));
			temp.setUsername(rs.getString("username"));
			temp.setPassword(rs.getString("password"));
			temp.setAccesslevel(rs.getInt("accesslevel"));
			temp.setSPrownumber(i++);

			operators.add(temp);
		}
	}

	private void GetAllOdtsByOperatoridBetweendates(Date date1, Date date2)
			throws ClassNotFoundException, SQLException {

		ResultSet rs = odtDao.GetAllByOperatoridBetweenDates(this.tempOperator.getOperatorid(), date1, date2);
		int i = 1;

		while (rs.next()) {
			Odt temp = new Odt();
			temp.setId(rs.getInt("id"));
			temp.setOperatorid(rs.getInt("operatorid"));
			temp.setDate(rs.getDate("date"));
			temp.setStime(rs.getTime("stime"));
			temp.setEtime(rs.getTime("etime"));
			temp.setOperatorname(this.tempOperator.getName());
			temp.setOperatorfamily(this.tempOperator.getFamily());
			temp.setSPrownumber(i++);

			odts.add(temp);
		}
	}

	public void UpdateMTable() throws ClassNotFoundException, SQLException {

		operators.clear();
		GetAllOperators();
		MfilterdOperators = FXCollections.observableArrayList(operators);
		ManageTable.setItems(MfilterdOperators);
	}

	public void UpdatePATable() throws ClassNotFoundException, SQLException {

		operators.clear();
		GetAllOperators();
		PAfilterdOperators = FXCollections.observableArrayList(operators);
		PAtable.setItems(PAfilterdOperators);
	}

	public void UpdateRTable() throws ClassNotFoundException, SQLException {

		operators.clear();
		GetAllOperators();
		RfilterdOperators = FXCollections.observableArrayList(operators);
		RTable.setItems(RfilterdOperators);
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
