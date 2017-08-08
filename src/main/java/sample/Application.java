package sample;

import com.sforce.soap.partner.LoginResult;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.soap.partner.SaveResult;
import com.sforce.soap.partner.sobject.SObject;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

import java.util.Arrays;

public class Application {

    public static void main(String... args) throws Exception {
        String loginId = "xxx";
        String password = "xxx";

        PartnerConnection conn = login(loginId, password);
        System.out.println("logged in : " + conn.getUserInfo());
        try {
            SObject testEvent = new SObject("TestEvent__e");
            testEvent.setField("Payload__c", "a,b,c,");

            SaveResult[] results = conn.create(new SObject[]{testEvent});
            for (SaveResult result : results) {
                System.out.println(result.getId() + ", " + result.isSuccess() + ", "
                        + Arrays.toString(result.getErrors()));
            }
        } finally {
            conn.logout();
        }
    }

    private static PartnerConnection login(String loginId, String password) throws ConnectionException {
        String endpoint = "https://login.salesforce.com/services/Soap/u/40.0";

        ConnectorConfig config = new ConnectorConfig();
        config.setAuthEndpoint(endpoint);
        config.setServiceEndpoint(endpoint);
        config.setManualLogin(true);

        PartnerConnection conn = new PartnerConnection(config);
        LoginResult loginResult = conn.login(loginId, password);

        ConnectorConfig partnerConfig = new ConnectorConfig();
        partnerConfig.setServiceEndpoint(loginResult.getServerUrl());
        partnerConfig.setSessionId(loginResult.getSessionId());
        return new PartnerConnection(partnerConfig);
    }
}
