package com.fujitsu.client;

import com.fujitsu.base.constants.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.ContainerProvider;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.net.URI;

/**
 * Created by Barrie on 15/12/3.
 */
public class GasQPReadingURLConnect {
    private static String uri = Const.WebSocket.URL + "/ccst_WC_GetGasQPReadingURL";

    private static Logger logger = LoggerFactory.getLogger(GasQPReadingURLConnect.class);

    public static Session session;


    static {
        getSession();
    }

    public static void getSession() {
        logger.info("start to connect " + uri);
        WebSocketContainer container = null;
        try {
            container = ContainerProvider.getWebSocketContainer();
        } catch (Exception ex) {
            logger.info("error" + ex);
        }

        try {
            URI r = URI.create(uri);
            session = container.connectToServer(GasQPReadingURLClient.class, r);
        } catch (Exception e) {
            logger.error("connectToServer:" + uri, e);
        }
        logger.info("end to connect " + uri);
    }

    public synchronized static void sendMsg(String msg) {
        logger.info("msg = " + msg);
        try {
            session.getBasicRemote().sendText(msg);
            Thread.sleep(Const.WebSocket.WEB_SOCKET_SLEEP);
            System.in.read();
        } catch (Exception e) {
            logger.error("sendMsg(String msg) error " + uri, e);
            getSession();
            sendMsgTwoTime(msg);
        }
        logger.info("end sendMsg(String msg)");
    }

    public synchronized static void sendMsgTwoTime(String msg) {
        logger.info("msg = " + msg);
        try {
            session.getBasicRemote().sendText(msg);
            Thread.sleep(Const.WebSocket.WEB_SOCKET_SLEEP);
            System.in.read();
        } catch (Exception e) {
            logger.error("sendMsg(String msg) error " + uri, e);
        }
        logger.info("end sendMsg(String msg)");
    }
}
