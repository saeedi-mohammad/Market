package Controllers;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Commons.Group;
import Commons.Item;
import Commons.Sale;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class NewSaleController {

	private ObservableList<Item> items = FXCollections.observableArrayList();
	private ObservableList<Item> Updateditems = FXCollections.observableArrayList();

	private ObservableList<Item> filterditems = FXCollections.observableArrayList();
	private ObservableList<Sale> Pitems = FXCollections.observableArrayList();

	private ItemDao itemDao = new ItemDao();
	private GroupDao groupDao = new GroupDao();

	@FXML
	private TextField searchfield;

	@FXML
	private Button AddToListButton;

	@FXML
	private Button DeleteFromListButton;

	@FXML
	private Button ShowInvoiseButton;

	@FXML
	private Button BackButton;

	@FXML
	private ToggleGroup SaleMode;

	@FXML
	private RadioButton singleRadioButton;

	@FXML
	private RadioButton MultipleRadioButton;

	@FXML
	private TextField ValueField;

	@FXML
	private TableView<Item> ItemsTable;

	@FXML
	private TableColumn<Item, String> itemgroup;

	@FXML
	private TableColumn<Item, Double> iteminventory;

	@FXML
	private TableColumn<Item, String> itemname;

	@FXML
	private TableColumn<Item, Long> itemretailprice;

	@FXML
	private TableColumn<Item, Integer> itemrow;

	@FXML
	private TableColumn<Item, Long> itemserial;

	@FXML
	private TableColumn<Item, Long> itemwholesaleprice;

	@FXML
	private TableView<Sale> SaleListTable;

	@FXML
	private TableColumn<Sale, Long> PitemPrice;

	@FXML
	private TableColumn<Sale, String> PitemRorW;

	@FXML
	private TableColumn<Sale, String> Pitemname;

	@FXML
	private TableColumn<Sale, Long> PitemCost;

	@FXML
	private TableColumn<Sale, Integer> Pitemrow;

	@FXML
	private TableColumn<Sale, Long> Pitemserial;

	@FXML
	private TableColumn<Sale, Double> Pitemvalue;

	@FXML
	void AddToList(ActionEvent event) throws ClassNotFoundException, SQLException {

		//////////////////////// write if checks

		if (ItemsTable.getSelectionModel().getSelectedItems().isEmpty()) {

			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("خطا");
			alert.setHeaderText(null);
			alert.setContentText("ابتدا کالای مورد نظر را انتخاب کنید.");
			alert.show();
		}

		else {
			Item item = ItemsTable.getSelectionModel().getSelectedItem();
			if (item.getSPinventory() < Double.parseDouble(ValueField.getText())) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("خطا");
				alert.setHeaderText(null);
				alert.setContentText("موجودی کالا کافی نمیباشد.");
				alert.show();
			}

			else {

				Sale sale = new Sale();

				sale.setItemid(item.getId());
				sale.setItemname(item.getName());
				sale.setItemserial(item.getSerial());
				sale.setItemgroupid(item.getGroupid());
				sale.setItemgroupname(item.getGroupname());
				sale.setItemvalue(Double.parseDouble(ValueField.getText()));

				if (singleRadioButton.isSelected()) {
					sale.setRetailORwholesale("خرده");
					sale.setItemprice(item.getRetailPrice());

				}

				else if (MultipleRadioButton.isSelected()) {
					sale.setRetailORwholesale("عمده");
					sale.setItemprice(item.getWholesalePrice());
				}

				sale.setSalecost((long) (sale.getItemvalue() * sale.getItemprice()));

				Pitems.add(sale);

				UpdateList();
				if (item.getNumber() == -1)
					item.setWeight(item.getWeight() - Double.parseDouble(ValueField.getText()));
				else
					item.setNumber(item.getNumber() - Integer.parseInt(ValueField.getText()));

				boolean flag = false;
				for (int x = 0; x < Updateditems.size(); x++)
					if (Updateditems.get(x).getId() == item.getId()) {
						Item tempitem = Updateditems.get(x);
						if (item.getNumber() == -1)
							tempitem.setWeight(tempitem.getWeight() - sale.getItemvalue());
						else
							tempitem.setNumber(tempitem.getNumber() - (int) sale.getItemvalue());
						flag = true;
						break;
					}
				if (!flag)
					Updateditems.add(item);

				ValueField.setText("1.0");
				ItemsTable.getItems().clear();
				ItemsTable.getItems().addAll(items);

			}
		}
	}

	@FXML
	void DeleteFromList(ActionEvent event) throws ClassNotFoundException, SQLException {

		if (SaleListTable.getSelectionModel().getSelectedItems().isEmpty()) {

			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("خطا");
			alert.setHeaderText(null);
			alert.setContentText("ابتدا کالای مورد نظر را انتخاب کنید.");
			alert.show();
		}

		else {

			Sale sale = SaleListTable.getSelectionModel().getSelectedItem();
			for (int x = 0; x < items.size(); x++) {

				if (items.get(x).getId() == sale.getItemid()) {
					Item item = items.get(x);
					if (item.getNumber() == -1)
						item.setWeight(item.getWeight() + sale.getItemvalue());
					else
						item.setNumber(item.getNumber() + (int) sale.getItemvalue());
				}
			}
			Pitems.remove(SaleListTable.getSelectionModel().getSelectedItem());

			ItemsTable.getItems().clear();
			ItemsTable.getItems().addAll(items);
			UpdateList();
		}
	}

	@FXML
	void ShowInvoise(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {

		FXMLLoader fxmlLoader = new FXMLLoader();
		Parent root = fxmlLoader.load(getClass().getResource("/View/ShowSaleInvoise.fxml").openStream());

		ShowSaleInvoiseController sic = (ShowSaleInvoiseController) fxmlLoader.getController();
		sic.OnStart(Pitems, Updateditems);

		Stage stage = new Stage();
		Scene scene = new Scene(root);
		stage.setTitle("  فاکتور نهایی");

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

	public void OnStart() throws ClassNotFoundException, SQLException {

		itemrow.setCellValueFactory(new PropertyValueFactory<Item, Integer>("SPrownumber"));
		iteminventory.setCellValueFactory(new PropertyValueFactory<Item, Double>("SPinventory"));
		itemname.setCellValueFactory(new PropertyValueFactory<Item, String>("SPname"));
		itemgroup.setCellValueFactory(new PropertyValueFactory<Item, String>("SPgroupname"));
		itemretailprice.setCellValueFactory(new PropertyValueFactory<Item, Long>("SPRetailPrice"));
		itemwholesaleprice.setCellValueFactory(new PropertyValueFactory<Item, Long>("SPWholesalePrice"));
		itemserial.setCellValueFactory(new PropertyValueFactory<Item, Long>("SPserial"));

		Pitemrow.setCellValueFactory(new PropertyValueFactory<Sale, Integer>("SPrownumber"));
		Pitemvalue.setCellValueFactory(new PropertyValueFactory<Sale, Double>("SPitemvalue"));
		Pitemname.setCellValueFactory(new PropertyValueFactory<Sale, String>("SPitemname"));
		PitemRorW.setCellValueFactory(new PropertyValueFactory<Sale, String>("SPretailORwholesale"));
		PitemPrice.setCellValueFactory(new PropertyValueFactory<Sale, Long>("SPitemprice"));
		PitemCost.setCellValueFactory(new PropertyValueFactory<Sale, Long>("SPsalecost"));
		Pitemserial.setCellValueFactory(new PropertyValueFactory<Sale, Long>("SPitemserial"));

		ValueField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					ValueField.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});

		searchfield.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				updateFilteredData();
			}
		});

		UpdateItemTable();
		ValueField.setText("1.0");

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
		ArrayList<TableColumn<Item, ?>> sortOrder = new ArrayList<>(ItemsTable.getSortOrder());
		ItemsTable.getSortOrder().clear();
		ItemsTable.getSortOrder().addAll(sortOrder);
	}

	public void UpdateItemTable() throws ClassNotFoundException, SQLException {

		items.clear();

		GetAllItems();
		filterditems = FXCollections.observableArrayList(items);
		ItemsTable.setItems(filterditems);

	}

	public void UpdateList() {

		for (int x = 0; x < Pitems.size(); x++) {
			Pitems.get(x).setSPrownumber(x + 1);
		}

		SaleListTable.getItems().setAll(Pitems);

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

	public void ReBack(ObservableList<Sale> Pitems) throws ClassNotFoundException, SQLException {

		this.Pitems = Pitems;

		OnStart();
		UpdateList();
	}
}
