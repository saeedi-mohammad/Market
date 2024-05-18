package Controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.ResultSet;
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
import Dao.ItemDao;
import Dao.SaleDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ShowSaleInvoiseDetailsController {

	private Invoise invoise;

	private ObservableList<Item> items = FXCollections.observableArrayList();
	private ObservableList<Sale> sales = FXCollections.observableArrayList();

	private ItemDao itemDao = new ItemDao();
	private SaleDao saleDao = new SaleDao();
	private CustomerDao customerDao = new CustomerDao();

	@FXML
	private Button BackButton;

	@FXML
	private Button PrintButton;

	@FXML
	private TableView<Sale> SaleTable;

	@FXML
	private Label customernameF;

	@FXML
	private Label dateF;

	@FXML
	private TableColumn<Sale, Integer> saleid;

	@FXML
	private TableColumn<Sale, Long> itemCost;

	@FXML
	private TableColumn<Sale, Long> itemPrice;

	@FXML
	private TableColumn<Sale, String> itemRorW;

	@FXML
	private TableColumn<Sale, String> itemname;

	@FXML
	private TableColumn<Sale, Long> itemserial;

	@FXML
	private TableColumn<Sale, Double> itemvalue;

	@FXML
	private Label pointF;

	@FXML
	private TableColumn<Sale, Integer> row;

	@FXML
	private Label timeF;

	@FXML
	private Label totalcostF;

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

	private void WrightToFile(File file) {

		try {
			@SuppressWarnings("resource")
			XSSFWorkbook workbook = new XSSFWorkbook();

			XSSFSheet sheet = workbook.createSheet("sheet1");// creating a blank sheet
			int rownum = 0;

			Row row0 = sheet.createRow(rownum++);
			CreateExelColumns(row0);

			for (Sale sale : sales) {
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
			celln.setCellValue(totalcostF.getText());

			celln = rown.createCell(1);
			celln.setCellValue(pointF.getText());

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

	public void OnStart(Invoise invoise) throws ClassNotFoundException, SQLException {

		this.invoise = invoise;

		row.setCellValueFactory(new PropertyValueFactory<Sale, Integer>("SPrownumber"));
		saleid.setCellValueFactory(new PropertyValueFactory<Sale, Integer>("SPsaleid"));
		itemvalue.setCellValueFactory(new PropertyValueFactory<Sale, Double>("SPitemvalue"));
		itemname.setCellValueFactory(new PropertyValueFactory<Sale, String>("SPitemname"));
		itemRorW.setCellValueFactory(new PropertyValueFactory<Sale, String>("SPretailORwholesale"));
		itemPrice.setCellValueFactory(new PropertyValueFactory<Sale, Long>("SPitemprice"));
		itemCost.setCellValueFactory(new PropertyValueFactory<Sale, Long>("SPsalecost"));
		itemserial.setCellValueFactory(new PropertyValueFactory<Sale, Long>("SPitemserial"));

		UpdateTable();

		dateF.setText(this.invoise.getSPJalaliDateString());
		timeF.setText(this.invoise.getTime().toString());

		Customer customer = new Customer();
		customer = customerDao.SelectById(this.invoise.getCustomerid());
		customernameF.setText(customer.getName() + " " + customer.getFamily());
		totalcostF.setText(Long.toString(this.invoise.getTotalcost()));
		Long temp = (long) 0;
		for (int x = 0; x < sales.size(); x++)
			temp += sales.get(x).getSalecost();
		pointF.setText(Long.toString(temp / 100000));

	}

	public ObservableList<Sale> GetAllSales() throws ClassNotFoundException, SQLException {

		ResultSet irs = itemDao.GetAllItems();

		while (irs.next()) {

			Item item = new Item();
			item.setId(irs.getInt("id"));
			item.setName(irs.getString("name"));
			item.setSerial(irs.getLong("serial"));

			items.add(item);
		}

		ResultSet rs = saleDao.GetAllByInvoiseId(this.invoise.getInvoiseid());
		int i = 1;

		while (rs.next()) {
			Sale sale = new Sale();
			sale.setSaleid(rs.getInt("id"));
			sale.setItemid(rs.getInt("itemid"));
			sale.setItemvalue(rs.getDouble("value"));
			sale.setRetailORwholesale(rs.getString("retailorwholesale"));
			sale.setItemprice(rs.getLong("price"));
			sale.setSalecost(rs.getLong("cost"));

			for (int x = 0; x < items.size(); x++)
				if (items.get(x).getId() == sale.getItemid()) {
					Item item = items.get(x);
					sale.setItemname(item.getName());
					sale.setItemserial(item.getSerial());
					sale.setItemgroupid(item.getGroupid());
					sale.setItemgroupname(item.getGroupname());
					break;
				}
			sale.setSPrownumber(i++);

			sales.add(sale);
		}

		return sales;

	}

	public void UpdateTable() throws ClassNotFoundException, SQLException {
		items.clear();
		sales.clear();
		GetAllSales();
		SaleTable.setItems(sales);
	}

}
