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

public class NewOperatorController {

	private OperatorManageController omc;

	private OperatorDao operatorDao = new OperatorDao();

	@FXML
	private Button BackButton;

	@FXML
	private Button CreateNewButton;

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
	void CreateNew(ActionEvent event) throws ClassNotFoundException, SQLException {

		if (name.getText() == null || family.getText() == null || mobile.getText() == null
				|| mellicode.getText() == null || username.getText() == null || password.getText() == null) {

			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("خطا");
			alert.setHeaderText(null);
			alert.setContentText("تمامی فیلد ها میبایستی پر شوند.");
			alert.show();
		}

		else {
			Operator temp = new Operator();
			temp.setName(name.getText());
			temp.setFamily(family.getText());
			temp.setMellicode(Long.parseLong(mellicode.getText()));
			temp.setMobile(mobile.getText());
			temp.setUsername(username.getText());
			temp.setPassword(password.getText());
			temp.setAccesslevel(accesslevel.getSelectionModel().getSelectedIndex() + 1);

			operatorDao.AddNewOperator(temp);
			this.omc.UpdateMTable();
			this.omc.UpdatePATable();
			this.omc.UpdateRTable();

			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("اپراتور جدید");
			alert.setHeaderText(null);
			alert.setContentText("اطلاعات اپراتور جدید با موفقیت ثبت شد.");
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

	public void OnStart(OperatorManageController omc) {

		this.omc = omc;

		accesslevel.getItems().add("ادمین سیستم");
		accesslevel.getItems().add("مسئول خرید");
		accesslevel.getItems().add("مسئول فروش");
		accesslevel.getItems().add("مسئول انبار");

		accesslevel.getSelectionModel().select("ادمین سیستم");

		mobile.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					mobile.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});

		mellicode.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					mellicode.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});

	}

}
