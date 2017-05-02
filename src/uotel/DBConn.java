package uotel;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class DBConn {
    private Connection conn;

    private static final String USER = "5530u12";
    private static final String PASS = "qrjbodn3";
    private static final String URL = "jdbc:mysql://georgia.eng.utah.edu/5530db12";

    public DBConn() throws ClassNotFoundException,
                           IllegalAccessException,
                           InstantiationException,
                           SQLException {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        conn = DriverManager.getConnection(URL, USER, PASS);
    }

    public void close() throws SQLException {
        conn.close();
    }

    public boolean loginExists(String login) throws SQLException {
        String query = "SELECT * FROM User u WHERE u.login = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, login);
        ResultSet res = stmt.executeQuery();
        boolean exists = res.isBeforeFirst();
        stmt.close();
        return exists;
    }

    public int createAddress(String street,
                             String unitNumber,
                             String zipcode,
                             String city,
                             String state,
                             String country) throws SQLException {
        String query = "INSERT INTO Address (street, unit_number, zip_code, city, state, country) " +
            "VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, street);
        stmt.setString(2, unitNumber);
        stmt.setString(3, zipcode);
        stmt.setString(4, city);
        stmt.setString(5, state);
        stmt.setString(6, country);
        stmt.execute();
        ResultSet generatedKey = stmt.getGeneratedKeys();
        generatedKey.next();
        int addrId = generatedKey.getInt(1);
        stmt.close();
        return addrId;
    }

    public boolean userExists(int userId) throws SQLException {
        String query = "SELECT * from User u where u.id = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, userId);
        ResultSet results = stmt.executeQuery();
        boolean exists = results.next();
        stmt.close();
        return exists;
    }

    public int createUser(String firstName,
                          String lastName,
                          String login,
                          String password,
                          String phoneNumber,
                          int addressId) throws SQLException {
        String query = "INSERT INTO User (first_name, last_name, login, password, phone_number, address_id) " +
            "VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, firstName);
        stmt.setString(2, lastName);
        stmt.setString(3, login);
        stmt.setString(4, password);
        stmt.setString(5, phoneNumber);
        stmt.setInt(6, addressId);
        stmt.execute();
        ResultSet generatedKey = stmt.getGeneratedKeys();
        generatedKey.next();
        int userId = generatedKey.getInt(1);
        stmt.close();
        return userId;
    }

    public int loginUser(String login,
                         String password) throws SQLException {
        String query = "SELECT u.id FROM User u where u.login = ? AND u.password = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, login);
        stmt.setString(2, password);
        ResultSet res = stmt.executeQuery();
        if (!res.next()) {
            // TODO: This isn't the _right_ thing to do here.
            // Use Maybe<> type if we know java 8 is supported.
            throw new IllegalArgumentException("No such user.");
        }
        int userId = res.getInt(1);
        stmt.close();
        return userId;
    }

    public int getUserId(String login) throws SQLException {
        String query = "SELECT u.id FROM User u WHERE u.login = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, login);
        ResultSet results = stmt.executeQuery();
        results.next();
        return results.getInt(1);
    }

    private List<Integer> getOneDegrees(int userId) throws SQLException {
        String query = "SELECT DISTINCT f.user_id "+
            "FROM FavoriteProperty f "+
            "WHERE f.user_id <> ? AND f.property_id IN " +
            "(SELECT f1.property_id FROM FavoriteProperty f1 WHERE f1.user_id = ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, userId);
        stmt.setInt(2, userId);
        ResultSet results = stmt.executeQuery();
        List<Integer> ids = new ArrayList<>();
        while (results.next()) {
            ids.add(results.getInt(1));
        }
        stmt.close();
        return ids;
    }

    public boolean areTwoDegreesSeparated(int uid1, int uid2) throws SQLException {
        List<Integer> id1s = getOneDegrees(uid1);
        if (id1s.contains(uid2)) {
            return false;
        }
        List<Integer> id2s = getOneDegrees(uid2);
        for (Integer i : id1s) {
            if (id2s.contains(i)) {
                return true;
            }
        }
        return false;
    }

    public int createProperty(String name,
                              int ownerId,
                              int addrId,
                              int categoryId,
                              String phoneNumber,
                              String url,
                              String yearBuilt) throws SQLException {
        String query = "INSERT INTO Property (name, owner_id, address_id, category_id, phone_number, url, year_built) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, name);
        stmt.setInt(2, ownerId);
        stmt.setInt(3, addrId);
        stmt.setInt(4, categoryId);
        stmt.setString(5, phoneNumber);
        stmt.setString(6, url);
        stmt.setString(7, yearBuilt);
        stmt.execute();
        ResultSet generatedKey = stmt.getGeneratedKeys();
        generatedKey.next();
        int propId = generatedKey.getInt(1);
        stmt.close();
        return propId;
    }

    public List<Property> getUserProperties(int userId) throws SQLException {
        String query = "SELECT p.id, p.name, p.owner_id, p.phone_number, p.url, p.year_built, " +
            "a.id, a.street, a.unit_number, a.zip_code, a.city, a.state, a.country, " +
            "c.id, c.name " +
            "FROM Property p, Address a, PropertyCategory c " +
            "WHERE p.owner_id = ? AND p.address_id = a.id AND p.category_id = c.id";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, userId);
        ResultSet results = stmt.executeQuery();
        List<Property> properties = new ArrayList<Property>();
        while (results.next()) {
            Address addr = new Address(results.getInt(7),
                                       results.getString(8),
                                       results.getString(9),
                                       results.getString(10),
                                       results.getString(11),
                                       results.getString(12),
                                       results.getString(13));
            Category category = new Category(results.getInt(14), results.getString(15));
            Property prop = new Property(results.getInt(1),
                                         results.getString(2),
                                         results.getInt(3),
                                         addr,
                                         category,
                                         results.getString(4),
                                         results.getString(5),
                                         results.getString(6));
            properties.add(prop);
        }
        stmt.close();
        return properties;
    }

    // Potentially updates all attributes except id.
    // Does not create address and category if they do not already exist.
    public boolean updateProperty(int propId, Property updatedProp) throws SQLException {
        String query = "UPDATE Property " +
            "SET name = ?, owner_id = ?, address_id = ?, category_id = ?, phone_number = ?, url = ?, year_built = ? " +
            "WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, updatedProp.name);
        stmt.setInt(2, updatedProp.ownerId);
        stmt.setInt(3, updatedProp.address.id);
        stmt.setInt(4, updatedProp.category.id);
        stmt.setString(5, updatedProp.phoneNumber);
        stmt.setString(6, updatedProp.url);
        stmt.setString(7, updatedProp.yearBuilt);
        stmt.setInt(8, propId);
        int updated = stmt.executeUpdate();
        stmt.close();
        return updated == 1;
    }

    public boolean propertyExists(int propId) throws SQLException {
        String query = "SELECT * FROM Property p WHERE p.id = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, propId);
        ResultSet results = stmt.executeQuery();
        boolean exists = results.next();
        stmt.close();
        return exists;
    }


    public void addPropertyKeyword(int propId, String kw) throws SQLException {
        String query = "INSERT INTO PropertyKeywordDescription (property_id, keyword) VALUES (?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, propId);
        stmt.setString(2, kw);
        stmt.executeUpdate();
        stmt.close();
    }

    public List<String> getPropertyKeywords(int propId) throws SQLException {
        String query = "SELECT k.keyword FROM PropertyKeywordDescription k WHERE k.property_id = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, propId);
        ResultSet results = stmt.executeQuery();
        List<String> keywords = new ArrayList<>();
        while (results.next()) {
            keywords.add(results.getString(1));
        }
        stmt.close();
        return keywords;
    }

    public boolean deletePropertyKeyword(int propId, String kw) throws SQLException {
        String query = "DELETE FROM PropertyKeywordDescription WHERE property_id = ? AND keyword = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, propId);
        stmt.setString(2, kw);
        int count = stmt.executeUpdate();
        boolean success = count > 0;
        stmt.close();
        return success;
    }

    public boolean addListing(int propId,
                              Date startDate,
                              Date endDate,
                              BigDecimal pricePerNight) throws SQLException {
        String query = "INSERT INTO PropertyListing (property_id, start_date, end_date, price_per_night) " +
            "VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, propId);
        stmt.setDate(2, startDate);
        stmt.setDate(3, endDate);
        stmt.setBigDecimal(4, pricePerNight);
        int updated = stmt.executeUpdate();
        stmt.close();
        return updated == 1;
    }

    public List<Listing> getListings(int propId) throws SQLException {
        String query = "SELECT l.start_date, l.end_date, l.price_per_night " +
            "FROM PropertyListing l " +
            "WHERE l.property_id = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, propId);
        ResultSet results = stmt.executeQuery();
        List<Listing> listings = new ArrayList<>();
        while (results.next()) {
            Date start = results.getDate(1);
            Date end = results.getDate(2);
            BigDecimal pricePerNight = results.getBigDecimal(3);
            Listing listing = new Listing(propId, start, end, pricePerNight);
            listings.add(listing);
        }
        stmt.close();
        return listings;
    }

    public List<NamedListing> getAllUserListings(int userId) throws SQLException {
        String query = "SELECT p.id, p.name, l.start_date, l.end_date, l.price_per_night " +
            "FROM PropertyListing l, Property p " +
            "WHERE p.owner_id = ? AND l.property_id = p.id";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, userId);
        ResultSet results = stmt.executeQuery();
        List<NamedListing> listings = new ArrayList<>();
        while (results.next()) {
            int propId = results.getInt(1);
            String propName = results.getString(2);
            Date start = results.getDate(3);
            Date end = results.getDate(4);
            BigDecimal pricePerNight = results.getBigDecimal(5);
            NamedListing listing = new NamedListing(propId, propName, start, end, pricePerNight);
            listings.add(listing);
        }
        stmt.close();
        return listings;
    }

    public List<String> getPropertyCategories() throws SQLException {
        String query = "SELECT c.name FROM PropertyCategory c";
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet res = stmt.executeQuery();
        List<String> categories = new ArrayList<>();
        while (res.next()) {
            String c = res.getString(1);
            categories.add(c);
        }
        stmt.close();
        return categories;
    }

    // Assumes the category exists.
    public int getPropertyCategoryId(String category) throws SQLException {
        String query = "SELECT c.id FROM PropertyCategory c WHERE c.name = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, category);
        ResultSet res = stmt.executeQuery();
        res.next();
        int id = res.getInt(1);
        stmt.close();
        return id;
    }

    public int createPropertyCategory(String category) throws SQLException {
        String query = "INSERT INTO PropertyCategory (name) VALUES (?)";
        PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, category);
        stmt.execute();
        ResultSet generatedKey = stmt.getGeneratedKeys();
        generatedKey.next();
        int categoryId = generatedKey.getInt(1);
        stmt.close();
        return categoryId;
    }

    public enum PropertySortOption {
        PRICE,
        FEEDBACK,
        TRUSTED_FEEDBACK
    }

    public enum SortOrder {
        ASCENDING,
        DESCENDING
    }

    public static class PropertySearchOptions {
        public BigDecimal lowPrice = null;
        public BigDecimal highPrice = null;
        public String city = null;
        public String state = null;
        public List<String> keywords = null;
        public String category = null;
        public PropertySortOption sortOption = null;
        public SortOrder sortOrder = null;
        public int limit = -1;
    }

    public List<Property> searchProperties(int userId, PropertySearchOptions opts) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("SELECT p.id, p.name, p.owner_id, p.phone_number, p.url, p.year_built, " +
                     "a.id, a.street, a.unit_number, a.zip_code, a.city, a.state, a.country, " +
                     "c.id, c.name, l.start_date, l.end_date, l.price_per_night, AVG(f.score) AS avg_rating " +
                     "FROM Property p INNER JOIN Address a ON p.address_id = a.id " +
                     "INNER JOIN PropertyCategory c ON p.category_id = c.id " +
                     "INNER JOIN PropertyListing l ON l.property_id = p.id "+
                     "LEFT OUTER JOIN PropertyFeedback f ON p.id = f.property_id");
        if (opts.keywords != null) {
            query.append(", PropertyKeywordDescription k");
        }
        query.append(" WHERE ");
        if (opts.lowPrice != null) {
            query.append("l.price_per_night >= ? AND l.price_per_night <= ? AND ");
        }
        if (opts.keywords != null) {
            query.append("k.property_id = p.id AND k.keyword IN (");
            for (int i = 0; i < opts.keywords.size() - 1; i++) {
                query.append("?, ");
            }
            query.append("?) AND ");
        }
        if (opts.city != null) {
            query.append("a.city = ? AND ");
        }
        if (opts.state != null) {
            query.append("a.state = ? AND ");
        }
        if (opts.category != null) {
            query.append("c.name = ? AND ");
        }
        if (opts.sortOption == PropertySortOption.TRUSTED_FEEDBACK) {
            query.append("f.user_id IN (SELECT tr.rated_user_id FROM UserTrustRating tr WHERE tr.rater_user_id = ? AND tr.rating = 1) AND ");
        }

        // Remove the trailing " AND "
        query.delete(query.length() - 5, query.length());

        query.append(" GROUP BY p.id, p.name, p.owner_id, p.phone_number, p.url, p.year_built, " +
                     "a.id, a.street, a.unit_number, a.zip_code, a.city, a.state, a.country, " +
                     "c.id, c.name, l.start_date, l.end_date, l.price_per_night");

        if (opts.sortOption == PropertySortOption.PRICE) {
            query.append(" ORDER BY l.price_per_night");
        } else if (opts.sortOption == PropertySortOption.FEEDBACK ||
                   opts.sortOption == PropertySortOption.TRUSTED_FEEDBACK) {
            query.append(" ORDER BY avg_rating");
        }

        if (opts.sortOrder == SortOrder.ASCENDING) {
            query.append(" ASC");
        } else if (opts.sortOrder == SortOrder.DESCENDING) {
            query.append(" DESC");
        }

        if (opts.limit != -1) {
            query.append(" LIMIT ?");
        }

        PreparedStatement stmt = conn.prepareStatement(query.toString());
        int idx = 1;
        if (opts.lowPrice != null) {
            stmt.setBigDecimal(idx++, opts.lowPrice);
            stmt.setBigDecimal(idx++, opts.highPrice);
        }
        if (opts.keywords != null) {
            for (String kw : opts.keywords) {
                stmt.setString(idx++, kw);
            }
        }
        if (opts.city != null) {
            stmt.setString(idx++, opts.city);
        }
        if (opts.state != null) {
            stmt.setString(idx++, opts.state);
        }
        if (opts.category != null) {
            stmt.setString(idx++, opts.category);
        }
        if (opts.sortOption == PropertySortOption.TRUSTED_FEEDBACK) {
            stmt.setInt(idx++, userId);
        }
        if (opts.limit != -1) {
            stmt.setInt(idx++, opts.limit);
        }
        ResultSet results = stmt.executeQuery();
        List<Property> properties = new ArrayList<>();
        while (results.next()) {
            Address address = new Address(results.getInt("a.id"),
                                          results.getString("a.street"),
                                          results.getString("a.unit_number"),
                                          results.getString("a.zip_code"),
                                          results.getString("a.city"),
                                          results.getString("a.state"),
                                          results.getString("a.country"));
            Category category = new Category(results.getInt("c.id"), results.getString("c.name"));
            SearchedProperty property = new SearchedProperty(results.getInt("p.id"),
                                                             results.getString("p.name"),
                                                             results.getInt("p.owner_id"),
                                                             address,
                                                             category,
                                                             results.getString("p.phone_number"),
                                                             results.getString("p.url"),
                                                             results.getString("p.year_built"),
                                                             results.getDate("l.start_date"),
                                                             results.getDate("l.end_date"),
                                                             results.getBigDecimal("l.price_per_night"),
                                                             results.getString("avg_rating"));
            properties.add(property);
        }
        stmt.close();
        return properties;
    }

    public List<Reservation> getReservations(int userId) throws SQLException {
        String query = "SELECT r.id, r.user_id, r.start_date, r.end_date, r.guests, " +
            "p.id, p.name, p.owner_id, p.phone_number, p.url, p.year_built, " +
            "a.id, a.street, a.unit_number, a.zip_code, a.city, a.state, a.country, " +
            "c.id, c.name " +
            "FROM Reservation r, Property p, Address a, PropertyCategory c " +
            "WHERE r.user_id = ? AND r.property_id = p.id AND p.address_id = a.id AND p.category_id = c.id";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, userId);
        ResultSet results = stmt.executeQuery();
        List<Reservation> reservations = new ArrayList<>();
        while (results.next()) {
            Address address = new Address(results.getInt("a.id"),
                                          results.getString("a.street"),
                                          results.getString("a.unit_number"),
                                          results.getString("a.zip_code"),
                                          results.getString("a.city"),
                                          results.getString("a.state"),
                                          results.getString("a.country"));
            Category category = new Category(results.getInt("c.id"), results.getString("c.name"));
            Property property = new Property(results.getInt("p.id"),
                                             results.getString("p.name"),
                                             results.getInt("p.owner_id"),
                                             address,
                                             category,
                                             results.getString("p.phone_number"),
                                             results.getString("p.url"),
                                             results.getString("p.year_built"));
            Reservation reservation = new Reservation(results.getInt("r.id"),
                                                      userId,
                                                      property,
                                                      results.getDate("r.start_date"),
                                                      results.getDate("r.end_date"),
                                                      results.getInt("r.guests"));
            reservations.add(reservation);
        }
        stmt.close();
        return reservations;
    }

    public boolean createReservation(Reservation.Order order) throws SQLException {
        String query = "INSERT INTO Reservation (user_id, property_id, start_date, end_date, guests) " +
            "VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, order.userId);
        stmt.setInt(2, order.propertyId);
        stmt.setDate(3, order.startDate);
        stmt.setDate(4, order.endDate);
        stmt.setInt(5, order.guests);
        int count = stmt.executeUpdate();
        boolean success = count > 0;
        stmt.close();
        return success;
    }

    public List<Property> getSuggestions(int reservedPropId) throws SQLException {
        String query = "SELECT p.id, p.name, p.owner_id, p.phone_number, p.url, p.year_built, " +
            "a.id, a.street, a.unit_number, a.zip_code, a.city, a.state, a.country, " +
            "c.id, c.name, " +
            "COUNT(DISTINCT r1.id) as visit_count " +
            "FROM Property p INNER JOIN Address a ON p.address_id = a.id INNER JOIN PropertyCategory c ON p.category_id = c.id, " +
            "UserStay s1 INNER JOIN Reservation r1 ON s1.reservation_id = r1.id, " +
            "UserStay s2 INNER JOIN Reservation r2 ON s2.reservation_id = r2.id " +
            "WHERE p.id = r1.property_id AND p.id <> r2.property_id AND r2.property_id = ? AND r1.user_id = r2.user_id " +
            "GROUP BY p.id, p.name, p.owner_id, p.phone_number, p.url, p.year_built, " +
            "a.id, a.street, a.unit_number, a.zip_code, a.city, a.state, a.country, " +
            "c.id, c.name " +
            "ORDER BY visit_count DESC";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, reservedPropId);
        ResultSet results = stmt.executeQuery();
        List<Property> properties = new ArrayList<>();
        while (results.next()) {
            Category category = new Category(results.getInt("c.id"),
                                             results.getString("c.name"));
            Address address = new Address(results.getInt("a.id"),
                                          results.getString("a.street"),
                                          results.getString("a.unit_number"),
                                          results.getString("a.zip_code"),
                                          results.getString("a.city"),
                                          results.getString("a.state"),
                                          results.getString("a.country"));
            SuggestedProperty property = new SuggestedProperty(results.getInt("p.id"),
                                                               results.getString("p.name"),
                                                               results.getInt("p.owner_id"),
                                                               address,
                                                               category,
                                                               results.getString("p.phone_number"),
                                                               results.getString("p.url"),
                                                               results.getString("p.year_built"),
                                                               results.getInt("visit_count"),
                                                               reservedPropId);
            properties.add(property);
        }
        stmt.close();
        return properties;
    }

    public List<Stay> getStays(int userId) throws SQLException {
        String query = "SELECT s.total_cost, " +
            "r.id, r.user_id, r.start_date, r.end_date, r.guests, " +
            "p.id, p.name, p.owner_id, p.phone_number, p.url, p.year_built, " +
            "a.id, a.street, a.unit_number, a.zip_code, a.city, a.state, a.country, " +
            "c.id, c.name " +
            "FROM UserStay s, Reservation r, Property p, Address a, PropertyCategory c " +
            "WHERE r.user_id = ? AND s.reservation_id = r.id AND r.property_id = p.id AND p.address_id = a.id AND p.category_id = c.id";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, userId);
        ResultSet results = stmt.executeQuery();
        List<Stay> stays = new ArrayList<>();
        while (results.next()) {
            Category category = new Category(results.getInt("c.id"),
                                             results.getString("c.name"));
            Address address = new Address(results.getInt("a.id"),
                                          results.getString("a.street"),
                                          results.getString("a.unit_number"),
                                          results.getString("a.zip_code"),
                                          results.getString("a.city"),
                                          results.getString("a.state"),
                                          results.getString("a.country"));
            Property property = new Property(results.getInt("p.id"),
                                             results.getString("p.name"),
                                             results.getInt("p.owner_id"),
                                             address,
                                             category,
                                             results.getString("p.phone_number"),
                                             results.getString("p.url"),
                                             results.getString("p.year_built"));
            Reservation reservation = new Reservation(results.getInt("r.id"),
                                                      results.getInt("r.user_id"),
                                                      property,
                                                      results.getDate("r.start_date"),
                                                      results.getDate("r.end_date"),
                                                      results.getInt("r.guests"));
            Stay stay = new Stay(reservation, results.getBigDecimal("s.total_cost"));
            stays.add(stay);
        }
        stmt.close();
        return stays;
    }

    public boolean createStay(Stay.Order order) throws SQLException {
        String query = "INSERT INTO UserStay (reservation_id, total_cost) VALUES (?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, order.reservationId);
        stmt.setBigDecimal(2, order.totalCost);
        int count = stmt.executeUpdate();
        boolean success = count > 0;
        stmt.close();
        return success;
    }

    public List<Feedback> getAllUserFeedback(int userId) throws SQLException {
        String query = "SELECT f.id, f.user_id, f.date, f.score, f.comment, " +
            "u.login, " +
            "p.id, p.name, p.owner_id, p.phone_number, p.url, p.year_built, " +
            "a.id, a.street, a.unit_number, a.zip_code, a.city, a.state, a.country, " +
            "c.id, c.name " +
            "FROM PropertyFeedback f, User u, Property p, Address a, PropertyCategory c " +
            "WHERE p.id = f.property_id AND p.address_id = a.id AND p.category_id = c.id " +
            "AND f.user_id = ? AND u.id = f.user_id";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, userId);
        ResultSet results = stmt.executeQuery();
        List<Feedback> feedbacks = new ArrayList<>();
        while (results.next()) {
            Address address = new Address(results.getInt("a.id"),
                                          results.getString("a.street"),
                                          results.getString("a.unit_number"),
                                          results.getString("a.zip_code"),
                                          results.getString("a.city"),
                                          results.getString("a.state"),
                                          results.getString("a.country"));
            Category category = new Category(results.getInt("c.id"), results.getString("c.name"));
            Property property = new Property(results.getInt("p.id"),
                                             results.getString("p.name"),
                                             results.getInt("p.owner_id"),
                                             address,
                                             category,
                                             results.getString("p.phone_number"),
                                             results.getString("p.url"),
                                             results.getString("p.year_built"));
            Feedback feedback = new Feedback(results.getInt("f.id"),
                                             results.getInt("f.user_id"),
                                             results.getString("u.login"),
                                             property,
                                             results.getDate("f.date"),
                                             results.getInt("f.score"),
                                             results.getString("f.comment"));
            feedbacks.add(feedback);
        }
        stmt.close();
        return feedbacks;
    }

    public Property getProperty(int propertyId) throws SQLException {
        Property property = null;
        String query = "SELECT P.name, P.owner_id, P.address_id, P.category_id," +
            " P.phone_number, P.url, P.year_built, A.street, A.unit_number, A.zip_code," +
            " A.city, A.state, A.country, PC.name FROM Property P, Address A," +
            " PropertyCategory PC WHERE P.id = ? AND P.address_id = A.id" +
            " AND P.category_id = PC.id";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, propertyId);
        ResultSet rs = stmt.executeQuery();
        while(rs.next()) {
            int addressId = rs.getInt(2);
            String street = rs.getString(8);
            String unitNumber = rs.getString(9);
            String zipcode = rs.getString(10);
            String city = rs.getString(11);
            String state = rs.getString(12);
            String country = rs.getString(13);
            Address address = new Address(addressId, street, unitNumber, zipcode, city, state, country);

            int categoryId = rs.getInt(4);
            String categoryName = rs.getString(14);
            Category category = new Category(categoryId, categoryName);

            String propertyName  = rs.getString(1);
            int ownerId = rs.getInt(2);
            String phoneNumber  = rs.getString(5);
            String url  = rs.getString(6);
            String yearBuilt  = rs.getString(7);
            property = new Property(propertyId, propertyName, ownerId, address, category, phoneNumber, url, yearBuilt);
        }
        stmt.close();
        return property;
    }

    public List<Feedback> getPropertyFeedback(int propertyId) throws SQLException {
        ArrayList<Feedback> feedbackList = new ArrayList<>();
        Property property = getProperty(propertyId);
        String query = "SELECT pf.id, pf.user_id, pf.date, pf.score, pf.comment, u.login " +
            "FROM PropertyFeedback pf, User u " +
            "WHERE pf.property_id = ? AND u.id = pf.user_id " +
            "ORDER BY pf.user_id";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, propertyId);
        ResultSet rs = stmt.executeQuery();
        while(rs.next()) {
            int feedbackId = rs.getInt(1);
            int feedbackUserId = rs.getInt(2);
            Date date = rs.getDate(3);
            int score = rs.getInt(4);
            String comment = rs.getString(5);
            String userLogin = rs.getString(6);
            Feedback feedback = new Feedback(feedbackId, feedbackUserId, userLogin, property, date, score, comment);
            feedbackList.add(feedback);
        }
        stmt.close();
        return feedbackList;
    }

    public boolean createPropertyFeedbackUsefulnessRating(int userId, int feedbackId, int rating) throws SQLException {
        String query = "INSERT INTO FeedbackUsefulnessRating(feedback_id, rater_user_id, rating) VALUES (?, ?, ?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, feedbackId);
            stmt.setInt(2, userId);
            stmt.setInt(3, rating);
            boolean success = stmt.executeUpdate() > 0;
            stmt.close();
            return success;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean userTrustRatingExists(int raterId, int rateeId) throws SQLException {
        String query = "SELECT * from UserTrustRating WHERE rater_user_id = ? AND rated_user_id = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, raterId);
        stmt.setInt(2, rateeId);
        ResultSet results = stmt.executeQuery();
        boolean exists = results.next();
        stmt.close();
        return exists;
    }

    public boolean addUserTrustRating(int rater, int ratee, int rating) throws SQLException {
        String query = "INSERT INTO UserTrustRating(rater_user_id, rated_user_id, rating) VALUES(?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, rater);
        stmt.setInt(2, ratee);
        stmt.setInt(3, rating);
        boolean success = stmt.executeUpdate() > 0;
        stmt.close();
        return success;
    }

    public TreeMap<String, List<Property>> getMostPopularPropertiesPerCategory() throws SQLException {
        TreeMap<String, List<Property>> properties = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        ArrayList<Property> list = null;
        String query = "SELECT P.id, P.name, P.owner_id, P.address_id, P.category_id, P.phone_number, " +
            "P.url, P.year_built, A.street, A.unit_number, A.zip_code, A.city, A.state, A.country, " +
            "ranks.name, ranks.cnt " +
            "FROM (SELECT PC.name, P1.id, COUNT(US.reservation_id) as cnt FROM " +
            "PropertyCategory PC, Property P1, Reservation R, UserStay US WHERE  PC.id = P1.category_id " +
            "AND P1.id = R.property_id AND US.reservation_id = R.id GROUP BY PC.name, P1.id " +
            "ORDER BY name, cnt DESC) as ranks LEFT JOIN Property P USING(id) LEFT JOIN Address A " +
            "ON P.address_id = A.id";
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        while(rs.next()){
            int addressId = rs.getInt(4);
            String street = rs.getString(9);
            String unitNumber = rs.getString(10);
            String zipCode = rs.getString(11);
            String city = rs.getString(12);
            String state = rs.getString(13);
            String country = rs.getString(14);
            Address address = new Address(addressId, street, unitNumber, zipCode, city, state, country);

            int categoryId = rs.getInt(5);
            String categoryName = rs.getString(15);
            Category category = new Category(categoryId, categoryName);

            int propertyId = rs.getInt(1);
            String propertyName = rs.getString(2);
            int ownerId = rs.getInt(3);
            String phoneNumber = rs.getString(6);
            String url = rs.getString(7);
            String year = rs.getString(8);
            int resCount = rs.getInt(16);
            PopularProperty property = new PopularProperty(propertyId, propertyName, ownerId, address, category, phoneNumber, url, year, resCount);

            if(properties.containsKey(categoryName)){
                properties.get(categoryName).add(property);
            } else {
                list = new ArrayList<>();
                list.add(property);
                properties.put(categoryName, list);
            }
        }
        stmt.close();
        return properties;
    }

    public TreeMap<String, List<Property>> getMostExpensivePropertiesPerCategory() throws SQLException {
        TreeMap<String, List<Property>> properties = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        ArrayList<Property> list = null;
        String query = "SELECT ranks.name, P.id, P.name, P.owner_id, " +
            "P.address_id, P.category_id, P.phone_number, P.url, P.year_built, " +
            "A.street, A.unit_number, A.zip_code, A.city, A.state, A.country, ranks.cost FROM " +
            "(SELECT C.name ,P1.id, SUM(US.total_cost)/SUM(R.guests) as cost " +
            "FROM PropertyCategory C, Property P1, Reservation R, UserStay US " +
            "WHERE P1.id = R.property_id AND R.id = US.reservation_id AND " +
            "P1.category_id = C.id GROUP BY C.name, P1.id ORDER BY C.name, " +
            "(SUM(US.total_cost)/SUM(R.guests)) DESC) ranks LEFT JOIN Property P " +
            "USING(id) LEFT JOIN Address A ON P.address_id = A.id";
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        while(rs.next()){
            int addressId = rs.getInt(5);
            String street = rs.getString(10);
            String unitNumber = rs.getString(11);
            String zipCode = rs.getString(12);
            String city = rs.getString(13);
            String state = rs.getString(14);
            String country = rs.getString(15);
            Address address = new Address(
                    addressId,
                    street,
                    unitNumber,
                    zipCode,
                    city,
                    state,
                    country
            );

            int categoryId = rs.getInt(6);
            String categoryName = rs.getString(1);
            Category category = new Category(categoryId, categoryName);

            int propertyId = rs.getInt(2);
            String propertyName = rs.getString(3);
            int ownerId = rs.getInt(4);
            String phoneNumber = rs.getString(7);
            String url = rs.getString(8);
            String year = rs.getString(9);
            double cost = rs.getDouble(16);
            ExpensiveProperty property = new ExpensiveProperty(
                    propertyId,
                    propertyName,
                    ownerId,
                    address,
                    category,
                    phoneNumber,
                    url,
                    year,
                    cost
            );

            if(properties.containsKey(categoryName)){
                properties.get(categoryName).add(property);
            } else {
                list = new ArrayList<>();
                list.add(property);
                properties.put(categoryName, list);
            }
        }
        stmt.close();
        return properties;
    }

    public TreeMap<String, List<Property>> getHighestRatedPropertiesPerCategory() throws SQLException {
        TreeMap<String, List<Property>> properties = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        ArrayList<Property> list = null;
        String query = "SELECT ranks.name, P.id, P.name, P.owner_id, P.address_id, " +
                "P.category_id, P.phone_number, P.url, P.year_built, A.street, " +
                "A.unit_number, A.zip_code, A.city, A.state, A.country, ranks.score FROM " +
                "(SELECT C.name ,P1.id, AVG(F.score) as score FROM PropertyCategory C, Property P1, " +
                "PropertyFeedback F WHERE P1.id = F.property_id AND P1.category_id = C.id " +
                "GROUP BY C.name, P1.id ORDER BY C.name, score DESC) ranks LEFT JOIN " +
                "Property P USING(id) LEFT JOIN Address A ON P.address_id = A.id";
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        while(rs.next()){
            int addressId = rs.getInt(5);
            String street = rs.getString(10);
            String unitNumber = rs.getString(11);
            String zipCode = rs.getString(12);
            String city = rs.getString(13);
            String state = rs.getString(14);
            String country = rs.getString(15);
            Address address = new Address(
                    addressId,
                    street,
                    unitNumber,
                    zipCode,
                    city,
                    state,
                    country
            );

            int categoryId = rs.getInt(6);
            String categoryName = rs.getString(1);
            Category category = new Category(categoryId, categoryName);

            int propertyId = rs.getInt(2);
            String propertyName = rs.getString(3);
            int ownerId = rs.getInt(4);
            String phoneNumber = rs.getString(7);
            String url = rs.getString(8);
            String year = rs.getString(9);
            double score = rs.getDouble(16);
            HighlyRatedProperty property = new HighlyRatedProperty(
                    propertyId,
                    propertyName,
                    ownerId,
                    address,
                    category,
                    phoneNumber,
                    url,
                    year,
                    score
            );

            if(properties.containsKey(categoryName)){
                properties.get(categoryName).add(property);
            } else {
                list = new ArrayList<>();
                list.add(property);
                properties.put(categoryName, list);
            }
        }
        stmt.close();
        return properties;
    }

    public List<Feedback> getTopRatedPropertyFeedback(int propertyId, int n) throws SQLException{
        ArrayList<Feedback> feedbacks = new ArrayList<>();
        Property property = getProperty(propertyId);
        String query = "SELECT PF.id, PF.user_id, PF.date, PF.score, PF.comment, ranks.rating, u.login " +
                "FROM (SELECT F.id, AVG(FU.rating) as rating FROM FeedbackUsefulnessRating FU, " +
                "PropertyFeedback F WHERE F.id = FU.feedback_id AND F.property_id = ? " +
                "GROUP BY FU.feedback_id ORDER BY rating DESC) ranks " +
                "LEFT JOIN PropertyFeedback PF USING(id) LEFT JOIN User u ON u.id = PF.user_id LIMIT ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, propertyId);
        stmt.setInt(2, n);
        ResultSet rs = stmt.executeQuery();
        while(rs.next()) {
            int feedbackId = rs.getInt(1);
            int userId = rs.getInt(2);
            Date date = rs.getDate(3);
            int score = rs.getInt(4);
            String comment = rs.getString(5);
            double rating = rs.getDouble(6);
            String userLogin = rs.getString(7);

            feedbacks.add(new UsefulFeedback(
                    feedbackId,
                    userId,
                    userLogin,
                    property,
                    date,
                    score,
                    comment,
                    rating
            ));
        }
        stmt.close();
        return feedbacks;
    }

    public List<User> getMostTrustedUsers(int n) throws SQLException{
        ArrayList<User> users = new ArrayList<>();
        String query = "SELECT U.*, A.*, IFNULL(plus, 0)-IFNULL(minus, 0) as rating FROM User U LEFT JOIN Address A ON U.address_id = A.id " +
                "LEFT JOIN (SELECT rated_user_id as id, COUNT(*) as plus FROM UserTrustRating " +
                "WHERE rating = 1 GROUP BY id) plus ON plus.id = U.id LEFT JOIN " +
                "(SELECT rated_user_id as id, COUNT(*) as minus FROM UserTrustRating " +
                "WHERE rating = 0 GROUP BY id) minus ON minus.id = U.id ORDER BY " +
                "rating DESC LIMIT ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, n);
        ResultSet rs = stmt.executeQuery();
        while(rs.next()) {
            int id = rs.getInt(1);
            String firstName = rs.getString(2);
            String lastName = rs.getString(3);
            String login = rs.getString(4);
            String password = rs.getString(5);
            String phoneNumber = rs.getString(6);

            int addressId = rs.getInt(7);
            String street = rs.getString(9);
            String unitNumber = rs.getString(10);
            String zipCode = rs.getString(11);
            String city = rs.getString(12);
            String state = rs.getString(13);
            String country = rs.getString(14);
            int rating = rs.getInt(15);
            Address address = new Address(
                    addressId,
                    street,
                    unitNumber,
                    zipCode,
                    city,
                    state,
                    country
            );
            users.add(new TrustedUser(id, firstName, lastName, login, password, phoneNumber, address, rating));
        }
        stmt.close();
        return users;
    }

    public List<User> getMostUsefulUsers(int n) throws SQLException{
        ArrayList<User> users = new ArrayList<>();
        String query = "SELECT U.*, A.*, ids.rating FROM User U LEFT JOIN Address A ON " +
                "U.address_id = A.id RIGHT JOIN (SELECT FFU.user_id, AVG(FFU.score) as rating FROM " +
                "(SELECT F.user_id, AVG(FU.rating) as score FROM PropertyFeedback F, " +
                "FeedbackUsefulnessRating FU WHERE F.id = FU.feedback_id GROUP BY " +
                "F.user_id, FU.feedback_id) FFU GROUP BY FFU.user_id ORDER BY " +
                "rating DESC) ids ON ids.user_id = U.id LIMIT ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, n);
        ResultSet rs = stmt.executeQuery();
        while(rs.next()) {
            int id = rs.getInt(1);
            String firstName = rs.getString(2);
            String lastName = rs.getString(3);
            String login = rs.getString(4);
            String password = rs.getString(5);
            String phoneNumber = rs.getString(6);

            int addressId = rs.getInt(7);
            String street = rs.getString(9);
            String unitNumber = rs.getString(10);
            String zipCode = rs.getString(11);
            String city = rs.getString(12);
            String state = rs.getString(13);
            String country = rs.getString(14);
            Address address = new Address(
                    addressId,
                    street,
                    unitNumber,
                    zipCode,
                    city,
                    state,
                    country
            );
            double score = rs.getDouble(15);
            users.add(new UsefulUser(id, firstName, lastName, login, password, phoneNumber, address, score));
        }
        stmt.close();
        return users;
    }

    public boolean feedbackExists(int userId, int propId) throws SQLException {
        String query = "SELECT * FROM PropertyFeedback WHERE user_id = ? AND property_id = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, userId);
        stmt.setInt(2, propId);
        ResultSet results = stmt.executeQuery();
        boolean exists = results.next();
        stmt.close();
        return exists;
    }

    public boolean createFeedback(int userId,
                                  int propId,
                                  Date date,
                                  int score,
                                  String comment) throws SQLException {
        String query = "INSERT INTO PropertyFeedback (user_id, property_id, date, score, comment) " +
            "VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, userId);
        stmt.setInt(2, propId);
        stmt.setDate(3, date);
        stmt.setInt(4, score);
        stmt.setString(5, comment);
        int count = stmt.executeUpdate();
        boolean success = count > 0;
        stmt.close();
        return success;
    }

    public boolean favoriteExists(int userId, int propId) throws SQLException {
        String query = "SELECT * FROM FavoriteProperty WHERE user_id = ? AND property_id = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, userId);
        stmt.setInt(2, propId);
        ResultSet results = stmt.executeQuery();
        boolean exists = results.next();
        stmt.close();
        return exists;
    }

    public boolean createFavoriteProperty(int userId, int propId) throws SQLException {
        String query = "INSERT INTO FavoriteProperty (user_id, property_id) VALUES (?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, userId);
        stmt.setInt(2, propId);
        int count = stmt.executeUpdate();
        boolean success = count > 0;
        stmt.close();
        return success;
    }

    public List<Property> getUserFavorites(int userId) throws SQLException {
        String query = "SELECT p.id, p.name, p.owner_id, p.phone_number, p.url, p.year_built, " +
            "a.id, a.street, a.unit_number, a.zip_code, a.city, a.state, a.country, " +
            "c.id, c.name " +
            "FROM Property p, FavoriteProperty f, Address a, PropertyCategory c " +
            "WHERE f.user_id = ? AND f.property_id = p.id AND p.address_id = a.id AND p.category_id = c.id";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, userId);
        ResultSet results = stmt.executeQuery();
        List<Property> favorites = new ArrayList<>();
        while (results.next()) {
            Address address = new Address(results.getInt("a.id"),
                                          results.getString("a.street"),
                                          results.getString("a.unit_number"),
                                          results.getString("a.zip_code"),
                                          results.getString("a.city"),
                                          results.getString("a.state"),
                                          results.getString("a.country"));
            Category category = new Category(results.getInt("c.id"), results.getString("c.name"));
            Property property = new Property(results.getInt("p.id"),
                                             results.getString("p.name"),
                                             results.getInt("p.owner_id"),
                                             address,
                                             category,
                                             results.getString("p.phone_number"),
                                             results.getString("p.url"),
                                             results.getString("p.year_built"));
            favorites.add(property);
        }
        stmt.close();
        return favorites;
    }

}
