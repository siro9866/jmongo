package com.sil.jmongo.global.util;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * IP 대역을 IP 리스트로 치환
 */
public class UtilCid {

    public static List<String> getIPList(String cidr) {
        if (!cidr.contains("/")) {
            // 단일 IP라면 그대로 반환
            return Collections.singletonList(cidr);
        }

        String ipPart = cidr.split("/")[0]; // 주소만 추출 (예: 192.168.0.0)

        try {
            InetAddress address = InetAddress.getByName(ipPart);
//            System.out.println("address.getHostAddress() = " + address.getHostAddress());
//            System.out.println("address.getAddress().length = " + address.getAddress().length);
            if (address.getAddress().length == 4) {
                return getIP4s(cidr);
            } else {
                // address.getAddress().length == 16
                return getIP6s(cidr);
            }
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * ip4 대역을 입력받아 리스트
     * @param cidr
     * @return
     * @throws UnknownHostException
     */
    public static List<String> getIP4s(String cidr) throws UnknownHostException {
        String[] parts = cidr.split("/");
        String ip = parts[0];
        int prefix = Integer.parseInt(parts[1]);

        long ipLong = ipToLong(InetAddress.getByName(ip));
        int hostBits = 32 - prefix;
        long numHosts = 1L << hostBits;

        long startIP = ipLong & (~0L << hostBits);

        List<String> result = new ArrayList<>();
        for (long i = 0; i < numHosts; i++) {
            result.add(longToIp(startIP + i));
        }

        return result;
    }

    private static long ipToLong(InetAddress ip) {
        byte[] octets = ip.getAddress();
        long result = 0;
        for (byte octet : octets) {
            result <<= 8;
            result |= octet & 0xff;
        }
        return result;
    }

    private static String longToIp(long ip) {
        return String.format("%d.%d.%d.%d",
                (ip >> 24) & 0xff,
                (ip >> 16) & 0xff,
                (ip >> 8) & 0xff,
                ip & 0xff);
    }


    /**
     * ip6 대역을 입력받아 리스트
     * @param cidr
     * @return
     * @throws UnknownHostException
     */
    public static List<String> getIP6s(String cidr) throws UnknownHostException {
        String[] parts = cidr.split("/");
        String baseIP = parts[0];
        int prefixLength = Integer.parseInt(parts[1]);

        InetAddress inetAddress = InetAddress.getByName(baseIP);
        BigInteger base = new BigInteger(1, inetAddress.getAddress());

        int hostBits = 128 - prefixLength;
        BigInteger count = BigInteger.ONE.shiftLeft(hostBits);

        List<String> result = new ArrayList<>();
        for (BigInteger i = BigInteger.ZERO; i.compareTo(count) < 0; i = i.add(BigInteger.ONE)) {
            BigInteger current = base.add(i);
            byte[] addr = toBytes(current, 16);
            InetAddress ip = InetAddress.getByAddress(addr);
            result.add(ip.getHostAddress());
        }

        return result;
    }

    private static byte[] toBytes(BigInteger value, int size) {
        byte[] raw = value.toByteArray();
        byte[] result = new byte[size];
        int start = raw.length > size ? raw.length - size : 0;
        int length = Math.min(raw.length, size);
        System.arraycopy(raw, start, result, size - length, length);
        return result;
    }


    // 테스트
    public static void main(String[] args) throws Exception {
//        List<String> ips = getIPList("185.1.0.0/24");
        List<String> ips = getIPList("fe80::5537:9e5c:74bb:551a%9");
        for (String ip : ips) {
            System.out.println(ip);
        }
    }
}
