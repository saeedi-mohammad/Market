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
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditOperatorController {

	private Operator operator;

	private OperatorManageController omc;

	OperatorDao operatorDao = new OperatorDao();

	@FXML
	private Button BackButton;

	@FXML
	private Button SaveButton;

	@FXML
	private ComboBox<String> accesslevel;

	@FXML
	private TextField family;

	@FXML
	private TextField mellicode;

	@FXML
	private TextField mobile;

	@FXML
	private TextField name;

	@FXML
	private PasswordField password;

	@FXML
	private TextField username;

	@FXML
	void SaveChanges(ActionEvent event) throws ClassNotFoundException, SQLException {

		if (name.getText() == null || family.getText() == null || mobile.getText() == null
				|| mellicode.getText() == null || username.getText() == null || password.getText() == null) {

			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("خطا");
			alert.setHeaderText(null);
			alert.setContentText("تمامی فیلد ها میبایستی پر شوند.");
			alert.show();
		} else {
			this.operator.setName(name.getText());
			this.operator.setFamily(family.getText());
			this.operator.setMellicode(Long.parseLong(mellicode.getText()));
			this.operator.setMobile(mobile.getText());
			this.operator.setUsername(username.getText());
			this.operator.setPassword(password.getText());
			this.operator.setAccesslevel(accesslevel.getSelectionModel().getSelectedIndex() + 1);

			operatorDao.UpdateOperator(this.operator);
			this.omc.UpdateMTable();
			this.omc.UpdatePATable();
			this.omc.UpdateRTable();

			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("ویرایش مشخصات اپراتور");
			alert.setHeaderText(null);
			alert.setContentText("اطلاعات اپراتور با موفقیت تغییر یافت.");
			alert.show();

			Stage currentstage = (Stage) BackButton.getScene().getWindow();
			currentstage.close();
		}
	}

	@FXML
	void back(ActionEvent event) {

		Stage currentstage = (Stage) BackButton.getScene().getWindow();
		currentstage.close();
	}

	public void OnStart(OperatorManageController omc, Operator operator) {

		this.omc = omc;
		this.operator = operator;

		accesslevel.getItems().add("ادمین سیستم");
		accesslevel.getItems().add("مسئول خرید");
		accesslevel.getItems().add("مسئول فروش");
		accesslevel.getItems().add("مسئول انبار");

		name.setText(operator.getName());
		family.setText(operator.getFamily());
		mobile.setText(operator.getMobile());
		mellicode.setText(Long.toString(operator.getMellicode()));
		username.setText(operator.getUsername());
		password.setText(operator.getPassword());
		accesslevel.getSelectionModel().select(operator.getAccesslevel() - 1);

		mellicode.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					mellicode.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});

		mobile.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					mobile.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});
	}

}
