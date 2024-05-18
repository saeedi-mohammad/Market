package Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Commons.Purchase;
import Connection.ConnectionToPsgsql;

public class PurchaseDao {

	private ConnectionToPsgsql connection = new ConnectionToPsgsql();
	private PreparedStatement pstmt = null;
	private Statement stmt = null;

	public ResultSet SelectById(int id) throws SQLException, ClassNotFoundException {

		Connection conn = connection.StartConnection();

		String sql = "SELECT * FROM purchase WHERE id = ?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, id);

		return pstmt.executeQuery();
	}

	public int AddNewPurchase(Purchase purchase) throws ClassNotFoundException, SQLException {

		Connection conn = connection.StartConnection();

		String sql = "INSERT INTO purchase(itemid,value,cost,invoiseid,details) " + "VALUES(?,?,?,?,?)";
		pstmt = conn.prepareStatement(sql);

		pstmt.setInt(1, purchase.getItemid());
		pstmt.setDouble(2, purchase.getValue());
		pstmt.setLong(3, purchase.getCost());
		pstmt.setInt(4, purchase.getInvoiseid());
		pstmt.setString(5, purchase.getDetails());

		return pstmt.executeUpdate();

	}

	public ResultSet GetAllByInvoiseId(int invoiseid) throws SQLException, ClassNotFoundException {

		Connection conn = connection.StartConnection();

		String sql = "SELECT * FROM purchase WHERE invoiseid = ?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, invoiseid);

		ResultSet rs = pstmt.executeQuery();

		return rs;

	}

}
