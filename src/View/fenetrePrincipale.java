package View;


import Model.SNMPConfig;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;

public class fenetrePrincipale extends JFrame
{
    private JPanel panel1;
    private JTextField textField_AgentIpAddress;
    private JTextField textField_ObjectID;
    private JComboBox comboBox_Operations;
    private JButton button_Advanced;
    private JButton button_Go;
    private JTable table_ReponseRequest;
    private DefaultTableModel tableModel;
    private SNMPConfig config;

    public fenetrePrincipale()
    {
        config = new SNMPConfig();
        //initalise le model de la table
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Name/OID");
        tableModel.addColumn("Value");
        tableModel.addColumn("Type");
        tableModel.addColumn("IP:Port");
        //ajoute le model a la table
        table_ReponseRequest.setModel(tableModel);

        //initialise le comboBox
        comboBox_Operations.addItem("GET");
        comboBox_Operations.addItem("GET_NEXT");
        comboBox_Operations.addItem("SET");

        textField_AgentIpAddress.setText(config.getIpAddress());

        //initialise le panel de la fenetre
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//ferme l'app quand quand on clic sur la croix
        setContentPane(panel1);
        setTitle("Manager SNMP");
        pack();
        setVisible(true);

    }

    public String getAgentIpAddress()
    {
        return textField_AgentIpAddress.getText();
    }

    public String getObjectID()
    {
        return textField_ObjectID.getText();
    }

    public String getSelectedOperation()
    {
        return comboBox_Operations.getSelectedItem().toString();
    }

    public void addResultTable(String nameOid, String value, String type, String ipPort)
    {
        tableModel.addRow(new Object[]{nameOid, value, type, ipPort});
    }
    public void clearTable()
    {
        tableModel.setRowCount(0);
    }

    public void showErrorMessage(String message)
    {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showInfoMessage(String message)
    {
        JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

   /* public void saveConfig(SNMPConfig config)
    {
        config.setIpAddress(getAgentIpAddress());
        config.setOid(getObjectID());
    }*/

    // Méthode pour charger une configuration
    public SNMPConfig getConfig()
    {
        config.setIpAddress(getAgentIpAddress());
        config.setOid(getObjectID());

        return config;
    }

    //ajoute un listener sur le bouton Go -> pour lancer la requete
    public void addGoButtonListener(ActionListener listener)
    {
        button_Go.addActionListener(listener);
    }

    //ajoute un listener sur le bouton Advanced -> permet d'afficher la fenetreAdvanced
    public void addAdvancedButtonListener(ActionListener listener)
    {
        button_Advanced.addActionListener(listener);
    }
}