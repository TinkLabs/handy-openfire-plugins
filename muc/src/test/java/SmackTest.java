import com.hi.handy.muc.util.SmackUtil;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author huangxiutao
 * @mail xiutao.huang@tinklabs.com
 * @create 2019-05-31 15:43
 * @Description
 */
public class SmackTest {
    public static void main(String[] args) throws IOException, InterruptedException, XMPPException, SmackException {
        /*String ip = "10.0.3.15";
        int port = 5222;
        XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                .setXmppDomain(ip)
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .setHostAddress(InetAddress.getByName(ip))
                .setPort(port)
                .build();
        XMPPTCPConnection connection = new XMPPTCPConnection(config);
        connection.connect();

        connection.login("","");*/

        try {
            SmackUtil.sendChatGroupMessage("spark-room","IDEA send");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
