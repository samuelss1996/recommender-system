package app.data;

public class Recommendation {
    private final Product product;
    private final User user;

    public Recommendation(Product product, User user) {
        this.product = product;
        this.user = user;
    }

    @Override
    public String toString() {
        return String.format("Recomendación: %s", this.product.getProductName());
    }
}
