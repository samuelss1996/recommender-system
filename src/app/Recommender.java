package app;

import net.sf.clipsrules.jni.Environment;
import net.sf.clipsrules.jni.FactAddressValue;
import net.sf.clipsrules.jni.MultifieldValue;

public class Recommender {
    private final String enginePath;
    private final Environment clips;

    public Recommender(String enginePath) {
        this.enginePath = enginePath;
        this.clips = new Environment();
    }

    public final void startEngine() {
        this.clips.load(this.enginePath);
        this.clips.reset();
        this.clips.run();
    }

    // TODO change this to retrieve recommendations instead of products
    public final void getRecommendations() {
        MultifieldValue recommendations = (MultifieldValue) clips.eval("(find-all-facts ((?f product)) TRUE)");

        try {
            for (int i = 0; i < recommendations.size(); i++) {
                FactAddressValue recommendation = (FactAddressValue) recommendations.multifieldValue().get(i);
                System.out.println(recommendation.getFactSlot("id"));
                System.out.println(recommendation.getFactSlot("name"));
            }
        } catch (Exception ignored) { }
    }
}