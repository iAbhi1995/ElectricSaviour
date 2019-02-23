package com.smartindia2k19.hackathon.Smartindia2k19.Controller;

import org.springframework.web.bind.annotation.*;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

//import com.google.api.client.json.*;

@RestController
public class HelloController {

    @RequestMapping(method = RequestMethod.GET, value = "/hello/{name}")
    public String sayHi(@PathVariable String name) {
        URL url = null;
        HttpURLConnection con = null;
        try {
            url = new URL("https://www.googleapis.com/gmail/v1/users/meabhi75%40gmail.com/messages?key=AIzaSyDl2s3QMc7gvRMC9W3IsjFvSrrAA6Fm2yk");
            con = (HttpsURLConnection) url.openConnection();

            con.setRequestMethod("GET");

            con.setRequestProperty("Accept", "application/json");

            if (con.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + con.getResponseCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (con.getInputStream())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }

            con.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }


        //AIzaSyDl2s3QMc7gvRMC9W3IsjFvSrrAA6Fm2yk
        return "hello " + name;
    }

    @PostMapping(value = "/hello")
    public String sayHiPOST(@RequestBody String name) {


        return "hello " + name;
    }
}
