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

public class SNMP4JGetNext
{
    public String GET_Asynchrone(SNMPConfig config)
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

        SnmpListener listener = new SnmpListener(snmp);
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
            }
            catch (InterruptedException ex)
            {
                Logger.getLogger(SNMP4JGetNext.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return reponse; //utiliser split pour recuperer les valeurs*/
    }

}
