package com.example.parsetagram5.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.parsetagram5.EndlessRecyclerViewScrollListener;
import com.example.parsetagram5.Post;
import com.example.parsetagram5.R;
import com.example.parsetagram5.TimelineAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TimelineFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TimelineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimelineFragment extends Fragment {
    public final String APP_TAG = "Parsetagram5";
    protected TimelineAdapter timelineAdapter;
    protected List<Post> posts;
    RecyclerView rvTimeline;
    Context context;
    SwipeRefreshLayout swipeContainer;
    private OnFragmentInteractionListener mListener;

    public TimelineFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static TimelineFragment newInstance(String param1, String param2) {
        TimelineFragment fragment = new TimelineFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        posts = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = container.getContext();

        View view = inflater.inflate(R.layout.fragment_timeline, container, false);
        rvTimeline = view.findViewById(R.id.rvTimeline);
        timelineAdapter = new TimelineAdapter(posts);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        rvTimeline.setLayoutManager(linearLayoutManager);
        rvTimeline.setAdapter(timelineAdapter);
        queryPosts(true);
        swipeContainer = view.findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeContainer.setRefreshing(false);
                queryPosts(true);
            }
        });
        rvTimeline.addOnScrollListener(new EndlessRecyclerViewScrollListener((LinearLayoutManager) rvTimeline.getLayoutManager()) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                queryPosts(false);
            }
        });
        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onTimelineFragmentInteraction();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    protected void queryPosts(final boolean isFirstTime) {
        ParseQuery<Post> postQuery = new ParseQuery<Post>(Post.class);
        postQuery.include(Post.KEY_USER);
        postQuery.addDescendingOrder(Post.DATE);
        postQuery.findInBackground(new FindCallback<Post>() {
                                       @Override
                                       public void done(List<Post> newPosts, ParseException e) {
                                           if (e != null) {
                                               Log.e(APP_TAG, e.toString());
                                           } else {
                                               if (!isFirstTime) {
                                                   for (int i = posts.size(); i < newPosts.size(); i++) {
                                                       posts.add(newPosts.get(i));
                                                       timelineAdapter.notifyItemChanged(i);
                                                   }
                                               } else {
                                                   posts.clear();
                                                   posts.addAll(newPosts);
                                                   timelineAdapter.notifyDataSetChanged();
                                               }
                                           }
                                       }
                                   }
        );
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onTimelineFragmentInteraction();
    }
}
