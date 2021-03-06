package com.majorproject2k19.MajorProject2k19.controller;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets.Details;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.Thread;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


@Controller
@RestController
public class GoogleMailController {

    private static final String APPLICATION_NAME = "MajorProject2k19";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static HttpTransport httpTransport;
    private static com.google.api.services.gmail.Gmail client;

    GoogleClientSecrets clientSecrets;
    GoogleAuthorizationCodeFlow flow;
    Credential credential;

    @Value("${gmail.client.clientId}")
    private String clientId;

    @Value("${gmail.client.clientSecret}")
    private String clientSecret;

    @Value("${gmail.client.redirectUri}")
    private String redirectUri;

    @Value("${gmail.client.scope}")
    private List<String> scope;

    public static Message createMessageWithEmail(MimeMessage email, String threadId)
            throws MessagingException, IOException {
        System.out.println("In create createMessageWithEmail");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        email.writeTo(baos);

        String encodedEmail = Base64.encodeBase64URLSafeString(baos.toByteArray());
        Message message = new Message();
        message.setThreadId(threadId);
        message.setRaw(encodedEmail);

        return message;
    }

    public static MimeMessage createEmail(String to,
                                          String from,
                                          String subject,
                                          String bodyText)
            throws MessagingException {
        System.out.println("In createEmail");
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);

        email.setFrom(new InternetAddress(from));
        email.addRecipient(javax.mail.Message.RecipientType.TO,
                new InternetAddress(to));
        email.setSubject(subject);
        email.setText(bodyText);
        return email;
    }

    /**
     * Send an email from the user's mailbox to its recipient.
     *
     * @param service      Authorized Gmail API instance.
     * @param userId       User's email address. The special value "me"
     *                     can be used to indicate the authenticated user.
     * @param emailContent Email to be sent.
     * @return The sent message
     * @throws MessagingException
     * @throws IOException
     */
    public static Message sendMessage(Gmail service,
                                      String userId,
                                      MimeMessage emailContent, String threadId)
            throws MessagingException, IOException {
        Message message = createMessageWithEmail(emailContent, threadId);
        System.out.println("In send Message!");
        message = service.users().messages().send(userId, message).execute();

        System.out.println("Message id: " + message.getId());
        System.out.println(message.toPrettyString());
        return message;
    }

    @RequestMapping(value = "/login/gmail", method = RequestMethod.GET)
    public RedirectView googleConnectionStatus(HttpServletRequest request) throws Exception {
        return new RedirectView(authorize());
    }

    @RequestMapping(value = "/login/gmailCallback", method = RequestMethod.GET, params = "code")
    public ResponseEntity<String> oauth2Callback(@RequestParam(value = "code") String code) {


        JSONObject json = new JSONObject();
        JSONArray arr = new JSONArray();

        // String message;
        try {
            TokenResponse response = flow.newTokenRequest(code).setRedirectUri(redirectUri).execute();
            credential = flow.createAndStoreCredential(response, "userID");

            client = new com.google.api.services.gmail.Gmail.Builder(httpTransport, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME).build();
            checkForNewMails();
        } catch (Exception e) {

            System.out.println("exception cached ");
            e.printStackTrace();
        }
        return new ResponseEntity<>(json.toString(), HttpStatus.OK);
    }

    @Scheduled(initialDelay = 30*1000,fixedRate = 60*1000)
    public void checkForNewMails() {
        System.out.println("Thread Name : " + Thread.currentThread().getName());
        /*
         * Filter filter = new Filter().setCriteria(new
         * FilterCriteria().setFrom("a2cart.com@gmail.com"))
         * .setAction(new FilterAction()); Filter result =
         * client.users().settings().filters().create("me",
         * filter).execute();
         *
         * System.out.println("Created filter " + result.getId());
         */

        String userId = "me";
        String query = "subject:'SIH19'";

//        System.out.println("code->" + code + " userId->" + userId + " query->" + query);

        try
        {
//            ListMessagesResponse MsgResponse = client.users().messages().list(userId).setQ(query).execute();
            ListMessagesResponse MsgResponse = client.users().messages().list(userId).execute();
            List<Label> labels = client.users().labels().list(userId).execute().getLabels();
            System.out.println("Labels : ");

//            for (Label x : labels)
//            {
//                if (x.getId().equals("INBOX"))
//                    System.out.println(x.toPrettyString());
//                System.out.println(x.getMessagesUnread() + " " + x.getName());
//            }

            System.out.println("message length:" + MsgResponse.getMessages().size());

            String to = null, from = null, replyMsg = "Here is the reply", subject = null;

            for (Message msg : MsgResponse.getMessages())
            {


                Message message = client.users().messages().get(userId, msg.getId()).execute();
//                System.out.println(message);

                String threadId=message.getThreadId();//thread id to send a reply to the same thread

                //Getting the basic properties from the unread mail
                if (message.getLabelIds().contains("UNREAD"))
                {
                    System.out.println(msg.getId());
                    System.out.println(msg.toPrettyString());
                    System.out.println("snippet : " + message.getSnippet());
                    List<MessagePartHeader> msgHeaders = message.getPayload().getHeaders();
                    for (MessagePartHeader y : msgHeaders) {
                        if (y.getName().equals("From")) {
                            to = y.getValue();
                            System.out.println("From : " + to);
                        } else if (y.getName().equals("Subject")) {
                            subject = "Re: " + y.getValue();
                        } else if (y.getName().equals("Delivered-To")) {
                            from = y.getValue();
                        }
                    }

                    //Making the message as read : removing the unread label
                    List<String> labelsToRemove=new ArrayList<>();
                    labelsToRemove.add("UNREAD");
                    ModifyThreadRequest mods = new ModifyThreadRequest()
                            .setRemoveLabelIds(labelsToRemove);
                    com.google.api.services.gmail.model.Thread thread = client.users().threads().modify(userId, threadId, mods).execute();

                    MimeMessage message1 = createEmail(to, userId, subject, replyMsg);
                    sendMessage(client, userId, message1, threadId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error in the checkNewMail  method!!!!");
        }
    }


    private String authorize() throws Exception {
        AuthorizationCodeRequestUrl authorizationUrl;
        if (flow == null) {
            Details web = new Details();
            web.setClientId(clientId);
            web.setClientSecret(clientSecret);
            clientSecrets = new GoogleClientSecrets().setWeb(web);
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
//            flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets,
//                    Collections.list(GmailScopes.GMAIL_MODIFY,GmailScopes.GMAIL_SEND)).build();
            flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets, scope).build();
        }
        authorizationUrl = flow.newAuthorizationUrl().setRedirectUri(redirectUri);

        System.out.println("gamil authorizationUrl ->" + authorizationUrl);
        return authorizationUrl.build();
    }
}