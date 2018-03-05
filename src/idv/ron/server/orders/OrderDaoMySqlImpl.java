package idv.ron.server.orders;

import idv.ron.server.main.Common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OrderDaoMySqlImpl implements OrderDao {

	public OrderDaoMySqlImpl() {
		super();
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Order findById(int orderId) {
		OrderMaster master = findMasterById(orderId);
		OrderDetail detail = findDetailById(orderId);
		Order order = new Order(orderId, master.getUserId(), master.getDate(),
				detail.getOrderProductList());
		return order;
	}

	@Override
	public int insert(String userId, List<OrderProduct> cart) {
		Connection conn = null;
		PreparedStatement psMaster = null;
		PreparedStatement psDetail = null;
		String sqlMaster = "INSERT INTO OrderMaster(userId) VALUES(?);";
		String sqlDetail = "INSERT INTO OrderDetail(orderId, productId, quantity) VALUES(?, ?, ?);";
		int orderId = -1;

		try {
			conn = DriverManager.getConnection(Common.URL, Common.USER,
					Common.PASSWORD);
			// roll back the transaction while insertion failure
			conn.setAutoCommit(false);
			// insert a new order and return the auto-increment order id
			psMaster = conn.prepareStatement(sqlMaster,
					Statement.RETURN_GENERATED_KEYS);
			psMaster.setString(1, userId);
			psMaster.executeUpdate();
			// get the generated order id
			ResultSet rs = psMaster.getGeneratedKeys();
			if (rs.next()) {
				orderId = rs.getInt(1);
			}
			for (OrderProduct orderProduct : cart) {
				int productId = orderProduct.getId();
				int quantity = orderProduct.getQuantity();
				psDetail = conn.prepareStatement(sqlDetail);
				psDetail.setInt(1, orderId);
				psDetail.setInt(2, productId);
				psDetail.setInt(3, quantity);
				psDetail.executeUpdate();
			}
			// commit without error
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				// roll back while SQLException
				conn.rollback();
				orderId = -1;
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				if (psMaster != null) {
					psMaster.close();
				}
				if (psDetail != null) {
					psDetail.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return orderId;
	}

	@Override
	public List<Order> getAll(String userId, String start, String end) {
		Connection conn = null;
		PreparedStatement ps = null;
		List<Order> orderList = new ArrayList<>();
		try {
			conn = DriverManager.getConnection(Common.URL, Common.USER,
					Common.PASSWORD);
			String sql;
			if (start == null || end == null || start.isEmpty()
					|| end.isEmpty()) {
				sql = "SELECT orderId FROM OrderMaster " + "WHERE userId = ? "
						+ "ORDER BY timestamp DESC;";
				ps = conn.prepareStatement(sql);
				ps.setString(1, userId);
			} else {
				sql = "SELECT orderId FROM OrderMaster "
						+ "WHERE userId = ? AND timestamp BETWEEN ? AND ? "
						+ "ORDER BY timestamp DESC;";
				ps = conn.prepareStatement(sql);
				ps.setString(1, userId);
				ps.setString(2, start);
				// using BETWEEN keyword, time should be added or
				// record set that occurred during the day will be missing
				ps.setString(3, end + " 23:59:59");
			}
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int orderId = rs.getInt(1);
				OrderMaster master = findMasterById(orderId);
				OrderDetail detail = findDetailById(orderId);
				Order order = new Order(orderId, master.getUserId(),
						master.getDate(), detail.getOrderProductList());
				orderList.add(order);
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
		return orderList;
	}

	private OrderMaster findMasterById(int orderId) {
		String sql = "SELECT name, OrderMaster.timestamp FROM OrderMaster, Member "
				+ "WHERE OrderMaster.userId = Member.userId AND orderId = ?;";
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DriverManager.getConnection(Common.URL, Common.USER,
					Common.PASSWORD);
			ps = conn.prepareStatement(sql);
			ps.setInt(1, orderId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				String user = rs.getString(1);
				long date = rs.getTimestamp(2).getTime();
				OrderMaster orderMaster = new OrderMaster(orderId, user, date);
				return orderMaster;
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

	private OrderDetail findDetailById(int orderId) {
		String sql = "SELECT Product.id, Product.name, price, description, quantity "
				+ "FROM OrderDetail, Product, Spot "
				+ "WHERE orderId = ? AND OrderDetail.productId = Product.id AND Product.spotId = Spot.id;";

		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DriverManager.getConnection(Common.URL, Common.USER,
					Common.PASSWORD);
			ps = conn.prepareStatement(sql);
			ps.setInt(1, orderId);
			ResultSet rs = ps.executeQuery();
			List<OrderProduct> orderProductList = new ArrayList<OrderProduct>();
			while (rs.next()) {
				int id = rs.getInt(1);
				String name = rs.getString(2);
				double price = rs.getDouble(3);
				String description = rs.getString(4);
				int quantity = rs.getInt(5);
				OrderProduct orderProduct = new OrderProduct(id, name, price,
						description, quantity);
				orderProductList.add(orderProduct);
			}
			OrderDetail orderDetail = new OrderDetail(orderId, orderProductList);
			return orderDetail;
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
