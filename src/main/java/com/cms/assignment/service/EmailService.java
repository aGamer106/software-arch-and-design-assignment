package com.cms.assignment.service;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.resource.Emailv31;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${mailjet.api.key}")
    private String apiKey;

    @Value("${mailjet.secret.key}")
    private String apiSecret;

    @Value("${mailjet.sender.email}")
    private String senderEmail;

    public void sendEscalationEmail(String supportPersonEmail, String ticketTitle, String consumerName, String consumerPhone, String description) {
        try {
            MailjetClient client = new MailjetClient(
                    ClientOptions.builder().apiKey(apiKey).apiSecretKey(apiSecret).build()
            );

            MailjetRequest request = new MailjetRequest(Emailv31.resource)
                    .property(Emailv31.MESSAGES, new JSONArray()
                            .put(new JSONObject()
                                    .put(Emailv31.Message.FROM, new JSONObject()
                                            .put("Email", senderEmail)
                                            .put("Name", "CMS System"))
                                    .put(Emailv31.Message.TO, new JSONArray()
                                            .put(new JSONObject()
                                                    .put("Email", supportPersonEmail)
                                                    .put("Name", "Support Person")))
                                    .put(Emailv31.Message.SUBJECT, "Escalation: Ticket - " + ticketTitle)
                                    .put(Emailv31.Message.TEXTPART,
                                            "You have been assigned a new ticket.\n\n" +
                                                    "Consumer: " + consumerName + "\n" +
                                                    "Phone: " + consumerPhone + "\n" +
                                                    "Issue: " + description + "\n\n" +
                                                    "Please contact the consumer immediately.")
                            ));

            MailjetResponse response = client.post(request);
            System.out.println("Email sent status: " + response.getStatus());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send email");
        }
    }


}
