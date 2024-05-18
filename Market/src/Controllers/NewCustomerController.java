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

public class NewCustomerController {

	CustomerDao customerDao = new CustomerDao();

	private CustomersManageController cmc;

	@FXML
	private TextField CustomerFamily;

	@FXML
	private TextField CustomerMobile;

	@FXML
	private TextField Customername;

	@FXML
	private Button backButton;

	@FXML
	private Button createButton;

	@FXML
	void AddNewCustomer(ActionEvent event) throws ClassNotFoundException, SQLException {

		Customer customer = new Customer();
		customer.setName(Customername.getText());
		customer.setFamily(CustomerFamily.getText());
		customer.setMobile(CustomerMobile.getText());
		customer.setPoint(0);

		customerDao.AddNewCustomer(customer);

		cmc.UpdateTable();

		Stage currentstage = (Stage) backButton.getScene().getWindow();
		currentstage.close();

		Alert a = new Alert(AlertType.INFORMATION);
		a.setTitle("مشتری ایجاد شده");
		a.setHeaderText(null);
		a.setContentText("مشتری مورد نظر با موفقیت ثبت شد.");
		a.show();
	}

	@FXML
	void back(ActionEvent event) {

		Stage currentstage = (Stage) backButton.getScene().getWindow();
		currentstage.close();
	}

	public void OnStart(CustomersManageController cmc) {

		this.cmc = cmc;

		CustomerMobile.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					CustomerMobile.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});
	}
}
