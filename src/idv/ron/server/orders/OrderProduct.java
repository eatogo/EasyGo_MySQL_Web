package idv.ron.server.orders;

import idv.ron.server.products.Product;

public class OrderProduct extends Product {
	private int quantity;

	public OrderProduct() {
		super();
	}

	public OrderProduct(int id, String name, double price, String description,
			int quantity) {
		super(id, name, price, description);
		this.quantity = quantity;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
