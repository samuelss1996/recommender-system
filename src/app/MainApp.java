package app;

public class MainApp {
    private static final String DEFAULT_ENGINE_PATH = "engine.clp";

    public static void main(String[] args) {
        Recommender recommender = new Recommender(args.length == 1? args[0] : DEFAULT_ENGINE_PATH);

        recommender.startEngine();
        recommender.getRecommendations();
    }
}