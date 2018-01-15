package app.data;

import java.util.List;

public class Product {
    private final int productId;
    private final String productName;
    private final List<String> tags;

    public Product(int productId, String productName, List<String> tags) {
        this.productId = productId;
        this.productName = productName;
        this.tags = tags;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public List<String> getTags() {
        return tags;
    }
}