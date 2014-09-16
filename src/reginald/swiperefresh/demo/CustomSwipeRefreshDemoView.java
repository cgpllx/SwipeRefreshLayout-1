package reginald.swiperefresh.demo;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import reginald.swiperefresh.R;
import reginald.swiperefresh.demo.dummydata.Cheeses;
import reginald.swiperefresh.view.CustomSwipeRefreshLayout;

import java.util.List;

/**
 * Created by tony.lxy on 2014/9/11.
 */
public class CustomSwipeRefreshDemoView extends LinearLayout {

    private Context mContext;
    private static final int LIST_ITEM_COUNT = 20;

    /**
     * The CustomSwipeRefreshLayout that detects swipe gestures and
     * triggers callbacks in the app.
     */
    private CustomSwipeRefreshLayout mSwipeRefreshLayout;

    /**
     * The {@link android.widget.ListView} that displays the content that should be refreshed.
     */
    private ListView mListView;

    /**
     * The {@link android.widget.ListAdapter} used to populate the {@link android.widget.ListView}
     * defined in the previous statement.
     */
    private ArrayAdapter<String> mListAdapter;

    public CustomSwipeRefreshDemoView(Context context) {
        super(context);
        mContext = context;
        setupView();
    }

    protected void setupView() {
        mSwipeRefreshLayout = new CustomSwipeRefreshLayout(mContext);
        // Enable the top progress bar
        mSwipeRefreshLayout.enableTopProgressBar(true);
        // Keeping the refreshing head on the top
        mSwipeRefreshLayout.enableTopRefreshingHead(false);
        // Set progress bar colors
        mSwipeRefreshLayout.setColorScheme(
                R.color.swiperefresh_color_1, R.color.swiperefresh_color_2,
                R.color.swiperefresh_color_3, R.color.swiperefresh_color_4);


        // Create one listview as the only content view in the CustomSwipeRefreshLayout
        mListView = new ListView(mContext);
        mListView.setCacheColorHint(Color.TRANSPARENT);

        mListAdapter = new ArrayAdapter<String>(
                mContext,
                R.layout.demo_list_item,
                R.id.item_text,
                Cheeses.randomList(LIST_ITEM_COUNT));

        mListView.setAdapter(mListAdapter);

        // set onRefresh listener
        mSwipeRefreshLayout.setOnRefreshListener(new CustomSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // do something here when it starts to refresh
                initiateRefresh();
            }
        });


        // add listview to CustomSwipeRefreshLayout. YOU SHOULD ONLY ADD ONE CHILD IN CUSTOMSWIPEREFRESHLAYOUT
        mSwipeRefreshLayout.addView(mListView);


        addView(mSwipeRefreshLayout);
        setOrientation(LinearLayout.VERTICAL);
    }


    private void initiateRefresh() {
        new DummyBackgroundTask().execute();
    }

    private void onRefreshComplete(List<String> result) {
        mListAdapter.clear();
        for (String cheese : result) {
            mListAdapter.add(cheese);
        }
        // to notify CustomSwipeRefreshLayout that the refreshing is completed
        mSwipeRefreshLayout.onRefreshingComplete();
    }

    private class DummyBackgroundTask extends AsyncTask<Void, Void, List<String>> {

        static final int TASK_DURATION = 3 * 1000; // 3 seconds

        @Override
        protected List<String> doInBackground(Void... params) {
            // Sleep for a small amount of time to simulate a background-task
            try {
                Thread.sleep(TASK_DURATION);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Return a new random list of cheeses
            return Cheeses.randomList(LIST_ITEM_COUNT);
        }

        @Override
        protected void onPostExecute(List<String> result) {
            super.onPostExecute(result);

            // Tell the view that the refresh has completed
            onRefreshComplete(result);
        }

    }


}