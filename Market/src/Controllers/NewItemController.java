package Controllers;

import java.sql.ResultSet;
import java.sql.SQLException;

import Commons.Item;
import Dao.GroupDao;
import Dao.ItemDao;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NewItemController {

	private ItemDao itemDao = new ItemDao();
	private GroupDao groupDao = new GroupDao();

	private ItemsManageController imc;
	private NewPurchaseController npc;

	@FXML
	private Button backButton;

	@FXML
	private Button createButton;

	@FXML
	private TextField itemname;

	@FXML
	private TextField itemserial;

	@FXML
	private ComboBox<String> typecombo;

	@FXML
	private ComboBox<String> groupcombo;

	@FXML
	void CreateItem(ActionEvent event) throws ClassNotFoundException, SQLException {

		if (this.itemname != null && this.itemserial != null && typecombo.isPickOnBounds()) {
			Item item = new Item();

			int id1 = groupDao.SelectByName(groupcombo.getSelectionModel().getSelectedItem().toString());

			if (typecombo.getSelectionModel().getSelectedIndex() == 0) {

				item.setNumber(-1);
				item.setWeight(0);
			} else if (typecombo.getSelectionModel().getSelectedIndex() == 1) {
				item.setWeight(-1);
				item.setNumber(0);
			}

			item.setName(itemname.getText());
			item.setSerial(Long.parseLong(itemserial.getText()));
			item.setGroupid(id1);

			itemDao.AddNewItemWithoutPrices(item);
			if (imc != null)
				imc.UpdateTable();

			if (npc != null)
				npc.UpdateItems();

			Stage currentstage = (Stage) backButton.getScene().getWindow();
			currentstage.close();

			Alert a = new Alert(AlertType.INFORMATION);
			a.setTitle("کالای ایجاد شده");
			a.setHeaderText(null);
			a.setContentText("کالای مورد نظر با موفقیت ثبت شد.");
			a.show();

		}

		else {
			Alert a = new Alert(AlertType.WARNING);
			a.setTitle("خطا");
			a.setHeaderText(null);
			a.setContentText("لطفا فیلد هارا با دقت پر کنید.");
			a.show();
		}
	}

	@FXML
	void back(ActionEvent event) {

		Stage currentstage = (Stage) backButton.getScene().getWindow();
		currentstage.close();
	}

	public void OnStart(ItemsManageController imc) throws ClassNotFoundException, SQLException {

		this.imc = imc;

		ResultSet r = groupDao.getAllGroups();

		while (r.next()) {
			groupcombo.getItems().add(r.getString("name"));
		}
		typecombo.getItems().add("مقدار");
		typecombo.getItems().add("تعداد");

	}

	public void OnStart() throws ClassNotFoundException, SQLException {

		ResultSet r = groupDao.getAllGroups();

		while (r.next()) {
			groupcombo.getItems().add(r.getString("name"));
		}
		typecombo.getItems().add("مقدار");
		typecombo.getItems().add("تعداد");

	}

	public void OnStart(NewPurchaseController npc) throws ClassNotFoundException, SQLException {

		this.npc = npc;

		ResultSet r = groupDao.getAllGroups();

		while (r.next()) {
			groupcombo.getItems().add(r.getString("name"));
		}
		typecombo.getItems().add("مقدار");
		typecombo.getItems().add("تعداد");

		itemserial.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					itemserial.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});

	}

}
