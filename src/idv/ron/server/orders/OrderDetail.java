package idv.ron.server.orders;

import java.util.List;

public class OrderDetail {
	private int orderId;
	private List<OrderProduct> orderProductList;

	public OrderDetail() {
		super();
	}

	public OrderDetail(int orderId, List<OrderProduct> orderProductList) {
		super();
		this.orderId = orderId;
		this.orderProductList = orderProductList;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public List<OrderProduct> getOrderProductList() {
		return orderProductList;
	}

	public void setOrderProductList(List<OrderProduct> orderProductList) {
		this.orderProductList = orderProductList;
	}

}
