package com.andreinicolae.app;
import java.io.*;
import java.sql.*;
import java.util.Vector;

/**
 * @author Andrei Nicolae Căluțiu.
 * @version 1.0
 * the class RequestDAO manages the interaction of the Data Structure Request with the Database
 * It holds functionality for the Database connection and sql statements needed on the table "requests"
 */
public class RequestDAO {
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

    /** Method for inserting a new Request to the DB "requests" table
     * @param request the Request object to be inserted
     */
    public void insertRequest(Request request) {
        connect();
        try {
            pstm = con.prepareStatement("INSERT INTO request VALUES(default, ?, ?, ?)");
            pstm.setInt(1, request.getClientId());
            pstm.setString(2, request.getDescription());
            ByteArrayInputStream inputFile = new ByteArrayInputStream(request.getFile());
            pstm.setBlob(3, inputFile);
            pstm.executeUpdate();
            inputFile.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        close();
    }

    /** Returns all the Requests from the DB related to a certain client by it's ID
     * @param searchClientId the clients ID to search it's requests
     * @return a Vector of {@code String} values, each containing the description of each Request
     */
    public Vector<String> getRequestsByClientId(int searchClientId) {
        Vector<String> requestsVector = new Vector<>();
        connect();
        try {
            stm = con.createStatement();
            rs = stm.executeQuery("SELECT id, description FROM request WHERE clientId = '" + searchClientId + "'");
            while(rs.next()){
                String description = "request id> " + rs.getInt(1);
                description += " - " + rs.getString(2);
                requestsVector.add(description + "\n");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        close();
        return requestsVector;
    }

    /** Returns a Request Entry from the DB by the searched ID
     * @param searchRequestId the Requests ID
     * @return a {@code Request} Data Structure where the ID matches a DB entry
     */
    public Request getRequestByRequestId(int searchRequestId) {
        Request request = null;
        connect();
        try {
            stm = con.createStatement();
            rs = stm.executeQuery("SELECT * FROM request WHERE id = '" + searchRequestId + "'");
            while(rs.next()){
                int id = rs.getInt(1);
                int clientId = rs.getInt(2);
                String description = rs.getString(3);
                Blob blob = rs.getBlob(4);
                byte[] fileData = blob.getBytes(1, (int)blob.length());
                request = new Request(id, clientId, description, fileData);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        close();
        return request;
    }

    /** Returns all the Requests from the DB
     * @return a Vector of {@code Request} Data Structures
     */
    public Vector<Request> getRequestsId() {
        Vector<Request> requestsVector = new Vector<>();
        connect();
        try {
            stm = con.createStatement();
            rs = stm.executeQuery("SELECT id, clientId FROM request");
            while(rs.next()){
                int id = rs.getInt(1);
                int clientId = rs.getInt(2);
                requestsVector.add(new Request(id, clientId));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        close();
        return requestsVector;
    }

    /** Method for downloading the file for a Request in the DB
     * @param requestId the ID of the Request for which we want to download the file */
    public void getFileByRequestId(int requestId){
       connect();
        File requestFile = null;
        try {
            stm = con.createStatement();
            rs = stm.executeQuery("SELECT file FROM request WHERE id = " + requestId);
            while (rs.next()) {
                InputStream input = rs.getBinaryStream(1);
                String stringIn = Util.convert(input);
                requestFile = new File("request " + requestId);
                FileOutputStream output = new FileOutputStream(requestFile);
                output.write(stringIn.getBytes());
                output.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        close();
    }

    /** Method for deleting Request in the DB "requests" table by marking the Request as done"
     * @param requestId the Request ID for the object to be deleted  */
    public void deleteRequestOnDone(int requestId) {
        connect();
        try {
            pstm = con.prepareStatement("DELETE FROM request WHERE id = " + requestId);
            pstm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        close();
    }
}
