package app;

import app.data.Product;
import app.data.Recommendation;
import app.data.User;
import net.sf.clipsrules.jni.*;

import java.util.ArrayList;
import java.util.List;

public class Recommender {
    private final String enginePath;
    private final Environment clips;

    public Recommender(String enginePath) {
        this.enginePath = enginePath;
        this.clips = new Environment();
    }

    public void startEngine() {
        this.clips.load(this.enginePath);
        this.clips.reset();
        this.clips.run();
    }

    // TODO change defaults
    public void storeUserIfNotExists(User user) {
        if(!this.existsUser(user)) {
            int userId = (user.getId() != null)? user.getId() : this.highestUserId() + 1;

            user.setId(userId);
            this.clips.eval(String.format("(assert (user (id %d) (name %s) (age 21) (sex m)))", user.getId(), user.getUsername()));
        }
    }

    public List<Product> getProducts() {
        MultifieldValue clipsProducts = (MultifieldValue) this.clips.eval("(find-all-facts ((?f product)) TRUE)");
        List<Product> products = new ArrayList<>();

        try {
            for (int i = 0; i < clipsProducts.size(); i++) {
                FactAddressValue product = (FactAddressValue) clipsProducts.multifieldValue().get(i);

                int id = ((IntegerValue) product.getFactSlot("id")).intValue();
                String name = ((SymbolValue) product.getFactSlot("name")).symbolValue();
                List tags = ((MultifieldValue) product.getFactSlot("tags")).multifieldValue();

                products.add(new Product(id, name, tags));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return products;
    }

    public List<Recommendation> getRecommendations(User user) {
        MultifieldValue clipsRecommendations = (MultifieldValue) this.clips.eval("(find-all-facts ((?f recommendation)) TRUE)");
        List<Recommendation> recommendations = new ArrayList<>();

        try {
            for (int i = 0; i < clipsRecommendations.size(); i++) {
                FactAddressValue recommendation = (FactAddressValue) clipsRecommendations.multifieldValue().get(i);

                int productId = ((IntegerValue) recommendation.getFactSlot("product_id")).intValue();
                int userId = ((IntegerValue) recommendation.getFactSlot("user_id")).intValue();

                if (user.getId() == userId) {
                    recommendations.add(new Recommendation(productId, userId));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return recommendations;
    }

    private List<User> findAllUsers() {
        MultifieldValue clipsUsers = (MultifieldValue) this.clips.eval("(find-all-facts ((?f user)) TRUE)");
        List<User> users = new ArrayList<>();

        try {
            for (int i = 0; i < clipsUsers.size(); i++) {
                FactAddressValue user = (FactAddressValue) clipsUsers.multifieldValue().get(i);

                int id = ((IntegerValue) user.getFactSlot("id")).intValue();
                String name = ((SymbolValue) user.getFactSlot("name")).symbolValue();

                users.add(new User(id, name));
            }
        } catch (Exception ignored) { }

        return users;
    }

    private boolean existsUser(User user) {
        for (User currentUser : this.findAllUsers()) {
            if(currentUser.getUsername().equals(user.getUsername())) {
                return true;
            }
        }

        return false;
    }

    private int highestUserId() {
        int result = 0;

        for (User user : this.findAllUsers()) {
            result = (user.getId() > result)? user.getId() : result;
        }

        return result;
    }
}