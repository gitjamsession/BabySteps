package com.beatitudes.babysteps;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.CalendarContract;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;


public class MainActivity extends ActionBarActivity {
    EditText etName, etHospVisitDate;
    TextView tvEntriesList;
    String MY_ACCOUNT_NAME = "md2782@gmail.com";


    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;

    private static final String[] COLUMNS_TO_BE_BOUND = new String[]{

            CalendarContract.Events.TITLE,
            CalendarContract.Events.DESCRIPTION,
            CalendarContract.Events.DURATION

    };

    private static final int[] LAYOUT_ITEMS_TO_FILL = new int[]{
            android.R.id.text1,
            android.R.id.text2,

    };

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vaccinecalendarprovider);

        Button view = (Button) findViewById(R.id.viewButton);
        Button add = (Button) findViewById(R.id.createButton);
        Button modify = (Button) findViewById(R.id.updateButton);
        Button delete = (Button) findViewById(R.id.deleteButton);

        etName = (EditText) findViewById(R.id.etName);
        etHospVisitDate = (EditText) findViewById(R.id.etHospVisitDate);

        tvEntriesList = (TextView) findViewById(R.id.tvContactsText);
/**
 //// CREATE CALENDAR
 ContentValues values = new ContentValues();
 values.put(
 CalendarContract.Calendars.ACCOUNT_NAME,
 MY_ACCOUNT_NAME);
 values.put(
 CalendarContract.Calendars.ACCOUNT_TYPE,
 CalendarContract.ACCOUNT_TYPE_LOCAL);
 values.put(
 CalendarContract.Calendars.NAME,
 "Vaccination Calendar");
 values.put(
 CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
 "Vaccination Calendar");
 values.put(
 CalendarContract.Calendars.CALENDAR_COLOR,
 0xffff0000);
 values.put(
 CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL,
 CalendarContract.Calendars.CAL_ACCESS_OWNER);
 values.put(
 CalendarContract.Calendars.OWNER_ACCOUNT,
 "md2782@gmail.com");
 values.put(
 CalendarContract.Calendars.CALENDAR_TIME_ZONE,
 "Asia/Calcutta");
 values.put(
 CalendarContract.Calendars.SYNC_EVENTS,
 0);
 Uri.Builder builder =
 CalendarContract.Calendars.CONTENT_URI.buildUpon();
 builder.appendQueryParameter(
 CalendarContract.Calendars.ACCOUNT_NAME,MY_ACCOUNT_NAME);
 //// "com.beatitudes.babysteps");
 builder.appendQueryParameter(
 CalendarContract.Calendars.ACCOUNT_TYPE,
 CalendarContract.ACCOUNT_TYPE_LOCAL);
 builder.appendQueryParameter(
 CalendarContract.CALLER_IS_SYNCADAPTER,
 "true");
 builder.appendQueryParameter(CalendarContract.Calendars.VISIBLE,"true");

 Uri uri =
 getContentResolver().insert(builder.build(), values);
 Log.v("VaccineCP<<<<<<<","tABLE CREAted>>>>>>>>>>>");
 //// END
**/



        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                displayEntries();
                Log.v("VaccineCP<<<<<<","Completed Displaying Entries list>>>>>>>>>>");
            }


        });

        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String name = etName.getText().toString();
                String visit_date = etHospVisitDate.getText().toString();

                if (name.equals("") && visit_date.equals("")) {
                    Toast.makeText(getApplicationContext(), "Fields are empty",
                            Toast.LENGTH_SHORT).show();
                    return;
                }


                createEntry(name, visit_date);
                Log.v("VaccineCP","<<<<Created new Hospital visit Entry");
            }
        });

        modify.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String name = etName.getText().toString();
                String visit_date = etHospVisitDate.getText().toString();

                if (name.equals("") && visit_date.equals("")) {
                    Toast.makeText(getApplicationContext(), "Fields are empty",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                updateEntry(name, visit_date);
                Log.v("VaccineCP<<<<<<<<<","updated vaccination entry>>>>>>");
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String name = etName.getText().toString();

                if (name.equals("")) {
                    Toast.makeText(getApplicationContext(),
                            "Vaccine Name Field is empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                deleteEntry(name);
                Log.v("VaccineCP<<<<<<","Deleted created entry>>>>>");
            }

        });}


    private void displayEntries() {

        String[] projection =
                new String[]{
                        CalendarContract.Calendars._ID,
                        CalendarContract.Calendars.ACCOUNT_NAME,
                        CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
                        CalendarContract.Calendars.ACCOUNT_TYPE};

        Cursor calCursor = null;

        ContentResolver cr = getContentResolver();

        Uri uri= CalendarContract.Calendars.CONTENT_URI;

        String selection = "((" + CalendarContract.Calendars.CALENDAR_DISPLAY_NAME + " = ?))";
        String[] selectionArgs = new String[] {"Vaccination Calendar"};

        calCursor = cr.query(uri, projection, selection, selectionArgs, null);


        while (calCursor.moveToNext()) {
            long calID = 0;
            String displayName = null;
            String accountName = null;
            String ownerName = null;

            // Get the field values
            calID = calCursor.getLong(PROJECTION_ID_INDEX);
            displayName = calCursor.getString(PROJECTION_DISPLAY_NAME_INDEX);
            accountName = calCursor.getString(PROJECTION_ACCOUNT_NAME_INDEX);
            ownerName = calCursor.getString(PROJECTION_OWNER_ACCOUNT_INDEX);

            Log.v("STRTAG>>>>>>","Details printed<<<<<<<<<<<<<<"+displayName+accountName+ownerName);

        }


    }

    //// To get Calendar ID - Helper Function
    private long getCalendarId() {
        String[] projection = new String[]{CalendarContract.Calendars._ID};
        String selection =
                CalendarContract.Calendars.ACCOUNT_NAME +
                        " = ? " +
                        CalendarContract.Calendars.ACCOUNT_TYPE +
                        " = ? ";
        // use the same values as above:
        String[] selArgs =
                new String[]{
                        MY_ACCOUNT_NAME,
                        CalendarContract.ACCOUNT_TYPE_LOCAL};
        Cursor cursor =
                getContentResolver().
                        query(
                                CalendarContract.Calendars.CONTENT_URI,
                                projection,
                                selection,
                                selArgs,
                                null);
        if (cursor.moveToFirst()) {
            return cursor.getLong(0);
        }
        return -1;
    }

    private void createEntry(String name, String entry) {


        long calId = getCalendarId();
        if (calId == -1) {
            // no calendar account; react meaningfully
            return;
        }
        Calendar cal = new GregorianCalendar(2012, 11, 14);
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long start = cal.getTimeInMillis();
        ContentValues values2 = new ContentValues();
        values2.put(CalendarContract.Events.DTSTART, start);
        values2.put(CalendarContract.Events.DTEND, start);
        values2.put(CalendarContract.Events.RRULE,
                "FREQ=DAILY;COUNT=20;BYDAY=MO,TU,WE,TH,FR;WKST=MO");
        values2.put(CalendarContract.Events.TITLE, "Some title");
        values2.put(CalendarContract.Events.EVENT_LOCATION, "Kalamassery");
        values2.put(CalendarContract.Events.CALENDAR_ID, calId);
        values2.put(CalendarContract.Events.EVENT_TIMEZONE, "Asia/Calcutta");
        values2.put(CalendarContract.Events.DESCRIPTION,
                "Description of the event - All day Vaccination event");
// reasonable defaults exist:
        values2.put(CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.ACCESS_PRIVATE);
        values2.put(CalendarContract.Events.SELF_ATTENDEE_STATUS,
                CalendarContract.Events.STATUS_CONFIRMED);
        values2.put(CalendarContract.Events.ALL_DAY, 1);
        values2.put(CalendarContract.Events.ORGANIZER, "some.mail@some.address.com");
        values2.put(CalendarContract.Events.GUESTS_CAN_INVITE_OTHERS, 1);
        values2.put(CalendarContract.Events.GUESTS_CAN_MODIFY, 1);
        values2.put(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
        Uri uri2 =
                getContentResolver().
                        insert(CalendarContract.Events.CONTENT_URI, values2);
        long eventId = new Long(uri2.getLastPathSegment());

      }

    private void updateEntry(String name, String entry) {

        long calID = getCalendarId();
        ContentValues values = new ContentValues();
// The new display name for the calendar
        values.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, "Vaccine B Calendar");
        Uri updateUri = ContentUris.withAppendedId(CalendarContract.Calendars.CONTENT_URI, calID);
        int rows = getContentResolver().update(updateUri, values, null, null);
        Log.v("Update Log:<<<<<", "Rows updated: " + rows + " " + calID + " " + CalendarContract.Calendars.CALENDAR_DISPLAY_NAME + ">>>>>>>>>");
    }

    private void deleteEntry(String name) {

        ContentResolver cr = getContentResolver();
        String where = CalendarContract.CalendarEntity.ACCOUNT_NAME + " = ? ";
        String[] params = new String[] { name };

        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        ops.add(ContentProviderOperation
                .newDelete(CalendarContract.Events.CONTENT_URI)
                .withSelection(where, params).build());
        try {
            cr.applyBatch(CalendarContract.AUTHORITY, ops);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Toast.makeText(MainActivity.this,
                "Deleted the entry with name '" + name + "'",
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }
}
