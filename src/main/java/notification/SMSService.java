package notification;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

public class SMSService {
    public static final String ACCOUNT_SID = "your_account_sid"; // TODO: set your Twilio SID
    public static final String AUTH_TOKEN = "your_auth_token"; // TODO: set your Twilio Auth Token
    public static final String FROM_NUMBER = "+1234567890"; // TODO: set your Twilio phone number

    public static void sendSMS(String to, String body) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message.creator(new com.twilio.type.PhoneNumber(to),
                        new com.twilio.type.PhoneNumber(FROM_NUMBER),
                        body).create();
    }
} 