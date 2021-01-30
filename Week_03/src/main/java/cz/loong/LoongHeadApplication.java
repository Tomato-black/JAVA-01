package cz.loong;

import cz.loong.inbound.HttpInboundServer;

/**
 * @author : zhang.cao
 * @date : 2021/1/30 11:05
 */
public class LoongHeadApplication {

    public static void main(String[] args) {
        String proxyServer = "re.100.com";
        int port = 8899;
        HttpInboundServer httpInboundServer = new HttpInboundServer(proxyServer, port);
        try {
            httpInboundServer.run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
