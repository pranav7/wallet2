﻿package com.example.wallet2;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;


public class SmsReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {
                // TODO Auto-generated method stub


                Bundle extras = intent.getExtras();
                if (extras == null)
                        return;


                // To display a Toast whenever there is an SMS.
                // Toast.makeText(context,"Recieved",Toast.LENGTH_LONG).show();


                Object[] pdus = (Object[]) extras.get("pdus");
                for (int i = 0; i < pdus.length; i++) {
                        SmsMessage SMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        String sender = SMessage.getOriginatingAddress();
                        String body = SMessage.getMessageBody().toString();


                        Log.d("Wallet 2", sender);


                        // A custom Intent that will used as another Broadcast
                        Intent in = new Intent("SmsMessage.intent.MAIN").putExtra(
                                        "get_msg", sender + ":" + body);


                        // You can place your check conditions here(on the SMS or the
                        // sender)
                        // and then send another broadcast
                        context.sendBroadcast(in);


                        // This is used to abort the broadcast and can be used to silently
                        // process incoming message and prevent it from further being
                        // broadcasted. Avoid this, as this is not the way to program an
                        // app.
                        // this.abortBroadcast();
                }
        }
}