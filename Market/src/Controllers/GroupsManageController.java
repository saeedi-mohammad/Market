package Controllers;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import Commons.Group;
import Dao.GroupDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class GroupsManageController {

	ObservableList<Group> groups = FXCollections.observableArrayList();

	GroupDao groupDao = new GroupDao();

	@FXML
	private Button BackButton;

	@FXML
	private TableView<Group> GroupsTable;

	@FXML
	private Button DeleteGroupButton;

	@FXML
	private Button EditGroupButton;

	@FXML
	private Button NewGroupButton;

	@FXML
	private TableColumn<Group, Integer> id;

	@FXML
	private TableColumn<Group, String> name;

	@FXML
	private Button refresh;

	@FXML
	private TableColumn<Group, Integer> row;

	@FXML
	void Back(ActionEvent event) {

		Stage currentstage = (Stage) BackButton.getScene().getWindow();
		currentstage.close();
	}

	@FXML
	void CreateNewGroup(ActionEvent event) throws IOException {

		FXMLLoader fxmlLoader = new FXMLLoader();
		Parent root = fxmlLoader.load(getClass().getResource("/View/NewGroup.fxml").openStream());

		NewGroupController sic = (NewGroupController) fxmlLoader.getController();
		sic.OnStart(this);

		Stage stage = new Stage();
		Scene scene = new Scene(root);
		stage.setTitle("  افزودن دسته بندی جدید");

		stage.setScene(scene);
		stage.show();
	}

	@FXML
	void DeleteGroup(ActionEvent event) throws ClassNotFoundException, SQLException {

		if (GroupsTable.getSelectionModel().getSelectedItems().isEmpty()) {

			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("خطا");
			alert.setHeaderText(null);
			alert.setContentText("ابتدا دسته بندی مورد نظر را انتخاب کنید.");
			alert.show();
		}

		else {

			try {
				groupDao.DeleteGroup(GroupsTable.getSelectionModel().getSelectedItem());

				Alert a = new Alert(AlertType.INFORMATION);
				a.setTitle("دسته بندی حذف شده");
				a.setHeaderText(null);
				a.setContentText("دسته بندی مورد نظر با موفقیت حذف شد.");
				a.show();

				UpdateTable();
			} catch (Exception e) {

				Alert a = new Alert(AlertType.ERROR);
				a.setTitle("خطا");
				a.setHeaderText(null);
				a.setContentText(
						"ابتدا کالا هایی که در این دسته بندی قرار گرفته اند را حذف کنید سپس برای حذف دسته بندی اقدام کتید.");
				a.show();
			}
		}
	}

	@FXML
	void EditGroup(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {

		if (GroupsTable.getSelectionModel().getSelectedItems().isEmpty()) {

			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("خطا");
			alert.setHeaderText(null);
			alert.setContentText("ابتدا دسته بندی مورد نظر را انتخاب کنید.");
			alert.show();
		}

		else {

			FXMLLoader fxmlLoader = new FXMLLoader();
			Parent root = fxmlLoader.load(getClass().getResource("/View/EditGroup.fxml").openStream());

			EditGroupController egc = (EditGroupController) fxmlLoader.getController();
			egc.UpdateFields(this, GroupsTable.getSelectionModel().getSelectedItem().getId());

			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setTitle("  ویرایش دسته بندی");

			stage.setScene(scene);
			stage.show();

		}

	}

	public void OnStart() throws ClassNotFoundException, SQLException {

		row.setCellValueFactory(new PropertyValueFactory<Group, Integer>("SPrownumber"));
		id.setCellValueFactory(new PropertyValueFactory<Group, Integer>("SPid"));
		name.setCellValueFactory(new PropertyValueFactory<Group, String>("SPname"));

		UpdateTable();
	}

	public void UpdateTable() throws ClassNotFoundException, SQLException {

		if (!GroupsTable.getItems().isEmpty())
			GroupsTable.getItems().clear();

		GetAllGroups();
		GroupsTable.setItems(groups);
	}

	public ObservableList<Group> GetAllGroups() throws ClassNotFoundException, SQLException {

		ResultSet rs = groupDao.getAllGroups();
		int i = 1;

		while (rs.next()) {
			Group temp = new Group();
			temp.setId(rs.getInt("id"));
			temp.setName(rs.getString("name"));
			temp.setSPrownumber(i++);

			groups.add(temp);

		}

		return groups;

	}

}
