package Model;

import org.snmp4j.*;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;
import java.io.ObjectInputFilter;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SNMP4JGet
{
    public String GET_Synchrone(SNMPConfig config)
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
            //ex.printStackTrace();
            Logger.getLogger(SNMP4JGet.class.getName()).log(Level.SEVERE, null, ex);
        }

        Snmp snmp = new Snmp(transport);

        //config de la cible SNMP
        CommunityTarget target = new CommunityTarget();
        target.setVersion(config.getSnmpVersionNumber());
        target.setCommunity(new OctetString(config.getReadCommunity()));
        Address targetAddress = new UdpAddress(config.getIpAddress() + "/" + config.getPort());
        target.setAddress(targetAddress);
        target.setRetries(2);
        target.setTimeout(1500);

        //config requête PDU
        PDU pdu = new PDU();
        pdu.setType(PDU.GET);
        pdu.add(new org.snmp4j.smi.VariableBinding(new OID(config.getOid())));


        ResponseEvent paquetReponse = null;
        try
        {
            paquetReponse = snmp.get(pdu, target);
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
                Vector<String> rowData = new Vector<>();
                VariableBinding vb = (org.snmp4j.smi.VariableBinding) vecReponse.get(i);
                Variable value = vb.getVariable();
                //Name/OID
                reponse=vb.getOid().toString() + "; ";
                //Value
                reponse=reponse + value.toString() + "; ";
                //type
                reponse=reponse + value.getSyntaxString() + "; ";
                //IP
                reponse=reponse + config.getIpAddress() +"; ";
                //Port
                reponse= reponse + Integer.toString(config.getPort());
            }
        }
        return reponse; //utiliser split pour recuperer les valeurs
    }


    public static void main(String[] args)
    {
        System.out.println("Hello world!");
    }
}
