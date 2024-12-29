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
        // On use la config de la feneter principale au lieu d'en créer une nouvelle
        this.config = viewPrincipale.getConfig();

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

        // MAJ la configuration
        config= viewPrincipale.getConfig();

        // Nettoyer la table avant d'ajouter de nouveaux résultats
        //viewPrincipale.clearTable();

        boolean isAsync =SNMPProperties.isAsynchronous();

        try
        {
            String operation = viewPrincipale.getSelectedOperation();
            String reponse = "";

            if (operation== "GET" || operation=="GET_NEXT")
            {
                if (config.getWriteCommunity().isEmpty())
                {
                    viewPrincipale.showErrorMessage("La communauté de lecture n'est pas configurée. " +
                            "Utilisez le bouton Advanced pour la configurer.");
                    return;
                }
            }

            switch (operation)
            {
                case "GET":
                    if(isAsync==true)
                    {
                        reponse=SNMP4JGet.GET_Asynchrone(config);
                    }
                    else
                    {
                        reponse=SNMP4JGet.GET_Synchrone(config);
                    }
                    handleResponse(reponse);
                    break;

                case "GET_NEXT":
                    if(isAsync==true)
                    {
                        reponse=SNMP4JGetNext.GETNEXT_Asynchrone(config);
                    }
                    else
                    {
                        reponse=SNMP4JGetNext.GETNEXT_Synchrone(config);
                    }
                    handleResponse(reponse);
                    break;

                case "SET":
                    handleSetOperation();
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
        //MAJ la configuration avant d'ouvrir la fenetre advanced
        config= viewPrincipale.getConfig();

        viewAdvanced = new fenetreAdvanced(config);
        viewAdvanced.pack();
        viewAdvanced.setLocationRelativeTo(viewPrincipale);
        viewAdvanced.setVisible(true);

    }

    private void handleSetOperation()
    {
        if (config.getWriteCommunity().isEmpty())
        {
            viewPrincipale.showErrorMessage("La communauté d'écriture n'est pas configurée. " +
                    "Utilisez le bouton Advanced pour la configurer.");
            return;
        }

        // Afficher la fenêtre SET
        viewSet = new fenetreSet(config);
        viewSet.pack();
        viewSet.setLocationRelativeTo(viewPrincipale);
        viewSet.setVisible(true);

        // Traiter la réponse seulement si une valeur a été définie (OK cliqué)
        if (!config.getValue().isEmpty())
        {
            String response = SNMPProperties.isAsynchronous() ?
                    SNMP4JSet.SET_Asynchrone(config) :
                    SNMP4JSet.SET_Synchrone(config);

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


}