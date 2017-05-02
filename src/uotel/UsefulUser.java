package uotel;

import java.text.DecimalFormat;

/**
 * Created by bernardcosgriff on 3/22/17.
 */
public class UsefulUser extends User {

    double usefulnessRating;

    public UsefulUser(int id, String firstName, String lastName, String login,
                      String password, String phoneNumber, Address address, double usefulnessRating) {
        super(id, firstName, lastName, login, password, phoneNumber, address);
        this.usefulnessRating = usefulnessRating;
    }

    @Override
    public String toString() {
        String s = "Average Usefulness Rating: " + new DecimalFormat("0.00").format(usefulnessRating) + '\n';
        s += "Username: " + login + '\n';
        return s;
    }
}
