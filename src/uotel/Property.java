package uotel;

public class Property {
    public int id;
    public String name;
    public int ownerId;
    public Address address;
    public Category category;
    public String phoneNumber;
    public String url;
    public String yearBuilt;

    public Property(int id,
                    String name,
                    int ownerId,
                    Address address,
                    Category category,
                    String phoneNumber,
                    String url,
                    String yearBuilt) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.address = address;
        this.category = category;
        this.phoneNumber = phoneNumber;
        this.url = url;
        this.yearBuilt = yearBuilt;
    }

    public Property(Property other) {
        this.id = other.id;
        this.name = other.name;
        this.ownerId = other.ownerId;
        this.address = other.address;
        this.category = other.category;
        this.phoneNumber = other.phoneNumber;
        this.url = other.url;
        this.yearBuilt = other.yearBuilt;
    }

    @Override
    public String toString() {
        String s = "ID: " + id + "\n";
        s += "Name: " + name + "\n";
        s += "Address: " + address.toString() + "\n";
        s += "Category: " + category + "\n";
        s += "Phone Number: " + phoneNumber + "\n";
        s += "URL: " + url + "\n";
        s += "Year Built: " + yearBuilt + "\n";
        return s;
    }
}

