package activitytest.android.com.estimoteandroidapp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Content {

    /**
     * An array of estimotes.
     */
    public static final List<Estimote> ESTIMOTES = new ArrayList<Estimote>();

    /**
     * A map of estimotes, by ID.
     */
    public static final Map<String, Estimote> ESTIMOTE_MAP = new HashMap<String, Estimote>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addEstimote(createEstimote(i));
        }
    }

    private static void addEstimote(Estimote estimote) {
        ESTIMOTES.add(estimote);
        ESTIMOTE_MAP.put(estimote.uuid, estimote);
    }

    private static Estimote createEstimote(int position) {
        return new Estimote(String.valueOf(position), "Estimote " + position, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Estimote: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore temperature information here.");
        }
        return builder.toString();
    }

    /**
     * An estimote representing a piece of color.
     */
    public static class Estimote {
        public final String uuid;
        public final String color;
        public final String temperature;

        public Estimote(String uuid, String color, String temperature) {
            this.uuid = uuid;
            this.color = color;
            this.temperature = temperature;
        }

        @Override
        public String toString() {
            return color;
        }
    }
}
