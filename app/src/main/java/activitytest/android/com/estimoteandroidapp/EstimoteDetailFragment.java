package activitytest.android.com.estimoteandroidapp;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import activitytest.android.com.estimoteandroidapp.model.Content;

/**
 * A fragment representing a single Estimote detail screen.
 * This fragment is either contained in a {@link EstimoteListActivity}
 * in two-pane mode (on tablets) or a {@link EstimoteDetailActivity}
 * on handsets.
 */
public class EstimoteDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ESTIMOTE_UUID = "item_id";

    private Content.Estimote _estimote;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EstimoteDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ESTIMOTE_UUID)) {
            _estimote = Content.ESTIMOTE_MAP.get(getArguments().getString(ARG_ESTIMOTE_UUID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(_estimote.color);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.estimote_detail, container, false);

        if (_estimote != null) {
            ((TextView) rootView.findViewById(R.id.estimote_detail)).setText(_estimote.temperature);
        }

        return rootView;
    }
}
