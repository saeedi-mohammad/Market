package Controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Commons.Customer;
import Commons.Invoise;
import Commons.Item;
import Commons.Sale;
import Dao.CustomerDao;
import Dao.InvoiseDao;
import Dao.ItemDao;
import Dao.SaleDao;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ShowSaleInvoiseController {

	private ObservableList<Sale> Pitems = FXCollections.observableArrayList();
	private ObservableList<Item> UpdatedItems = FXCollections.observableArrayList();

	private InvoiseDao invoiseDao = new InvoiseDao();
	private SaleDao saleDao = new SaleDao();
	private CustomerDao customerDao = new CustomerDao();
	private ItemDao itemDao = new ItemDao();

	private Customer customer = new Customer();
	private Invoise invoise = new Invoise();

	@FXML
	private Button BackButton;

	@FXML
	private TextField CustomerIdField;

	@FXML
	private Button CustomersListButton;

	@FXML
	private Button DiscountButton;

	@FXML
	private Button PrintInvoiseButton;

	@FXML
	private Label SalePoint;

	@FXML
	private Button SetInvoiseButton;

	@FXML
	private Button SetPointButton;

	@FXML
	private Label TotalCost;

	@FXML
	private TableView<Sale> ListTable;

	@FXML
	private TableColumn<Sale, Long> itemcost;

	@FXML
	private TableColumn<Sale, String> itemname;

	@FXML
	private TableColumn<Sale, Long> itemprice;

	@FXML
	private TableColumn<Sale, Integer> itemrow;

	@FXML
	private TableColumn<Sale, Double> itemvalue;

	@FXML
	void ApplyPointsDiscount(ActionEvent event) throws NumberFormatException, ClassNotFoundException, SQLException {
		if (CustomerIdField.getText() == null || CustomerIdField.getText().equals("")) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("خطا");
			alert.setHeaderText(null);
			alert.setContentText("ابتدا کد مشتری را وارد کنید.");
			alert.show();
		}

		else {
			this.customer = customerDao.SelectById(Integer.parseInt(CustomerIdField.getText()));
			if (customer.getName() == null) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("خطا");
				alert.setHeaderText(null);
				alert.setContentText("کد مشتری وارد شده نامعتبر است.");
				alert.show();
			}

			else {
				Long temp = (long) (customer.getPoint() * 5000);
				TotalCost.setText(Long.toString(Long.parseLong(TotalCost.getText()) - temp));
				customer.setPoint(0);
			}
		}
	}

	@FXML
	void Back(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {
		FXMLLoader fxmlLoader = new FXMLLoader();
		Parent root = fxmlLoader.load(getClass().getResource("/View/NewSale.fxml").openStream());

		NewSaleController nsc = (NewSaleController) fxmlLoader.getController();
		nsc.ReBack(Pitems);

		Stage stage = new Stage();
		Scene scene = new Scene(root);
		stage.setTitle("  ثبت فروش جدید");

		stage.setScene(scene);
		stage.show();

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

			for (Sale sale : Pitems) {
				Row row = sheet.createRow(rownum++);
				CreateExelList(sale, row);
			}

			Row rown_1 = sheet.createRow(rownum + 2);
			Cell celln_1 = rown_1.createCell(0);
			celln_1.setCellValue("مبلغ کل");

			celln_1 = rown_1.createCell(1);
			celln_1.setCellValue("امتیاز این خرید");

			Row rown = sheet.createRow(rownum + 3);
			Cell celln = rown.createCell(0);
			celln.setCellValue(TotalCost.getText());

			celln = rown.createCell(1);
			celln.setCellValue(SalePoint.getText());

			FileOutputStream out = new FileOutputStream(file); // file name with path
			workbook.write(out);
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void CreateExelColumns(Row row0) {
		Cell cell = row0.createCell(0);
		cell.setCellValue("کد کالا");

		cell = row0.createCell(1);
		cell.setCellValue("نام کالا");

		cell = row0.createCell(2);
		cell.setCellValue("دسته بندی");

		cell = row0.createCell(3);
		cell.setCellValue("تعداد یا مقدار");

		cell = row0.createCell(4);
		cell.setCellValue("عمده یا خرده");

		cell = row0.createCell(5);
		cell.setCellValue("قیمت کالا");

		cell = row0.createCell(6);
		cell.setCellValue("سریال کالا");

		cell = row0.createCell(7);
		cell.setCellValue("هزینه کل آیتم");
	}

	private void CreateExelList(Sale sale, Row row) // creating cells for each row
	{
		Cell cell = row.createCell(0);
		cell.setCellValue(sale.getItemid());

		cell = row.createCell(1);
		cell.setCellValue(sale.getItemname());

		cell = row.createCell(2);
		cell.setCellValue(sale.getItemgroupname());

		cell = row.createCell(3);
		cell.setCellValue(sale.getItemvalue());

		cell = row.createCell(4);
		cell.setCellValue(sale.getRetailORwholesale());

		cell = row.createCell(5);
		cell.setCellValue(sale.getItemprice());

		cell = row.createCell(6);
		cell.setCellValue(sale.getItemserial());

		cell = row.createCell(7);
		cell.setCellValue(sale.getSalecost());
	}

	@FXML
	void SetInvoise(ActionEvent event) throws ClassNotFoundException, SQLException {

		if (customer.getName() == null)
			invoise.setCustomerid(1);
		else
			invoise.setCustomerid(customer.getId());

		invoise.setSaleOrPurchase("فروش");
		invoise.setTotalcost(Long.parseLong(TotalCost.getText()));
		java.util.Date utilDate = new java.util.Date();
		invoise.setDate(new java.sql.Date(utilDate.getTime()));
		invoise.setTime(new java.sql.Time(utilDate.getTime()));

		invoise.setInvoiseid(invoiseDao.CreateNewSaleInvoise(this.invoise));
		customer.setLastPurchaseDate(invoise.getDate());
		customerDao.UpdateCustomer(customer);

		for (int x = 0; x < Pitems.size(); x++) {
			Pitems.get(x).setInvoiseid(invoise.getInvoiseid());
			saleDao.CreateNewSale(Pitems.get(x));
		}

		for (int x = 0; x < UpdatedItems.size(); x++)
			itemDao.UpdateItemValue(UpdatedItems.get(x));

		Stage currentstage = (Stage) BackButton.getScene().getWindow();
		currentstage.close();

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("تایید");
		alert.setHeaderText(null);
		alert.setContentText("فاکتور با موفقیت ثبت شد.");
		alert.show();

	}

	@FXML
	void SetPoint(ActionEvent event) throws NumberFormatException, ClassNotFoundException, SQLException {
		if (CustomerIdField.getText().equals("")) {

			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("خطا");
			alert.setHeaderText(null);
			alert.setContentText("ابتدا کد مشتری را وارد کنید.");
			alert.show();
		}

		else {
			this.customer = customerDao.SelectById(Integer.parseInt(CustomerIdField.getText()));
			if (customer.getName() == null) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("خطا");
				alert.setHeaderText(null);
				alert.setContentText("کد مشتری وارد شده نامعتبر است.");
				alert.show();
			}

			else {
				customer.setPoint(customer.getPoint() + Integer.parseInt(SalePoint.getText()));

				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("!");
				alert.setHeaderText(null);
				alert.setContentText("امتیاز خرید به امتیازات مشتری اضافه شد.");
				alert.show();

				CustomerIdField.setDisable(true);
				SetPointButton.setDisable(true);
			}
		}
	}

	@FXML
	void ShowCustomersList(ActionEvent event)
			throws IOException, InterruptedException, ClassNotFoundException, SQLException {
		FXMLLoader fxmlLoader = new FXMLLoader();
		Parent root = fxmlLoader.load(getClass().getResource("/View/CustomersList.fxml").openStream());

		CustomersListController clc = (CustomersListController) fxmlLoader.getController();
		clc.OnStart(this);

		Stage stage = new Stage();
		Scene scene = new Scene(root);
		stage.setTitle("  لیست مشتریان");

		stage.setScene(scene);
		stage.show();
	}

	public void OnStart(ObservableList<Sale> Pitems, ObservableList<Item> updateditems) {
		this.Pitems = Pitems;
		this.UpdatedItems = updateditems;

		itemrow.setCellValueFactory(new PropertyValueFactory<Sale, Integer>("SPrownumber"));
		itemvalue.setCellValueFactory(new PropertyValueFactory<Sale, Double>("SPitemvalue"));
		itemname.setCellValueFactory(new PropertyValueFactory<Sale, String>("SPitemname"));
		itemprice.setCellValueFactory(new PropertyValueFactory<Sale, Long>("SPitemprice"));
		itemcost.setCellValueFactory(new PropertyValueFactory<Sale, Long>("SPsalecost"));

		ListTable.setItems(Pitems);

		Long temp = (long) 0;
		for (int x = 0; x < Pitems.size(); x++)
			temp += Pitems.get(x).getSalecost();
		TotalCost.setText(Long.toString(temp));
		SalePoint.setText(Long.toString(temp / 100000));

		CustomerIdField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					CustomerIdField.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});
	}

	public void UpdateCustomerIdFromCustomersListController(Customer customer) {
		this.CustomerIdField.setText(Integer.toString(customer.getId()));
	}
}
