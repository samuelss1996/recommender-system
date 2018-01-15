package app.data;

public class Recommendation {
    private final int productId;
    private final int userId;

    public Recommendation(int productId, int userId) {
        this.productId = productId;
        this.userId = userId;
    }

    @Override
    public String toString() {
        return String.format("Producto: %d --> Usuario %d", this.productId, this.userId);
    }
}
