package View;

import Model.SNMPConfig;

import javax.swing.*;
import java.awt.event.*;

public class fenetreAdvanced extends JDialog
{
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textField_AgentIpAddress;
    private JTextField textField_Port;
    private JTextField textField_ReadCommunity;
    private JTextField textField_WriteCommunity;
    private JComboBox comboBox_SNMPversion;
    private SNMPConfig config;

    public fenetreAdvanced(SNMPConfig config)
    {
        this.config=config;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("SNMP Advanced Properties");

        comboBox_SNMPversion.removeAllItems();
        comboBox_SNMPversion.addItem("1");
        comboBox_SNMPversion.addItem("2c");
        comboBox_SNMPversion.addItem("3");


        initValueForTextFied();
        textField_AgentIpAddress.setEditable(false);//empeche toute modif direct par l'utilisateur


        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void initValueForTextFied()
    {
        textField_AgentIpAddress.setText(config.getIpAddress());
        textField_Port.setText(String.valueOf(config.getPort()));
        textField_ReadCommunity.setText(config.getReadCommunity());
        textField_WriteCommunity.setText(config.getWriteCommunity());
        comboBox_SNMPversion.setSelectedItem(config.getSnmpVersion());
    }

    private void onOK()
    {
        // add your code here
        if (getReadCommunity().contains(" ") || getWriteCommunity().contains(" "))
        {
            JOptionPane.showMessageDialog(this,
                    "Les communautés ne peuvent pas contenir d'espaces",
                    "Erreur de validation",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (getPort() < 1 || getPort() > 65535)
        {
            JOptionPane.showMessageDialog(this,
                    "Le port doit être compris entre 1 et 65535",
                    "Erreur de validation",
                    JOptionPane.ERROR_MESSAGE);
            return ;
        }
        saveConfig(config);
        dispose();
    }

    private void onCancel()
    {
        // add your code here if necessary
        dispose();
    }

    public String getAgentIpAddress() {
        return textField_AgentIpAddress.getText();
    }

    public int getPort() {
        try
        {
            return Integer.parseInt(textField_Port.getText());
        }
        catch(NumberFormatException e)
        {
            return 161; // Port par défaut
        }
    }

    public String getReadCommunity()
    {
        return textField_ReadCommunity.getText();
    }

    public String getWriteCommunity() {
        return textField_WriteCommunity.getText();
    }

    public String getSnmpVersion() {
        return (String) comboBox_SNMPversion.getSelectedItem();
    }

    private void saveConfig(SNMPConfig config)
    {
        config.setPort(getPort());
        config.setReadCommunity(getReadCommunity());
        config.setWriteCommunity(getWriteCommunity());
        config.setSnmpVersion(getSnmpVersion());
    }

    // Méthode pour charger une configuration
    public SNMPConfig getConfig()
    {
        return config;
    }


    public static void main(String[] args) 
    {
        SNMPConfig config = new SNMPConfig();
        System.out.println("Avant appuis sur OK");
        System.out.println("IP address: "+config.getIpAddress()+ " Port: "+ config.getPort()+ " Read Community: "+ config.getReadCommunity()+" Write Community: "+ config.getWriteCommunity() + " SNMP version: "+config.getSnmpVersion());

        fenetreAdvanced dialog = new fenetreAdvanced(config);
        dialog.pack();
        dialog.setVisible(true);

        System.out.println("\nApres appuis sur OK");
        config= dialog.getConfig();
        System.out.println("IP address: "+config.getIpAddress()+ " Port: "+ config.getPort()+ " Read Community: "+ config.getReadCommunity()+" Write Community: "+ config.getWriteCommunity() + " SNMP version: "+config.getSnmpVersion());
        System.exit(0);
    }
}
