package activitytest.android.com.estimoteandroidapp;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.estimote.sdk.Nearable;


/**
 * A fragment representing a single Nearable detail screen.
 * This fragment is either contained in a {@link EstimoteListActivity}
 * in two-pane mode (on tablets) or a {@link EstimoteDetailActivity}
 * on handsets.
 */
public class EstimoteDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_NEARABLE = "BD2_Estimote";

    private Nearable _nearable;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EstimoteDetailFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_NEARABLE)) {
            _nearable = this.getArguments().getParcelable(ARG_NEARABLE);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(_nearable.type.toString());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.estimote_detail, container, false);

        if (_nearable != null) {
            ((TextView) rootView.findViewById(R.id.nearable_color)).setText(_nearable.color.toString());
            ((TextView) rootView.findViewById(R.id.nearable_temperature)).setText(String.valueOf(_nearable.temperature));
        }

        return rootView;
    }
}
