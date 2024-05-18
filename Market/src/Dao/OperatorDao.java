package Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Commons.Operator;
import Connection.ConnectionToPsgsql;

public class OperatorDao {

	private ConnectionToPsgsql connection = new ConnectionToPsgsql();
	private PreparedStatement pstmt = null;
	private Statement stmt = null;

	public int AddNewOperator(Operator operator) throws ClassNotFoundException, SQLException {

		Connection conn = connection.StartConnection();

		String sql = "INSERT INTO operator(name,family,mobile,mellicode,username,password,accesslevel) "
				+ "VALUES(?,?,?,?,?,?,?)";
		pstmt = conn.prepareStatement(sql);

		pstmt.setString(1, operator.getName());
		pstmt.setString(2, operator.getFamily());
		pstmt.setString(3, operator.getMobile());
		pstmt.setLong(4, operator.getMellicode());
		pstmt.setString(5, operator.getUsername());
		pstmt.setString(6, operator.getPassword());
		pstmt.setInt(7, operator.getAccesslevel());

		return pstmt.executeUpdate();

	}

	public Operator CheckLogin(String user, String pass) throws SQLException, ClassNotFoundException {

		Connection conn = connection.StartConnection();

		String sql = "SELECT * FROM operator WHERE username = ? AND password = ?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, user);
		pstmt.setString(2, pass);

		ResultSet rs = pstmt.executeQuery();
		Operator temp = new Operator();

		while (rs.next()) {

			temp.setOperatorid(rs.getInt("id"));
			temp.setName(rs.getString("name"));
			temp.setFamily(rs.getString("family"));
			temp.setUsername(rs.getString("username"));
			temp.setPassword(rs.getString("password"));
			temp.setMobile(rs.getString("mobile"));
			temp.setMellicode(rs.getLong("mellicode"));
			temp.setAccesslevel(rs.getInt("accesslevel"));

		}
		return temp;

	}

	public Operator SelectById(int id) throws SQLException, ClassNotFoundException {

		Connection conn = connection.StartConnection();

		String sql = "SELECT * FROM operator WHERE id = ?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, id);

		ResultSet rs = pstmt.executeQuery();
		Operator operator = new Operator();
		while (rs.next()) {
			operator.setOperatorid(rs.getInt("id"));
			operator.setName(rs.getString("name"));
			operator.setFamily(rs.getString("family"));
			operator.setMobile(rs.getString("mobile"));
			operator.setUsername(rs.getString("username"));
			operator.setPassword(rs.getString("password"));
			operator.setMellicode(rs.getLong("mellicode"));
			operator.setAccesslevel(rs.getInt("accesslevel"));
		}

		return operator;
	}

	public Operator SelectByUsernameMellicode(String username, long mellicode)
			throws ClassNotFoundException, SQLException {

		Connection conn = connection.StartConnection();

		String sql = "SELECT * FROM operator WHERE username = ? AND mellicode = ?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, username);
		pstmt.setLong(2, mellicode);

		ResultSet rs = pstmt.executeQuery();
		Operator operator = new Operator();
		while (rs.next()) {
			operator.setOperatorid(rs.getInt("id"));
			operator.setName(rs.getString("name"));
			operator.setFamily(rs.getString("family"));
			operator.setMobile(rs.getString("mobile"));
			operator.setUsername(rs.getString("username"));
			operator.setPassword(rs.getString("password"));
			operator.setMellicode(rs.getLong("mellicode"));
			operator.setAccesslevel(rs.getInt("accesslevel"));
		}
		return operator;
	}

	public void UpdateProfile(Operator operator) throws SQLException, ClassNotFoundException {

		Connection conn = connection.StartConnection();

		String sql = "UPDATE operator SET username = ? , password = ? , name =? , family = ? , mobile = ? , mellicode = ? WHERE id = ?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, operator.getUsername());
		pstmt.setString(2, operator.getPassword());
		pstmt.setString(3, operator.getName());
		pstmt.setString(4, operator.getFamily());
		pstmt.setString(5, operator.getMobile());
		pstmt.setLong(6, operator.getMellicode());
		pstmt.setInt(7, operator.getOperatorid());

		pstmt.executeUpdate();
	}

	public void UpdateOperator(Operator operator) throws SQLException, ClassNotFoundException {

		Connection conn = connection.StartConnection();

		String sql = "UPDATE operator SET username = ? , password = ? , name =? , family = ? , mobile = ? , mellicode = ? , accesslevel = ? WHERE id = ?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, operator.getUsername());
		pstmt.setString(2, operator.getPassword());
		pstmt.setString(3, operator.getName());
		pstmt.setString(4, operator.getFamily());
		pstmt.setString(5, operator.getMobile());
		pstmt.setLong(6, operator.getMellicode());
		pstmt.setInt(7, operator.getAccesslevel());
		pstmt.setInt(8, operator.getOperatorid());

		pstmt.executeUpdate();
	}

	public ResultSet GetAllOperators() throws ClassNotFoundException, SQLException {

		Connection conn = connection.StartConnection();

		String sql = "SELECT * FROM operator";
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);

		return rs;
	}

	public void DeleteOperator(Operator operator) throws SQLException, ClassNotFoundException {

		Connection conn = connection.StartConnection();

		String sql = "DELETE FROM operator WHERE id = ?";

		pstmt = conn.prepareStatement(sql);

		pstmt.setInt(1, operator.getOperatorid());

		pstmt.executeUpdate();
	}

}
