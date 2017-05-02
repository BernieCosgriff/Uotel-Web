package uotel;

/**
 * Created by bernardcosgriff on 3/22/17.
 */
public class TrustedUser extends User {

    int trustRating;

    public TrustedUser(int id, String firstName, String lastName, String login,
                       String password, String phoneNumber, Address address, int trustRating) {
        super(id, firstName, lastName, login, password, phoneNumber, address);
        this.trustRating = trustRating;
    }

    @Override
    public String toString() {
        String s = "Overall Trust Rating: " + trustRating + '\n';
        s += "Username: " + login + '\n';
        return s;
    }
}
