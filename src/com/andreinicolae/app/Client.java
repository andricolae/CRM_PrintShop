package com.andreinicolae.app;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Andrei Nicolae Căluțiu.
 * @version 1.0
 * The class Client holds information about a specific client.
 * Together with the contact information, it also stores information
 * about active requests of the client.
 */
public class Client{
    /** Clients Email Validator */
    private static final String EMAIL_VALIDATOR = "[A-Za-z0-9+_.-]+@(.+)$";

    /** Pattern type variable for the email validation */
    private static final Pattern patternEmail = Pattern.compile(EMAIL_VALIDATOR);

    /** Clients Phone Number Validator */
    private static final String PHONE_VALIDATOR = "\\+4?[0-9]{11}";

    /** Pattern type variable for the phoneNr validation */
    private static final Pattern patternPhone = Pattern.compile(PHONE_VALIDATOR);

    /** Clients ID Number */
    private int id;

    /** Clients Name */
    private String name;

    /** Clients Email */
    private String email;

    /** Clients Phone */
    private String phone;

    /** Clients boolean status of pending Request */
    private boolean pendingRequest;

    /** Constructor for Client with all the params
     * @param id the Clients ID
     * @param name the Clients Name
     * @param email the Clients Email
     * @param phone the Clients Phone Number
     * @param pendingRequest the Clients Active Requests Status
     */
    public Client(int id, String name, String email, String phone, boolean pendingRequest) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.pendingRequest = pendingRequest;
    }

    /** Constructor for Client without id
     * @param name the Clients Name
     * @param email the Clients Email
     * @param phone the Clients Phone Number
     * @param pendingRequest the Clients Active Requests Status
     */
    public Client(String name, String email, String phone, boolean pendingRequest) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.pendingRequest = pendingRequest;
    }

    /** Constructor for Client without the name
     * @param id the Clients id
     * @param email the Clients Email
     * @param phone the Clients Phone Number
     * @param pendingRequest the Clients Active Requests Status
     */
    public Client(int id, String email, String phone, boolean pendingRequest) {
        this.id = id;
        this.email = email;
        this.phone = phone;
        this.pendingRequest = pendingRequest;
    }

    /** Returns the Clients ID
     * @return an {@code int} value
     */
    public int getId() {
        return id;
    }

    /** Returns the Clients Name
     * @return a {@code String} value
     */
    public String getName() {
        return name;
    }

    /** Returns the Clients Email
     * @return a {@code String} value
     */
    public String getEmail() {
        return email;
    }

    /** Returns the Clients Phone
     * @return a {@code String} value
     */
    public String getPhone() {
        return phone;
    }

    /** Returns the Clients Request Status
     * @return a {@code boolean} value
     */
    public boolean isPendingRequest() {
        return pendingRequest;
    }

    /** Sets the Clients ID
     * @param id the CLients ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /** Sets the Clients Name
     * @param name the CLients name
     */
    public void setName(String name) {
        this.name = name;
    }

    /** Sets the Clients Email
     * @param email the CLients Email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /** Sets the Clients Phone
     * @param phone the CLients Phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /** Sets the Clients Request Status
     * @param pendingRequest the CLients Request Status
     */
    public void setPendingRequest(boolean pendingRequest) {
        this.pendingRequest = pendingRequest;
    }

    /** Method for the Validation of the Clients Email
     * @param email the Clients email
     * @return a {@code boolean} value if it matches the pattern or not
     */
    public static boolean isEmailValid(String email) {
        Matcher match = patternEmail.matcher(email);
        return match.matches();
    }

    /** Method for the Validation of the Clients Phone
     * @param phone the Clients phone
     * @return a {@code boolean} value if it matches the pattern or not
     */
    public static boolean isPhoneValid(String phone) {
        Matcher match = patternPhone.matcher(phone);
        return match.matches();
    }

    /** The Overriding of the toString Method
     * @return a {@code String} value with the needed values
     */
    @Override
    public String toString() {
        return "ID = " + id +
                " | " + email +
                " | " + phone +
                " | REQUEST ? -> " + pendingRequest;

    }
}