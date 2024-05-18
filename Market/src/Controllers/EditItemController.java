package Controllers;

import java.sql.SQLException;

import Commons.Item;
import Dao.ItemDao;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditItemController {

	ItemDao itemDao = new ItemDao();

	private ItemsManageController imc;

	@FXML
	private Button BackButton;

	@FXML
	private TextField PurchasePricefield;

	@FXML
	private TextField RetailPricefield;

	@FXML
	private Button SaveButton;

	@FXML
	private TextField WholesalePricefield;

	@FXML
	private TextField groupnamefield;

	@FXML
	private TextField groupidfield;

	@FXML
	private TextField idfield;

	@FXML
	private TextField namefield;

	@FXML
	private TextField numberfield;

	@FXML
	private TextField serialfield;

	@FXML
	private TextField weightfield;

	@FXML
	public void Back(ActionEvent event) {

		Stage currentstage = (Stage) BackButton.getScene().getWindow();
		currentstage.close();
	}

	@FXML
	public void SaveChanges(ActionEvent event) throws ClassNotFoundException, SQLException {

		Item item = new Item();

		item.setId(Integer.parseInt(idfield.getText()));
		item.setName(namefield.getText());
		item.setSerial(Long.parseLong(serialfield.getText()));
		item.setGroupid(Integer.parseInt(groupidfield.getText()));
		item.setNumber(Integer.parseInt(numberfield.getText()));
		item.setWeight(Double.parseDouble(weightfield.getText()));
		item.setPurchasePrice(Long.parseLong(PurchasePricefield.getText()));
		item.setRetailPrice(Long.parseLong(RetailPricefield.getText()));
		item.setWholesalePrice(Long.parseLong(WholesalePricefield.getText()));

		itemDao.UpdateItem(item);

		imc.UpdateTable();

		Stage currentstage = (Stage) BackButton.getScene().getWindow();
		currentstage.close();

		Alert a = new Alert(AlertType.INFORMATION);
		a.setTitle("کالای تغییر داده شده");
		a.setHeaderText(null);
		a.setContentText("کالای مورد نظر به روز رسانی شد.");
		a.show();

	}

	public void UpdateFields(ItemsManageController imc, int id) throws ClassNotFoundException, SQLException {

		this.imc = imc;

		Item item = itemDao.SelectById(id);

		this.namefield.setText(item.getName());
		this.idfield.setText(Integer.toString(item.getId()));
		this.serialfield.setText(Long.toString(item.getSerial()));
		this.groupnamefield.setText(item.getGroupname());
		this.groupidfield.setText(Integer.toString(item.getGroupid()));
		this.PurchasePricefield.setText(Long.toString(item.getPurchasePrice()));
		this.RetailPricefield.setText(Long.toString(item.getRetailPrice()));
		this.WholesalePricefield.setText(Long.toString(item.getWholesalePrice()));

		if (item.getNumber() < 0) {
			this.numberfield.setText(Integer.toString(-1));
			this.numberfield.setDisable(true);
			this.weightfield.setText(Double.toString(item.getWeight()));
		}

		if (item.getWeight() < 0) {
			this.weightfield.setText(Integer.toString(-1));
			this.weightfield.setDisable(true);
			this.numberfield.setText(Integer.toString(item.getNumber()));
		}

		LimitFields();

	}

	public void LimitFields() {

		PurchasePricefield.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					PurchasePricefield.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});

		serialfield.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					serialfield.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});

		RetailPricefield.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					RetailPricefield.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});

		WholesalePricefield.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					WholesalePricefield.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});

		numberfield.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					numberfield.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});

		weightfield.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					weightfield.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});
	}

}
