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

public class EditGroupController {

	GroupDao groupDao = new GroupDao();

	private GroupsManageController gmc;

	@FXML
	private Button BackButton;

	@FXML
	private Button SaveButton;

	@FXML
	private TextField idfield;

	@FXML
	private TextField namefield;

	@FXML
	void Back(ActionEvent event) {

		Stage currentstage = (Stage) BackButton.getScene().getWindow();
		currentstage.close();
	}

	@FXML
	void SaveChanges(ActionEvent event) throws ClassNotFoundException, SQLException {

		Group group = new Group();

		group.setId(Integer.parseInt(idfield.getText()));
		group.setName(namefield.getText());

		groupDao.UpdateGroup(group);

		gmc.UpdateTable();

		Stage currentstage = (Stage) BackButton.getScene().getWindow();
		currentstage.close();

		Alert a = new Alert(AlertType.INFORMATION);
		a.setTitle("مشخصات تغییر داده شده");
		a.setHeaderText(null);
		a.setContentText("دسته بندی مورد نظر به روز رسانی شد.");
		a.show();

	}

	public void UpdateFields(GroupsManageController gmc, int id) throws ClassNotFoundException, SQLException {

		this.gmc = gmc;

		Group group = groupDao.SelectById(id);

		this.idfield.setText(Integer.toString(group.getId()));
		this.namefield.setText(group.getName());
	}
}
