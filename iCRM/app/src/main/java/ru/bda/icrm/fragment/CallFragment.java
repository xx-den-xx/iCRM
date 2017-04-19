package ru.bda.icrm.fragment;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ru.bda.icrm.R;
import ru.bda.icrm.adapter.CallRecyclerAdapter;
import ru.bda.icrm.database.DBController;
import ru.bda.icrm.holders.AppPref;
import ru.bda.icrm.model.Call;
import ru.bda.icrm.receiver.CallReceiver;

/**
 * Created by User on 31.08.2016.
 */
public class CallFragment extends Fragment {

    private RecyclerView mRvCall;
    private LinearLayoutManager mLayoutManager;
    private DBController mDBController;
    private CallRecyclerAdapter mAdapter;
    private List<Call> mListCall = new ArrayList<>();
    private ProgressBar mProgressBar;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_call, null);
        mDBController = new DBController(getActivity());
        initContent(view);
        //getCallDetails();
        return view;
    }

    private void initContent(View v) {
        mRvCall = (RecyclerView) v.findViewById(R.id.rv_call);
        mAdapter = new CallRecyclerAdapter(mListCall);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRvCall.setLayoutManager(mLayoutManager);
        mRvCall.setAdapter(mAdapter);
        mRvCall.setHasFixedSize(true);

        mProgressBar = (ProgressBar) v.findViewById(R.id.progress_bar);
    }

    private void getCallDetails() {
        StringBuffer sb = new StringBuffer();
        Cursor managedCursor = getActivity().managedQuery(CallLog.Calls.CONTENT_URI,null, null,null, null);
        int number = managedCursor.getColumnIndex( CallLog.Calls.NUMBER );
        int type = managedCursor.getColumnIndex( CallLog.Calls.TYPE );
        int date = managedCursor.getColumnIndex( CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex( CallLog.Calls.DURATION);
        long time = AppPref.getInstance().getDate(getActivity());
        sb.append( "Call Details :");
        while ( managedCursor.moveToNext() ) {
            Call call = new Call();
            String phNumber = managedCursor.getString( number );
            String callType = managedCursor.getString( type );
            String callDate = managedCursor.getString( date );
            String callDuration = managedCursor.getString( duration );
            String dir = null;
            int dircode = Integer.parseInt( callType );
            switch( dircode ) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "Исходящий";
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    dir = "Входящий";
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = "Пропущенный";
                    break;
            }
            call.setPhone(phNumber);
            call.setSend(false);
            call.setTime(Long.valueOf(callDate));
            call.setType(dir);
            call.setDuration(callDuration);
            if (time <= call.getTime()) mListCall.add(call);
        }
        mAdapter.setListCall(mListCall);
        mAdapter.notifyDataSetChanged();
        managedCursor.close();
        Log.d("Log_Call", sb.toString());
    }

    @Override
    public void onResume() {
        super.onResume();
        new GetDbCallTask().execute();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        synchronized (DBController.class) {
            mDBController.closeDb();
        }
    }

    private class GetDbCallTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            synchronized (DBController.class) {
                mListCall = mDBController.getCallList(true);
                mDBController.closeDb();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mProgressBar.setVisibility(View.GONE);
            if (mListCall != null) {
                mAdapter.setListCall(mListCall);
                mAdapter.notifyDataSetChanged();
            } else {
                synchronized (DBController.class) {
                    getCallDetails();
                    mDBController.closeDb();
                }
            }
        }
    }
}
