package Dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Commons.Odt;
import Commons.Operator;
import Connection.ConnectionToPsgsql;

public class OdtDao {

	private OperatorDao operatorDao = new OperatorDao();

	private ConnectionToPsgsql connection = new ConnectionToPsgsql();
	private PreparedStatement pstmt = null;
	private Statement stmt = null;

	public Odt SelectById(int id) throws SQLException, ClassNotFoundException {

		Connection conn = connection.StartConnection();

		String sql = "SELECT * FROM odt WHERE id = ?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, id);

		ResultSet rs = pstmt.executeQuery();

		Odt odt = new Odt();
		while (rs.next()) {
			odt.setId(rs.getInt("id"));
			odt.setOperatorid(rs.getInt("operatorid"));
			odt.setDate(rs.getDate("date"));
			odt.setStime(rs.getTime("stime"));
			odt.setEtime(rs.getTime("etime"));

			Operator tempOpe = operatorDao.SelectById(odt.getOperatorid());
			odt.setOperatorname(tempOpe.getName());
			odt.setOperatorfamily(tempOpe.getFamily());
		}
		return odt;
	}

	public Odt SelectLastByOperatorid(int operatorid) throws SQLException, ClassNotFoundException {

		java.util.Date utilDate = new java.util.Date();

		Connection conn = connection.StartConnection();

		String sql = "SELECT * FROM odt WHERE operatorid = ? AND date = ?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, operatorid);
		pstmt.setDate(2, new java.sql.Date(utilDate.getTime()));

		ResultSet rs = pstmt.executeQuery();

		Odt odt = null;
		while (rs.next()) {

			odt = new Odt();
			odt.setId(rs.getInt("id"));
			odt.setOperatorid(rs.getInt("operatorid"));
			odt.setDate(rs.getDate("date"));
			odt.setStime(rs.getTime("stime"));
			odt.setEtime(rs.getTime("etime"));

			Operator tempOpe = operatorDao.SelectById(odt.getOperatorid());
			odt.setOperatorname(tempOpe.getName());
			odt.setOperatorfamily(tempOpe.getFamily());

		}
		return odt;
	}

	public ResultSet GetAllByOperatorid(int operatorid) throws ClassNotFoundException, SQLException {

		Connection conn = connection.StartConnection();

		String sql = "SELECT * FROM odt WHERE operatorid = ?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, operatorid);

		ResultSet rs = pstmt.executeQuery();

		return rs;
	}

	public ResultSet GetAll() throws ClassNotFoundException, SQLException {
		Connection conn = connection.StartConnection();

		String sql = "SELECT * FROM odt";
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);

		return rs;
	}

	public int AddNewRecourd(Odt odt) throws ClassNotFoundException, SQLException {
		Connection conn = connection.StartConnection();

		String sql = "INSERT INTO odt(operatorid,date,stime,etime) " + "VALUES(?,?,?,?)";
		pstmt = conn.prepareStatement(sql);

		pstmt.setInt(1, odt.getOperatorid());
		pstmt.setDate(2, odt.getDate());
		pstmt.setTime(3, odt.getStime());
		pstmt.setTime(4, odt.getEtime());

		return pstmt.executeUpdate();
	}

	public void UpdateRecord(Odt odt) throws SQLException, ClassNotFoundException {

		Connection conn = connection.StartConnection();

		String sql = "UPDATE odt SET operatorid = ?, date = ?, stime = ?, etime = ? WHERE id = ? ";

		pstmt = conn.prepareStatement(sql);

		pstmt.setInt(1, odt.getOperatorid());
		pstmt.setDate(2, odt.getDate());
		pstmt.setTime(3, odt.getStime());
		pstmt.setTime(4, odt.getEtime());
		pstmt.setInt(5, odt.getId());

		pstmt.executeUpdate();
	}

	public void DeleteRecord(Odt odt) throws ClassNotFoundException, SQLException {

		Connection conn = connection.StartConnection();

		String sql = "DELETE FROM odt WHERE id = ?";

		pstmt = conn.prepareStatement(sql);

		pstmt.setInt(1, odt.getId());

		pstmt.executeUpdate();

	}

	public ResultSet GetAllByOperatoridBetweenDates(int operatorid, Date date1, Date date2)
			throws ClassNotFoundException, SQLException {
		Connection conn = connection.StartConnection();

		String sql = "SELECT * FROM odt WHERE operatorid = ? AND date BETWEEN ? AND ?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, operatorid);
		pstmt.setDate(2, date1);
		pstmt.setDate(3, date2);

		ResultSet rs = pstmt.executeQuery();

		return rs;
	}
}
