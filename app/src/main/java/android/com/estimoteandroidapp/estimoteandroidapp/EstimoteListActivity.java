package android.com.estimoteandroidapp.estimoteandroidapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Nearable;
import com.estimote.sdk.SystemRequirementsChecker;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import activitytest.android.com.estimoteandroidapp.R;

/**
 * An activity representing a list of Estimotes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link EstimoteDetailActivity} representing
 * item color. On tablets, the activity presents the list of items and
 * item color side-by-side using two vertical panes.
 */
public class EstimoteListActivity extends AppCompatActivity {

    private String TAG = "EstimoteListActivity";
    private String ABSOLUTE_API_URL = "http://estimote-api.azurewebsites.net";
    private String API_TEST_CALL = "/api/test";
    private String API_ESTIMOTE_CALL = "/api/estimotes";

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;


    private String scanId;
    private BeaconManager _beaconManager;
    private View _recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estimote_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        _recyclerView = findViewById(R.id.estimote_list);
        //setupRecyclerView((RecyclerView) _recyclerView);

        if (findViewById(R.id.estimote_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        _beaconManager = new BeaconManager(this);
        _beaconManager.setForegroundScanPeriod(1000,2000);
        Log.d(TAG, "Set Nearables listener");
        _beaconManager.setNearableListener(new BeaconManager.NearableListener() {

            @Override public void onNearablesDiscovered(List<Nearable> nearables) {
                Log.d(TAG, "Discovered Nearables: " + nearables);

                try {
                    saveDataToServer(nearables);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                /*
                if(!nearables.isEmpty())
                {
                    try
                    {
                        saveDataToServer(nearables);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }*/

                setupRecyclerView((RecyclerView) _recyclerView, nearables);
            }

        });
    }

    @Override
    protected void onStart(){
        super.onStart();

        _beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override public void onServiceReady() {
                scanId = _beaconManager.startNearableDiscovery();
            }
        });
    }

    @Override
    protected void onStop(){
        super.onStop();

        _beaconManager.stopNearableDiscovery(scanId);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        _beaconManager.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();

        SystemRequirementsChecker.checkWithDefaultDialogs(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, List<Nearable> nearables) {
        recyclerView.setAdapter(new RecyclerViewAdapter(nearables));
    }

    private void saveDataToServer(List<Nearable> nearables) throws IOException {
        new EstimoteApiTask(nearables).execute(new URL(ABSOLUTE_API_URL + API_TEST_CALL));


    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

        private final List<Nearable> _nearables;

        public RecyclerViewAdapter(List<Nearable> nearables) {
            this._nearables = nearables;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.estimote_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.nearable = _nearables.get(position);
            holder.typeView.setText(_nearables.get(position).type.toString());
            holder.colorView.setText(_nearables.get(position).color.toString());

            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putParcelable(EstimoteDetailFragment.ARG_NEARABLE, holder.nearable);
                        EstimoteDetailFragment fragment = new EstimoteDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.estimote_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, EstimoteDetailActivity.class);
                        intent.putExtra(EstimoteDetailFragment.ARG_NEARABLE, holder.nearable);

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return _nearables.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View view;
            public final TextView typeView;
            public final TextView colorView;
            public Nearable nearable;

            public ViewHolder(View view) {
                super(view);
                this.view = view;
                typeView = (TextView) view.findViewById(R.id.type);
                colorView = (TextView) view.findViewById(R.id.color);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + typeView.getText() + "'";
            }
        }
    }
}
