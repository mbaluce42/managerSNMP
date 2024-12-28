package Model;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SNMP4JSet
{
    public String SET_Synchrone(SNMPConfig config)
    {
        String reponse = "";
        TransportMapping transport= null;
        try
        {
            transport = new DefaultUdpTransportMapping();
            transport.listen();
        }
        catch (IOException ex)
        {
            Logger.getLogger(SNMP4JGet.class.getName()).log(Level.SEVERE, null, ex);
        }

        Snmp snmp = new Snmp(transport);

        //config de la cible SNMP
        CommunityTarget target = new CommunityTarget();
        target.setVersion(config.getSnmpVersionNumber());
        target.setCommunity(new OctetString(config.getWriteCommunity()));
        Address targetAddress = new UdpAddress(config.getIpAddress() + "/" + config.getPort());
        target.setAddress(targetAddress);
        target.setRetries(2);
        target.setTimeout(1500);

        //config requête PDU
        PDU pdu = new PDU();
        pdu.setType(PDU.SET);
        pdu.add(new org.snmp4j.smi.VariableBinding(new OID(config.getOid()),new OctetString(config.getValue())));


        ResponseEvent paquetReponse = null;
        try
        {
            paquetReponse = snmp.set(pdu, target);
            System.out.println("Requete SNMP envoyée à l'agent");
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

        if(paquetReponse != null)
        {
            PDU pduReponse = paquetReponse.getResponse();
            System.out.println("Status réponse = " + pduReponse.getErrorStatus());
            System.out.println("Status réponse = " + pduReponse.getErrorStatusText());

            Vector vecReponse= (Vector) pduReponse.getVariableBindings();

            for(int i = 0; i < vecReponse.size(); i++)
            {
                System.out.println("Reponse recue dans le SNMP listener");

                //Vector<String> rowData = new Vector<>();
                VariableBinding vb = (org.snmp4j.smi.VariableBinding) vecReponse.get(i);
                Variable valu = vb.getVariable();
                //Name/OID
                reponse=vb.getOid().toString() + "; ";
                //Value
                reponse=reponse + valu.toString() + "; ";
                //type
                reponse=reponse + valu.getSyntaxString() + "; ";
                //IP
                reponse=reponse + config.getIpAddress() +"; ";
                //Port
                reponse= reponse + Integer.toString(config.getPort());
            }
        }
        return reponse; //utiliser split pour recuperer les valeurs
    }
}
