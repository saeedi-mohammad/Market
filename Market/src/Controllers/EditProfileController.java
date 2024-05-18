package Controllers;

import java.sql.SQLException;

import Commons.Operator;
import Dao.OperatorDao;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditProfileController {

	private Operator operator = new Operator();

	OperatorDao operatorDao = new OperatorDao();

	@FXML
	private TextField mellicode;

	@FXML
	private Button SaveChanges;

	@FXML
	private Button BackButton;

	@FXML
	private TextField firstname;

	@FXML
	private TextField lastname;

	@FXML
	private PasswordField passwordfield;

	@FXML
	private TextField phone;

	@FXML
	private TextField usernamefield;

	@FXML
	void EditInformation(ActionEvent event) throws ClassNotFoundException, SQLException {

		operator.setMobile(phone.getText());
		operator.setUsername(usernamefield.getText());
		operator.setPassword(passwordfield.getText());

		operatorDao.UpdateProfile(this.operator);

		Alert a = new Alert(AlertType.INFORMATION);
		a.setTitle("ویرایش اطلاعات");
		a.setHeaderText(null);
		a.setContentText("ویرایش اطلاعات با موفقیت انجام شد.");
		a.show();

		Stage currentstage = (Stage) BackButton.getScene().getWindow();
		currentstage.close();

	}

	@FXML
	void back(ActionEvent event) {

		Stage currentstage = (Stage) BackButton.getScene().getWindow();
		currentstage.close();

	}

	public void SetOperatorinfo(int id) throws ClassNotFoundException, SQLException {

		this.operator = operatorDao.SelectById(id);

	}

	public void UpdateFields() {

		firstname.setText(this.operator.getName());
		lastname.setText(this.operator.getFamily());
		mellicode.setText(this.operator.getMellicode().toString());
		phone.setText(this.operator.getMobile());
		usernamefield.setText(this.operator.getUsername());
		passwordfield.setText(this.operator.getPassword());
	}

	public void OnStart() {
		Stage currentstage = (Stage) BackButton.getScene().getWindow();
		currentstage.setResizable(false);

		phone.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					phone.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});
	}

}
