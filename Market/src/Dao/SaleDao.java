package Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Commons.Sale;
import Connection.ConnectionToPsgsql;

public class SaleDao {

	private ConnectionToPsgsql connection = new ConnectionToPsgsql();
	private PreparedStatement pstmt = null;
	private Statement stmt = null;

	public ResultSet SelectById(int id) throws SQLException, ClassNotFoundException {

		Connection conn = connection.StartConnection();

		String sql = "SELECT * FROM sale WHERE id = ?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, id);

		return pstmt.executeQuery();
	}

	public int CreateNewSale(Sale sale) throws ClassNotFoundException, SQLException {

		Connection conn = connection.StartConnection();

		String sql = "INSERT INTO sale(invoiseid,value,itemid,retailorwholesale,price,cost) " + "VALUES(?,?,?,?,?,?)";
		pstmt = conn.prepareStatement(sql);

		pstmt.setInt(1, sale.getInvoiseid());
		pstmt.setDouble(2, sale.getItemvalue());
		pstmt.setInt(3, sale.getItemid());
		pstmt.setString(4, sale.getRetailORwholesale());
		pstmt.setLong(5, sale.getItemprice());
		pstmt.setLong(6, sale.getSalecost());

		return pstmt.executeUpdate();

	}

	public ResultSet GetAllByInvoiseId(int invoiseid) throws SQLException, ClassNotFoundException {

		Connection conn = connection.StartConnection();

		String sql = "SELECT * FROM sale WHERE invoiseid = ?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, invoiseid);

		ResultSet rs = pstmt.executeQuery();

		return rs;

	}

}
