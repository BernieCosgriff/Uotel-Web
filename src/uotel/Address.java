package uotel;

public class Address {
    public final int id;
    public final String street;
    public final String unitNumber;
    public final String zipcode;
    public final String city;
    public final String state;
    public final String country;

    public Address(int id,
                   String street,
                   String unitNumber,
                   String zipcode,
                   String city,
                   String state,
                   String country) {
        this.id = id;
        this.street = street;
        this.unitNumber = unitNumber;
        this.zipcode = zipcode;
        this.city = city;
        this.state = state;
        this.country = country;
    }

    @Override
    public String toString() {
        return unitNumber + " " + street + ", " + city + ", " + state + ", " + country + " " + zipcode;
    }
}
