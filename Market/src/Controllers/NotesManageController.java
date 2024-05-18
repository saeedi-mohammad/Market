package Controllers;

import java.sql.ResultSet;
import java.sql.SQLException;

import Commons.Note;
import Commons.Operator;
import Dao.NotesDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class NotesManageController {

	private ObservableList<Note> notes = FXCollections.observableArrayList();
	private Operator operator = new Operator();
	private Note tempnote = new Note();

	private NotesDao notesDao = new NotesDao();

	@FXML
	private Button BackButton;

	@FXML
	private TextArea ContentField;

	@FXML
	private Button DeleteNoteButton;

	@FXML
	private Button EditNoteButton;

	@FXML
	private Button NewNoteButton;

	@FXML
	private TableView<Note> NotesTable;

	@FXML
	private ComboBox<String> TypeCombo;

	@FXML
	private TableColumn<Note, String> head;

	@FXML
	private TextField headField;

	@FXML
	private TableColumn<Note, Integer> id;

	@FXML
	private TableColumn<Note, Integer> operatorid;

	@FXML
	private TableColumn<Note, String> pop;

	@FXML
	private TableColumn<Note, Integer> row;

	@FXML
	void DeleteNote(ActionEvent event) throws ClassNotFoundException, SQLException {

		if (EditNoteButton.getText().equals("ثبت")) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("خطا");
			alert.setHeaderText(null);
			alert.setContentText("ابتدا یادداشت فعلی را ثبت کنید.");
			alert.show();
		}

		else {

			if (NotesTable.getSelectionModel().getSelectedItems().isEmpty()) {

				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("خطا");
				alert.setHeaderText(null);
				alert.setContentText("ابتدا یادداشت مورد نظر را از داخل لیست انتخاب کنید.");
				alert.show();
			}

			else {

				notesDao.DeleteNote(NotesTable.getSelectionModel().getSelectedItem());
				UpdateTable();
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("!");
				alert.setHeaderText(null);
				alert.setContentText("یادداشت مورد نظر حذف شد.");
				alert.show();

			}
		}
	}

	@FXML
	void EditNote(ActionEvent event) throws ClassNotFoundException, SQLException {

		if (EditNoteButton.getText().equals("ویرایش / مشاهده"))
			if (NotesTable.getSelectionModel().getSelectedItems().isEmpty()) {

				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText(null);
				alert.setTitle("خطا");
				alert.setContentText("ابتدا فیلد مورد نظر را از داخل لیست انتخاب کنید.");
				alert.show();
			}

			else {
				this.tempnote = NotesTable.getSelectionModel().getSelectedItem();
				this.headField.setText(tempnote.getHead());
				this.ContentField.setText(tempnote.getContent());
				TypeCombo.setDisable(true);
				EditNoteButton.setStyle("-fx-background-color: #ff2950");
				EditNoteButton.setText("ثبت");

				if (this.tempnote.getOperatorid() != this.operator.getOperatorid()
						&& this.operator.getAccesslevel() != 1) {
					this.headField.setEditable(false);
					this.ContentField.setEditable(false);
				}

			}

		else {

			this.tempnote.setHead(this.headField.getText());
			this.tempnote.setContent(this.ContentField.getText());

			notesDao.UpdateNote(this.tempnote);
			tempnote = new Note();

			UpdateTable();
			ResetFields();

			EditNoteButton.setStyle(null);
			EditNoteButton.setText("ویرایش / مشاهده");

		}
	}

	@FXML
	void CreateNewNote(ActionEvent event) throws ClassNotFoundException, SQLException {

		if (EditNoteButton.getText().equals("ثبت")) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("خطا");
			alert.setHeaderText(null);
			alert.setContentText("ابتدا یادداشت فعلی را ثبت کنید.");
			alert.show();
		}

		else {

			if (headField.getText() == null || ContentField.getText() == null) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText(null);
				alert.setTitle("خطا");
				alert.setContentText("ابتدا فیلد های مورد نظر را پر کنید.");
				alert.show();
			}

			else {
				tempnote = new Note();
				tempnote.setHead(headField.getText());
				tempnote.setContent(ContentField.getText());
				tempnote.setOperatorid(this.operator.getOperatorid());
				tempnote.setPop(this.TypeCombo.getSelectionModel().getSelectedItem());

				notesDao.AddNewNote(tempnote);
				tempnote = new Note();
				UpdateTable();
				ResetFields();

				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("افزودن یادداشت");
				alert.setHeaderText(null);
				alert.setContentText("یادداشت مورد نظر ثبت شد.");
				alert.show();
			}
		}
	}

	@FXML
	void back(ActionEvent event) {

		Stage currentstage = (Stage) BackButton.getScene().getWindow();
		currentstage.close();
	}

	public void OnStart(Operator operator) throws ClassNotFoundException, SQLException {

		this.operator = operator;

		row.setCellValueFactory(new PropertyValueFactory<Note, Integer>("SPrownumber"));
		id.setCellValueFactory(new PropertyValueFactory<Note, Integer>("SPid"));
		operatorid.setCellValueFactory(new PropertyValueFactory<Note, Integer>("SPoperatorid"));
		head.setCellValueFactory(new PropertyValueFactory<Note, String>("SPhead"));
		pop.setCellValueFactory(new PropertyValueFactory<Note, String>("SPpop"));

		this.TypeCombo.getItems().add("شخصی");
		this.TypeCombo.getItems().add("عمومی");

		UpdateTable();

	}

	public void GetAllByOperatorid() throws ClassNotFoundException, SQLException {

		ObservableList<Note> notes = FXCollections.observableArrayList();
		ResultSet rs = notesDao.GetAllByOperatorid(this.operator.getOperatorid());

		int i = 1;
		while (rs.next()) {
			Note note = new Note();
			note.setId(rs.getInt("id"));
			note.setHead(rs.getString("head"));
			note.setContent(rs.getString("content"));
			note.setOperatorid(rs.getInt("operatorid"));
			note.setPop(rs.getString("pop"));
			note.setSPrownumber(i++);

			notes.add(note);
		}

		this.notes = notes;

	}

	public void GetAllpublicAndOwnNotes() throws ClassNotFoundException, SQLException {

		ObservableList<Note> notes = FXCollections.observableArrayList();
		ResultSet rs = notesDao.GetAllpublicAndOwnNotes(this.operator.getOperatorid());

		int i = 1;
		while (rs.next()) {
			Note note = new Note();
			note.setId(rs.getInt("id"));
			note.setHead(rs.getString("head"));
			note.setContent(rs.getString("content"));
			note.setOperatorid(rs.getInt("operatorid"));
			note.setPop(rs.getString("pop"));
			note.setSPrownumber(i++);

			notes.add(note);
		}
		this.notes = notes;
	}

	public void GetAllNotes() throws ClassNotFoundException, SQLException {

		ObservableList<Note> notes = FXCollections.observableArrayList();
		ResultSet rs = notesDao.GetAllNotes();

		int i = 1;
		while (rs.next()) {
			Note note = new Note();
			note.setId(rs.getInt("id"));
			note.setHead(rs.getString("head"));
			note.setContent(rs.getString("content"));
			note.setOperatorid(rs.getInt("operatorid"));
			note.setPop(rs.getString("pop"));
			note.setSPrownumber(i++);

			notes.add(note);
		}
		this.notes = notes;
	}

	public void UpdateTable() throws ClassNotFoundException, SQLException {
		if (!NotesTable.getItems().isEmpty())
			NotesTable.getItems().clear();

		if (this.operator.getAccesslevel() == 1)
			GetAllNotes();
		else
			GetAllpublicAndOwnNotes();
		NotesTable.setItems(this.notes);
	}

	public void ResetFields() {
		this.headField.setText(null);
		this.headField.setEditable(true);

		this.ContentField.setText(null);
		this.ContentField.setEditable(true);

		TypeCombo.setDisable(false);

	}
}
