package Dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Commons.Invoise;
import Connection.ConnectionToPsgsql;

public class InvoiseDao {

	private ConnectionToPsgsql connection = new ConnectionToPsgsql();
	private PreparedStatement pstmt = null;
	private Statement stmt = null;

	public Invoise SelectById(int id) throws SQLException, ClassNotFoundException {

		Connection conn = connection.StartConnection();

		String sql = "SELECT * FROM invoise WHERE id = ?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, id);

		ResultSet rs = pstmt.executeQuery();
		Invoise invoise = new Invoise();
		while (rs.next()) {
			invoise.setInvoiseid(rs.getInt("id"));
			invoise.setCustomerid(rs.getInt("customerid"));
			invoise.setTotalcost(rs.getLong("totalcost"));
			invoise.setSaleOrPurchase(rs.getString("saleorpurchase"));

			///////////////////// set hejri time
			invoise.setTime(rs.getTime("time"));
			invoise.setDate(rs.getDate("date"));
		}
		return invoise;
	}

	public int CreateNewSaleInvoise(Invoise invoise) throws SQLException, ClassNotFoundException {

		Connection conn = connection.StartConnection();

		String sql = "INSERT INTO invoise(totalcost,customerid,date,time,saleorpurchase) " + "VALUES(?,?,?,?,?)";
		pstmt = conn.prepareStatement(sql, new String[] { "id" });

		pstmt.setLong(1, invoise.getTotalcost());
		pstmt.setInt(2, invoise.getCustomerid());
		pstmt.setDate(3, invoise.getDate());
		pstmt.setTime(4, invoise.getTime());
		pstmt.setString(5, invoise.getSaleOrPurchase());

		int count = pstmt.executeUpdate();
		int id = 0;
		if (count > 0) {
			ResultSet result = pstmt.getGeneratedKeys();

			if (result.next()) {
				id = result.getInt(1);

			}
		}

		return id;

	}

	public int CreateNewPurchaseInvoise(Invoise invoise) throws ClassNotFoundException, SQLException {
		Connection conn = connection.StartConnection();

		String sql = "INSERT INTO invoise(totalcost,date,time,saleorpurchase) " + "VALUES(?,?,?,?)";
		pstmt = conn.prepareStatement(sql, new String[] { "id" });

		pstmt.setLong(1, invoise.getTotalcost());
		pstmt.setDate(2, invoise.getDate());
		pstmt.setTime(3, invoise.getTime());
		pstmt.setString(4, invoise.getSaleOrPurchase());

		int count = pstmt.executeUpdate();
		int id = 0;
		if (count > 0) {
			ResultSet result = pstmt.getGeneratedKeys();
			if (result.next()) {
				id = result.getInt(1);

			}
		}

		return id;
	}

	public ResultSet GetAll() throws ClassNotFoundException, SQLException {
		Connection conn = connection.StartConnection();

		String sql = "SELECT * FROM invoise";
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);

		return rs;
	}

	public ResultSet SelectByDate(Date date) throws ClassNotFoundException, SQLException {

		Connection conn = connection.StartConnection();

		String sql = "SELECT * FROM invoise WHERE date = ?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setDate(1, date);

		ResultSet rs = pstmt.executeQuery();

		return rs;
	}

	public ResultSet SelectByBetween(Date date1, Date date2) throws ClassNotFoundException, SQLException {

		Connection conn = connection.StartConnection();

		String sql = "SELECT * FROM invoise WHERE date BETWEEN ? AND ?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setDate(1, date1);
		pstmt.setDate(2, date2);

		ResultSet rs = pstmt.executeQuery();

		return rs;
	}

}
