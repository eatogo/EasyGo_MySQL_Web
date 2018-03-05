package idv.ron.server.products;

import idv.ron.server.main.Common;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoMySqlImpl implements ProductDao {

	public ProductDaoMySqlImpl() {
		super();
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int insert(Product product) {
		return 0;
	}

	@Override
	public int update(Product product) {
		return 0;
	}

	@Override
	public int delete(int id) {
		return 0;
	}

	@Override
	public byte[] getImage(int id) {
		String sql = "SELECT image FROM Product, Spot "
				+ "WHERE Product.spotId = Spot.id AND Product.id = ?;";
		Connection connection = null;
		PreparedStatement ps = null;
		byte[] image = null;
		try {
			connection = DriverManager.getConnection(Common.URL, Common.USER,
					Common.PASSWORD);
			ps = connection.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				image = rs.getBytes(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ps != null) {
					// When a Statement object is closed,
					// its current ResultSet object is also closed
					ps.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return image;
	}

	@Override
	public Product findById(int id) {
		String sql = "SELECT name, price, description "
				+ "FROM Product WHERE id = ?;";
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DriverManager.getConnection(Common.URL, Common.USER,
					Common.PASSWORD);
			ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				String name = rs.getString(1);
				double price = rs.getDouble(2);
				String description = rs.getString(3);
				Product product = new Product(id, name, price, description);
				return product;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public List<Product> getAll() {
		String sql = "SELECT id, name, price, description FROM Product;";
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DriverManager.getConnection(Common.URL, Common.USER,
					Common.PASSWORD);
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			List<Product> productList = new ArrayList<Product>();
			while (rs.next()) {
				int id = rs.getInt(1);
				String name = rs.getString(2);
				double price = rs.getDouble(3);
				String description = rs.getString(4);
				Product product = new Product(id, name, price, description);
				productList.add(product);
			}
			return productList;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
