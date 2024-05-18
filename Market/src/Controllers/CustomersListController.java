package Controllers;

import java.io.IOException;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class CustomersListController {

	Customer customer = new Customer();

	ObservableList<Customer> customers = FXCollections.observableArrayList();
	ObservableList<Customer> filterdcustomers = FXCollections.observableArrayList();

	CustomerDao customerDao = new CustomerDao();

	private ShowSaleInvoiseController ssic;

	@FXML
	private Button BackButton;

	@FXML
	private TextField searchfield;

	@FXML
	private Button ChooseButton;

	@FXML
	private TableView<Customer> CustomersListTable;

	@FXML
	private TableColumn<Customer, String> family;

	@FXML
	private TableColumn<Customer, Integer> id;

	@FXML
	private TableColumn<Customer, String> mobile;

	@FXML
	private TableColumn<Customer, String> name;

	@FXML
	private TableColumn<Customer, Integer> point;

	@FXML
	private TableColumn<Customer, Integer> row;

	@FXML
	void Back(ActionEvent event) {

		Stage currentstage = (Stage) BackButton.getScene().getWindow();
		currentstage.close();
	}

	@FXML
	void ChooseCustomer(ActionEvent event) throws InterruptedException, IOException {

		if (CustomersListTable.getSelectionModel().getSelectedItems().isEmpty()) {

			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("خطا");
			alert.setHeaderText(null);
			alert.setContentText("ابتدا مشتری مورد نظر را انتخاب کنید.");
			alert.show();
		}

		else {

			this.customer = CustomersListTable.getSelectionModel().getSelectedItem();

			this.ssic.UpdateCustomerIdFromCustomersListController(customer);

			Stage currentstage = (Stage) BackButton.getScene().getWindow();
			currentstage.close();

		}
	}

	public void OnStart(ShowSaleInvoiseController ssic) throws ClassNotFoundException, SQLException {

		this.ssic = ssic;

		row.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("SPrownumber"));
		id.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("SPid"));
		point.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("SPpoint"));
		name.setCellValueFactory(new PropertyValueFactory<Customer, String>("SPname"));
		family.setCellValueFactory(new PropertyValueFactory<Customer, String>("SPfamily"));
		mobile.setCellValueFactory(new PropertyValueFactory<Customer, String>("SPmobile"));

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
		ArrayList<TableColumn<Customer, ?>> sortOrder = new ArrayList<>(CustomersListTable.getSortOrder());
		CustomersListTable.getSortOrder().clear();
		CustomersListTable.getSortOrder().addAll(sortOrder);
	}

	public void UpdateTable() throws ClassNotFoundException, SQLException {

		customers.clear();
		GetAllCustomers();
		CustomersListTable.setItems(customers);
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
