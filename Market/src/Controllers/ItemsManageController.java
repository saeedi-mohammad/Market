package Controllers;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import Commons.Group;
import Commons.Item;
import Dao.GroupDao;
import Dao.ItemDao;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ItemsManageController {

	private ObservableList<Item> items = FXCollections.observableArrayList();
	private ObservableList<Item> filterditems = FXCollections.observableArrayList();

	private ItemDao itemDao = new ItemDao();
	private GroupDao groupDao = new GroupDao();

	@FXML
	private Button BackButton;

	@FXML
	private TextField searchfield;

	@FXML
	private Button DeleteItemButton;

	@FXML
	private Button EditItemButton;

	@FXML
	private TableView<Item> ItemTable;

	@FXML
	private Button NewItemButton;

	@FXML
	private TableColumn<Item, Long> PurchasePrice;

	@FXML
	private TableColumn<Item, Long> RetailPrice;

	@FXML
	private TableColumn<Item, Long> WholesalePrice;

	@FXML
	private TableColumn<Item, String> groupname;

	@FXML
	private TableColumn<Item, Integer> id;

	@FXML
	private TableColumn<Item, String> name;

	@FXML
	private TableColumn<Item, Integer> number;

	@FXML
	private TableColumn<Item, Integer> row;

	@FXML
	private TableColumn<Item, Long> serial;

	@FXML
	private TableColumn<Item, Double> weight;

	@FXML
	void CreateNewItem(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {

		FXMLLoader fxmlLoader = new FXMLLoader();
		Parent root = fxmlLoader.load(getClass().getResource("/View/NewItem.fxml").openStream());

		NewItemController nic = (NewItemController) fxmlLoader.getController();
		nic.OnStart(this);

		Stage stage = new Stage();
		Scene scene = new Scene(root);
		stage.setTitle("  افزودن کالا جدید");

		stage.setScene(scene);
		stage.show();
	}

	@FXML
	void EditItem(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {

		if (ItemTable.getSelectionModel().getSelectedItems().isEmpty()) {

			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("خطا");
			alert.setHeaderText(null);
			alert.setContentText("ابتدا کالای مورد نظر را انتخاب کنید.");
			alert.show();
		}

		else {

			FXMLLoader fxmlLoader = new FXMLLoader();
			Parent root = fxmlLoader.load(getClass().getResource("/View/EditItem.fxml").openStream());

			EditItemController eic = (EditItemController) fxmlLoader.getController();
			eic.UpdateFields(this, ItemTable.getSelectionModel().getSelectedItem().getId());

			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setTitle("  ویرایش مشخصات کالا");

			stage.setScene(scene);
			stage.show();

		}

	}

	@FXML
	void DeleteItem(ActionEvent event) throws ClassNotFoundException, SQLException {

		if (ItemTable.getSelectionModel().getSelectedItems().isEmpty()) {

			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("خطا");
			alert.setHeaderText(null);
			alert.setContentText("ابتدا کالای مورد نظر را انتخاب کنید.");
			alert.show();
		}

		else {

			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("هشدار");
			alert.setHeaderText(null);
			alert.setContentText(
					"در صورت حذف کالا تمامی تراکنش هایی که در مورد این کالا هستند نیز پاک خواهد شد، آیا مطمعن هستید؟");
			ButtonType byes = new ButtonType("بله");
			ButtonType bno = new ButtonType("خیر");
			alert.getButtonTypes().clear();
			alert.getButtonTypes().add(byes);
			alert.getButtonTypes().add(bno);

			Optional<ButtonType> option = alert.showAndWait();
			if (option.get() == byes) {
				itemDao.DeleteItem(ItemTable.getSelectionModel().getSelectedItem());

				Alert a = new Alert(AlertType.INFORMATION);
				a.setTitle("کالای حذف شده");
				a.setHeaderText(null);
				a.setContentText("کالای مورد نظر با موفقیت حذف شد.");
				a.show();

				UpdateTable();
			}

			else if (option.get() == null || option.get() == bno)
				return;

		}
	}

	@FXML
	void Back(ActionEvent event) {

		Stage currentstage = (Stage) ItemTable.getScene().getWindow();
		currentstage.close();
	}

	public void OnStart() throws ClassNotFoundException, SQLException {

		row.setCellValueFactory(new PropertyValueFactory<Item, Integer>("SPrownumber"));
		id.setCellValueFactory(new PropertyValueFactory<Item, Integer>("SPid"));
		number.setCellValueFactory(new PropertyValueFactory<Item, Integer>("SPnumber"));
		name.setCellValueFactory(new PropertyValueFactory<Item, String>("SPname"));
		groupname.setCellValueFactory(new PropertyValueFactory<Item, String>("SPgroupname"));
		PurchasePrice.setCellValueFactory(new PropertyValueFactory<Item, Long>("SPPurchasePrice"));
		RetailPrice.setCellValueFactory(new PropertyValueFactory<Item, Long>("SPRetailPrice"));
		WholesalePrice.setCellValueFactory(new PropertyValueFactory<Item, Long>("SPWholesalePrice"));
		serial.setCellValueFactory(new PropertyValueFactory<Item, Long>("SPserial"));
		weight.setCellValueFactory(new PropertyValueFactory<Item, Double>("SPweight"));

		searchfield.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				updateFilteredData();
			}
		});

		UpdateTable();

		items.addListener(new ListChangeListener<Item>() {

			@Override
			public void onChanged(Change<? extends Item> arg0) {
			}
		});
	}

	private void updateFilteredData() {
		filterditems.clear();

		for (Item p : items) {
			if (matchesFilter(p)) {
				filterditems.add(p);
			}
		}
		// Must re-sort table after items changed
		reapplyTableSortOrder();
	}

	private boolean matchesFilter(Item item) {
		String filterString = searchfield.getText();
		if (filterString == null || filterString.isEmpty()) {
			// No filter --> Add all.
			return true;
		}

		String lowerCaseFilterString = filterString.toLowerCase();

		if (item.getName().toLowerCase().indexOf(lowerCaseFilterString) != -1)
			return true;

		return false; // Does not match
	}

	private void reapplyTableSortOrder() {

		ArrayList<TableColumn<Item, ?>> sortOrder = new ArrayList<>(ItemTable.getSortOrder());
		ItemTable.getSortOrder().clear();
		ItemTable.getSortOrder().addAll(sortOrder);
	}

	public ObservableList<Item> GetAllItems() throws ClassNotFoundException, SQLException {

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

	public void UpdateTable() throws ClassNotFoundException, SQLException {

		items.clear();
		GetAllItems();
		filterditems = FXCollections.observableArrayList(items);
		ItemTable.setItems(filterditems);
	}

}
