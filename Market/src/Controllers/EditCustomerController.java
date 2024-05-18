package Controllers;

import java.sql.SQLException;

import Commons.Customer;
import Dao.CustomerDao;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditCustomerController {

	private CustomerDao customerDao = new CustomerDao();
	private Customer customer = new Customer();

	private CustomersManageController cmc;

	@FXML
	private Button BackButton;

	@FXML
	private Button SaveButton;

	@FXML
	private TextField familyfield;

	@FXML
	private TextField idfield;

	@FXML
	private TextField lastpurchasedatefield;

	@FXML
	private TextField mobilefield;

	@FXML
	private TextField namefield;

	@FXML
	private TextField pointfield;

	@FXML
	void Back(ActionEvent event) {

		Stage currentstage = (Stage) BackButton.getScene().getWindow();
		currentstage.close();
	}

	@FXML
	void SaveChanges(ActionEvent event) throws ClassNotFoundException, SQLException {

		Customer customer = new Customer();

		customer.setId(Integer.parseInt(idfield.getText()));
		customer.setName(namefield.getText());
		customer.setFamily(familyfield.getText());
		customer.setPoint(Integer.parseInt(pointfield.getText()));
		customer.setMobile(mobilefield.getText());
		customer.setLastPurchaseDate(this.customer.getlastPurchaseDate());

		customerDao.UpdateCustomer(customer);

		cmc.UpdateTable();

		Stage currentstage = (Stage) BackButton.getScene().getWindow();
		currentstage.close();

		Alert a = new Alert(AlertType.INFORMATION);
		a.setTitle("مشخصات تغییر داده شده");
		a.setHeaderText(null);
		a.setContentText("اطلاعات مشتری مورد نظر به روز رسانی شد.");
		a.show();

	}

	public void UpdateFields(CustomersManageController cmc, int id) throws ClassNotFoundException, SQLException {

		this.cmc = cmc;

		this.customer = customerDao.SelectById(id);

		this.idfield.setText(Integer.toString(customer.getId()));
		this.namefield.setText(customer.getName());
		this.familyfield.setText(customer.getFamily());
		this.mobilefield.setText(customer.getMobile());
		this.pointfield.setText(Integer.toString(customer.getPoint()));
		if (customer.getlastPurchaseDate() == null)
			this.lastpurchasedatefield.setText(null);
		else
			this.lastpurchasedatefield.setText(customer.getSPlastPurchaseJalaliDateString());
		lastpurchasedatefield.setDisable(true);

		mobilefield.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					mobilefield.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});

		pointfield.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					pointfield.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});

	}
}
