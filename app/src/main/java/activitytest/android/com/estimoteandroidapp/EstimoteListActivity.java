package activitytest.android.com.estimoteandroidapp;

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

import java.util.List;

import activitytest.android.com.estimoteandroidapp.model.Content;

/**
 * An activity representing a list of Estimotes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link EstimoteDetailActivity} representing
 * item temperature. On tablets, the activity presents the list of items and
 * item temperature side-by-side using two vertical panes.
 */
public class EstimoteListActivity extends AppCompatActivity {

    private String TAG = "EstimoteListActivity";

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private String scanId;
    //private static final UUID ESTIMOTE_PROXIMITY_UUID = UUID.fromString("d0d3fa86-ca76-45ec-9bd9-6af4a91e6443");
    private BeaconManager _beaconManager;

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

        View recyclerView = findViewById(R.id.estimote_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.estimote_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        _beaconManager = new BeaconManager(this);
        Log.d(TAG, "Set nearables listener");
        _beaconManager.setNearableListener(new BeaconManager.NearableListener() {

            @Override public void onNearablesDiscovered(List<Nearable> nearables) {
                Log.d(TAG, "Discovered nearables: " + nearables);
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

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new RecyclerViewAdapter(Content.ESTIMOTES));
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

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

        private final List<Content.Estimote> _estimotes;

        public RecyclerViewAdapter(List<Content.Estimote> estimotes) {
            _estimotes = estimotes;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.estimote_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.estimote = _estimotes.get(position);
            holder.uuidView.setText(_estimotes.get(position).uuid);
            holder.colorView.setText(_estimotes.get(position).color);

            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(EstimoteDetailFragment.ARG_ESTIMOTE_UUID, holder.estimote.uuid);
                        EstimoteDetailFragment fragment = new EstimoteDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.estimote_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, EstimoteDetailActivity.class);
                        intent.putExtra(EstimoteDetailFragment.ARG_ESTIMOTE_UUID, holder.estimote.uuid);

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return _estimotes.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View view;
            public final TextView uuidView;
            public final TextView colorView;
            public Content.Estimote estimote;

            public ViewHolder(View view) {
                super(view);
                this.view = view;
                uuidView = (TextView) view.findViewById(R.id.uuid);
                colorView = (TextView) view.findViewById(R.id.color);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + colorView.getText() + "'";
            }
        }
    }
}
