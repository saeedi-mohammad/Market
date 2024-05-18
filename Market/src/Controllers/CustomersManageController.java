package Controllers;

import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Commons.Customer;
import Dao.CustomerDao;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class CustomersManageController {

	ObservableList<Customer> customers = FXCollections.observableArrayList();
	ObservableList<Customer> filterdcustomers = FXCollections.observableArrayList();

	CustomerDao customerDao = new CustomerDao();

	@FXML
	private TextField searchfield;

	@FXML
	private Button BackButton;

	@FXML
	private TableView<Customer> CustomerTable;

	@FXML
	private Button DeleteCustomerButton;

	@FXML
	private Button EditCustomerButton;

	@FXML
	private TableColumn<Customer, Date> LastPurchaseDate;

	@FXML
	private Button NewCustomerButton;

	@FXML
	private TableColumn<Customer, Integer> Point;

	@FXML
	private TableColumn<Customer, String> family;

	@FXML
	private TableColumn<Customer, Integer> id;

	@FXML
	private TableColumn<Customer, String> name;

	@FXML
	private TableColumn<Customer, String> mobile;

	@FXML
	private TableColumn<Customer, Integer> row;

	@FXML
	void Back(ActionEvent event) {

		Stage currentstage = (Stage) BackButton.getScene().getWindow();
		currentstage.close();
	}

	@FXML
	void CreateNewCustomer(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {

		FXMLLoader fxmlLoader = new FXMLLoader();
		Parent root = fxmlLoader.load(getClass().getResource("/View/NewCustomer.fxml").openStream());

		NewCustomerController ncc = (NewCustomerController) fxmlLoader.getController();
		ncc.OnStart(this);

		Stage stage = new Stage();
		Scene scene = new Scene(root);
		stage.setTitle("  افزودن مشتری جدید");

		stage.setScene(scene);
		stage.show();
	}

	@FXML
	void DeleteCustomer(ActionEvent event) throws ClassNotFoundException, SQLException {

		if (CustomerTable.getSelectionModel().getSelectedItems().isEmpty()) {

			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("خطا");
			alert.setHeaderText(null);
			alert.setContentText("ابتدا مشتری مورد نظر را انتخاب کنید.");
			alert.show();
		}

		else {
			customerDao.DeleteCustomer(CustomerTable.getSelectionModel().getSelectedItem());

			UpdateTable();

			Alert a = new Alert(AlertType.INFORMATION);
			a.setTitle("مشتری حذف شده");
			a.setHeaderText(null);
			a.setContentText("مشتری مورد نظر با موفقیت حذف شد.");
			a.show();
		}
	}

	@FXML
	void EditCustomer(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {

		if (CustomerTable.getSelectionModel().getSelectedItems().isEmpty()) {

			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("خطا");
			alert.setHeaderText(null);
			alert.setContentText("ابتدا مشتری مورد نظر را انتخاب کنید.");
			alert.show();
		}

		else {

			FXMLLoader fxmlLoader = new FXMLLoader();
			Parent root = fxmlLoader.load(getClass().getResource("/View/EditCustomer.fxml").openStream());

			EditCustomerController ecc = (EditCustomerController) fxmlLoader.getController();
			ecc.UpdateFields(this, CustomerTable.getSelectionModel().getSelectedItem().getId());

			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setTitle("  ویرایش مشخصات مشتری");

			stage.setScene(scene);
			stage.show();
		}
	}

	public void OnStart() throws ClassNotFoundException, SQLException {

		row.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("SPrownumber"));
		id.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("SPid"));
		Point.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("SPpoint"));
		name.setCellValueFactory(new PropertyValueFactory<Customer, String>("SPname"));
		family.setCellValueFactory(new PropertyValueFactory<Customer, String>("SPfamily"));
		mobile.setCellValueFactory(new PropertyValueFactory<Customer, String>("SPmobile"));
		LastPurchaseDate.setCellValueFactory(new PropertyValueFactory<Customer, Date>("SPlastPurchaseJalaliDate"));

		searchfield.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				updateFilteredData();
			}
		});

		UpdateTable();

		customers.addListener(new ListChangeListener<Customer>() {

			@Override
			public void onChanged(Change<? extends Customer> arg0) {

			}

		});
	}

	private void updateFilteredData() {
		filterdcustomers.clear();

		for (Customer p : customers) {
			if (matchesFilter(p)) {
				filterdcustomers.add(p);
			}
		}

		// Must re-sort table after items changed
		reapplyTableSortOrder();
	}

	private boolean matchesFilter(Customer customer) {
		String filterString = searchfield.getText();
		if (filterString == null || filterString.isEmpty()) {
			// No filter --> Add all.
			return true;
		}

		String lowerCaseFilterString = filterString.toLowerCase();

		if (customer.getName().toLowerCase().indexOf(lowerCaseFilterString) != -1)
			return true;
		else if (customer.getFamily().toLowerCase().indexOf(lowerCaseFilterString) != -1)
			return true;
		else if (customer.getMobile().toLowerCase().indexOf(lowerCaseFilterString) != -1)
			return true;

		return false; // Does not match
	}

	private void reapplyTableSortOrder() {
		ArrayList<TableColumn<Customer, ?>> sortOrder = new ArrayList<>(CustomerTable.getSortOrder());
		CustomerTable.getSortOrder().clear();
		CustomerTable.getSortOrder().addAll(sortOrder);
	}

	public void UpdateTable() throws ClassNotFoundException, SQLException {

		customers.clear();
		GetAllCustomers();
		filterdcustomers = FXCollections.observableArrayList(customers);
		CustomerTable.setItems(filterdcustomers);
	}

	public ObservableList<Customer> GetAllCustomers() throws ClassNotFoundException, SQLException {

		ResultSet rs = customerDao.getAllCustomers();
		int i = 1;

		while (rs.next()) {
			Customer temp = new Customer();
			temp.setId(rs.getInt("id"));
			temp.setName(rs.getString("name"));
			temp.setFamily(rs.getString("family"));
			temp.setMobile(rs.getString("mobile"));
			temp.setPoint(rs.getInt("point"));
			temp.setLastPurchaseDate(rs.getDate("lastpurchasedate"));
			temp.setSPrownumber(i++);

			customers.add(temp);
		}
		return customers;
	}
}
