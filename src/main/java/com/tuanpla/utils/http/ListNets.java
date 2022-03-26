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

//    public static void main(String args[]) throws SocketException {
//        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
//        for (NetworkInterface netint : Collections.list(nets)) {
//            displayInterfaceInformation(netint);
//        }
//    }
    static void displayInterfaceInformation(NetworkInterface netint) throws SocketException {
        out.printf("Display name: %s\n", netint.getDisplayName());
        out.printf("Name: %s\n", netint.getName());
        Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
        for (InetAddress inetAddress : Collections.list(inetAddresses)) {
            out.printf("InetAddress: %s\n", inetAddress);
        }
        out.printf("\n");
    }

    public static void main(String args[]) {
        // explain what program does and how to use it 
        if (args.length != 1) {
            System.err.println("Print out a sorted list of mail exchange servers ");
            System.err.println("    for a network domain name");
            System.err.println("USAGE: java MailHostsLookup domainName");
            System.exit(-1);
        }
        try {   // print the sorted mail exhchange servers
            args[0] = "htcjsc.vn";
            for (String mailHost : lookupMailHosts(args[0])) {
                LogUtils.debug(mailHost);
            }
        } catch (NamingException e) {
            System.err.println("ERROR: No DNS record for '" + args[0] + "'");
            System.exit(-2);
        }
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
        Arrays.sort(pvhn, new Comparator<String[]>() {
            public int compare(String[] o1, String[] o2) {
                return (Integer.parseInt(o1[0]) - Integer.parseInt(o2[0]));
            }
        });

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
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getIpAddress() {
        try {
            InetAddress thisIp = InetAddress.getLocalHost();
            return thisIp.getHostAddress().toString();
        } catch (Exception e) {
            return "Unknow";
        }
    }

    public static String getHostName() {
        try {
            InetAddress thisIp = InetAddress.getLocalHost();
            return thisIp.getHostName();
        } catch (Exception e) {
            return "Unknow";
        }
    }

    public static String getExternalIp() {
        try {
            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(
                        whatismyip.openStream()));
                String ip = in.readLine();
                return ip;
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            return getIpAddress();
        }

    }
//    public String getMXRecordsForEmailAddress(String eMailAddress) {
//        String returnValue = new String();
//
//        try {
//            String parts[] = eMailAddress.split("@");
//            String hostName = parts[1];
//
//            Record[] records = new Lookup(hostName, Type.MX).run();
//            if (records == null) {
//                throw new RuntimeException("No MX records found for domain " + hostName + ".");
//            }
//
//            if (records.length > 0) {
//                MXRecord mxr = (MXRecord) records[0];
//                for (int i = 0; i < records.length; i++) {
//                    MXRecord tocompare = (MXRecord) records[i];
//                    if (mxr.getPriority() > tocompare.getPriority()) {
//                        mxr = tocompare;
//                    }
//                }
//                returnValue = mxr.getTarget().toString();
//            }
//
//        } catch (TextParseException e) {
//            return new String("NULL");
//
//        }
//        return returnValue;
//    }
}
