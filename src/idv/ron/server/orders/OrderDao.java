package idv.ron.server.orders;

import java.util.List;

public interface OrderDao {

	Order findById(int orderId);

	int insert(String userId, List<OrderProduct> cart);

	List<Order> getAll(String userId, String start, String end);

}
