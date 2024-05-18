package Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionToPsgsql {

	private static final String JDBC_DRIVER = "org.postgresql.Driver";
	private final static String DB_URL = "jdbc:postgresql://localhost/Market";

	private final static String USER = "postgres";
	private final static String PASS = "1234";

	private Connection conn = null;

	public Connection StartConnection() {
		try {
			Class.forName(JDBC_DRIVER);
			this.conn = DriverManager.getConnection(DB_URL, USER, PASS);

		} catch (Exception e) {
			System.out.println("Error in Connecting To DataBase!!");
		}
		return this.conn;
	}

	public void StopConnection() throws SQLException {

		this.conn = null;
		this.conn.close();
	}
}
