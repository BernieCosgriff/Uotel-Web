package uotel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TreeMap;

public class Main {

    private static int readInt(BufferedReader in) {
        while (true) {
            try {
                return Integer.parseInt(in.readLine());
            } catch (Exception e) {

            }
            System.out.println("Hey! That's not a valid option.");
        }
    }

    private static int readOption(BufferedReader in, int low, int high) {
        while (true) {
            int option = readInt(in);
            if (option >= low && option <= high) {
                return option;
            }
            System.out.println("Hey! That's not an option.");
        }
    }

    private static java.sql.Date readSqlDate(BufferedReader in) throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        while (true) {
            String raw = in.readLine();
            java.util.Date date = df.parse(raw);
            if (date == null) {
                System.out.println("Oops! Wrong format. Remember, it's MM-dd-yyyy");
                continue;
            }
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            return sqlDate;
        }
    }

    private static BigDecimal readCurrencyAmount(BufferedReader in) throws Exception {
        while (true) {
            String raw = in.readLine();
            try {
                BigDecimal res = new BigDecimal(raw);
                return res;
            } catch (NumberFormatException e) {
                System.out.println("That's not a valid amount!");
            }
        }
    }

    private static int readUserId(DBConn conn, BufferedReader in) throws Exception {
        while (true) {
            String login = in.readLine();
            try {
                int uid = conn.getUserId(login);
                return uid;
            } catch (Exception e) {
            }
            System.out.println("Sorry! There is no user with that login. Try again.");
        }
    }

    // Reads a category name from the user and creates it if it doesn't exist.
    // Returns the category id.
    private static int readCategory(DBConn conn, BufferedReader in) throws Exception {
        List<String> categories = conn.getPropertyCategories();
        while (true) {
            System.out.println("1     Show existing categories.");
            System.out.println("2     Enter a category name.");

            int option = readOption(in, 1, 2);
            if (option == 1) {
                System.out.println("");
                for (String category : categories) {
                    System.out.println(category);
                }
                System.out.println("");
            } else {
                break;
            }
        }
        System.out.println("Category:");
        String category = in.readLine();
        if (categories.contains(category)) {
            return conn.getPropertyCategoryId(category);
        }
        return conn.createPropertyCategory(category);

    }

    private static int createUserCmd(DBConn conn, BufferedReader in) throws Exception {
        System.out.println("First, we'll need your address info.");
        System.out.println("Enter your street:");
        String street = in.readLine();
        System.out.println("Enter your unit/house number:");
        String unitNumber = in.readLine();
        System.out.println("Enter your city:");
        String city = in.readLine();
        System.out.println("Enter your state/province:");
        String state = in.readLine();
        System.out.println("Enter your country:");
        String country = in.readLine();
        System.out.println("Enter your zipcode:");
        String zipcode = in.readLine();

        int addrId = conn.createAddress(street, unitNumber, zipcode, city, state, country);

        System.out.println("Great!");
        System.out.println("Now enter a login.");
        String login;
        while (true) {
            login = in.readLine();
            if (conn.loginExists(login)) {
                System.out.println("Sorry! That login already exists.");
            } else {
                break;
            }
        }
        System.out.println("Enter your first name:");
        String firstName = in.readLine();
        System.out.println("Enter your last name:");
        String lastName = in.readLine();
        System.out.println("Enter a password:");
        String password = in.readLine();
        System.out.println("Enter a phone number:");
        String phoneNumber = in.readLine();

        return conn.createUser(firstName, lastName, login, password, phoneNumber, addrId);
    }

    private static int loginUserCmd(DBConn conn, BufferedReader in) throws Exception {
        while (true) {
            System.out.println("");
            System.out.println("Enter your login:");
            String login = in.readLine();
            System.out.println("Enter your password:");
            String password = in.readLine();
            try {
                return conn.loginUser(login, password);
            } catch (IllegalArgumentException e) {
                System.out.println("Sorry! We don't recognize that login combo.");
            }
        }
    }

    private static void displayProperties(List<Property> properties) {
        System.out.println("");
        for (Property p : properties) {
            System.out.println("--------------------------------------------------");
            System.out.println(p);
        }
        System.out.println("--------------------------------------------------");
        System.out.println("");
    }

    private static void addReservationCmd(DBConn conn,
                                          BufferedReader in,
                                          int userId) throws Exception {
        List<Reservation.Order> orders = new ArrayList<>();
        while (true) {
            System.out.println("");
            System.out.println("1     Browse properties.");
            System.out.println("2     Create a reservation.");
            System.out.println("3     Finalize your pending reservations.");
            System.out.println("4     Cancel your reservations and exit this menu.");
            System.out.println("");

            int option = readOption(in, 1, 4);
            if (option == 1) {
                browsePropertiesCmd(conn, in, userId);
            } else if (option == 2) {
                Reservation.Order order = new Reservation.Order();
                order.userId = userId;
                System.out.println("Enter the property ID:");
                order.propertyId = readInt(in);
                if (!conn.propertyExists(order.propertyId)) {
                    System.out.println("Hmm. It looks like that ID doesn't match any property.");
                    System.out.println("You'll have to try again.");
                    continue;
                }
                System.out.println("Enter the start date for your stay (MM-dd-yyyy):");
                order.startDate = readSqlDate(in);
                System.out.println("Enter the end date for your stay (MM-dd-yyyy):");
                order.endDate = readSqlDate(in);
                System.out.println("Enter the number of guests you expect to bring:");
                order.guests = readInt(in);

                List<Listing> listings = conn.getListings(order.propertyId);
                boolean listingExists = false;
                for (Listing l : listings) {
                    if ((l.startDate.before(order.startDate) || l.startDate.equals(order.startDate)) &&
                            (l.endDate.equals(order.endDate) || l.endDate.after(order.endDate))) {
                        listingExists = true;
                        break;
                    }
                }
                if (!listingExists) {
                    System.out.println("Hmm. It looks like those dates don't match up with " +
                            "any listing for the property you gave.");
                    System.out.println("Sorry, you'll have to try again.");
                    continue;
                }
                orders.add(order);
                System.out.println("Nice! Reservation added to your pending orders.");
            } else if (option == 3) {
                if (orders.size() == 0) {
                    System.out.println("Hmm. It looks like you haven't made any reservations yet!");
                    continue;
                }
                System.out.println("Okay! Let's finalize those.");
                while (true) {
                    System.out.println("");
                    System.out.println("1     Review your pending reservations.");
                    System.out.println("2     Finalize all pending reservations.");
                    System.out.println("3     Cancel all pending reservations");
                    System.out.println("");

                    int action = readOption(in, 1, 3);
                    if (action == 1) {
                        List<Integer> toRemove = new ArrayList<>();
                        for (int i = 0; i < orders.size(); i++) {
                            System.out.println("Pending Reservation " + (i + 1) + ":");
                            System.out.println("--------------------------------------------------");
                            Reservation.Order order = orders.get(i);
                            System.out.println(order);
                            System.out.println("--------------------------------------------------");
                            System.out.println("");
                            System.out.println("Would you like to keep this reservation?");
                            System.out.println("1     Yes.");
                            System.out.println("2     No.");
                            System.out.println("");
                            int shouldKeep = readOption(in, 1, 2);
                            if (shouldKeep == 1) {
                                System.out.println("Okay. We'll keep this one.");
                            } else {
                                System.out.println("Okay. Removing this reservation.");
                                toRemove.add(i);
                            }
                        }
                        for (int i = toRemove.size() - 1; i >= 0; i--) {
                            orders.remove(toRemove.get(i));
                        }
                    } else if (action == 2) {
                        for (int i = 0; i < orders.size(); i++) {
                            Reservation.Order order = orders.get(i);
                            boolean success = conn.createReservation(order);
                            if (success) {
                                System.out.println("Pending reservation " + (i + 1) + " finalized.");
                            } else {
                                System.out.println("Oh no! There was a database problem " +
                                        "finalizing pending reservation " + (i + 1));
                            }
                        }

                        //
                        // Give suggestions.
                        //
                        boolean suggestionShown = false;
                        for (Reservation.Order order : orders) {
                            List<Property> suggestions = conn.getSuggestions(order.propertyId);
                            if (suggestions.size() == 0) {
                                continue;
                            }
                            if (!suggestionShown) {
                                System.out.println("");
                                System.out.println("Based on your picks, we also have some suggestions for you:");
                                System.out.println("");
                                suggestionShown = true;
                            }
                            System.out.println("Because you picked property " + order.propertyId + ":");
                            displayProperties(suggestions);
                        }
                        if (suggestionShown) {
                            System.out.println("");
                            System.out.println("Press enter when you're done browsing the suggestions.");
                            System.out.println("");
                        } else {
                            System.out.println("Unfortunately, We don't have any additional suggestions based on your reservations!");
                            System.out.println("Press enter to return to the main menu.");
                            System.out.println("");
                        }

                        in.readLine();
                        return;
                    } else {
                        System.out.println("Okay. We've cancelled your pending reservations.");
                        return;
                    }
                }

            } else {
                System.out.println("Okay.");
                if (orders.size() > 0) {
                    System.out.println("We've cancelled your pending reservations.");
                }
                return;
            }
        }
    }

    private static void displayReservations(List<Reservation> reservations) {
        System.out.println("");
        if (reservations.size() == 0) {
            System.out.println("You don't have any pending reservations.");
            System.out.println("");
            return;
        }
        System.out.println("Your reservations:");
        System.out.println("");
        for (Reservation r : reservations) {
            System.out.println("--------------------------------------------------");
            System.out.println(r);
            System.out.println("--------------------------------------------------");
            System.out.println("");
        }
        System.out.println("");
    }

    private static void viewReservationsCmd(DBConn conn,
                                            BufferedReader in,
                                            int userId) throws Exception {

        List<Reservation> reservations = conn.getReservations(userId);
        displayReservations(reservations);
        System.out.println("Press enter when you want to return to the main menu.");
        in.readLine();
    }

    private static void recordPropertyCmd(DBConn conn,
                                          BufferedReader in,
                                          int userId) throws Exception {
        System.out.println("First, we'll need the address.");
        System.out.println("Enter the street:");
        String street = in.readLine();
        System.out.println("Enter the unit/house number:");
        String unitNumber = in.readLine();
        System.out.println("Enter the city:");
        String city = in.readLine();
        System.out.println("Enter the state/province:");
        String state = in.readLine();
        System.out.println("Enter the country:");
        String country = in.readLine();
        System.out.println("Enter the zipcode:");
        String zipcode = in.readLine();

        int addrId = conn.createAddress(street, unitNumber, zipcode, city, state, country);

        System.out.println("Good!");
        System.out.println("Now enter the property name:");
        String name = in.readLine();
        System.out.println("Enter the property's phone number:");
        String phoneNumber = in.readLine();
        System.out.println("Enter a URL associated with the property:");
        String url = in.readLine();
        System.out.println("Enter the year built:");
        String yearBuilt = null;
        while (true) {
            yearBuilt = in.readLine();
            if (yearBuilt.length() == 4) {
                break;
            }
            System.out.println("Hey! That's not a year.");
        }
        System.out.println(yearBuilt);
        System.out.println("Now you need to pick a property category.");
        int categoryId = readCategory(conn, in);
        System.out.println("Great! Now enter any keywords that describe your property.");
        System.out.println("Enter a blank line when you're done.");
        List<String> keywords = new ArrayList<>();
        while (true) {
            String kw = in.readLine();
            if (kw.length() == 0) {
                break;
            }
            keywords.add(kw);
        }

        int propId = conn.createProperty(name, userId, addrId, categoryId, phoneNumber, url, yearBuilt);
        for (String kw : keywords) {
            conn.addPropertyKeyword(propId, kw);
        }
        System.out.println("Record created.");
    }

    private static void viewPropertiesCmd(DBConn conn,
                                          BufferedReader in,
                                          int userId) throws Exception {
        List<Property> properties = conn.getUserProperties(userId);
        System.out.println("");
        System.out.println("Your properties:");
        displayProperties(properties);
        System.out.println("Press enter to return to the main menu.");
        in.readLine();
    }

    private static void updatePropertyCmd(DBConn conn,
                                          BufferedReader in,
                                          int userId) throws Exception {
        List<Property> properties = conn.getUserProperties(userId);
        Property selected = null;
        while (true) {
            System.out.println("1     List your properties.");
            System.out.println("2     Enter the id of the property to update.");
            System.out.println("3     Exit from this menu.");
            System.out.println("");
            int option = readOption(in, 1, 3);
            if (option == 1) {
                System.out.println("");
                System.out.println("Your properties:");
                displayProperties(properties);
            } else if (option == 2) {
                System.out.println("Please enter the id.");
                int propId = readInt(in);
                for (Property p : properties) {
                    if (p.id == propId) {
                        selected = p;
                        break;
                    }
                }
                if (selected != null) {
                    break;
                }
                System.out.println("Hmm. We don't recognize that property.");
            } else {
                return;
            }
        }

        System.out.println("What would you like to update?");
        System.out.println("1     Name");
        System.out.println("2     Address");
        System.out.println("3     Category");
        System.out.println("4     Phone Number");
        System.out.println("5     URL");
        System.out.println("6     Year Built");
        System.out.println("7     Keywords");
        Property updated = new Property(selected);
        int option = readOption(in, 1, 7);
        switch (option) {
        case 1: {
            System.out.println("Enter a new name.");
            updated.name = in.readLine();
            break;
        }
        case 2: {
            System.out.println("Enter the street:");
            String street = in.readLine();
            System.out.println("Enter the unit/house number:");
            String unitNumber = in.readLine();
            System.out.println("Enter the city:");
            String city = in.readLine();
            System.out.println("Enter the state/province:");
            String state = in.readLine();
            System.out.println("Enter the country:");
            String country = in.readLine();
            System.out.println("Enter the zipcode:");
            String zipcode = in.readLine();
            int addrId = conn.createAddress(street, unitNumber, zipcode, city, state, country);
            updated.address = new Address(addrId, street, unitNumber, zipcode, city, state, country);
            break;
        }
        case 3: {
            System.out.println("Enter a new category.");
            int categoryId = readCategory(conn, in);
            updated.category = new Category(categoryId, ""); // TODO: Hacky.
            break;
        }
        case 4: {
            System.out.println("Enter a new phone number.");
            String phoneNumber = in.readLine();
            updated.phoneNumber = phoneNumber;
            break;
        }
        case 5: {
            System.out.println("Enter a new URL.");
            String url = in.readLine();
            updated.url = url;
            break;
        }
        case 6: {
            String yearBuilt = null;
            while (true) {
                System.out.println("Enter a new year built");
                yearBuilt = in.readLine();
                if (yearBuilt.length() == 4) {
                    break;
                }
                System.out.println("Hey! That's not a valid year.");
            }
            updated.yearBuilt = yearBuilt;
            break;
        }
        case 7: {
            System.out.println("Would you like to:");
            System.out.println("1     Remove existing keywords.");
            System.out.println("2     Add keywords to describe the property.");
            int kopt = readOption(in, 1, 2);
            if (kopt == 1) {
                List<String> keywords = conn.getPropertyKeywords(selected.id);
                System.out.println("Current keywords are:");
                for (String kw : keywords) {
                    System.out.println("\t" + kw);
                }
                System.out.println("Which keywords would you like to remove?");
                System.out.println("Enter each on a separate line, followed by a blank line when you're done.");
                List<String> toRemove = new ArrayList<>();
                while (true) {
                    String kw = in.readLine();
                    if (kw.length() == 0) {
                        break;
                    }
                    if (!keywords.contains(kw)) {
                        System.out.println("Oops! That keyword doesn't exist.");
                        continue;
                    }
                    toRemove.add(kw);
                }
                for (String kw : toRemove) {
                    conn.deletePropertyKeyword(selected.id, kw);
                }
                System.out.println("Done!");
                return;
            }
            System.out.println("Enter each keyword on a separate line.");
            System.out.println("Enter a blank line when you're done.");
            List<String> keywords = conn.getPropertyKeywords(selected.id);
            List<String> toAdd = new ArrayList<>();
            while (true) {
                String kw = in.readLine();
                if (kw.length() == 0) {
                    break;
                }
                if (keywords.contains(kw)) {
                    System.out.println("Your property is already using that keyword.");
                    continue;
                }
                toAdd.add(kw);
            }
            for (String kw : toAdd) {
                conn.addPropertyKeyword(selected.id, kw);
            }
            System.out.println("Done!");
            return;
        }
        default:
        }
        boolean success = conn.updateProperty(selected.id, updated);
        if (success) {
            System.out.println("Property updated!");
        } else {
            System.out.println("Sorry. The update failed!");
        }
    }

    private static void displayListings(List<NamedListing> listings) {
        System.out.println("Your listings: ");
        for (NamedListing l : listings) {
            System.out.println("--------------------------------------------------");
            System.out.println(l);
            System.out.println("--------------------------------------------------");
            System.out.println("");
        }
        System.out.println("");
    }

    private static void addListingCmd(DBConn conn,
                                      BufferedReader in,
                                      int userId) throws Exception {
        List<Property> properties = conn.getUserProperties(userId);
        Property selected = null;
        while (true) {
            System.out.println("1     Show your properties.");
            System.out.println("2     Show your current listings.");
            System.out.println("3     Enter the id of the property to list.");
            System.out.println("4     Exit from this menu.");
            System.out.println("");
            int option = readOption(in, 1, 3);
            if (option == 1) {
                System.out.println("");
                System.out.println("Your properties:");
                displayProperties(properties);
            } else if (option == 2) {
                List<NamedListing> allListings = conn.getAllUserListings(userId);
                displayListings(allListings);
            } else if (option == 3) {
                System.out.println("Enter the property id:");
                int propertyId = readInt(in);
                for (Property p : properties) {
                    if (p.id == propertyId) {
                        selected = p;
                        break;
                    }
                }
                if (selected != null) {
                    break;
                }
                System.out.println("Hmm. we don't recognize that property.");
            } else {
                return;
            }
        }
        System.out.println("Great! Now we'll just need to record the listing dates and price-per-night");
        List<Listing> currListings = conn.getListings(selected.id);
        java.sql.Date startDate;
        java.sql.Date endDate;
        while (true) {
            System.out.println("Enter the first date of availability, in MM-dd-yyyy format:");
            startDate = readSqlDate(in);
            System.out.println("Enter the last date of availability, in MM-dd-yyyy format:");
            endDate = readSqlDate(in);
            boolean overlap = false;
            for (Listing l : currListings) {
                if ((l.startDate.before(endDate) || l.startDate.equals(endDate)) &&
                    (l.endDate.after(startDate) || l.endDate.equals(startDate))) {
                    System.out.println("Oh no! It looks like those dates overlap " +
                                       "with an existing listing:");
                    System.out.println(l);
                    System.out.println("You'll have to pick a new set of dates.");
                    overlap = true;
                    break;
                }
            }
            if (!overlap) {
                break;
            }
        }
        System.out.println("Enter the price-per-night during the course of the listing as a decimal in xx.xx format:");
        BigDecimal pricePerNight = readCurrencyAmount(in);
        boolean success = conn.addListing(selected.id, startDate, endDate, pricePerNight);
        if (success) {
            System.out.println("Listing created.");
        } else {
            System.out.println("Sorry. The attempt to create the listing failed.");
        }
    }

    private static void viewListingsCmd(DBConn conn,
                                        BufferedReader in,
                                        int userId) throws Exception {
        List<NamedListing> listings = conn.getAllUserListings(userId);
        displayListings(listings);
        System.out.println("Press enter to return to the main menu.");
        in.readLine();
    }

    private static void addStayCmd(DBConn conn,
                                   BufferedReader in,
                                   int userId) throws Exception {
        List<Stay.Order> orders = new ArrayList<>();
        List<Reservation> reservations = conn.getReservations(userId);
        List<Stay> stays = conn.getStays(userId);
        if (reservations.size() == 0) {
            System.out.println("Hmm. It looks like you don't have any reservations to record a stay for.");
            return;
        }
        while (true) {
            System.out.println("");
            System.out.println("1     View your reservations.");
            System.out.println("2     Create a stay.");
            System.out.println("3     Finalize your pending stays.");
            System.out.println("4     Cancel your pending stays and exit this menu.");
            System.out.println("");

            int option = readOption(in, 1, 4);
            if (option == 1) {
                displayReservations(reservations);
            } else if (option == 2) {
                Stay.Order order = new Stay.Order();
                System.out.println("Enter the ID of the associated reservation:");
                order.reservationId = readInt(in);
                boolean reservationExists = false;
                for (Reservation r : reservations) {
                    if (r.id == order.reservationId) {
                        reservationExists = true;
                        break;
                    }
                }
                if (!reservationExists) {
                    System.out.println("You don't have a reservation with that ID!");
                    System.out.println("You'll have to try again.");
                    continue;
                }
                boolean stayAlreadyExists = false;
                for (Stay s : stays) {
                    if (s.reservation.id == order.reservationId) {
                        stayAlreadyExists = true;
                        break;
                    }
                }
                if (stayAlreadyExists) {
                    System.out.println("Sorry! You've already recorded a stay for that reservation.");
                    continue;
                }
                boolean alreadyAddedStay = false;
                for (Stay.Order prevOrder : orders) {
                    if (prevOrder.reservationId == order.reservationId) {
                        alreadyAddedStay = true;
                        break;
                    }
                }
                if (alreadyAddedStay) {
                    System.out.println("You already have a pending stay for that reservation!");
                    continue;
                }
                System.out.println("Great!");
                System.out.println("How much did you spend in total for this stay?");
                order.totalCost = readCurrencyAmount(in);
                orders.add(order);
                System.out.println("Excellent. We've added this stay to your pending stays.");
            } else if (option == 3) {
                if (orders.size() == 0) {
                    System.out.println("You haven't recorded any stays yet!");
                    continue;
                }
                System.out.println("Okay! Let's finalize those.");
                while (true) {
                    System.out.println("");
                    System.out.println("1     Review your pending stays.");
                    System.out.println("2     Finalize all pending stays.");
                    System.out.println("3     Cancel all pending stays.");
                    System.out.println("");

                    int action = readOption(in, 1, 3);
                    if (action == 1) {
                        System.out.println("Okay, let's review those.");
                        System.out.println("");
                        List<Integer> toRemove = new ArrayList<>();
                        for (int i = 0; i < orders.size(); i++) {
                            System.out.println("Stay number " + (i + 1));
                            System.out.println("--------------------------------------------------");
                            Stay.Order order = orders.get(i);
                            System.out.println(order);
                            System.out.println("--------------------------------------------------");
                            System.out.println("");
                            System.out.println("Would you like to keep this stay?");
                            System.out.println("1     Yes.");
                            System.out.println("2     No.");
                            int shouldKeep = readOption(in, 1, 2);
                            if (shouldKeep == 1) {
                                System.out.println("Great!");
                            } else {
                                toRemove.add(i);
                                System.out.println("Okay. We've removed it from your pending stays.");
                            }
                        }
                        for (int i = toRemove.size() - 1; i >= 0; i--) {
                            orders.remove(toRemove.get(i));
                        }
                    } else if (action == 2) {
                        for (int i = 0; i < orders.size(); i++) {
                            Stay.Order order = orders.get(i);
                            boolean success = conn.createStay(order);
                            if (success) {
                                System.out.println("Stay number " + (i + 1) + " finalized.");
                            } else {
                                System.out.println("Oh no! There was a database problem finalizing stay " + (i + 1));
                            }
                        }
                        return;
                    } else {
                        System.out.println("Okay.");
                        return;
                    }
                }
            } else {
                System.out.println("Okay.");
                return;
            }
        }
    }

    private static void displayStays(List<Stay> stays) {
        if (stays.size() == 0) {
            System.out.println("Looks like you don't have any stays yet!");
            return;
        }
        System.out.println("");
        System.out.println("Your stays:");
        System.out.println("");
        for (Stay s : stays) {
            System.out.println("--------------------------------------------------");
            System.out.println(s);
            System.out.println("--------------------------------------------------");
            System.out.println("");
        }
        System.out.println("");
    }

    private static void viewStaysCmd(DBConn conn,
                                     BufferedReader in,
                                     int userId) throws Exception {
        List<Stay> stays = conn.getStays(userId);
        displayStays(stays);
        System.out.println("Press enter to return to the main menu.");
        in.readLine();
    }

    private static void addFavoritePropertyCmd(DBConn conn,
                                               BufferedReader in,
                                               int userId) throws Exception {
        List<Stay> stays = conn.getStays(userId);
        while (true) {
            System.out.println("Do you know the property ID of the property you want to favorite?");
            System.out.println("1     No. Show me my recent stays so I can find it.");
            System.out.println("2     Yes!");
            int option = readOption(in, 1, 2);
            if (option == 1) {
                displayStays(stays);
                continue;
            }
            System.out.println("Great!");
            System.out.println("Enter the property ID:");
            int propId = readInt(in);
            if (!conn.propertyExists(propId)) {
                System.out.println("Sorry! That property doesn't exist.");
                System.out.println("You must have entered the wrong ID!");
                continue;
            }
            if (conn.favoriteExists(userId, propId)) {
                System.out.println("You've already favorited that property!");
                return;
            }
            boolean success = conn.createFavoriteProperty(userId, propId);
            if (success) {
                System.out.println("We've recorded your new favorite!");
            } else {
                System.out.println("Sorry! It looks like there was a database problem.");
                System.out.println("You'll have to try again.");
            }
            return;
        }
    }

    private static void viewFavoritesCmd(DBConn conn,
                                         BufferedReader in,
                                         int userId) throws Exception {
        List<Property> favorites = conn.getUserFavorites(userId);
        System.out.println("Your favorites:");
        displayProperties(favorites);
        System.out.println("Press enter to return to the main menu.");
        in.readLine();
    }

    private static void addFeedbackCmd(DBConn conn,
                                       BufferedReader in,
                                       int userId) throws Exception {
        List<Stay> stays = conn.getStays(userId);
        while (true) {
            System.out.println("Do you know the property ID of the property you want to review?");
            System.out.println("1     No. Show me my recent stays so I can find the property.");
            System.out.println("2     Yes! I'm ready to provide feedback.");
            int option = readOption(in, 1, 2);
            if (option == 1) {
                displayStays(stays);
                continue;
            }
            System.out.println("Great!");
            System.out.println("Enter the property ID:");
            int propId = readInt(in);
            if (!conn.propertyExists(propId)) {
                System.out.println("Hmm. No property with that ID exists!");
                System.out.println("You'll have to have a closer look and try again.");
                continue;
            }
            if (conn.feedbackExists(userId, propId)) {
                System.out.println("Sorry. You already left feedback for that property!");
                System.out.println("You can only leave one feedback per property.");
                return;
            }
            System.out.println("Great!");
            System.out.println("What score would you give the property (0 - 10)?");
            int score;
            while (true) {
                try {
                    String input = in.readLine();
                    score = Integer.parseInt(input);
                    if (score >= 0 && score <= 10) {
                        break;
                    }
                } catch (Exception e) {
                }
                System.out.println("Remember, the score has to be between 0 and 10!");
            }
            System.out.println("Now give any feedback comments you have.");
            System.out.println("Hit enter on a blank line when you're done or if you don't want to leave a comment.");
            String comment = "";
            while (true) {
                String input = in.readLine();
                if (input.length() == 0) {
                    break;
                }
                comment += input;
            }
            java.sql.Date feedbackDate = new java.sql.Date(Calendar.getInstance().getTime().getTime());
            boolean success = conn.createFeedback(userId, propId, feedbackDate, score, comment);
            if (success) {
                System.out.println("That's it! We've recorded your feedback.");
            } else {
                System.out.println("Oh no! There was a database error while entering your feedback.");
                System.out.println("You'll have to try again.");
            }
            return;
        }
    }

    private static void viewFeedbackCmd(DBConn conn,
                                        BufferedReader in,
                                        int userId) throws Exception {
        List<Feedback> feedback = conn.getAllUserFeedback(userId);
        System.out.println("Your feedback:");
        System.out.println("");
        for (Feedback f : feedback) {
            System.out.println("--------------------------------------------------");
            System.out.println(f);
            System.out.println("--------------------------------------------------");
        }
        System.out.println("");
        System.out.println("Press enter to return to the main menu.");
        in.readLine();
    }

    private static void viewPropertyFeedbackCmd(DBConn conn, BufferedReader in, int userId) throws SQLException {
        List<Feedback> feedbacks = null;
        ArrayList<Integer> fids = new ArrayList<>();
        System.out.println("What would you like to do?");
        System.out.println("1     View all feedback for a property.");
        System.out.println("2     View the top n most useful feedbacks for a property.");
        int option = readOption(in, 1, 2);
        System.out.println("Please enter the Property ID:");
        System.out.println();
        int propId;
        while (true) {
            propId = readInt(in);
            if (conn.propertyExists(propId)) {
                break;
            }
            System.out.println("Looks like there's no property associated with that ID. Try again.");
        }
        if (option == 1) {
            feedbacks = conn.getPropertyFeedback(propId);
            for (Feedback f : feedbacks) {
                fids.add(f.id);
            }
            displayPropertyFeedback(feedbacks);
        } else if (option == 2) {
            System.out.println();
            System.out.println("How many feedbacks would you like to see?");
            System.out.println();
            int n = readInt(in);
            feedbacks = conn.getTopRatedPropertyFeedback(propId, n);
            for (Feedback f : feedbacks) {
                fids.add(f.id);
            }
            displayPropertyFeedback(feedbacks);
        }
        System.out.println("The Feedbacks are listed above. What would you like to do now?");
        System.out.println("1     Rate how useful a feedback is.");
        System.out.println("2     Go back to the Main Menu.");
        System.out.println("");
        option = readOption(in, 1, 2);
        if (option == 1) {
            System.out.println("");
            System.out.println("Please enter the ID of the Feedback.");
            System.out.println();
            while (true) {
                option = readInt(in);
                if (fids.contains(option)) {
                    System.out.println();
                    System.out.println("Please enter a usefulness rating from 0-2 where 0 is 'useless',\n" +
                                       "1 is 'useful' and 2 is 'very useful'.");
                    System.out.println();
                    int rating = readOption(in, 0, 2);
                    if (conn.createPropertyFeedbackUsefulnessRating(userId, option, rating)) {
                        System.out.println();
                        System.out.println("Feedback Added!");
                        System.out.println();
                    } else {
                        System.out.println();
                        System.out.println("Looks like something went wrong. Remember, you can only rate a feedback once.\n" +
                                           "If you haven't already rated this feedback, please try again later.");
                        System.out.println();
                    }
                    break;
                } else {
                    displayPropertyFeedback(feedbacks);
                    System.out.println("");
                    System.out.println("Looks like there's no Feedback associated with that ID. Try again.");
                    System.out.println("");
                }
            }
        }
    }

    private static void displayPropertyFeedback(List<Feedback> feedbacks) {
        for (Feedback feedback : feedbacks) {
            System.out.println("--------------------------------------------------");
            System.out.println(feedback);
        }
        System.out.println("--------------------------------------------------");
        System.out.println("");
    }

    private static void addUserTrustRatingCmd(DBConn conn, BufferedReader in, int userId) throws Exception {
        System.out.println("");
        System.out.println("Please enter the User's Login.");
        System.out.println("");
        while (true) {
            String ratee = in.readLine();
            if (!conn.loginExists(ratee)) {
                System.out.println("Looks like there is no User with that ID. Please try again.");
                continue;
            }
            int rateeId = conn.getUserId(ratee);
            if (conn.userTrustRatingExists(userId, rateeId)) {
                System.out.println("Sorry. You've already rated that user!");
                return;
            }
            System.out.println("");
            System.out.println("Please enter a rating of either 0 for 'Not Trusted' or 1 for 'Trusted'.");
            System.out.println("");
            int rating = readOption(in, 0, 2);
            boolean success = conn.addUserTrustRating(userId, rateeId, rating);
            if (success) {
                System.out.println("Rating Added!");
            } else {
                System.out.println("Something went wrong! Remember, you can only rate a user once.");
            }
            return;
        }
    }

    private static void browsePropertiesCmd(DBConn conn,
                                            BufferedReader in,
                                            int userId) throws Exception {
        System.out.println("How would you like to browse?");
        System.out.println("You can select multiple options, each on a separate line.");
        System.out.println("Enter a blank line when you've selected all desired options.");
        System.out.println("1     By price range.");
        System.out.println("2     By address.");
        System.out.println("3     By keyword(s).");
        System.out.println("4     By category.");
        List<Integer> browseOptions = new ArrayList<>();
        while (true) {
            String line = in.readLine();
            if (line.length() == 0) {
                if (browseOptions.size() == 0) {
                    System.out.println("You have to choose at least one option!");
                    continue;
                }
                break;
            }
            int option;
            try {
                option = Integer.parseInt(line);
            } catch (Exception e) {
                System.out.println("Hmm. We don't recognize that option");
                continue;
            }
            if (option < 1 || option > 4) {
                System.out.println("Hmm. We don't recognize that option.");
                continue;
            }
            browseOptions.add(option);
        }
        DBConn.PropertySearchOptions searchOpts = new DBConn.PropertySearchOptions();
        if (browseOptions.contains(1)) {
            System.out.println("Enter a low price per night:");
            searchOpts.lowPrice = readCurrencyAmount(in);
            System.out.println("Enter a high price per night:");
            searchOpts.highPrice = readCurrencyAmount(in);
        }
        if (browseOptions.contains(2)) {
            System.out.println("For the address, would you like to search by city or by state?");
            System.out.println("1     By city.");
            System.out.println("2     By state.");
            int option = readOption(in, 1, 2);
            if (option == 1) {
                System.out.println("Enter the city name:");
                searchOpts.city = in.readLine();
            } else {
                System.out.println("Enter the state name:");
                searchOpts.state = in.readLine();
            }
        }
        if (browseOptions.contains(3)) {
            List<String> keywords = new ArrayList<>();
            System.out.println("Enter a keyword to search by, or a blank line if you've entered all keywords.");
            while (true) {
                String keyword = in.readLine();
                if (keyword.length() == 0) {
                    break;
                }
                keywords.add(keyword);
            }
            searchOpts.keywords = keywords;

        }
        if (browseOptions.contains(4)) {
            System.out.println("Enter a category to search by.");
            searchOpts.category = in.readLine();
        }

        System.out.println("Now, how would you like to sort the results?");
        System.out.println("1     By Price.");
        System.out.println("2     By the average numerical score of the properties' feedback.");
        System.out.println("3     By the average numerical score of trusted user feedback.");

        int sopt = readOption(in, 1, 3);
        DBConn.PropertySortOption sortOption;
        if (sopt == 1) {
            sortOption = DBConn.PropertySortOption.PRICE;
        } else if (sopt == 2) {
            sortOption = DBConn.PropertySortOption.FEEDBACK;
        } else {
            sortOption = DBConn.PropertySortOption.TRUSTED_FEEDBACK;
        }
        searchOpts.sortOption = sortOption;

        System.out.println("Would you like the results sorted");
        System.out.println("1     In ascending order.");
        System.out.println("2     In descending order.");

        int sorder = readOption(in, 1, 2);
        DBConn.SortOrder sortOrder;
        if (sorder == 1) {
            sortOrder = DBConn.SortOrder.ASCENDING;
        } else {
            sortOrder = DBConn.SortOrder.DESCENDING;
        }
        searchOpts.sortOrder = sortOrder;

        System.out.println("Would you like to limit the number of results?");
        System.out.println("If so, enter the number you would like to see.");
        System.out.println("Otherwise, enter a blank line.");
        while (true) {
            String lstr = in.readLine();
            if (lstr.length() == 0) {
                break;
            }
            try {
                searchOpts.limit = Integer.parseInt(lstr);
                break;
            } catch (Exception e) {
            }
            System.out.println("Oops! That's not a number!");
        }

        List<Property> properties = conn.searchProperties(userId, searchOpts);
        System.out.println("");
        System.out.println("Query complete.");
        System.out.println("Properties:");
        System.out.println("");
        for (Property p : properties) {
            System.out.println("--------------------------------------------------");
            System.out.println(p);
            System.out.println("--------------------------------------------------");
            System.out.println("");
        }
        System.out.println("");
        System.out.println("Press enter to continue.");
        in.readLine();
    }

    private static void degreeOfSeparationCmd(DBConn conn, BufferedReader in) throws Exception {
        System.out.println("Enter the first user's login:");
        int uid1 = readUserId(conn, in);
        System.out.println("Enter the second user's login:");
        int uid2 = readUserId(conn, in);
        boolean areTwoDegreesSeparated = conn.areTwoDegreesSeparated(uid1, uid2);
        System.out.println("");
        if (areTwoDegreesSeparated) {
            System.out.println("Yes! The degree of separation between those users is exactly two.");
        } else {
            System.out.println("Nope! Those users are not separated by two degrees.");
        }
    }

    private static void statisticsCmd(DBConn conn, BufferedReader in) throws Exception {
        System.out.println("");
        System.out.println("What would you like to see?");
        System.out.println("1     Most popular properties per category");
        System.out.println("2     Most expensive properties per category");
        System.out.println("3     Highest rated properties per category.");
        System.out.println("4     Exit");
        System.out.println("");
        int option = readOption(in, 1, 4);
        if (option == 4) {
            return;
        }
        System.out.println("How many properties would you like to see per category?");
        System.out.println("");
        while (true) {
            int n = readInt(in);
            if (n < 0) {
                System.out.println("Please give a valid number.");
            }
            if (option == 1) {
                displayProperties(conn.getMostPopularPropertiesPerCategory(), n);
                break;
            } else if (option == 2) {
                displayProperties(conn.getMostExpensivePropertiesPerCategory(), n);
                break;
            } else if (option == 3) {
                displayProperties(conn.getHighestRatedPropertiesPerCategory(), n);
                break;
            }
        }
        System.out.println("Press enter to return to the main menu.");
        in.readLine();
    }

    private static void displayProperties(TreeMap<String, List<Property>> propertyMap, int n) {
        for (String key : propertyMap.navigableKeySet()) {
            List<Property> properties = propertyMap.get(key);
            System.out.println();
            System.out.println("Category: " + key);
            for (int i = 0; i < Math.min(n, properties.size()); i++) {
                System.out.println("--------------------------------------------------");
                System.out.println("Rank: " + (i + 1)); //TODO: Still want rank with statistics?
                System.out.println(properties.get(i));
            }
            System.out.println("--------------------------------------------------");
            System.out.println();
        }
    }

    private static void awardUsersCmd(DBConn conn, BufferedReader in) throws Exception {
        System.out.println("What would you like to see?");
        System.out.println("1     Users who are trusted the most.");
        System.out.println("2     Users who give the most useful feedback.");
        System.out.println("3     Exit");
        int option = readOption(in, 1, 3);
        if (option == 1) {
            System.out.println();
            System.out.println("How many Users would you like to see?");
            System.out.println();
            displayUsers(conn.getMostTrustedUsers(readInt(in)));
        } else if (option == 2) {
            System.out.println();
            System.out.println("How many Users would you like to see?");
            System.out.println();
            displayUsers(conn.getMostUsefulUsers(readInt(in)));
        }
        System.out.println("Press enter to return to the main menu.");
        in.readLine();
    }

    private static void displayUsers(List<User> users) {
        for (int i = 0; i < users.size(); i++) {
            System.out.println("--------------------------------------------------");
            System.out.print("Rank: " + (i+1) + '\n');
            System.out.println(users.get(i));
        }
        System.out.println("--------------------------------------------------");
        System.out.println();
    }

    private static void run(DBConn conn, BufferedReader in) throws Exception {

        //Display Initial Menu
        System.out.println("");
        System.out.println("\t\tWelcome to Uotel!");
        System.out.println("");
        System.out.println("Enter an action number.\n");
        System.out.println("1     Register as a new user.");
        System.out.println("2     Login as an existing user.");
        System.out.println("");

        //Get choice.
        int option = readOption(in, 1, 2);

        int userId;
        if (option == 1) {
            userId = createUserCmd(conn, in);
        } else {
            userId = loginUserCmd(conn, in);
        }

        System.out.println("You're logged in!");

        while (true) {
            System.out.println();
            System.out.println("                      Main Menu");
            System.out.println("-------------------------------------------------------");
            System.out.println("1      Add a reservation.");
            System.out.println("2      View your reservations.");
            System.out.println("3      Record a new property.");
            System.out.println("4      View your properties.");
            System.out.println("5      Update information about one of your properties.");
            System.out.println("6      Add a listing for one of your properties.");
            System.out.println("7      View your property listings.");
            System.out.println("8      Record a recent stay or stays.");
            System.out.println("9      View your stays.");
            System.out.println("10     Declare a property as a favorite.");
            System.out.println("11     View your favorite properties.");
            System.out.println("12     Add feedback for a property.");
            System.out.println("13     View property feedback you've given.");
            System.out.println("14     View feedback for a property and rate its usefulness.");
            System.out.println("15     Add a trust rating for another user.");
            System.out.println("16     Browse properties.");
            System.out.println("17     Determine if the degree of separation between two users is two.");
            System.out.println("18     View Property Statistics.");
            System.out.println("19     View User Awards.");
            System.out.println("20     Exit.");
            System.out.println("");

            int action = readOption(in, 1, 20);
            switch (action) {
            case 1:
                addReservationCmd(conn, in, userId);
                break;
            case 2:
                viewReservationsCmd(conn, in, userId);
                break;
            case 3:
                recordPropertyCmd(conn, in, userId);
                break;
            case 4:
                viewPropertiesCmd(conn, in, userId);
                break;
            case 5:
                updatePropertyCmd(conn, in, userId);
                break;
            case 6:
                addListingCmd(conn, in, userId);
                break;
            case 7:
                viewListingsCmd(conn, in, userId);
                break;
            case 8:
                addStayCmd(conn, in, userId);
                break;
            case 9:
                viewStaysCmd(conn, in, userId);
                break;
            case 10:
                addFavoritePropertyCmd(conn, in, userId);
                break;
            case 11:
                viewFavoritesCmd(conn, in, userId);
                break;
            case 12:
                addFeedbackCmd(conn, in, userId);
                break;
            case 13:
                viewFeedbackCmd(conn, in, userId);
                break;
            case 14:
                viewPropertyFeedbackCmd(conn, in, userId);
                break;
            case 15:
                addUserTrustRatingCmd(conn, in, userId);
                break;
            case 16:
                browsePropertiesCmd(conn, in, userId);
                break;
            case 17:
                degreeOfSeparationCmd(conn, in);
                break;
            case 18:
                statisticsCmd(conn, in);
                break;
            case 19:
                awardUsersCmd(conn, in);
                break;
            case 20:
                return;
            default:
                System.out.println("Oops! We didn't recognize that option.");
            }
        }
    }

    public static void main(String[] args) {
        DBConn conn = null;
        BufferedReader in = null;
        try {
            System.out.println("Connecting to database...");
            conn = new DBConn();
            System.out.println("Connected");
            in = new BufferedReader(new InputStreamReader(System.in));
            run(conn, in);
        } catch (Exception e) {
            System.err.println("Oh no! Something went wrong.");
            System.err.println(e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
            }
        }
    }
}
