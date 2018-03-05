package idv.ron.server.products;

import java.util.List;

public interface ProductDao {
	int insert(Product product);
	int update(Product product);
	int delete(int id);
	Product findById(int id);
	List<Product> getAll();
	byte[] getImage(int id);
}
