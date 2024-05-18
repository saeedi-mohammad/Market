package Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Commons.Group;
import Commons.Item;
import Connection.ConnectionToPsgsql;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ItemDao {

	GroupDao groupDao = new GroupDao();

	private ConnectionToPsgsql connection = new ConnectionToPsgsql();
	private PreparedStatement pstmt = null;
	private Statement stmt = null;

	public Item SelectById(int id) throws SQLException, ClassNotFoundException {

		Connection conn = connection.StartConnection();

		String sql = "SELECT * FROM item WHERE id = ?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, id);

		ResultSet rs = pstmt.executeQuery();

		ObservableList<Group> groups = FXCollections.observableArrayList();
		ResultSet grs = groupDao.getAllGroups();

		while (grs.next()) {

			Group group = new Group();
			group.setId(grs.getInt("id"));
			group.setName(grs.getString("name"));
			groups.add(group);
		}

		Item item = new Item();
		while (rs.next()) {
			item.setId(rs.getInt("id"));
			item.setName(rs.getString("name"));
			item.setSerial(rs.getLong("serial"));
			item.setWeight(rs.getDouble("weight"));
			item.setNumber(rs.getInt("number"));
			item.setPurchasePrice(rs.getLong("purchaseprice"));
			item.setRetailPrice(rs.getLong("retailprice"));
			item.setWholesalePrice(rs.getLong("wholesaleprice"));
			item.setGroupid(rs.getInt("groupid"));

			int x;
			for (x = 0; x < groups.size(); x++)
				if (item.getGroupid() == groups.get(x).getId())
					break;
			item.setGroupname(groups.get(x).getName());

		}

		return item;

	}

	public int AddNewItemWithoutPrices(Item item) throws ClassNotFoundException, SQLException {

		Connection conn = connection.StartConnection();

		String sql = "INSERT INTO item(name,serial,weight,number,purchaseprice"
				+ ",retailprice,wholesaleprice,groupid) " + "VALUES(?,?,?,?,?,?,?,?)";
		pstmt = conn.prepareStatement(sql);

		pstmt.setString(1, item.getName());
		pstmt.setLong(2, item.getSerial());
		pstmt.setDouble(3, item.getWeight());
		pstmt.setInt(4, item.getNumber());
		pstmt.setLong(5, 0);
		pstmt.setLong(6, 0);
		pstmt.setLong(7, 0);
		pstmt.setInt(8, item.getGroupid());

		return pstmt.executeUpdate();

	}

	public int AddNewItemWithPrices(Item item) throws ClassNotFoundException, SQLException {

		Connection conn = connection.StartConnection();

		String sql = "INSERT INTO item(name,serial,weight,number,purchaseprice,petailprice,wholesaleprice,groupid) "
				+ "VALUES(?,?,?,?,?,?,?,?)";
		pstmt = conn.prepareStatement(sql);

		pstmt.setString(1, item.getName());
		pstmt.setLong(2, item.getSerial());
		pstmt.setDouble(3, item.getWeight());
		pstmt.setInt(4, item.getNumber());
		pstmt.setLong(5, item.getPurchasePrice());
		pstmt.setLong(6, item.getRetailPrice());
		pstmt.setLong(7, item.getWholesalePrice());
		pstmt.setInt(8, item.getGroupid());

		return pstmt.executeUpdate();

	}

	public ResultSet GetAllItems() throws ClassNotFoundException, SQLException {
		Connection conn = connection.StartConnection();

		String sql = "SELECT * FROM item";
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);

		return rs;
	}

	public void UpdateItem(Item item) throws SQLException, ClassNotFoundException {

		Connection conn = connection.StartConnection();

		String sql = "UPDATE item SET name = ?, serial = ?, weight = ?, number = ?, retailprice = ?, wholesaleprice = ? , purchaseprice = ?, groupid = ? WHERE id = ? ";

		pstmt = conn.prepareStatement(sql);

		pstmt.setString(1, item.getName());
		pstmt.setLong(2, item.getSerial());
		pstmt.setDouble(3, item.getWeight());
		pstmt.setInt(4, item.getNumber());
		pstmt.setLong(5, item.getRetailPrice());
		pstmt.setLong(6, item.getWholesalePrice());
		pstmt.setLong(7, item.getPurchasePrice());
		pstmt.setInt(8, item.getGroupid());
		pstmt.setInt(9, item.getId());

		pstmt.executeUpdate();
	}

	public void UpdateItemValue(Item item) throws SQLException, ClassNotFoundException {

		Connection conn = connection.StartConnection();

		String sql = "UPDATE item SET weight = ?, number = ? WHERE id = ? ";

		pstmt = conn.prepareStatement(sql);

		pstmt.setDouble(1, item.getWeight());
		pstmt.setInt(2, item.getNumber());
		pstmt.setInt(3, item.getId());

		pstmt.executeUpdate();
	}

	public void DeleteItem(Item item) throws ClassNotFoundException, SQLException {
		Connection conn = connection.StartConnection();

		String sql = "DELETE FROM item WHERE id = ?";
		pstmt = conn.prepareStatement(sql);

		pstmt.setInt(1, item.getId());

		pstmt.executeUpdate();
	}

}
