package app;

import app.data.Product;
import app.data.Recommendation;
import app.data.User;

import java.util.List;
import java.util.Scanner;

public class Shop {
    private final Recommender recommender;
    private User user;

    public Shop(Recommender recommender) {
        this.recommender = recommender;
    }

    public void start() {
        this.recommender.startEngine();
        this.loginMenu();

        while (true) {
            switch(this.mainMenu()) {
                case SHOW_RECOMMENDATIONS:
                    this.showRecommendations();
                    break;
                case BUY_PRODUCTS:
                    this.buyProducts();
                    break;
                case EXIT:
                    return;
            }
        }
    }

    private void loginMenu() {
        System.out.print("Bienvenido. Introduzca su nombre de usuario: ");
        String username = new Scanner(System.in).next();

        this.user = new User(null, username);
        this.recommender.storeUserIfNotExists(this.user);
    }

    private MainMenuAction mainMenu() {
        MainMenuAction chosenAction = null;

        while(chosenAction == null) {
            System.out.println("Hola, " + this.user.getUsername() + ". Puedes realizar las siguientes tareas");
            System.out.println(MainMenuAction.actionsToString("\t"));
            System.out.print("Acción (introducir número): ");

            chosenAction = MainMenuAction.findById(new Scanner(System.in).next());
        }

        return chosenAction;
    }

    private void buyProducts() {
        for (Product product : this.recommender.getProducts()) {
            System.out.println(product.getProductName());
        }
    }

    private void showRecommendations() {
        List<Recommendation> recommendations = this.recommender.getRecommendations(this.user);

        if (!recommendations.isEmpty()) {
            for (Recommendation recommendation : recommendations) {
                System.out.println(recommendation.toString());
            }
        } else {
            System.out.println("Parece que no tienes ninguna recomendación todavía.");
            System.out.println("Prueba a comprar un par de productos para concocer tus intereses.");
        }
    }

    private enum MainMenuAction {
        SHOW_RECOMMENDATIONS("Ver mis recomendaciones"),
        BUY_PRODUCTS("Comprar productos"),
        EXIT("Salir");

        private final String menuDescription;

        MainMenuAction(String menuDescription) {
            this.menuDescription = menuDescription;
        }

        public static String actionsToString(String prefix) {
            StringBuilder builder = new StringBuilder();

            for (MainMenuAction action : MainMenuAction.values()) {
                builder.append(prefix).append(action.ordinal() + 1).append(". ").append(action.menuDescription).append("\n");
            }

            return builder.toString();
        }

        public static MainMenuAction findById(String actionId) {
            try {
                for (MainMenuAction action : MainMenuAction.values()) {
                    if(Integer.valueOf(actionId) == action.ordinal() + 1) {
                        return action;
                    }
                }
            } catch (NumberFormatException ignored) { }

            return null;
        }
    }
}