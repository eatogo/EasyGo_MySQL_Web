package idv.ron.server.orders;

public class OrderMaster {
	private int orderId;
	private String userId;
	private long date;

	public OrderMaster(int orderId, String userId, long date) {
		super();
		this.orderId = orderId;
		this.userId = userId;
		this.date = date;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

}
