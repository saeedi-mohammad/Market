package Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Commons.Group;
import Connection.ConnectionToPsgsql;

public class GroupDao {

	private ConnectionToPsgsql connection = new ConnectionToPsgsql();
	private PreparedStatement pstmt = null;
	private Statement stmt = null;

	private int id;
	private String name;

	public int AddNewGroup(Group group) throws SQLException, ClassNotFoundException {

		Connection conn = connection.StartConnection();

		String sql = "INSERT INTO itemgroups(name) " + "VALUES(?)";
		pstmt = conn.prepareStatement(sql);

		pstmt.setString(1, group.getName());

		return pstmt.executeUpdate();
	}

	public Group SelectById(int id) throws SQLException, ClassNotFoundException {

		Connection conn = connection.StartConnection();

		String sql = "SELECT * FROM itemgroups WHERE id = ?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, id);

		ResultSet rs = pstmt.executeQuery();

		Group group = new Group();

		while (rs.next()) {
			group.setId(rs.getInt("id"));
			group.setName(rs.getString("name"));
		}
		return group;

	}

	public ResultSet getAllGroups() throws SQLException, ClassNotFoundException {

		Connection conn = connection.StartConnection();

		String sql = "SELECT * FROM itemgroups";
		stmt = conn.createStatement();

		return stmt.executeQuery(sql);

	}

	public int SelectByName(String name) throws SQLException, ClassNotFoundException {

		Connection conn = connection.StartConnection();

		String sql = "SELECT * FROM itemgroups WHERE name = ?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, name);

		ResultSet rs = pstmt.executeQuery();

		while (rs.next()) {
			return rs.getInt("id");
		}

		return 0;

	}

	public void UpdateGroup(Group group) throws SQLException, ClassNotFoundException {

		Connection conn = connection.StartConnection();

		String sql = "UPDATE itemgroups SET name = ? WHERE id = ? ";

		pstmt = conn.prepareStatement(sql);

		pstmt.setInt(1, group.getId());
		pstmt.setString(2, group.getName());

		pstmt.executeUpdate();
	}

	public void DeleteGroup(Group group) throws SQLException, ClassNotFoundException {

		Connection conn = connection.StartConnection();

		String sql = "DELETE FROM itemgroups WHERE id = ?";

		pstmt = conn.prepareStatement(sql);

		pstmt.setInt(1, group.getId());

		pstmt.executeUpdate();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
