package org.csu.mypetstore.service;

import org.csu.mypetstore.domain.Category;
import org.csu.mypetstore.domain.Product;
import org.csu.mypetstore.domain.Item;

import java.util.List;

public interface CatalogService {
    List<Category> getCategoryList();

    Category getCategory(String categoryId);

    Product getProduct(String productId);

    List<Product> getProductListByCategory(String categoryId);

    // TODO enable using more than one keyword
    List<Product> searchProductList(String keyword);

    public List<Item> getItemListByProduct(String productId);

    public Item getItem(String itemId);

    public boolean isItemInStock(String itemId);
}
