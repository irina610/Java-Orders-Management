package business;

import dataAccess.GenericDAO;
import model.Product;
import java.util.List;

public class ProductBLL {
    private final GenericDAO<Product> productDAO = new GenericDAO<>(Product.class);

    public List<Product> getAllProducts() {
        return productDAO.findAll();
    }

    public void addProduct(Product product) {
        productDAO.insert(product);
    }

    public void updateProduct(Product product) {
        productDAO.update(product, "id", product.getId());
    }

    public void deleteProduct(int id) {
        productDAO.delete("id", id);
    }

}
