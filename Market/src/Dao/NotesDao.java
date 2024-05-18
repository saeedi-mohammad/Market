package Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Commons.Note;
import Connection.ConnectionToPsgsql;

public class NotesDao {

	private ConnectionToPsgsql connection = new ConnectionToPsgsql();
	private PreparedStatement pstmt = null;
	private Statement stmt = null;

	public Note SelectById(int id) throws SQLException, ClassNotFoundException {

		Connection conn = connection.StartConnection();

		String sql = "SELECT * FROM notes WHERE id = ?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, id);

		ResultSet rs = pstmt.executeQuery();

		Note note = new Note();
		while (rs.next()) {
			note.setId(rs.getInt("id"));
			note.setHead(rs.getString("head"));
			note.setContent(rs.getString("content"));
			note.setOperatorid(rs.getInt("operatorid"));

		}

		return note;

	}

	public int AddNewNote(Note note) throws ClassNotFoundException, SQLException {

		Connection conn = connection.StartConnection();

		String sql = "INSERT INTO notes(head,content,operatorid,pop) " + "VALUES(?,?,?,?)";
		pstmt = conn.prepareStatement(sql);

		pstmt.setString(1, note.getHead());
		pstmt.setString(2, note.getContent());
		pstmt.setInt(3, note.getOperatorid());
		pstmt.setString(4, note.getPop());

		return pstmt.executeUpdate();

	}

	public ResultSet GetAllNotes() throws ClassNotFoundException, SQLException {

		Connection conn = connection.StartConnection();

		String sql = "SELECT * FROM notes";
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);

		return rs;
	}

	public ResultSet GetAllByOperatorid(int operatorid) throws ClassNotFoundException, SQLException {
		Connection conn = connection.StartConnection();

		String sql = "SELECT * FROM notes WHERE operatorid=?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, operatorid);
		ResultSet rs = pstmt.executeQuery();

		return rs;
	}

	public ResultSet GetAllpublicAndOwnNotes(int operatorid) throws ClassNotFoundException, SQLException {
		Connection conn = connection.StartConnection();

		String sql = "SELECT * FROM notes WHERE operatorid=? OR pop =?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, operatorid);
		pstmt.setString(2, "عمومی");
		ResultSet rs = pstmt.executeQuery();

		return rs;
	}

	public void DeleteNote(Note note) throws SQLException, ClassNotFoundException {

		Connection conn = connection.StartConnection();

		String sql = "DELETE FROM notes WHERE id = ?";

		pstmt = conn.prepareStatement(sql);

		pstmt.setInt(1, note.getId());

		pstmt.executeUpdate();
	}

	public void UpdateNote(Note note) throws SQLException, ClassNotFoundException {

		Connection conn = connection.StartConnection();

		String sql = "UPDATE notes SET head = ?, content = ?, operatorid = ?, pop = ? WHERE id = ? ";

		pstmt = conn.prepareStatement(sql);

		pstmt.setString(1, note.getHead());
		pstmt.setString(2, note.getContent());
		pstmt.setInt(3, note.getOperatorid());
		pstmt.setString(4, note.getPop());
		pstmt.setInt(5, note.getId());

		pstmt.executeUpdate();
	}

}
