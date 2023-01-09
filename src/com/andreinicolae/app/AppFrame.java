package com.andreinicolae.app;
import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Vector;

import static javax.swing.KeyStroke.getKeyStroke;

/**
 * @author Andrei Nicolae Căluțiu.
 * @version 1.0
 * The class that launches the app.
 * Sets the connection between the interface and the methods
 * and instantiates the data structures.
 */
public class AppFrame extends JFrame {
    /** The apps font constant */
    private static final Font font = new Font("Courier", Font.BOLD,12);

    /** a byte array for managing the file */
    byte[] bytes;

    /** Panel for the Clients management Tab */
    private JPanel registerClientPanel;

    /** Panel for the Requests management Tab */
    private JPanel requestsPanel;

    /** Pane */
    private JTabbedPane tabbedPane;

    /** The menu bar */
    private JMenuBar menuBar;

    /** File menu item */
    private JMenu mFile;

    /** About menu item */
    private JMenu mAbout;

    /** Info menu item */
    private JMenuItem mIInfo;

    /** Exit menu item */
    private JMenuItem mIExit;

    /** Upload Client Button */
    private JButton btnUploadClient;

    /** Update Client Button */
    private JButton btnUpdateClient;

    /** Reset Client Tab Fields Button */
    private JButton btnResetFields;

    /** Client Name Text Field */
    private JTextField tfClientName;

    /** Client Email Text Field */
    private JTextField tfClientEmail;

    /** Client Phone Text Field */
    private JTextField tfClientPhone;

    /** Client Request Pending Radio Button */
    private JRadioButton rBPendingRequest;

    /** Client Request Not Pending Radio Button */
    private JRadioButton rBNoPendingRequest;

    /** Client Request Radio Button Group */
    private ButtonGroup bGPendingRequest;

    /** Clients Display Combo Box */
    private JComboBox<Client> cBClient;

    /** Model for the Client Combo Box */
    private DefaultComboBoxModel<Client> cBModelClient;

    /** ClientDAO Object for management */
    private ClientDAO clientDAO;

    /** Requests Display Combo Box */
    private JComboBox<Request> cBRequest;

    /** Model for the Request Combo Box */
    private DefaultComboBoxModel<Request> cBModelRequest;

    /** Label to choosing a File for Upload */
    JLabel lbChooseFile;

    /** Request ID Text Field */
    private JTextField tFRequestId;

    /** Clients ID for a Request Text Field */
    private JTextField tFCLientId;

    /** Description of the Request Text Area */
    private JTextArea tARequestDescription;

    /** Register Request Button */
    private JButton btnRegisterRequest;

    /** Get Request from DB Button */
    private JButton btnGetRequest;

    /** Choose File to Upload Request Button */
    private JButton btnChooseFile;

    /** Download File for Request Button */
    private JButton btnDownloadFile;

    /** Reset Request Fields Button */
    private JButton btnResetRequestFields;

    /** Mark Request as Done Button */
    private JButton btnRequestDelivered;

    /** Object to launch an Explorer for Choosing a File for the Request */
    private JFileChooser fileChooser;

    /** RequestDAO Object for management */
    private RequestDAO requestDAO;

    /** Constructor for the Apps Frame w/ none @param
        Initiates the Frame, the Menu and the Listeners for each button in the app
     */
    public AppFrame() {
        super("My Print Shop CRM");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 500);
        setResizable(false);

        menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        mFile = new JMenu("File");
        menuBar.add(mFile);
        mAbout = new JMenu("About");
        menuBar.add(mAbout);
        mIExit = new JMenuItem("Exit");
        mIExit.addActionListener(e -> System.exit(0));
        mIExit.setAccelerator(getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK));
        mFile.add(mIExit);
        mIInfo = new JMenuItem("Info");
        mIInfo.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "<html><center><h3>My Print Shop CRM App</h3><br><h3>Version 1.0</h3></center> </html>"));
        mAbout.add(mIInfo);

        clientDAO = new ClientDAO();
        requestDAO = new RequestDAO();

        buildRegisterClientPanel();
        buildRequestsPanel();

        fileChooser = new JFileChooser(".");
        fileChooser.setMultiSelectionEnabled(false);

        tabbedPane = new JTabbedPane();
        tabbedPane.add("Clients Database", registerClientPanel);
        tabbedPane.add("Requests", requestsPanel);
        setContentPane(tabbedPane);
        setLocationRelativeTo(null);

        cBClient.addItemListener(e -> onClientSelect());
        btnUploadClient.addActionListener(e -> uploadClient());
        btnUploadClient.setMnemonic(KeyEvent.VK_U);
        btnUpdateClient.addActionListener(e -> updateClient());
        btnUpdateClient.setMnemonic(KeyEvent.VK_P);
        btnResetFields.addActionListener(e -> resetFields());
        btnResetFields.setMnemonic(KeyEvent.VK_X);
        cBRequest.addItemListener(e -> onRequestSelect());
        btnChooseFile.addActionListener(e -> chooseFile());
        btnChooseFile.setMnemonic(KeyEvent.VK_O);
        btnRegisterRequest.addActionListener(e -> registerRequest());
        btnRegisterRequest.setMnemonic(KeyEvent.VK_R);
        btnGetRequest.addActionListener(e -> showRequests());
        btnGetRequest.setMnemonic(KeyEvent.VK_S);
        btnDownloadFile.addActionListener(e -> downloadFileForRequest());
        btnDownloadFile.setMnemonic(KeyEvent.VK_D);
        btnResetRequestFields.addActionListener(e -> resetRequestFields());
        tabbedPane.addChangeListener(e -> onTabChange());
        btnRequestDelivered.addActionListener(e -> onRequestDelivered());
        btnRequestDelivered.setMnemonic(KeyEvent.VK_K);
    }

    /** Method to build the Clients management Panel
     *  Initiates the Objects and positions them on the frame
     */
    private void buildRegisterClientPanel() {
        registerClientPanel = new JPanel();
        registerClientPanel.setLayout(null);

        JLabel lbIntro = new JLabel("Choose a Client from the following List");
        lbIntro.setFont(font);
        lbIntro.setBounds(50, 15, 250, 25);
        registerClientPanel.add(lbIntro);

        cBClient = new JComboBox<>();
        cBModelClient = new DefaultComboBoxModel<>(clientDAO.getClientsData());
        cBClient.setModel(cBModelClient);
        cBClient.setBounds(30, 45, 500, 30);
        cBClient.setSelectedItem(null);
        registerClientPanel.add(cBClient);

        JLabel lbName = new JLabel("Name");
        lbName.setBounds(50, 75, 200, 25);
        lbName.setFont(font);
        registerClientPanel.add(lbName);
        tfClientName = new JTextField();
        tfClientName.setBounds(50, 100, 200, 25);
        registerClientPanel.add(tfClientName);

        JLabel lbEmail = new JLabel("E-mail");
        lbEmail.setBounds(50, 125, 200, 25);
        lbEmail.setFont(font);
        registerClientPanel.add(lbEmail);
        tfClientEmail = new JTextField();
        tfClientEmail.setBounds(50, 150, 200, 25);
        registerClientPanel.add(tfClientEmail);

        JLabel lbPhone = new JLabel("Phone");
        lbPhone.setBounds(50, 175, 200, 25);
        lbPhone.setFont(font);
        registerClientPanel.add(lbPhone);
        tfClientPhone = new JTextField();
        tfClientPhone.setBounds(50, 200, 200, 25);
        registerClientPanel.add(tfClientPhone);

        JLabel lbPendingRequest = new JLabel("Pending Request?");
        lbPendingRequest.setBounds(50, 225, 200, 25);
        lbPendingRequest.setFont(font);
        registerClientPanel.add(lbPendingRequest);
        rBPendingRequest = new JRadioButton();
        rBPendingRequest.setText("Yes");
        rBNoPendingRequest = new JRadioButton();
        rBNoPendingRequest.setText("No");
        rBPendingRequest.setBounds(50, 250, 200, 20);
        rBNoPendingRequest.setBounds(50, 270, 200, 20);
        bGPendingRequest = new ButtonGroup();
        bGPendingRequest.add(rBPendingRequest);
        bGPendingRequest.add(rBNoPendingRequest);
        registerClientPanel.add(rBPendingRequest);
        registerClientPanel.add(rBNoPendingRequest);

        JPanel cmdPanel = new JPanel();
        cmdPanel.setBounds(20, 350, 560, 150);
        btnUploadClient = new JButton("Upload Client to Database");
        btnUploadClient.setFont(font);
        cmdPanel.add(btnUploadClient);
        btnUpdateClient = new JButton("Update Client Info");
        btnUpdateClient.setFont(font);
        cmdPanel.add(btnUpdateClient);
        btnResetFields = new JButton("Reset Fields");
        btnResetFields.setFont(font);
        cmdPanel.add(btnResetFields);
        registerClientPanel.add(cmdPanel);
    }

    /** Method to build the Requests management Panel
     *  Initiates the Objects and positions them on the frame
     */
    private void buildRequestsPanel() {
        requestsPanel = new JPanel();
        requestsPanel.setLayout(null);

        JLabel lbIntro = new JLabel("Lookup for Requests");
        lbIntro.setFont(font);
        lbIntro.setBounds(50, 15, 250, 25);
        requestsPanel.add(lbIntro);

        cBRequest = new JComboBox<>();
        cBModelRequest = new DefaultComboBoxModel<>(requestDAO.getRequestsId());
        cBRequest.setModel(cBModelRequest);
        cBRequest.setBounds(50, 45, 250, 30);
        cBRequest.setSelectedItem(null);
        requestsPanel.add(cBRequest);

        JLabel lbRequest = new JLabel("Search Requests by Client ID");
        lbRequest.setBounds(50, 75, 200, 25);
        lbRequest.setFont(font);
        requestsPanel.add(lbRequest);
        tFRequestId = new JTextField();
        tFRequestId.setBounds(50, 100, 75, 25);
        requestsPanel.add(tFRequestId);

        JLabel lbClientId = new JLabel("Client ID to Upload Request");
        lbClientId.setBounds(250, 75, 200, 25);
        lbClientId.setFont(font);
        requestsPanel.add(lbClientId);
        tFCLientId = new JTextField();
        tFCLientId.setBounds(250, 100, 75, 25);
        requestsPanel.add(tFCLientId);

        JLabel lbDescription = new JLabel("Request Description");
        lbDescription.setBounds(50, 125, 150, 25);
        lbDescription.setFont(font);
        requestsPanel.add(lbDescription);
        tARequestDescription = new JTextArea();
        tARequestDescription.setBounds(50, 150, 300, 125);
        tARequestDescription.setLineWrap(true);
        requestsPanel.add(tARequestDescription);

        lbChooseFile = new JLabel("Choose a File for the Request");
        lbChooseFile.setBounds(50, 275, 200, 25);
        lbChooseFile.setFont(font);
        requestsPanel.add(lbChooseFile);
        btnChooseFile = new JButton("Open Explorer");
        btnChooseFile.setFont(font);
        btnChooseFile.setBounds(55, 300, 160, 25);
        requestsPanel.add(btnChooseFile);
        btnResetRequestFields = new JButton("Reset Fields");
        btnResetRequestFields.setFont(font);
        btnResetRequestFields.setBounds(220, 300, 160, 25);
        requestsPanel.add(btnResetRequestFields);
        btnRequestDelivered = new JButton("Done");
        btnRequestDelivered.setFont(font);
        btnRequestDelivered.setBounds(385, 300, 160, 25);
        requestsPanel.add(btnRequestDelivered);

        JPanel cmdPanel = new JPanel();
        cmdPanel.setBounds(20, 355, 560, 150);
        btnRegisterRequest = new JButton("Register");
        btnRegisterRequest.setFont(font);
        FontMetrics fm = btnRegisterRequest.getFontMetrics(btnRegisterRequest.getFont());
        int width = fm.stringWidth("MMMMMMMMMMMMMMMM");
        int height = 25;
        btnRegisterRequest.setPreferredSize(new Dimension(width, height));
        cmdPanel.add(btnRegisterRequest);
        btnGetRequest = new JButton("Show");
        btnGetRequest.setFont(font);
        FontMetrics fm2 = btnRegisterRequest.getFontMetrics(btnGetRequest.getFont());
        int width2 = fm2.stringWidth("MMMMMMMMMMMMMMMM");
        int height2 = 25;
        btnGetRequest.setPreferredSize(new Dimension(width2, height2));
        cmdPanel.add(btnGetRequest);
        btnDownloadFile = new JButton("Download");
        btnDownloadFile.setFont(font);
        FontMetrics fm3 = btnDownloadFile.getFontMetrics(btnDownloadFile.getFont());
        int width3 = fm3.stringWidth("MMMMMMMMMMMMMMMM");
        int height3 = 25;
        btnDownloadFile.setPreferredSize(new Dimension(width3, height3));
        cmdPanel.add(btnDownloadFile);
        requestsPanel.add(cmdPanel);
    }

    /** Method on selecting a Client from the Combo Box
     *  It fills the Text Fields with the Information on that Client
     */
    private void onClientSelect() {
        if(cBModelClient.getSize() == 0)
            return;
        Client client = cBModelClient.getElementAt(cBClient.getSelectedIndex());
        Client clientResult = clientDAO.getClientByID(Integer.parseInt(String.valueOf(client.getId())));
        tfClientName.setText(clientResult.getName());
        tfClientEmail.setText(clientResult.getEmail());
        tfClientPhone.setText(clientResult.getPhone());
        if(clientResult.isPendingRequest())
            rBPendingRequest.setSelected(true);
        else
            rBNoPendingRequest.setSelected(true);
    }

    /** Method on uploading a Client
     *  It collect the information in the Text Fields and other elements and
     *  creates a Client Object which it uploads to the DB
     */
    private void uploadClient() {
        Client clientToUpload = null;
        if((tfClientEmail.getText().equals("") && tfClientPhone.getText().equals(""))) {
            JOptionPane.showMessageDialog(this, "Introduceti Datele!");
            return;
        }
        if(tfClientName.getText().length() > 50 || tfClientEmail.getText().length() > 100 || tfClientPhone.getText().length() > 25){
            JOptionPane.showMessageDialog(this, "Valori prea mari. Reintroduceți!");
            return;
        }
        if(rBPendingRequest.isSelected())
            clientToUpload = new Client(tfClientName.getText(), tfClientEmail.getText(), tfClientPhone.getText(), true);
        else
            clientToUpload = new Client(tfClientName.getText(), tfClientEmail.getText(), tfClientPhone.getText(), false);
        clientDAO.insertClient(clientToUpload);
        JOptionPane.showMessageDialog(this, "Client Added to Database!");
        cBModelClient.removeAllElements();
        for (Client client : clientDAO.getClientsData())
            cBModelClient.addElement(client);
    }

    /** Method on updating a Client
     *  It collect the information in the Text Fields and other elements and
     *  updates the Client with te current id in the DB
     */
    private void updateClient() {
        Client clientToUpdate = null;
        Client clientSelected = cBModelClient.getElementAt(cBClient.getSelectedIndex());
        if(clientSelected == null) {
            JOptionPane.showMessageDialog(this, "Alegeti un Client de Modificat!");
            return;
        }
        if(tfClientEmail.getText().equals("") && tfClientPhone.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Email and Phone can not be null");
            return;
        }
        if(rBPendingRequest.isSelected())
            clientToUpdate = new Client(clientSelected.getId(), tfClientName.getText(), tfClientEmail.getText(), tfClientPhone.getText(), true);
        else
            clientToUpdate = new Client(clientSelected.getId()  , tfClientName.getText(), tfClientEmail.getText(), tfClientPhone.getText(), false);
        clientDAO.updateClient(clientToUpdate);
        JOptionPane.showMessageDialog(this, "Client Info Updated!");
        cBModelClient.removeAllElements();
        for (Client client : clientDAO.getClientsData())
            cBModelClient.addElement(client);
    }

    /** Method on reseting the Fields in the Client tab to a blank instance */
    private void resetFields() {
        cBClient.setSelectedItem(cBModelClient.getElementAt(0));
        tfClientName.setText("");
        tfClientEmail.setText("");
        tfClientPhone.setText("");
        bGPendingRequest.clearSelection();
    }

    /** It launches the File Chooser and loads it into the byte array */
    private void chooseFile() {
        bytes = null;
        if(fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                bytes = Files.readAllBytes(Paths.get(String.valueOf(fileChooser.getSelectedFile())));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            lbChooseFile.setText("File Chosen");
        }
    }

    /** Method on selecting a Request from the Combo Box
     *  It fills the Text Fields with the Information on that Request
     */
    private void onRequestSelect() {
        if(cBModelRequest.getSize() == 0)
            return;
        Request request = cBModelRequest.getElementAt(cBRequest.getSelectedIndex());
        Request requestResult = requestDAO.getRequestByRequestId(Integer.parseInt(String.valueOf(request.getId())));
        tARequestDescription.setText(requestResult.getDescription());
    }

    /** Method on uploading a Request
     *  It collect the information in the Text Fields andother elements and
     *  creates a Client Object which it uploads to the DB
     */
    private void registerRequest() {
        Request requestToUpload = null;
        if(fileChooser.getSelectedFile() == null){
            JOptionPane.showMessageDialog(this, "File can not be empty");
            return;
        }
        if(tFCLientId.getText().equals("") && tARequestDescription.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Client ID and Description can not be empty");
            return;
        }
        requestToUpload = new Request(Integer.parseInt(tFCLientId.getText()), tARequestDescription.getText(), bytes);
        requestDAO.insertRequest(requestToUpload);
        clientDAO.updateRequest(Integer.parseInt(tFCLientId.getText()));
        JOptionPane.showMessageDialog(this, "Request Registered");
        lbChooseFile.setText("Choose a File for the Request");
        cBModelRequest.removeAllElements();
        for (Request request : requestDAO.getRequestsId())
            cBModelRequest.addElement(request);
    }

    /** Method on printing the Requests for a specific client
     *  It prints all the Requests Description to get an Overview on a Specific Clients Requests
     */
    private void showRequests() {
        if(tFRequestId == null) {
            JOptionPane.showMessageDialog(this, "Choose a Client!");
            return;
        }
        int clientId = Integer.valueOf(tFRequestId.getText());
        Vector<String> requests = requestDAO.getRequestsByClientId(clientId);
        String result = Util.printDescription(requests);
        tARequestDescription.setText(result);
    }
    private void downloadFileForRequest() {
        if(cBModelRequest.getSize() == 0)
            return;
        requestDAO.getFileByRequestId(cBModelRequest.getElementAt(cBRequest.getSelectedIndex()).getId());
        JOptionPane.showMessageDialog(this, "File Downloaded");
    }

    /** Method on reseting the Fields in the Request tab to a blank instance */
    private void resetRequestFields() {
        cBRequest.setSelectedItem(cBModelRequest.getElementAt(0));
        tARequestDescription.setText("");
        tFCLientId.setText("");
        tFRequestId.setText("");
    }

    /** Method on updating the Combo Boxes every time another one is in focus */
    private void onTabChange() {
        if(tabbedPane.getSelectedComponent() == registerClientPanel) {
            cBModelClient.removeAllElements();
            for (Client client : clientDAO.getClientsData())
                cBModelClient.addElement(client);
        }
        if(tabbedPane.getSelectedComponent() == requestsPanel) {
            cBModelRequest.removeAllElements();
            for (Request request : requestDAO.getRequestsId())
                cBModelRequest.addElement(request);
        }
    }

    /** Method on marking a request as done
     *  It deletes the requests, checks the client if it still has any request, or marks it's status as false.
     *  Updates the Combo Box for the Requests
     */
    private void onRequestDelivered() {
        requestDAO.deleteRequestOnDone(cBModelRequest.getElementAt(cBRequest.getSelectedIndex()).getId());
        clientDAO.falseIfNoRequest();
        JOptionPane.showMessageDialog(this, "Request Done");
        cBModelRequest.removeAllElements();
        for (Request request : requestDAO.getRequestsId())
            cBModelRequest.addElement(request);
    }
}