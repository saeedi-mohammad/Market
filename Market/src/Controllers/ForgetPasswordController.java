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
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ForgetPasswordController {

	private Operator operator = new Operator();
	private OperatorDao operatorDao = new OperatorDao();

	@FXML
	private GridPane passwordgridpane;

	@FXML
	private Button backButton;

	@FXML
	private Button checkButton;

	@FXML
	private PasswordField confirmpassword;

	@FXML
	private TextField mellicodefield;

	@FXML
	private PasswordField passwordfield;

	@FXML
	private Button setButton;

	@FXML
	private TextField usernamefield;

	@FXML
	void Check(ActionEvent event) throws NumberFormatException, ClassNotFoundException, SQLException {

		if (usernamefield.getText() == "" || mellicodefield.getText() == "") {

			Alert a = new Alert(AlertType.ERROR);
			a.setTitle("خطا");
			a.setHeaderText(null);
			a.setContentText("فیلد ها را پر کنید");
			a.show();

			return;
		}

		this.operator = operatorDao.SelectByUsernameMellicode(usernamefield.getText(),
				Long.parseLong(mellicodefield.getText()));

		if (this.operator.getUsername() == null) {

			Alert a = new Alert(AlertType.ERROR);
			a.setTitle("خطا");
			a.setHeaderText(null);
			a.setContentText("کاربر نامعتبر است.");
			a.show();
		}

		else {
			passwordgridpane.setDisable(false);

		}
	}

	@FXML
	void Set(ActionEvent event) throws ClassNotFoundException, SQLException {

		if (passwordgridpane.isDisable()) {
			Alert a = new Alert(AlertType.ERROR);
			a.setTitle("خطا");
			a.setHeaderText(null);
			a.setContentText("ابتدا اهراز هویت را انجام دهید.");
			a.show();
		}

		else if (passwordfield.getText() == null || confirmpassword.getText() == null) {
			Alert a = new Alert(AlertType.ERROR);
			a.setTitle("خطا");
			a.setHeaderText(null);
			a.setContentText("فیلد های رمز جدید را پر کنید.");
			a.show();
		} else if (!passwordfield.getText().equals(confirmpassword.getText())) {
			Alert a = new Alert(AlertType.ERROR);
			a.setTitle("خطا");
			a.setHeaderText(null);
			a.setContentText("تکرار رمز عبور صحیح نمیباشد.");
			a.show();
		}

		else {
			this.operator.setPassword(passwordfield.getText());
			operatorDao.UpdateProfile(this.operator);

			Alert a = new Alert(AlertType.INFORMATION);
			a.setTitle("تایید شده");
			a.setHeaderText(null);
			a.setContentText("رمز عبور با موفقیت تغییر یافت.");
			a.show();

			Stage currentstage = (Stage) backButton.getScene().getWindow();
			currentstage.close();
		}
	}

	@FXML
	void back(ActionEvent event) {

		Stage currentstage = (Stage) backButton.getScene().getWindow();
		currentstage.close();
	}

	public void OnStart() {

		this.passwordgridpane.setDisable(true);

		mellicodefield.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					mellicodefield.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});

	}
}
