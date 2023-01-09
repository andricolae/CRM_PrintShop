package com.andreinicolae.app;
import java.sql.*;
import java.util.Vector;

/**
 * @author Andrei Nicolae Căluțiu.
 * @version 1.0
 * the class ClientDAO manages the interaction of the Data Structure Client with the Database
 * It holds functionality for the Database connection and sql statements needed on the table "clients"
 */
public class ClientDAO {
    /** URL pointing to the used DB */
    static final String DB_URL = "jdbc:mysql://localhost:3306/crm_printshop";

    /** Connection variable to link the DB */
    private Connection con;

    /** Statement variable to process the SQL Statements */
    private Statement stm;

    /** Prepared Statement variable to process the SQL Statements */
    private PreparedStatement pstm;

    /** Result Set variable to process the returned result of the SQL Statements */
    private ResultSet rs;

    /** Method for the connection to the DB */
    private void connect() {
        try {
            con = DriverManager.getConnection(DB_URL, "root", "");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /** Method for the closing of the DB connection */
    private void close() {
        try {
            if(rs != null)
                rs.close();
            if(stm != null)
                stm.close();
            if(pstm != null)
                pstm.close();
            if(con != null)
                con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Method for inserting a new Client to the DB "clients" table
     * @param client the Client object to be inserted
     */
    public void insertClient(Client client) {
        connect();
        try {
            pstm = con.prepareStatement("INSERT INTO client VALUES(default, ?, ?, ?, ?)");
            pstm.setString(1, client.getName());
            pstm.setString(2, client.getEmail());
            pstm.setString(3, client.getPhone());
            pstm.setBoolean(4, client.isPendingRequest());
            pstm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        close();
    }

    /** Returns a Client Entry from the DB by the searched ID
     * @param searchId the Clients ID
     * @return a {@code Client} Data Structure where the ID matches a DB entry
     */
    public Client getClientByID(int searchId) {
        connect();
        Client client = null;
        try {
            stm = con.createStatement();
            rs = stm.executeQuery("SELECT * FROM client WHERE id = '" + searchId + "'");
            rs.next();
            int id = rs.getInt(1);
            String name = rs.getString(2);
            String email = rs.getString(3);
            String phone = rs.getString(4);
            boolean pendingRequest = rs.getBoolean(5);
            client = new Client(id, name, email, phone, pendingRequest);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        close();
        return client;
    }

    /** Returns all the Clients from the DB
     * @return a Vector of {@code Client} Data Structures
     */
    public Vector<Client> getClientsData() {
        Vector<Client> clientsVector = new Vector<>();
        connect();
        try {
            stm = con.createStatement();
            rs = stm.executeQuery("SELECT id, email, phone, pendingRequest FROM client");
            while(rs.next()) {
                int id = rs.getInt(1);
                String email = rs.getString(2);
                String phone = rs.getString(3);
                boolean pendingRequest = rs.getBoolean(4);
                clientsVector.add(new Client(id, email, phone, pendingRequest));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        close();
        return clientsVector;
    }

    /** Method for updating a Clients info in the DB "clients" table
     * @param client the Client object to get the ID that needs an update
     */
    public void updateClient(Client client) {
        connect();
        try {
            pstm = con.prepareStatement("UPDATE client SET name = ?, email = ?, phone = ?, pendingRequest = ? WHERE id = " + client.getId());
            pstm.setString(1, client.getName());
            pstm.setString(2, client.getEmail());
            pstm.setString(3, client.getPhone());
            pstm.setBoolean(4, client.isPendingRequest());
            pstm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        close();
    }

    /** Method for updating a Clients Request Status in the DB "clients" table
     * @param clientId the Client object to be inserted
     */
    public void updateRequest(int clientId){
        connect();
        try {
            pstm = con.prepareStatement("UPDATE client SET pendingRequest = true WHERE id = " + clientId);
            pstm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        close();
    }

    /** Method for updating a Clients Request Status to false
     * if there is no Request for a specific client in the requests table */
    public void falseIfNoRequest() {
        connect();
        try {
            pstm = con.prepareStatement("UPDATE client SET pendingRequest = false WHERE client.id NOT IN ( SELECT clientId FROM request)");
            pstm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        close();
    }
}
