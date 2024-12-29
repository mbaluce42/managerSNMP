package Model;

import View.fenetrePrincipale;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;

import java.util.List;
import java.util.Vector;

public class SnmpListener implements ResponseListener
{
    private Snmp snmpManager;
    private String responseData;     // Pour GET/GETNEXT ->asynchrone
    private boolean operationSuccess; // Pour GET/GETNEXT/Set ->asynchrone
    private String errorMessage; // Pour GET/GETNEXT/Set ->asynchrone
    private String operationType;    // Pour différencier le type d'opération

    public SnmpListener(Snmp s, String operation)
    {
        snmpManager = s;
        responseData = "";
        operationSuccess = false;
        errorMessage = "";
        operationType = operation;
    }

    public void onResponse(ResponseEvent event)
    {
        if(event != null)
        {
            ((Snmp)event.getSource()).cancel(event.getRequest(), this);//quand on a la rep, on la suprimme la requete

            PDU pduReponse = event.getResponse();

            System.out.println("Status réponse = " + pduReponse.getErrorStatus());
            System.out.println("Status réponse = " + pduReponse.getErrorStatusText());

            operationSuccess = (pduReponse.getErrorStatus() == PDU.noError);

            if (!operationSuccess)
            {
                errorMessage = pduReponse.getErrorStatusText();
            }
            if(!operationType.equals("SET")) // Pour GET et GETNEXT
            {
                //Vector vecReponse = (Vector) pduReponse.getVariableBindings();
                List<? extends VariableBinding> vecReponse = pduReponse.getVariableBindings();

                String reponse;
                for (int i = 0; i < vecReponse.size(); i++) {

                    System.out.println("Reponse recue dans le SNMP listener");

                    VariableBinding vb = (org.snmp4j.smi.VariableBinding) vecReponse.get(i);
                    Variable valu = vb.getVariable();
                    //Name/OID
                    reponse = vb.getOid().toString() + "; ";
                    //Value
                    reponse = reponse + valu.toString() + "; ";
                    //type
                    reponse = reponse + valu.getSyntaxString() + "; ";
                    //IP
                    reponse = reponse + event.getUserObject() + "; ";
                    //Port
                    reponse = reponse + "targetPort\n";
                }
            }
            synchronized(snmpManager)
            {
                snmpManager.notify();
            }
        }

    }

    // Méthodes pour récupérer les résultats
    public String getResponse()
    {
        return responseData;
    }

    public boolean isSuccessful()
    {
        return operationSuccess;
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }

}
