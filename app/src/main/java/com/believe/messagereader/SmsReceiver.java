package com.believe.messagereader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import static android.R.attr.data;

/**
 * Created by HP on 25-Nov-17.
 */

public class SmsReceiver extends BroadcastReceiver {
    MainActivity mActivity = new MainActivity();

    public SmsReceiver() {

    }


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            SmsMessage[] smsMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
            DataBaseHelper dbHelper = new DataBaseHelper(ApplicationContextProvider.getContext());
            for (SmsMessage message : smsMessages) {
                // Fetch Information From Message
                String mobileNumber = message.getOriginatingAddress();
                String messageBody = message.getMessageBody();
                String[] data = messageBody.split("#");
                if (data.length == 3) {
                    if (dbHelper.checkDataNumber(mobileNumber)) {
                        data[0] = data[0].toUpperCase();
                        data[1] = data[1].toUpperCase();
                        data[2] = data[2].toUpperCase();
                        String uniqueId = mActivity.uniqueIdGenerator(data[1]);
                        if (dbHelper.checkDataId(uniqueId)) {
                     /*
                      ** mobileNumber = Mobile Number Of Participant
                      * data[0] = Name of Participant
                      * data[1] = Name of Coaching
                      * data[2] = Name of Batch
                      */
                            dbHelper.insertData(data[0], data[1], data[2], mobileNumber, uniqueId);
                           mActivity.sendSMS(mobileNumber, uniqueId);
                            Log.v(data[0] + " messages ::", "Found");
                        }
                    }
                } else {
                    Log.v("Sync Status::", "Not going on properly");
                }
            }
        }
    }

}
