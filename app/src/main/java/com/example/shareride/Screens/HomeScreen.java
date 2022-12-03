package com.example.shareride.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shareride.MapsActivity;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.example.shareride.R;

import java.text.DateFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class HomeScreen extends AppCompatActivity {

    // components
    private BottomAppBar bottomAppBar;

    public void click(View view) {
        startActivity(new Intent(getApplicationContext(), PaymentActivity.class));
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String stringSenderEmail = "shareride14931@gmail.com";
        //Windows computer - ygbbvwuguzhgyyij
        String stringPasswordSenderEmail = "ygbbvwuguzhgyyij";

        // Value hard coded
        String stringReceiverEmail1 = "ndpatel.tech@gmail.com";
        //String stringReceiverEmail2 = "prachivasava30@gmail.com";
        //String stringReceiverEmail3 = "ndpatel.tech@gmail.com";
        String offerride ="Nikunj ";
        String[] riders = {"Krupa ", "Prachi "};
        String source = "Anand ";
        String destination ="Baroda";
        Date date = new Date();
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
        String Rdate = dateFormat.format(date);
        int cost = 350;
        String payment = "Success";
        String payid = "W45THbncdj";


        //Body
        String body = " Hello Users," +
                "\n We are delighted and grateful that you have chosen to utilise share ride." +
                "\n\n ************Ride Details************             " +
                "\n\n Offered Ride By : " + offerride +
                "\n Rider : " + riders[0] + riders[1] +
                "\n Source : " + source +
                "\n Destination : " + destination +
                "\n Date : " + Rdate +
                "\n Cost per person: " + cost +
                "\n Payment : " + payment +
                "\n Payment reference : " + payid +
                "\n\n  If you have any additional questions. Email:shareride14931@gmail.com" +
                "\n We look forward to assisting you along the road." +
                "\n Thank you";



        String stringHost = "smtp.gmail.com";

        Properties properties = new Properties();

        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", stringHost);
        properties.put("mail.smtp.port", "587"); //465

        Session session = Session.getInstance(properties,
                new javax.mail.Authenticator(){
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(stringSenderEmail, stringPasswordSenderEmail);
                    }
                });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(stringSenderEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(stringReceiverEmail1));
            message.setSubject(" Ride Details ");
            message.setText(body);
            Transport.send(message);
            Toast.makeText(HomeScreen.this, "Email Send Successfully", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(HomeScreen.this, "Email not send", Toast.LENGTH_LONG).show();
        }
    }

    public void add(View view) {
        startActivity(new Intent(getApplicationContext(), SourceLocationScreen.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        initializeComponents();
        setSupportActionBar(bottomAppBar);


        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Profile.class));
            }
        });
//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        mAuth.signOut();

    }


    private void initializeComponents() {
        // this method will be used to initialze all the components
        bottomAppBar = findViewById(R.id.bottom_action_bar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.bottom_app_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.app_bar_search:
                startActivity(new Intent(HomeScreen.this, SearchRide.class));
                break;
            case R.id.app_bar_notification:
                startActivity(new Intent(HomeScreen.this, Notification.class));
                break;
        }

        return true;

    }
}