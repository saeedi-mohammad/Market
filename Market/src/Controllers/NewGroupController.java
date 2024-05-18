package Controllers;

import java.sql.SQLException;

import Commons.Group;
import Dao.GroupDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NewGroupController {

	GroupDao groupDao = new GroupDao();

	private GroupsManageController gmc;

	@FXML
	private TextField GroupName;

	@FXML
	private Button backButton;

	@FXML
	private Button createButton;

	@FXML
	void CreateGroup(ActionEvent event) throws ClassNotFoundException, SQLException {

		Group group = new Group();
		group.setName(GroupName.getText());

		groupDao.AddNewGroup(group);

		gmc.UpdateTable();

		Stage currentstage = (Stage) backButton.getScene().getWindow();
		currentstage.close();

		Alert a = new Alert(AlertType.INFORMATION);
		a.setTitle("دسته بندی ایجاد شده");
		a.setHeaderText(null);
		a.setContentText("دسته بندی مورد نظر با موفقیت ثبت شد.");
		a.show();
	}

	@FXML
	void back(ActionEvent event) {

		Stage currentstage = (Stage) backButton.getScene().getWindow();
		currentstage.close();
	}

	public void OnStart(GroupsManageController gmc) {
		this.gmc = gmc;
	}
}
