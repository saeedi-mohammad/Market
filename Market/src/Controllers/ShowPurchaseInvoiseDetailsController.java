package Controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Commons.Invoise;
import Commons.Item;
import Commons.Purchase;
import Dao.ItemDao;
import Dao.PurchaseDao;
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

public class ShowPurchaseInvoiseDetailsController {

	private Invoise invoise;

	private ObservableList<Item> items = FXCollections.observableArrayList();
	private ObservableList<Purchase> purchases = FXCollections.observableArrayList();

	private ItemDao itemDao = new ItemDao();
	private PurchaseDao purchaseDao = new PurchaseDao();

	@FXML
	private Button BackButton;

	@FXML
	private Button PrintButton;

	@FXML
	private TableView<Purchase> PurchaseTable;

	@FXML
	private TableColumn<Purchase, Long> cost;

	@FXML
	private Label dateF;

	@FXML
	private TableColumn<Purchase, Integer> invoiseid;

	@FXML
	private TableColumn<Purchase, String> itemname;

	@FXML
	private TableColumn<Purchase, Long> itemserial;

	@FXML
	private TableColumn<Purchase, Integer> purchaseid;

	@FXML
	private TableColumn<Purchase, Integer> row;

	@FXML
	private Label timeF;

	@FXML
	private Label totalcostF;

	@FXML
	private TableColumn<Purchase, Double> value;

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

			for (Purchase purchase : purchases) {
				Row row = sheet.createRow(rownum++);
				CreateExelList(purchase, row);
			}

			Row rown_1 = sheet.createRow(rownum + 2);
			Cell celln_1 = rown_1.createCell(0);
			celln_1.setCellValue("مبلغ کل");

			Row rown = sheet.createRow(rownum + 3);
			Cell celln = rown.createCell(0);
			celln.setCellValue(totalcostF.getText());

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
		cell.setCellValue("تعداد یا مقدار");

		cell = row0.createCell(3);
		cell.setCellValue("کد فاکتور");

		cell = row0.createCell(4);
		cell.setCellValue("سریال کالا");

		cell = row0.createCell(5);
		cell.setCellValue("هزینه کل آیتم");
	}

	private void CreateExelList(Purchase purchase, Row row) // creating cells for each row
	{
		Cell cell = row.createCell(0);
		cell.setCellValue(purchase.getItemid());

		cell = row.createCell(1);
		cell.setCellValue(purchase.getItemname());

		cell = row.createCell(2);
		cell.setCellValue(purchase.getValue());

		cell = row.createCell(3);
		cell.setCellValue(purchase.getInvoiseid());

		cell = row.createCell(4);
		cell.setCellValue(purchase.getItemserial());

		cell = row.createCell(5);
		cell.setCellValue(purchase.getCost());
	}

	public void OnStart(Invoise invoise) throws ClassNotFoundException, SQLException {

		this.invoise = invoise;

		row.setCellValueFactory(new PropertyValueFactory<Purchase, Integer>("SPrownumber"));
		purchaseid.setCellValueFactory(new PropertyValueFactory<Purchase, Integer>("SPpurchaseid"));
		value.setCellValueFactory(new PropertyValueFactory<Purchase, Double>("SPvalue"));
		itemname.setCellValueFactory(new PropertyValueFactory<Purchase, String>("SPitemname"));
		invoiseid.setCellValueFactory(new PropertyValueFactory<Purchase, Integer>("SPinvoiseid"));
		cost.setCellValueFactory(new PropertyValueFactory<Purchase, Long>("SPcost"));
		itemserial.setCellValueFactory(new PropertyValueFactory<Purchase, Long>("SPitemserial"));

		UpdateTable();

		dateF.setText(this.invoise.getSPJalaliDateString());
		timeF.setText(this.invoise.getTime().toString());
		totalcostF.setText(Long.toString(this.invoise.getTotalcost()));

	}

	private void UpdateTable() throws ClassNotFoundException, SQLException {

		items.clear();
		purchases.clear();
		GetAllPurchases();
		PurchaseTable.setItems(purchases);
	}

	private ObservableList<Purchase> GetAllPurchases() throws SQLException, ClassNotFoundException {
		ResultSet irs = itemDao.GetAllItems();

		while (irs.next()) {

			Item item = new Item();
			item.setId(irs.getInt("id"));
			item.setName(irs.getString("name"));
			item.setSerial(irs.getLong("serial"));

			items.add(item);
		}

		ResultSet rs = purchaseDao.GetAllByInvoiseId(this.invoise.getInvoiseid());
		int i = 1;

		while (rs.next()) {
			Purchase purchase = new Purchase();
			purchase.setPurchaseid(rs.getInt("id"));
			purchase.setItemid(rs.getInt("itemid"));
			purchase.setValue(rs.getDouble("value"));
			purchase.setInvoiseid(rs.getInt("invoiseid"));
			purchase.setCost(rs.getLong("cost"));

			for (int x = 0; x < items.size(); x++)
				if (items.get(x).getId() == purchase.getItemid()) {
					Item item = items.get(x);
					purchase.setItemname(item.getName());
					purchase.setItemserial(item.getSerial());
					break;
				}
			purchase.setSPrownumber(i++);

			purchases.add(purchase);
		}

		return purchases;
	}

}
