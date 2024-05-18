package Controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Commons.Invoise;
import Commons.Item;
import Commons.Purchase;
import Dao.InvoiseDao;
import Dao.ItemDao;
import Dao.PurchaseDao;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ShowPurchaseInvoiseController {

	private ObservableList<Purchase> Pitems = FXCollections.observableArrayList();
	private ObservableList<Item> UpdatedItems = FXCollections.observableArrayList();
	private NewPurchaseController npc;

	private ItemDao itemDoa = new ItemDao();
	private PurchaseDao purchaseDao = new PurchaseDao();
	private InvoiseDao invoiseDao = new InvoiseDao();

	@FXML
	private Button BackButton;

	@FXML
	private TableView<Purchase> ListTable;

	@FXML
	private Button PrintInvoiseButton;

	@FXML
	private Button SetInvoiseButton;

	@FXML
	private Label TotalCost;

	@FXML
	private TableColumn<Purchase, Long> itemcost;

	@FXML
	private TableColumn<Purchase, String> itemname;

	@FXML
	private TableColumn<Purchase, Integer> itemrow;

	@FXML
	private TableColumn<Purchase, Double> itemvalue;

	@FXML
	void Back(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {

		Stage currentstage = (Stage) BackButton.getScene().getWindow();
		currentstage.close();

		FXMLLoader fxmlLoader = new FXMLLoader();
		Parent root = fxmlLoader.load(getClass().getResource("/View/NewPurchase.fxml").openStream());

		NewPurchaseController npc = (NewPurchaseController) fxmlLoader.getController();
		npc.OnReBackFromInvoise(this.UpdatedItems, this.Pitems);

		Stage stage = new Stage();
		Scene scene = new Scene(root);
		stage.setTitle("  ثبت خرید جدید");

		stage.setScene(scene);
		stage.show();

	}

	@FXML
	void PrintInvoise(ActionEvent event) {

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

			for (Purchase purchase : Pitems) {
				Row row = sheet.createRow(rownum++);
				CreateExelList(purchase, row);
			}

			Row rown_1 = sheet.createRow(rownum + 2);
			Cell celln_1 = rown_1.createCell(0);
			celln_1.setCellValue("مبلغ کل");

			Row rown = sheet.createRow(rownum + 3);
			Cell celln = rown.createCell(0);
			celln.setCellValue(TotalCost.getText());

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
		cell.setCellValue(purchase.getCost());
	}

	@FXML
	void SetInvoise(ActionEvent event) throws ClassNotFoundException, SQLException {

		for (int x = 0; x < UpdatedItems.size(); x++)
			itemDoa.UpdateItem(UpdatedItems.get(x));

		Invoise invoise = new Invoise();
		invoise.setSaleOrPurchase("خرید");
		invoise.setTotalcost(Long.parseLong(TotalCost.getText()));
		///////////////////////// change date time to hejri
		java.util.Date utilDate = new java.util.Date();
		invoise.setDate(new java.sql.Date(utilDate.getTime()));
		invoise.setTime(new java.sql.Time(utilDate.getTime()));

		invoise.setInvoiseid(invoiseDao.CreateNewPurchaseInvoise(invoise));

		for (int x = 0; x < Pitems.size(); x++) {
			Pitems.get(x).setInvoiseid(invoise.getInvoiseid());
			purchaseDao.AddNewPurchase(Pitems.get(x));
		}

		Stage currentstage = (Stage) BackButton.getScene().getWindow();
		currentstage.close();

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("تایید");
		alert.setHeaderText(null);
		alert.setContentText("فاکتور با موفقیت ثبت شد.");
		alert.show();
	}

	public void OnStart(NewPurchaseController npc, ObservableList<Purchase> pitems, ObservableList<Item> updateditems) {

		this.Pitems = pitems;
		this.UpdatedItems = updateditems;
		this.npc = npc;

		itemrow.setCellValueFactory(new PropertyValueFactory<Purchase, Integer>("SPrownumber"));
		itemvalue.setCellValueFactory(new PropertyValueFactory<Purchase, Double>("SPvalue"));
		itemname.setCellValueFactory(new PropertyValueFactory<Purchase, String>("SPitemname"));
		itemcost.setCellValueFactory(new PropertyValueFactory<Purchase, Long>("SPcost"));

		ListTable.setItems(Pitems);

		Long temp = (long) 0;
		for (int x = 0; x < Pitems.size(); x++)
			temp += Pitems.get(x).getCost();
		TotalCost.setText(Long.toString(temp));
	}

}
