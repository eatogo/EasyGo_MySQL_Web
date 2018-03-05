package idv.ron.server.members;

import idv.ron.server.main.Common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;





public class MemberDaoMySqlImpl implements MemberDao {

	public MemberDaoMySqlImpl() {
		super();
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean memberExist(String user_cellphone, String user_password) {
		String sql = "SELECT user_cellphone FROM Users WHERE user_cellphone = ? AND user_password  = ?;";
		Connection conn = null;
		PreparedStatement ps = null;
		boolean isMember = false;
		try {
			conn = DriverManager.getConnection(Common.URL, Common.USER,
					Common.PASSWORD);
			ps = conn.prepareStatement(sql);
			ps.setString(1, user_cellphone);
			String encrypedString = GlobalService.encryptString(user_password);
			ps.setString(2,GlobalService.getMD5Endocing(encrypedString));
			ResultSet rs = ps.executeQuery();
			isMember = rs.next();
			return isMember;
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
		return isMember;
	}

	@Override
	public boolean memberIdExist(String user_cellphone) {
		String sql = "SELECT user_cellphone FROM users WHERE user_cellphone = ?;";
		Connection conn = null;
		PreparedStatement ps = null;
		boolean isUserIdExist = false;
		try {
			conn = DriverManager.getConnection(Common.URL, Common.USER,
					Common.PASSWORD);
			ps = conn.prepareStatement(sql);
			ps.setString(1, user_cellphone);
			ResultSet rs = ps.executeQuery();
			isUserIdExist = rs.next();
			return isUserIdExist;
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
		return isUserIdExist;
	}

	@Override
	public int insert(Member mb) {
		String sql = "INSERT INTO Users(user_cellphone, user_password, user_name, user_email, user_avatar, user_create_time, user_status, user_identity, user_store) "
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?);";
		Connection conn = null;
		PreparedStatement ps = null;
		int count = 0;

		try {
			conn = DriverManager.getConnection(Common.URL, Common.USER,
					Common.PASSWORD);
			ps = conn.prepareStatement(sql);
			ps.setString(1, mb.getUser_cellphone());
			String encrypedString = GlobalService.encryptString(mb.getUser_password());
			ps.setString(2, GlobalService.getMD5Endocing(encrypedString));
			ps.setString(3, mb.getUser_name());
			ps.setString(4, mb.getUser_email());
			ps.setString(5, mb.getUser_avatar());
			java.sql.Timestamp now = new java.sql.Timestamp(System.currentTimeMillis());
			ps.setTimestamp(6,now);
			
			ps.setString(7,mb.getUser_status());
			ps.setString(8,mb.getUser_identity());
			ps.setInt(9, mb.getUser_store());
			count = ps.executeUpdate();
			
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return count;
	}

	@Override
	public int update(Member mb) {
		String sql = "UPDATE Users "
				+ "SET user_password = ?, user_name = ?, user_email = ?, user_avatar = ?, user_create_time = ?, user_status = ?, user_identity = ?, user_store = ? "
				+ "WHERE user_id = ?;";
		Connection conn = null;
		PreparedStatement ps = null;
		int count = 0;
		try {
			conn = DriverManager.getConnection(Common.URL, Common.USER,
					Common.PASSWORD);
			ps = conn.prepareStatement(sql);
			String encrypedString = GlobalService.encryptString(mb.getUser_password());
			ps.setString(1, encrypedString);
			ps.setString(2, mb.getUser_name());
			ps.setString(3, mb.getUser_email());
			ps.setString(4, mb.getUser_avatar());
			java.sql.Timestamp now = new java.sql.Timestamp(System.currentTimeMillis());
			ps.setTimestamp(5,now);
			count = ps.executeUpdate();
			ps.setString(6,mb.getUser_status());
			ps.setString(7,mb.getUser_identity());
			ps.setInt(8, mb.getUser_store());
			ps.setString(9, mb.getUser_cellphone());
			count = ps.executeUpdate();
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return count;
	}

	@Override
	public int delete(String id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Member findById(String user_cellphone) {
		String sql = "SELECT user_id, user_password, user_name, user_email, user_avater, user_create_time, user_status, user_identity, user_store FROM users WHERE user_cellphone = ?;";
		Connection conn = null;
		PreparedStatement ps = null;
		Member member = null;
		try {
			conn = DriverManager.getConnection(Common.URL, Common.USER,
					Common.PASSWORD);
			ps = conn.prepareStatement(sql);
			ps.setString(1, user_cellphone);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				Integer user_id=rs.getInt(1);
				String passwordfromdatabase=rs.getString(2);
				String user_password=GlobalService.decryptString(
						GlobalService.KEY, passwordfromdatabase);
				String user_name = rs.getString(3);
				String user_email = rs.getString(4);
				String user_avater = rs.getString(5);
				Timestamp user_create_time = rs.getTimestamp(6);
				String user_status = rs.getString(7);
				String user_identity = rs.getString(8);
				Integer user_store = rs.getInt(9);
				member = new  Member(user_id, user_cellphone, user_password,user_name, user_email,
						user_avater, user_create_time, user_status, user_identity,
						user_store);
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
		return member;
	}

	@Override
	public List<Member> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
