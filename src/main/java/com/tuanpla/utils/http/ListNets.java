package com.tuanpla.utils.http;

import com.tuanpla.utils.logging.LogUtils;
import java.io.*;
import static java.lang.System.out;
import java.net.*;
import java.util.*;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author tuanp
 */
public class ListNets {

    private static Logger logger = LoggerFactory.getLogger(ListNets.class);

    static void displayInterfaceInformation(NetworkInterface netint) throws SocketException {
        out.printf("Display name: %s\n", netint.getDisplayName());
        out.printf("Name: %s\n", netint.getName());
        Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
        for (InetAddress inetAddress : Collections.list(inetAddresses)) {
            out.printf("InetAddress: %s\n", inetAddress);
        }
        out.printf("\n");
    }

    // returns a String array of mail exchange servers (mail hosts) 
    //     sorted from most preferred to least preferred
    static String[] lookupMailHosts(String domainName) throws NamingException {
        // see: RFC 974 - Mail routing and the domain system
        // see: RFC 1034 - Domain names - concepts and facilities
        // see: http://java.sun.com/j2se/1.5.0/docs/guide/jndi/jndi-dns.html
        //    - DNS Service Provider for the Java Naming Directory Interface (JNDI)

        // get the default initial Directory Context
        InitialDirContext iDirC = new InitialDirContext();
        // get the MX records from the default DNS directory service provider
        //    NamingException thrown if no DNS record found for domainName
        Attributes attributes = iDirC.getAttributes("dns:/" + domainName, new String[]{"MX"});
        // attributeMX is an attribute ('list') of the Mail Exchange(MX) Resource Records(RR)
        Attribute attributeMX = attributes.get("MX");

        // if there are no MX RRs then default to domainName (see: RFC 974)
        if (attributeMX == null) {
            return (new String[]{domainName});
        }

        // split MX RRs into Preference Values(pvhn[0]) and Host Names(pvhn[1])
        String[][] pvhn = new String[attributeMX.size()][2];
        for (int i = 0; i < attributeMX.size(); i++) {
            pvhn[i] = ("" + attributeMX.get(i)).split("\\s+");
        }

        // sort the MX RRs by RR value (lower is preferred)
        Arrays.sort(pvhn, (String[] o1, String[] o2) -> (Integer.parseInt(o1[0]) - Integer.parseInt(o2[0])));

        // put sorted host names in an array, get rid of any trailing '.' 
        String[] sortedHostNames = new String[pvhn.length];
        for (int i = 0; i < pvhn.length; i++) {
            sortedHostNames[i] = pvhn[i][1].endsWith(".")
                    ? pvhn[i][1].substring(0, pvhn[i][1].length() - 1) : pvhn[i][1];
        }
        return sortedHostNames;
    }

    public ListNets() {
        URL ipAdress;
        try {
            ipAdress = new URL("http://myexternalip.com/raw");
            BufferedReader in = new BufferedReader(new InputStreamReader(ipAdress.openStream()));
            String ip = in.readLine();
            LogUtils.debug(ip);
        } catch (IOException e) {
            logger.error(LogUtils.getLogMessage(e));
        }
    }

    public static String getIpAddress() {
        try {
            InetAddress thisIp = InetAddress.getLocalHost();
            return thisIp.getHostAddress();
        } catch (UnknownHostException e) {
            logger.error(LogUtils.getLogMessage(e));
            return "Unknow";
        }
    }

    public static String getHostName() {
        try {
            InetAddress thisIp = InetAddress.getLocalHost();
            return thisIp.getHostName();
        } catch (UnknownHostException e) {
            logger.error(LogUtils.getLogMessage(e));
            return "Unknow";
        }
    }

    public static String getExternalIp() {
        try {
            URL whatismyip = new URL("http://checkip.amazonaws.com");
            String ip;
            try (BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()))) {
                ip = in.readLine();
            }
            return ip;
        } catch (IOException e) {
            logger.error(LogUtils.getLogMessage(e));
            return getIpAddress();
        }

    }

}
