package uotel;

/**
 * Created by bernardcosgriff on 3/22/17.
 */
public class PopularProperty extends Property {

    int reservationCount;

    public PopularProperty(int id,
                           String name,
                           int ownerId,
                           Address address,
                           Category category,
                           String phoneNumber,
                           String url,
                           String yearBuilt,
                           int reservationCount) {
        super(id, name, ownerId, address, category, phoneNumber, url, yearBuilt);
        this.reservationCount = reservationCount;
    }

    @Override
    public String toString() {
        String s = "Number of Reservations: " + reservationCount + '\n';
        s += super.toString();
        return s;
    }
}
