package Controllers;

import java.io.IOException;
import java.sql.SQLException;

import Commons.Odt;
import Commons.Operator;
import Dao.OdtDao;
import Dao.OperatorDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class HomePageController {

	private Operator operator = new Operator();

	private OperatorDao operatorDao = new OperatorDao();
	private OdtDao odtDao = new OdtDao();

	private Odt odt;

	@FXML
	private MenuItem ExitAll;

	@FXML
	private Label accesslevel;

	@FXML
	private ImageView inOrOutImage;

	@FXML
	private ImageView homeImage;

	@FXML
	private MenuItem InOutButton;

	@FXML
	private Button CustomerManageButton;

	@FXML
	private Button GroupsManageButton;

	@FXML
	private Button ItemManageButton;

	@FXML
	private Button NewItemButton;

	@FXML
	private Button NewPurchaseButton;

	@FXML
	private Button NewSaleButton;

	@FXML
	private MenuItem OperatorEditInfoButton;

	@FXML
	private MenuItem OperatorExitButton;

	@FXML
	private Button OperatorManageButton;

	@FXML
	private MenuButton namefamily;

	@FXML
	private Button ReportsButton;

	@FXML
	private Button OperatorExitButton2;

	@FXML
	private Button TransactionButton;

	@FXML
	private Button notesButton;

	@FXML
	void CreateNewItem(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {

		FXMLLoader fxmlLoader = new FXMLLoader();
		Parent root = fxmlLoader.load(getClass().getResource("/View/NewItem.fxml").openStream());

		NewItemController nic = (NewItemController) fxmlLoader.getController();
		nic.OnStart();

		Stage stage = new Stage();
		Scene scene = new Scene(root);
		stage.setTitle("  افزودن کالا جدید");

		stage.setScene(scene);
		stage.show();

	}

	@FXML
	void CreateNewPurchase(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {

		FXMLLoader fxmlLoader = new FXMLLoader();
		Parent root = fxmlLoader.load(getClass().getResource("/View/NewPurchase.fxml").openStream());

		Stage stage = new Stage();
		stage.setTitle("  ثبت خرید جدید ");
		Scene scene = new Scene(root);

		NewPurchaseController npc = (NewPurchaseController) fxmlLoader.getController();
		npc.OnStart();

		stage.setScene(scene);
		stage.show();
	}

	@FXML
	void CreateNewSale(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {

		FXMLLoader fxmlLoader = new FXMLLoader();
		Parent root = fxmlLoader.load(getClass().getResource("/View/NewSale.fxml").openStream());

		Stage stage = new Stage();
		stage.setTitle("  ثبت فروش جدید ");
		Scene scene = new Scene(root);

		NewSaleController nsc = (NewSaleController) fxmlLoader.getController();
		nsc.OnStart();

		stage.setScene(scene);
		stage.show();
	}

	@FXML
	void EditInformation(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {

		FXMLLoader fxmlLoader = new FXMLLoader();
		Parent root = fxmlLoader.load(getClass().getResource("/View/ProfileEdit.fxml").openStream());

		EditProfileController pec = (EditProfileController) fxmlLoader.getController();
		pec.SetOperatorinfo(this.operator.getOperatorid());
		pec.UpdateFields();

		Stage stage = new Stage();
		Scene scene = new Scene(root);
		stage.setTitle("  مشخصات کاربری");

		stage.setScene(scene);
		stage.show();
	}

	@FXML
	void SetInOut(ActionEvent event) throws ClassNotFoundException, SQLException {

		java.util.Date utilDate = new java.util.Date();

		UpdateOnorOff();

		if (this.odt == null) {
			this.odt = new Odt();
			this.odt.setOperatorid(this.operator.getOperatorid());
			this.odt.setDate(new java.sql.Date(utilDate.getTime()));
			this.odt.setStime(new java.sql.Time(utilDate.getTime()));
			odtDao.AddNewRecourd(this.odt);
			UpdateOnorOff();

			Alert a = new Alert(AlertType.INFORMATION);
			a.setTitle("حضور غیاب");
			a.setHeaderText(null);
			a.setContentText("ورود شما برای امروز ثبت شد.");
			a.show();

		} else if (this.odt.getEtime() != null) {
			this.odt = new Odt();
			this.odt.setOperatorid(this.operator.getOperatorid());
			this.odt.setDate(new java.sql.Date(utilDate.getTime()));
			this.odt.setStime(new java.sql.Time(utilDate.getTime()));
			odtDao.AddNewRecourd(this.odt);
			UpdateOnorOff();

			Alert a = new Alert(AlertType.INFORMATION);
			a.setTitle("حضور غیاب");
			a.setHeaderText(null);
			a.setContentText("ورود مجدد شما برای امروز ثبت شد.");
			a.show();
		}

		else if (this.odt.getStime() != null && this.odt.getEtime() == null) {
			this.odt.setEtime(new java.sql.Time(utilDate.getTime()));
			odtDao.UpdateRecord(this.odt);
			UpdateOnorOff();

			Alert a = new Alert(AlertType.INFORMATION);
			a.setTitle("حضور غیاب");
			a.setHeaderText(null);
			a.setContentText("خروج شما برای امروز ثبت شد.");
			a.show();
		}
	}

	@FXML
	void Exit(ActionEvent event) throws IOException {

		FXMLLoader fxmlLoader = new FXMLLoader();
		Parent root = fxmlLoader.load(getClass().getResource("/View/Login.fxml").openStream());

		Stage stage = new Stage();
		Scene scene = new Scene(root);

		stage.setScene(scene);
		stage.show();

		Stage currentstage = (Stage) CustomerManageButton.getScene().getWindow();
		currentstage.close();
	}

	@FXML
	void ExitFromSoftware(ActionEvent event) {

		Stage currentstage = (Stage) CustomerManageButton.getScene().getWindow();
		currentstage.close();
	}

	@FXML
	void GoCustomerManageForm(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {

		FXMLLoader fxmlLoader = new FXMLLoader();
		Parent root = fxmlLoader.load(getClass().getResource("/View/CustomersManage.fxml").openStream());

		Stage stage = new Stage();
		stage.setTitle("  مدیریت مشتریان ");
		Scene scene = new Scene(root);

		CustomersManageController cmc = (CustomersManageController) fxmlLoader.getController();
		cmc.OnStart();

		stage.setScene(scene);
		stage.show();
	}

	@FXML
	void GoItemManageForm(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {

		FXMLLoader fxmlLoader = new FXMLLoader();
		Parent root = fxmlLoader.load(getClass().getResource("/View/ItemsManage.fxml").openStream());

		Stage stage = new Stage();
		stage.setTitle("  مدیریت کالا ها");
		Scene scene = new Scene(root);

		ItemsManageController imc = (ItemsManageController) fxmlLoader.getController();
		imc.OnStart();

		stage.setScene(scene);
		stage.show();
	}

	@FXML
	void GoOperatorManageForm(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {

		FXMLLoader fxmlLoader = new FXMLLoader();
		Parent root = fxmlLoader.load(getClass().getResource("/View/OperatorManage.fxml").openStream());

		Stage stage = new Stage();
		stage.setTitle("  مدیریت کارکنان");
		Scene scene = new Scene(root);

		OperatorManageController omc = (OperatorManageController) fxmlLoader.getController();
		omc.OnStart(this);

		stage.setScene(scene);
		stage.show();
	}

	@FXML
	void GoGroupsManage(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {

		FXMLLoader fxmlLoader = new FXMLLoader();
		Parent root = fxmlLoader.load(getClass().getResource("/View/GroupsManage.fxml").openStream());

		Stage stage = new Stage();
		stage.setTitle("  مدیریت دسته بندی ها");
		Scene scene = new Scene(root);

		GroupsManageController gmc = (GroupsManageController) fxmlLoader.getController();
		gmc.OnStart();

		stage.setScene(scene);
		stage.show();
	}

	@FXML
	void GoReports(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {

		FXMLLoader fxmlLoader = new FXMLLoader();
		Parent root = fxmlLoader.load(getClass().getResource("/View/ShowReports.fxml").openStream());

		Stage stage = new Stage();
		stage.setTitle(" گزارشات");
		Scene scene = new Scene(root);

		ShowReportsController src = (ShowReportsController) fxmlLoader.getController();
		src.OnStart();

		stage.setScene(scene);
		stage.show();
	}

	@FXML
	void GoNotes(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {

		FXMLLoader fxmlLoader = new FXMLLoader();
		Parent root = fxmlLoader.load(getClass().getResource("/View/NotesManage.fxml").openStream());

		Stage stage = new Stage();
		stage.setTitle("  مدیریت یادداشت ها");
		Scene scene = new Scene(root);

		NotesManageController nmc = (NotesManageController) fxmlLoader.getController();
		nmc.OnStart(this.operator);

		stage.setScene(scene);
		stage.show();
	}

	@FXML
	void GoTransactions(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {

		FXMLLoader fxmlLoader = new FXMLLoader();
		Parent root = fxmlLoader.load(getClass().getResource("/View/Transaction.fxml").openStream());

		Stage stage = new Stage();
		stage.setTitle("  مدیریت تراکنش ها ");
		Scene scene = new Scene(root);

		TransactionController tc = (TransactionController) fxmlLoader.getController();
		tc.OnStart();

		stage.setScene(scene);
		stage.show();
	}

	public void OnStart(Operator operator) throws ClassNotFoundException, SQLException {

		this.operator = operator;

		if (operator.getAccesslevel() == 2) {
			this.OperatorManageButton.setDisable(true);
			this.ItemManageButton.setDisable(true);
			this.GroupsManageButton.setDisable(true);
			this.CustomerManageButton.setDisable(true);
			this.NewSaleButton.setDisable(true);
			this.accesslevel.setText("مسئول خرید");
		} else if (operator.getAccesslevel() == 3) {
			this.OperatorManageButton.setDisable(true);
			this.ItemManageButton.setDisable(true);
			this.GroupsManageButton.setDisable(true);
			this.CustomerManageButton.setDisable(true);
			this.NewItemButton.setDisable(true);
			this.NewPurchaseButton.setDisable(true);
			this.accesslevel.setText("مسئول فروش");
		} else if (operator.getAccesslevel() == 4) {
			this.OperatorManageButton.setDisable(true);
			this.CustomerManageButton.setDisable(true);
			this.NewPurchaseButton.setDisable(true);
			this.NewSaleButton.setDisable(true);
			this.accesslevel.setText("مسئول انبار");
		} else
			this.accesslevel.setText("ادمین سیستم");

		UpdateOnorOff();

		Image image = new Image("images/home2.png");
		homeImage.setImage(image);
		homeImage.setFitWidth(780);
		homeImage.setFitHeight(615);
		homeImage.setPreserveRatio(false);

		this.namefamily.setText(this.operator.getName() + "   " + this.operator.getFamily());

	}

	public void UpdateOnorOff() throws ClassNotFoundException, SQLException {

		this.odt = odtDao.SelectLastByOperatorid(this.operator.getOperatorid());

		Image image = null;
		if (this.odt == null) {
			image = new Image("images/red.png");
		} else if (this.odt.getEtime() != null) {
			image = new Image("images/red.png");
		} else if (this.odt.getStime() != null && this.odt.getEtime() == null) {
			image = new Image("images/green.png");
		}
		inOrOutImage.setImage(image);
		inOrOutImage.setFitHeight(25);
		inOrOutImage.setFitWidth(25);
		inOrOutImage.setPreserveRatio(true);
	}
}
