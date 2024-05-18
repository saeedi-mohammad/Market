package Controllers;

import java.io.IOException;
import java.sql.SQLException;

import Commons.Operator;
import Dao.OperatorDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class LoginController {

	private OperatorDao operatorDao = new OperatorDao();

	private Operator operator = new Operator();

	@FXML
	private Hyperlink forgetpasswordLink;

	@FXML
	private Button loginButton;

	@FXML
	private PasswordField passwordField;

	@FXML
	private TextField usernameField;

	@FXML
	void ForgetPassword(ActionEvent event) throws IOException {

		FXMLLoader fxmlLoader = new FXMLLoader();
		Parent root = fxmlLoader.load(getClass().getResource("/View/ForgetPassword.fxml").openStream());

		ForgetPasswordController fpc = (ForgetPasswordController) fxmlLoader.getController();
		fpc.OnStart();

		Stage stage = new Stage();
		Scene scene = new Scene(root);
		stage.setTitle("  رمز فراموشی");

		stage.setScene(scene);
		stage.show();

	}

	@FXML
	void CheckLogin(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {

		this.operator = operatorDao.CheckLogin(this.usernameField.getText(), this.passwordField.getText());
		if (operator.getOperatorid() != 0) {

			FXMLLoader fxmlLoader = new FXMLLoader();
			Parent root = fxmlLoader.load(getClass().getResource("/View/HomePage.fxml").openStream());

			HomePageController hpc = (HomePageController) fxmlLoader.getController();
			hpc.OnStart(this.operator);

			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setTitle("  صفحه اصلی");

			stage.setScene(scene);
			stage.show();

			Stage currentstage = (Stage) loginButton.getScene().getWindow();
			currentstage.close();

		}

		else {

			Alert a = new Alert(AlertType.ERROR);
			a.setTitle("خطا");
			a.setHeaderText(null);
			a.setContentText("نام کاربری یا رمز عبور اشتباه است!");
			a.show();
		}
	}

	@FXML
	void keypressed(KeyEvent e) throws ClassNotFoundException, SQLException, IOException {

		if (e.getCode().equals(KeyCode.ENTER))
			CheckLogin(null);

	}

}
