package Controllers;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import Commons.Group;
import Commons.Item;
import Commons.Purchase;
import Dao.GroupDao;
import Dao.ItemDao;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class NewPurchaseController {

	ObservableList<Group> groups = FXCollections.observableArrayList();

	private ObservableList<Item> items = FXCollections.observableArrayList();
	private ObservableList<Item> Updateditems = FXCollections.observableArrayList();
	private ObservableList<Purchase> Pitems = FXCollections.observableArrayList();
	private Item tempupdateditem = new Item();
	private Item tempitem = new Item();
	private Purchase temppurchase = new Purchase();

	private ItemDao itemDao = new ItemDao();
	private GroupDao groupDao = new GroupDao();

	@FXML
	private Button AddToListButton;

	@FXML
	private Button BackButton;

	@FXML
	private TextField CodeOrSerialField;

	@FXML
	private Button DeleteItemFromListButton;

	@FXML
	private Button EditItemFromListButton;

	@FXML
	private GridPane FieldsGridePane;

	@FXML
	private Button ItemsListButton;

	@FXML
	private Button NewItemButton;

	@FXML
	private TextField NewPurchaseValue;

	@FXML
	private Button SearchButton;

	@FXML
	private ComboBox<String> SearchComboBox;

	@FXML
	private Button ShowInvoiseButton;

	@FXML
	private TextField groupNameT;

	@FXML
	private TextField idT;

	@FXML
	private TableView<Purchase> purchaseListTable;

	@FXML
	private TableColumn<Purchase, Long> itemCost;

	@FXML
	private TableColumn<Purchase, Integer> itemid;

	@FXML
	private TableColumn<Purchase, String> itemname;

	@FXML
	private TableColumn<Purchase, Long> itemserial;

	@FXML
	private TableColumn<Purchase, Integer> row;

	@FXML
	private TableColumn<Purchase, Double> purchasevalue;

	@FXML
	private TextField nameT;

	@FXML
	private TextField numberT;

	@FXML
	private TextField purchasepriceT;

	@FXML
	private TextField retailpriceT;

	@FXML
	private TextField purchsaseDetails;

	@FXML
	private TextField serialT;

	@FXML
	private TextField weightT;

	@FXML
	private TextField wholesalepriceT;

	@FXML
	void CreateNewItem(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {

		FXMLLoader fxmlLoader = new FXMLLoader();
		Parent root = fxmlLoader.load(getClass().getResource("/View/NewItem.fxml").openStream());

		NewItemController nic = (NewItemController) fxmlLoader.getController();
		nic.OnStart(this);

		Stage stage = new Stage();
		Scene scene = new Scene(root);
		stage.setTitle("  ایجاد کالا جدید");

		stage.setScene(scene);
		stage.show();
	}

	@FXML
	void DeleteItemFromList(ActionEvent event) {

		if (purchaseListTable.getSelectionModel().getSelectedItems().isEmpty()) {

			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("خطا");
			alert.setHeaderText(null);
			alert.setContentText("ابتدا فیلد مورد نظر را از داخل لیست انتخاب کنید.");
			alert.show();
		}

		else {

			Pitems.remove(purchaseListTable.getSelectionModel().getSelectedItem());
			UpdateList();

		}
	}

	@FXML
	void AddToList(ActionEvent event) throws ClassNotFoundException, SQLException {

		tempupdateditem = new Item(tempitem);

		tempupdateditem.setName(nameT.getText());
		tempupdateditem.setSerial(Long.parseLong(serialT.getText()));
		if (tempupdateditem.getWeight() < 0)
			tempupdateditem.setNumber(tempupdateditem.getNumber() + Integer.parseInt(NewPurchaseValue.getText()));
		else {
			tempupdateditem.setWeight(tempupdateditem.getWeight() + Double.parseDouble(NewPurchaseValue.getText()));
		}
		tempupdateditem.setPurchasePrice(Long.parseLong(purchasepriceT.getText()));
		tempupdateditem.setRetailPrice(Long.parseLong(retailpriceT.getText()));
		tempupdateditem.setWholesalePrice(Long.parseLong(wholesalepriceT.getText()));

		Updateditems.add(tempupdateditem);

		temppurchase.setItemid(tempupdateditem.getId());
		temppurchase.setItemname(tempupdateditem.getName());
		temppurchase.setItemserial(tempupdateditem.getSerial());
		temppurchase.setValue(Double.parseDouble(NewPurchaseValue.getText()));
		temppurchase.setCost((long) (temppurchase.getValue() * tempupdateditem.getPurchasePrice()));
		temppurchase.setDetails(purchsaseDetails.getText());

		Pitems.add(temppurchase);

		UpdateList();

		ResetFields();

	}

	@FXML
	void EditItemFromList(ActionEvent event) throws ClassNotFoundException, SQLException {

		if (EditItemFromListButton.getText().equals("ویرایش")) {
			if (purchaseListTable.getSelectionModel().getSelectedItems().isEmpty()) {

				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("خطا");
				alert.setHeaderText(null);
				alert.setContentText("ابتدا فیلد مورد نظر را از داخل لیست انتخاب کنید.");
				alert.show();
			} else

			{

				this.temppurchase = purchaseListTable.getSelectionModel().getSelectedItem();
				NewPurchaseValue.setText(Double.toString(temppurchase.getValue()));
				purchsaseDetails.setText(temppurchase.getDetails());
				for (int x = 0; x < Updateditems.size(); x++)
					if (Updateditems.get(x).getId() == temppurchase.getItemid())
						this.tempitem = new Item(Updateditems.get(x));

				SearchComboBox.getSelectionModel().select("کد کالا");
				CodeOrSerialField.setText(Integer.toString(tempitem.getId()));

				FieldsGridePane.setDisable(false);
				idT.setText(Integer.toString(this.tempitem.getId()));
				nameT.setText(this.tempitem.getName());
				groupNameT.setText(this.tempitem.getGroupname());
				serialT.setText(Long.toString(this.tempitem.getSerial()));
				weightT.setText(Double.toString(this.tempitem.getWeight()));
				numberT.setText(Integer.toString(this.tempitem.getNumber()));
				purchasepriceT.setText(Long.toString(this.tempitem.getPurchasePrice()));
				retailpriceT.setText(Long.toString(this.tempitem.getRetailPrice()));
				wholesalepriceT.setText(Long.toString(this.tempitem.getWholesalePrice()));

				Pitems.remove(purchaseListTable.getSelectionModel().getSelectedItem());
				Updateditems.remove(tempitem);
				UpdateList();

				EditItemFromListButton.setStyle("-fx-background-color: #ff2950");
				EditItemFromListButton.setText("ثبت");

			}
		}

		else {

			AddToList(null);
			EditItemFromListButton.setStyle(null);
			EditItemFromListButton.setText("ویرایش");

		}
	}

	@FXML
	void GoItemsList(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {

		FXMLLoader fxmlLoader = new FXMLLoader();
		Parent root = fxmlLoader.load(getClass().getResource("/View/ItemsList.fxml").openStream());

		ItemsListController ilc = (ItemsListController) fxmlLoader.getController();
		ilc.OnStart(this);

		Stage stage = new Stage();
		Scene scene = new Scene(root);
		stage.setTitle("  لیست کالا ها");

		stage.setScene(scene);
		stage.show();
	}

	@FXML
	void Search(ActionEvent event) {

		tempitem = new Item();

		if (CodeOrSerialField.getText() == null) {
			if (SearchComboBox.getSelectionModel().getSelectedItem().equals("کد کالا")) {

				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("خطا");
				alert.setHeaderText(null);
				alert.setContentText("ابتدا کد کالای مورد نظر را وارد کنید.");
				alert.show();
			}

			else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("خطا");
				alert.setHeaderText(null);
				alert.setContentText("ابتدا سریال کالای مورد نظر را وارد کنید.");
				alert.show();
			}

		}

		else {
			if (SearchComboBox.getSelectionModel().getSelectedItem().equals("کد کالا")) {
				for (int x = 0; x < items.size(); x++)
					if (Integer.parseInt(CodeOrSerialField.getText()) == items.get(x).getId()) {
						this.tempitem = new Item(items.get(x));
					}
			}

			else {
				for (int x = 0; x < items.size(); x++)
					if (Long.parseLong(CodeOrSerialField.getText()) == items.get(x).getSerial())
						this.tempitem = new Item(items.get(x));
			}

			if (tempitem.getName() == null) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("خطا");
				alert.setHeaderText(null);
				alert.setContentText("کالایی با این کد یا سریال یافت نشد.");
				alert.show();
			}

			else {

				FieldsGridePane.setDisable(false);
				idT.setText(Integer.toString(this.tempitem.getId()));
				nameT.setText(this.tempitem.getName());
				groupNameT.setText(this.tempitem.getGroupname());
				serialT.setText(Long.toString(this.tempitem.getSerial()));
				weightT.setText(Double.toString(this.tempitem.getWeight()));
				numberT.setText(Integer.toString(this.tempitem.getNumber()));
				purchasepriceT.setText(Long.toString(this.tempitem.getPurchasePrice()));
				retailpriceT.setText(Long.toString(this.tempitem.getRetailPrice()));
				wholesalepriceT.setText(Long.toString(this.tempitem.getWholesalePrice()));
			}
		}
	}

	@FXML
	void ShowInvoise(ActionEvent event) throws IOException {

		FXMLLoader fxmlLoader = new FXMLLoader();
		Parent root = fxmlLoader.load(getClass().getResource("/View/ShowPurchaseInvoise.fxml").openStream());

		ShowPurchaseInvoiseController spic = (ShowPurchaseInvoiseController) fxmlLoader.getController();
		spic.OnStart(this, this.Pitems, this.Updateditems);

		Stage stage = new Stage();
		Scene scene = new Scene(root);
		stage.setTitle("  فاکتور خرید کالا ها");

		stage.setScene(scene);
		stage.show();

		Stage currentstage = (Stage) BackButton.getScene().getWindow();
		currentstage.close();
	}

	@FXML
	void back(ActionEvent event) {

		Stage currentstage = (Stage) BackButton.getScene().getWindow();
		currentstage.close();
	}

	public void UpdateIdOrSerialField(Item item) {

		SearchComboBox.getSelectionModel().select("کد کالا");
		CodeOrSerialField.setText(Integer.toString(item.getId()));
		Search(null);
	}

	public void OnStart() throws ClassNotFoundException, SQLException {

		row.setCellValueFactory(new PropertyValueFactory<Purchase, Integer>("SPrownumber"));
		purchasevalue.setCellValueFactory(new PropertyValueFactory<Purchase, Double>("SPvalue"));
		itemname.setCellValueFactory(new PropertyValueFactory<Purchase, String>("SPitemname"));
		itemid.setCellValueFactory(new PropertyValueFactory<Purchase, Integer>("SPitemid"));
		itemCost.setCellValueFactory(new PropertyValueFactory<Purchase, Long>("SPcost"));
		itemserial.setCellValueFactory(new PropertyValueFactory<Purchase, Long>("SPitemserial"));

		SearchComboBox.getItems().add("کد کالا");
		SearchComboBox.getItems().add("سریال کالا");
		NewPurchaseValue.setText("0");

		GetAllItems();
		LimitFields();

	}

	public List<Item> GetAllItems() throws ClassNotFoundException, SQLException {

		ObservableList<Group> groups = FXCollections.observableArrayList();
		ResultSet grs = groupDao.getAllGroups();

		while (grs.next()) {

			Group group = new Group();
			group.setId(grs.getInt("id"));
			group.setName(grs.getString("name"));
			groups.add(group);
		}

		ResultSet rs = itemDao.GetAllItems();
		int i = 1;

		while (rs.next()) {
			Item temp = new Item();
			temp.setId(rs.getInt("id"));
			temp.setName(rs.getString("name"));
			temp.setSerial(rs.getLong("serial"));
			temp.setWeight(rs.getDouble("weight"));
			temp.setNumber(rs.getInt("number"));
			temp.setPurchasePrice(rs.getLong("purchaseprice"));
			temp.setRetailPrice(rs.getLong("retailprice"));
			temp.setWholesalePrice(rs.getLong("wholesaleprice"));
			temp.setGroupid(rs.getInt("groupid"));
			int x;
			for (x = 0; x < groups.size(); x++)
				if (temp.getGroupid() == groups.get(x).getId())
					break;
			temp.setGroupname(groups.get(x).getName());
			temp.setSPrownumber(i++);

			items.add(temp);

		}
		return items;

	}

	public void UpdateList() {

		for (int x = 0; x < Pitems.size(); x++) {
			Pitems.get(x).setSPrownumber(x + 1);
		}

		purchaseListTable.getItems().setAll(Pitems);

	}

	public void ResetFields() throws ClassNotFoundException, SQLException {

		FieldsGridePane.setDisable(true);
		CodeOrSerialField.setText(null);
		idT.setText(null);
		nameT.setText(null);
		groupNameT.setText(null);
		serialT.setText(null);
		weightT.setText(null);
		numberT.setText(null);
		purchasepriceT.setText(null);
		retailpriceT.setText(null);
		wholesalepriceT.setText(null);
		NewPurchaseValue.setText("0");
		purchsaseDetails.setText(null);

		tempitem = new Item();
		temppurchase = new Purchase();
		tempupdateditem = new Item();

//		GetAllItems();
	}

	public void OnReBackFromInvoise(ObservableList<Item> UpdatedItems, ObservableList<Purchase> Pitems)
			throws ClassNotFoundException, SQLException {

		this.Updateditems = FXCollections.observableArrayList(UpdatedItems);
		this.Pitems = FXCollections.observableArrayList(Pitems);

		OnStart();
		UpdateList();
		LimitFields();

	}

	public void UpdateItems() throws ClassNotFoundException, SQLException {

		items.clear();
		GetAllItems();
	}

	public void LimitFields() {

		CodeOrSerialField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					CodeOrSerialField.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});

		NewPurchaseValue.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					NewPurchaseValue.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});

		purchasepriceT.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					purchasepriceT.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});

		retailpriceT.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					retailpriceT.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});

		wholesalepriceT.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					wholesalepriceT.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});

		serialT.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					serialT.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});

		weightT.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					weightT.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});

		numberT.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					numberT.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});
	}

}
