package Model;

import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.IpAddress;

public class SNMPConfig
{
    private String ipAddress;
    private int port;
    private String readCommunity;
    private String writeCommunity;
    private String snmpVersion;

    private String oid;
    private String value;


    // Constructeur
    public SNMPConfig()
    {
        // Valeurs par défaut
        ipAddress = "127.0.0.1";
        port = 161;
        readCommunity = "";
        writeCommunity = "";
        snmpVersion = "2";

        oid="";
        value="";
    }

    public void setIpAddress(String ipAddress) {this.ipAddress = ipAddress;}
    public String getIpAddress() {return ipAddress;}

    public void setPort(int port) {this.port = port;}
    public int getPort() {return port;}

    public void setReadCommunity(String readCommunity) {this.readCommunity = readCommunity;}
    public String getReadCommunity() {return readCommunity;}

    public void setWriteCommunity(String writeCommunity) {this.writeCommunity = writeCommunity;}
    public String getWriteCommunity() {return writeCommunity;}

    public void setSnmpVersion(String snmpVersion) { this.snmpVersion = snmpVersion; }
    public String getSnmpVersion() { return snmpVersion; }
    public int getSnmpVersionNumber()
    {
        switch (snmpVersion)
        {
            case "1":
                return SnmpConstants.version1;
            case "2c":
                return SnmpConstants.version2c;
            case "3":
                return SnmpConstants.version3;
            default:
                return SnmpConstants.version1;
        }
    }

    public void setOid(String oid)
    {
        if(oid.endsWith(".")==true)
        {
            oid=oid + "0";
        }
        else if(oid.endsWith(".0")== false)//permet de verif si l'oid se termine par .0
        {
            oid = oid + ".0";
        }

        this.oid = oid;
    }
    public String getOid() { return oid; }


    public void setValue(String value) { this.value = value; }
    public String getValue() { return value; }

}
