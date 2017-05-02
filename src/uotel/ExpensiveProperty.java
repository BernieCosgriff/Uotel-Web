package uotel;

import java.text.NumberFormat;

/**
 * Created by bernardcosgriff on 3/22/17.
 */
public class ExpensiveProperty extends Property {

    double cost;

    public ExpensiveProperty(int id,
                           String name,
                           int ownerId,
                           Address address,
                           Category category,
                           String phoneNumber,
                           String url,
                           String yearBuilt,
                           double cost) {
        super(id, name, ownerId, address, category, phoneNumber, url, yearBuilt);
        this.cost = cost;
    }

    @Override
    public String toString() {
        String s = "Average Cost Per Guest: " + NumberFormat.getCurrencyInstance().format(cost) + '\n';
        s += super.toString();
        return s;
    }
}
