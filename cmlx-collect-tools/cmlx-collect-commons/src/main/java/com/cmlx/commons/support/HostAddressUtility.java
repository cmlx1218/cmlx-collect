package com.cmlx.commons.support;

import lombok.experimental.UtilityClass;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 18:07
 * @Desc ->
 **/
@UtilityClass
public class HostAddressUtility {


    public Set<String> multiGetLocalAddressString(String... ignoreHost) throws SocketException {
        Set<String> strings = new HashSet<>();
        Set<InetAddress> inetAddresses = multiGetLocalAddress(ignoreHost);
        if (CollectionUtils.isEmpty(inetAddresses)) return strings;
        inetAddresses.forEach(inetAddress -> {
            strings.add(inetAddress.getHostAddress());
        });
        return strings;
    }

    public Set<InetAddress> multiGetLocalAddress(String... ignoreHost) throws SocketException {
        List<String> ignores = new ArrayList<>();
        if (null != ignoreHost)
            ignores.addAll(Arrays.asList(ignoreHost));
        return multiGetLocalAddress(ignores);
    }

    public Set<InetAddress> multiGetLocalAddress(List<String> ignore) throws SocketException {
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        Set<InetAddress> ips = new HashSet<>();
        Set<String> ignoreIps = new HashSet<>();
        ignoreIps.add("127.0.0.1");
        ignoreIps.add("255.255.255.255");
        if (null != ignore) ignoreIps.addAll(ignore);
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = networkInterfaces.nextElement();
            Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                InetAddress inetAddress = inetAddresses.nextElement();
                String hostAddress = inetAddress.getHostAddress();
                if (matchIpv4(hostAddress) && !ignoreIps.contains(hostAddress)) {
                    ips.add(inetAddress);
                }
            }
        }
        return ips;
    }


    public String getLocalAddress() throws UnknownHostException {
        InetAddress localHost = InetAddress.getLocalHost();
        return localHost.getHostAddress();
    }

    /**
     * 把host地址转换成long
     *
     * @param host 192.168.199.1或192.168.199.1:5555
     * @param port
     * @return
     */
    public long hostToLong(String host, int port) {
        try {
            String[] a = host.split(":");
            if (a.length >= 2) {
                port = Integer.parseInt(a[1].trim());
            }
            if (port == -1) {
                return 0;
            }
            InetSocketAddress addr = new InetSocketAddress(a[0], port);
            return hostTolong(addr);
        } catch (Exception e) {
        }
        return 0;
    }

    public long hostTolong(InetSocketAddress address) {
        if ((address == null) || (address.getAddress() == null)) {
            return 0;
        }
        byte[] ip = address.getAddress().getAddress();
        long addr = (address.getPort() & 0xffff);

        long ipa = 0;
        ipa |= ((ip[3] << 24) & 0xff000000);
        ipa |= ((ip[2] << 16) & 0xff0000);
        ipa |= ((ip[1] << 8) & 0xff00);
        ipa |= (ip[0] & 0xff);
        if (ipa < 0)
            addr += 1;
        addr <<= 32;
        return addr + ipa;
    }

    /**
     * 把long转换成host
     *
     * @param id
     * @return 192.168.199.1:5555
     */
    public String longToHost(long id) {
        StringBuffer host = new StringBuffer(25);

        host.append((id & 0xff)).append('.');
        host.append(((id >> 8) & 0xff)).append('.');
        host.append(((id >> 16) & 0xff)).append('.');
        host.append(((id >> 24) & 0xff));

        int port = (int) ((id >> 32) & 0xffff);
        host.append(":" + port);
        return host.toString();
    }

    /**
     * 把mac地址转换成long
     *
     * @param mac ff-ff-ff-ff-ff-ff 或 ffffffffffff
     * @return
     */
    public long macToLong(String mac) {
        if (StringUtils.isEmpty(mac))
            return 0;
        String[] split = mac.split("-");
        if (split.length != 6 && mac.length() != 12) {
            return 0;
        }
        byte[] macByte = new byte[6];
        if (split.length == 6) {
            for (int i = 0; i < 6; i++) {
                int parseInt = Integer.parseInt(split[i], 16);
                macByte[i] = (byte) parseInt;
            }
        } else {
            for (int i = 0; i < 6; i++) {
                String substring = mac.substring(i * 2, i * 2 + 2);
                int parseInt = Integer.parseInt(substring, 16);
                macByte[i] = (byte) parseInt;
            }
        }
        return macToLong(macByte);
    }

    public long macToLong(byte[] mac) {
        long ipa = 0;
        ipa |= (Long.rotateLeft(mac[5], 40) & 0xff0000000000L);
        ipa |= (Long.rotateLeft(mac[4], 32) & 0xff00000000L);
        ipa |= (Long.rotateLeft(mac[3], 24) & 0xff000000L);
        ipa |= (Long.rotateLeft(mac[2], 16) & 0xff0000L);
        ipa |= (Long.rotateLeft(mac[1], 8) & 0xff00L);
        ipa |= (mac[0] & 0xff);
        return ipa;
    }

    public long macToLong(InetAddress address) {
        try {
            NetworkInterface netInterface = NetworkInterface.getByInetAddress(address);
            byte[] macAddr = netInterface.getHardwareAddress();
            return macToLong(macAddr);
        } catch (SocketException e) {
            return 0;
        }
    }

    /**
     * 把long转换成mac地址
     *
     * @param id
     * @return ff-ff-ff-ff-ff-ff
     */
    public String longToMac(long id) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < 6; i++) {
            long n = (id >> i * 8) & 0xff;
            String hexString = Long.toHexString(n);
            if (hexString.length() == 1)
                hexString += '0';
            buffer.append(hexString);
            if (i != 5)
                buffer.append('-');
        }
        return buffer.toString();
    }

    public long localMacToLong() {
        try {
            return macToLong(InetAddress.getLocalHost());
        } catch (UnknownHostException e) {
            return 0;
        }
    }

    public long localHostToLong() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            InetSocketAddress inetSocketAddress = new InetSocketAddress(localHost, 0);
            return hostTolong(inetSocketAddress);
        } catch (UnknownHostException e) {
            return 0;
        }
    }

    public long localHostNotPortToLong() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            byte[] ip = localHost.getAddress();
            long ipa = 0;
            ipa |= ((ip[3] << 24) & 0xff000000);
            ipa |= ((ip[2] << 16) & 0xff0000);
            ipa |= ((ip[1] << 8) & 0xff00);
            ipa |= (ip[0] & 0xff);
            if (ipa < 0) {
                ipa = ipa ^ (-1L << 32);
            }
            return ipa;
        } catch (UnknownHostException e) {
            return 0;
        }
    }

    public long localHostAfterTwo() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            byte[] ip = localHost.getAddress();
            long ipa = 0;
            ipa |= ((ip[3] << 8) & 0xff00);
            ipa |= (ip[2] & 0xff);
            if (ipa < 0) {
                ipa = ipa ^ (-1L << 32);
            }
            return ipa;
        } catch (UnknownHostException e) {
            return 0;
        }
    }

    public long localHostAfterTwo(InetAddress inetAddress) {
        byte[] ip = inetAddress.getAddress();
        long ipa = 0;
        ipa |= ((ip[3] << 8) & 0xff00);
        ipa |= (ip[2] & 0xff);
        if (ipa < 0) {
            ipa = ipa ^ (-1L << 32);
        }
        return ipa;
    }

    public boolean matchIpv4(String ip) {
        if (!StringUtility.hasText(ip)) return false;
        Pattern p = Pattern.compile("((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))");
        return p.matcher(ip).matches();
    }

    /**
     * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址;
     *
     * @param request
     * @return
     * @throws IOException
     */
    public String getIpAddress(HttpServletRequest request){
        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址

        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (int index = 0; index < ips.length; index++) {
                String strIp = (String) ips[index];
                if (!("unknown".equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        return ip;
    }
}

