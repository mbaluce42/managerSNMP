package Model;

import View.fenetrePrincipale;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;

import java.util.Vector;

public class SnmpListener implements ResponseListener
{
    private Snmp snmpManager;

    public SnmpListener(Snmp s) {snmpManager=s;}

    public void onResponse(ResponseEvent event)
    {
        if(event != null)
        {
            ((Snmp)event.getSource()).cancel(event.getRequest(), this);//quand on a la rep, on la suprimme la requete

            PDU pduReponse = event.getResponse();
            System.out.println("Status réponse = " + pduReponse.getErrorStatus());
            System.out.println("Status réponse = " + pduReponse.getErrorStatusText());

            Vector vecReponse= (Vector) pduReponse.getVariableBindings();

            for(int i = 0; i < vecReponse.size(); i++)
            {
                String reponse;
                System.out.println("Reponse recue dans le SNMP listener");

                VariableBinding vb = (org.snmp4j.smi.VariableBinding) vecReponse.get(i);
                Variable valu = vb.getVariable();
                //Name/OID
                reponse=vb.getOid().toString() + "; ";
                //Value
                reponse=reponse + valu.toString() + "; ";
                //type
                reponse=reponse + valu.getSyntaxString() + "; ";
                //IP
                reponse=reponse + "targetIp" +"; ";
                //Port
                reponse= reponse + "targetPort";


            }
        }

    }

}
