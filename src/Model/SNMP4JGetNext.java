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
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SNMP4JGetNext
{
    public static String GETNEXT_Synchrone(SNMPConfig config)
    {
        String reponse = "";
        TransportMapping transport = null;
        try
        {
            transport = new DefaultUdpTransportMapping();
            transport.listen();
        }
        catch (IOException ex)
        {
            Logger.getLogger(SNMP4JGetNext.class.getName()).log(Level.SEVERE, null, ex);
        }

        Snmp snmp = new Snmp(transport);

        // Config de la cible SNMP
        CommunityTarget target = new CommunityTarget();
        target.setVersion(config.getSnmpVersionNumber());
        target.setCommunity(new OctetString(config.getReadCommunity()));
        Address targetAddress = new UdpAddress(config.getIpAddress() + "/" + config.getPort());
        target.setAddress(targetAddress);
        target.setRetries(2);
        target.setTimeout(1500);

        // Config requête PDU
        PDU pdu = new PDU();
        pdu.setType(PDU.GETNEXT);
        pdu.add(new VariableBinding(new OID(config.getOid())));

        // Envoi synchrone
        ResponseEvent paquetReponse = null;
        try
        {
            paquetReponse = snmp.getNext(pdu, target);
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

            boolean operationSuccess = (pduReponse.getErrorStatus() == PDU.noError);

            if (!operationSuccess)
            {
                reponse="Erreur: "+pduReponse.getErrorStatusText();
            }
            else
            {
                //Vector vecReponse = (Vector) pduReponse.getVariableBindings();
                List<? extends VariableBinding> vecReponse = pduReponse.getVariableBindings();


                for (int i = 0; i < vecReponse.size(); i++)
                {
                    System.out.println("Reponse recue dans le SNMP listener");
                    Vector<String> rowData = new Vector<>();
                    VariableBinding vb = (VariableBinding) vecReponse.get(i);
                    Variable value = vb.getVariable();
                    // Name/OID
                    reponse = vb.getOid().toString() + "; ";
                    // Value
                    reponse = reponse + value.toString() + "; ";
                    // type
                    reponse = reponse + value.getSyntaxString() + "; ";
                    // IP
                    reponse = reponse + config.getIpAddress() + "; ";
                    // Port
                    reponse = reponse + Integer.toString(config.getPort());
                }
            }
        }
        return reponse;
    }


    public static String GETNEXT_Asynchrone(SNMPConfig config)
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
            Logger.getLogger(SNMP4JGetNext.class.getName()).log(Level.SEVERE, null, ex);
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
        pdu.setType(PDU.GETNEXT);
        pdu.add(new org.snmp4j.smi.VariableBinding(new OID(config.getOid())));

        SnmpListener listener = new SnmpListener(snmp,"GETNEXT");
        try
        {
            snmp.send(pdu,target,null,listener);
        }
        catch (IOException ex)
        {
            Logger.getLogger(SNMP4JGetNext.class.getName()).log(Level.SEVERE, null, ex);
        }

        synchronized (snmp)
        {
            try
            {
                snmp.wait();
                if (listener.isSuccessful()==true)
                {
                    reponse= listener.getResponse()+config.getIpAddress() +"; " +config.getPort() +"\n";
                }
                else
                {
                    reponse="Erreur: " + listener.getErrorMessage();
                }
            }
            catch (InterruptedException ex)
            {
                Logger.getLogger(SNMP4JGetNext.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return reponse;
    }

}
