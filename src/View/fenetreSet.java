package View;

import Model.SNMPConfig;

import javax.swing.*;
import java.awt.event.*;

public class fenetreSet extends JDialog
{
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textField_ObjectID;
    private JTextField textField_Value;
    private JComboBox comboBox_DataType;
    private SNMPConfig config;

    public fenetreSet(SNMPConfig config)
    {
        this.config = config;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("SNMP SET");


        comboBox_DataType.removeAllItems();
        comboBox_DataType.addItem("OctectString");
        comboBox_DataType.addItem("Integer");


        textField_ObjectID.setText(config.getOid());
        comboBox_DataType.setSelectedItem("OctectString");

        textField_ObjectID.setEditable(false);//empeche toute modif direct par l'utilisateur


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

    private void onOK()
    {
        // add your code here
        saveConfig(config);
        dispose();
    }

    private void onCancel()
    {
        // add your code here if necessary
        dispose();
    }

    public void setValue(String value)
    {
        textField_Value.setText(value);
    }

    public String getValue()
    {
        return textField_Value.getText();
    }


    private void saveConfig(SNMPConfig config)
    {
        config.setValue(getValue());
    }

    public SNMPConfig getConfig()
    {
        return config;
    }

    public static void main(String[] args)
    {
        SNMPConfig config = new SNMPConfig();
        config.setOid("1.1.0.1.3.0");
        System.out.println("Avant appuis sur OK");
        System.out.println("ObjectID: "+config.getOid()+ " Value: "+ config.getValue());


        fenetreSet dialog = new fenetreSet(config);
        dialog.pack();
        dialog.setVisible(true);

        config=dialog.getConfig();

        System.out.println("Apres appuis sur OK");
        System.out.println("ObjectID: "+config.getOid()+ " Value: "+ config.getValue());


        System.exit(0);
    }
}
