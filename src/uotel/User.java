package uotel;

/**
 * Created by bernardcosgriff on 3/15/17.
 */
public class User {
    protected int id;
    protected String firstName;
    protected String lastName;
    protected String login;
    protected String password;
    protected String phoneNumber;
    protected Address address;

    public User(int id, String firstName, String lastName, String login,
                String password, String phoneNumber, Address address){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.login = login;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    @Override
    public String toString() {
        String s = "ID: " + id + '\n';
        s += "First Name: " + firstName + '\n';
        s += "Last Name: " + lastName + '\n';
        s += "Phone Number: " + phoneNumber + '\n';
        s += "Address: " + address.toString() + "\n";
        return s;
    }
}
