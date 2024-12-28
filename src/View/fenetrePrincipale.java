package View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;

public class fenetrePrincipale extends JFrame
{
    private JPanel panel1;
    private JTextField textField_AgentIpAddress;
    private JButton button_Go;
    private JTable table_ReponseRequest;
    private JTextField textField_ObjectID;
    private JComboBox comboBox_Operations;
    private JButton button_Advanced;
    private DefaultTableModel tableModel;

    public fenetrePrincipale()
    {
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
        comboBox_Operations.addItem("SET");

        //initialise le panel de la fenetre
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//ferme l'app quand quand on clic sur la croix
        setContentPane(panel1);
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

    public void showErrorMessage(String message)
    {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    //ajoute un listener sur le bouton Go
    public void addGoButtonListener(ActionListener listener)
    {
        button_Go.addActionListener(listener);
    }

    //ajoute un listener sur le bouton Advanced
    public void addAdvancedButtonListener(ActionListener listener)
    {
        button_Advanced.addActionListener(listener);
    }
}