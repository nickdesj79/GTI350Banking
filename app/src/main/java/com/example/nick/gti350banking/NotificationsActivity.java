package com.example.nick.gti350banking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NotificationsActivity extends AppCompatActivity {

    private OnlineAccount account;
    private ArrayList<InteracTransfer> itList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);


        account = (OnlineAccount) getIntent().getSerializableExtra("account");

        List<Map<String, String>> notificationList = new ArrayList<Map<String, String>>();
        ListView lv = (ListView)findViewById(R.id.listItem);

        for (InteracTransfer it : SingletonAccountManager.getInstance().getInteracTransferList()) {
            if(it.getToAccount().getEmail().equals(account.getEmail()) && it.getState().equals("SEND")) {
                itList.add(it);
                Map<String, String> notification = new HashMap<String, String>(2);
                notification.put("title", "From : "+it.getFromEmail());
                notification.put("amount", "Amount : " +Integer.toString((int)it.getAmount()));
                notificationList.add(notification);
            }
        }

        SimpleAdapter adapter = new SimpleAdapter(this, notificationList,
                android.R.layout.simple_list_item_2,
                new String[] {"title", "amount"},
                new int[] {android.R.id.text1,
                        android.R.id.text2});
        lv.setAdapter(adapter);

        addAction(lv);

    }

    public void openMenu(View v) {


        Intent i = new Intent(getApplicationContext(), RightMenuActivity.class);

        i.putExtra("account", account);
        startActivity(i);
        finish();
    }

    private void addAction(ListView lv) {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView <?> parentAdapter, View view, int position,
                                    long id) {
                Intent i = new Intent(getApplicationContext(), AcceptingInteractTransferActivity.class);

                InteracTransfer it = itList.get(position);

                i.putExtra("account", account);
                i.putExtra("interacTransfer", it);
                startActivity(i);
                finish();

            }
        });
    }
}
