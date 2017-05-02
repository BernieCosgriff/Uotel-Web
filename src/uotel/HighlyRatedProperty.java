package uotel;

import java.text.DecimalFormat;

/**
 * Created by bernardcosgriff on 3/22/17.
 */
public class HighlyRatedProperty extends Property {

    double score;

    public HighlyRatedProperty(int id,
                           String name,
                           int ownerId,
                           Address address,
                           Category category,
                           String phoneNumber,
                           String url,
                           String yearBuilt,
                           double score) {
        super(id, name, ownerId, address, category, phoneNumber, url, yearBuilt);
        this.score = score;
    }

    @Override
    public String toString() {
        String s = "Average Feedback Score: " + new DecimalFormat("0.00").format(score) + '\n';
        s += super.toString();
        return s;
    }

}
