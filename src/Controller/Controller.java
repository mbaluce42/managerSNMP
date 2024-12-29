package Controller;

import Model.*;
import View.*;

public class Controller
{
    private fenetrePrincipale viewPrincipale;
    private fenetreAdvanced viewAdvanced;
    private fenetreSet viewSet;
    private SNMPConfig config;

    public Controller(fenetrePrincipale viewPrincipale)
    {
        this.viewPrincipale = viewPrincipale;
        this.config = new SNMPConfig();

        initListeners();
    }

    private void initListeners()
    {
        // Listener pour le bouton Go
        viewPrincipale.addGoButtonListener(e -> handleGoButton());

        // Listener pour le bouton Advanced
        viewPrincipale.addAdvancedButtonListener(e -> showAdvancedDialog());
    }

    private void handleGoButton()
    {
        // Vérifier si l'adresse IP et l'OID sont renseignés
        if (viewPrincipale.getAgentIpAddress().isEmpty() || viewPrincipale.getObjectID().isEmpty())
        {
            viewPrincipale.showErrorMessage("L'adresse IP et l'OID sont requis");
            return;
        }

        // Mettre à jour la configuration
        config.setIpAddress(viewPrincipale.getAgentIpAddress());
        config.setOid(viewPrincipale.getObjectID());

        // Nettoyer la table avant d'ajouter de nouveaux résultats
        //viewPrincipale.clearTable();

        try
        {
            String operation = viewPrincipale.getSelectedOperation();
            String response = "";

            switch (operation)
            {
                case "GET":
                    response = SNMP4JGet.GET_Synchrone(config);
                    handleResponse(response);
                    break;

                case "GET_NEXT":
                    response = SNMP4JGetNext.GET_Synchrone(config);
                    handleResponse(response);
                    break;

                case "SET":
                    showSetDialog();
                    break;
            }
        }
        catch (Exception ex)
        {
            viewPrincipale.showErrorMessage("Erreur lors de l'opération SNMP: " + ex.getMessage());
        }
    }

    private void handleResponse(String response)
    {
        if (response.startsWith("Erreur:"))
        {
            viewPrincipale.showErrorMessage(response);
        }
        else
        {
            String[] parts = response.split("; ");
            if (parts.length >= 4)
            {
                String ipPort = parts[3] + ":" + parts[4];
                viewPrincipale.addResultTable(parts[0], parts[1], parts[2], ipPort);
            }
        }
    }

    private void showAdvancedDialog()
    {
        viewAdvanced = new fenetreAdvanced(config);
        viewAdvanced.pack();
        viewAdvanced.setLocationRelativeTo(viewPrincipale);
        viewAdvanced.setVisible(true);
    }

    private void showSetDialog()
    {
        if (config.getWriteCommunity().isEmpty())
        {
            viewPrincipale.showErrorMessage("La communauté d'écriture n'est pas configurée. " +
                    "Utilisez le bouton Advanced pour la configurer.");
            return;
        }

        viewSet = new fenetreSet(config);
        viewSet.pack();
        viewSet.setLocationRelativeTo(viewPrincipale);
        viewSet.setVisible(true);

        // Si une valeur a été définie (OK cliqué)
        if (!config.getValue().isEmpty())
        {
            String response = SNMP4JSet.SET_Synchrone(config);
            if (response.equals("SET reussi"))
            {
                viewPrincipale.showInfoMessage(response);
            }
            else
            {
                viewPrincipale.showErrorMessage(response);
            }
        }
    }

    // Point d'entrée de l'application
    public static void main(String[] args)
    {
        javax.swing.SwingUtilities.invokeLater(() -> {
            fenetrePrincipale view = new fenetrePrincipale();
            Controller controller = new Controller(view);
        });
    }
}