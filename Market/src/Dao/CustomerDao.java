package Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Commons.Customer;
import Connection.ConnectionToPsgsql;

public class CustomerDao {

	private ConnectionToPsgsql connection = new ConnectionToPsgsql();
	private PreparedStatement pstmt = null;
	private Statement stmt = null;

	public int AddNewCustomer(Customer customer) throws ClassNotFoundException, SQLException {

		Connection conn = connection.StartConnection();

		String sql = "INSERT INTO customer(name,family,mobile,point,lastpurchasedate) " + "VALUES(?,?,?,?,?)";
		pstmt = conn.prepareStatement(sql);

		pstmt.setString(1, customer.getName());
		pstmt.setString(2, customer.getFamily());
		pstmt.setString(3, customer.getMobile());
		pstmt.setInt(4, customer.getPoint());
		pstmt.setDate(5, customer.getlastPurchaseDate());

		return pstmt.executeUpdate();
	}

	public Customer SelectById(int id) throws SQLException, ClassNotFoundException {

		Connection conn = connection.StartConnection();

		String sql = "SELECT * FROM customer WHERE id = ?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, id);

		ResultSet rs = pstmt.executeQuery();

		Customer customer = new Customer();

		while (rs.next()) {
			customer.setId(rs.getInt("id"));
			customer.setName(rs.getString("name"));
			customer.setFamily(rs.getString("family"));
			customer.setMobile(rs.getString("mobile"));
			customer.setPoint(rs.getInt("point"));
			customer.setLastPurchaseDate(rs.getDate("lastpurchasedate"));

		}

		return customer;

	}

	public void UpdateCustomer(Customer customer) throws SQLException, ClassNotFoundException {

		Connection conn = connection.StartConnection();

		String sql = "UPDATE customer SET name = ?, family = ?, mobile = ?, point = ?, lastpurchasedate = ? WHERE id = ? ";

		pstmt = conn.prepareStatement(sql);

		pstmt.setString(1, customer.getName());
		pstmt.setString(2, customer.getFamily());
		pstmt.setString(3, customer.getMobile());
		pstmt.setInt(4, customer.getPoint());
		pstmt.setDate(5, customer.getlastPurchaseDate());
		pstmt.setLong(6, customer.getId());

		pstmt.executeUpdate();

	}

	public void DeleteCustomer(Customer customer) throws ClassNotFoundException, SQLException {

		Connection conn = connection.StartConnection();

		String sql = "DELETE FROM customer WHERE id = ?";

		pstmt = conn.prepareStatement(sql);

		pstmt.setInt(1, customer.getId());

		pstmt.executeUpdate();

	}

	public ResultSet getAllCustomers() throws SQLException, ClassNotFoundException {

		Connection conn = connection.StartConnection();

		String sql = "SELECT * FROM customer";
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);

		return rs;
	}

}
