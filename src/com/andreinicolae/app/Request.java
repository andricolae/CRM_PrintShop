package com.andreinicolae.app;

/**
 * @author Andrei Nicolae Căluțiu.
 * @version 1.0
 * The class Request holds information about all the Requests the Print Shop has.
 * It holds the description and the file needed for the request.
 */
public class Request {
    /** Requests ID Number */
    private int id;

    /** Clients ID that made the Request */
    private int clientId;

    /** Requests Description */
    private String description;

    /** Requests related File */
    private byte[] fileData;

    /** Constructor for Requests with all the params
     * @param id the Request ID
     * @param clientId the Clients ID that made the Request
     * @param description the Requests Description
     * @param fileData the Requests File
     */
    public Request(int id, int clientId, String description, byte[] fileData) {
        this.id = id;
        this.clientId = clientId;
        this.description = description;
        this.fileData = fileData;
    }

    /** Constructor for Requests without the ID
     * @param clientId the Clients ID that made the Request
     * @param description the Requests Description
     * @param fileData the Requests File
     */
    public Request(int clientId, String description, byte[] fileData) {
        this.clientId = clientId;
        this.description = description;
        this.fileData = fileData;
    }

    /** Constructor for Requests without Description and File
     * @param id the Request ID
     * @param clientId the Clients ID that made the Request
     */
    public Request(int id, int clientId) {
        this.id = id;
        this.clientId = clientId;
    }

    /** Returns the Requests ID
     * @return an {@code int} value
     */
    public int getId() {
        return id;
    }

    /** Returns the Requests ID
     * @return an {@code int} value
     */
    public int getClientId() {
        return clientId;
    }

    /** Returns the Requests Description
     * @return a {@code String} value
     */
    public String getDescription() {
        return description;
    }

    /** Returns the Requests File
     * @return a {@code byte} Array
     */
    public byte[] getFile() {
        return fileData;
    }

    /** Sets the Requests ID
     * @param id the Requests ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /** Sets the Clients ID related to the Request
     * @param clientId the CLients ID
     */
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    /** Sets the Requests Description
     * @param description the Requsts Description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /** Sets the Requests File
     * @param fileData the Requests File
     */
    public void setFile(byte[] fileData) {
        this.fileData = fileData;
    }

    /** The Overriding of the toString Method
     * @return a {@code String} value with the needed values
     */
    @Override
    public String toString() {
        return "ID = " + id +
                " | CLIENT ID = " + clientId;
    }
}
