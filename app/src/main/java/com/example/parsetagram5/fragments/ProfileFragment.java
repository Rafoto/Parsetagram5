package com.example.parsetagram5.fragments;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.example.parsetagram5.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnProfileFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends TimelineFragment {
    public ProfileFragment() {

    }

    @Override
    protected void queryPosts(final boolean isFirstTime) {
        ParseQuery<Post> postQuery = new ParseQuery<Post>(Post.class);
        Intent f = new Intent();
        postQuery.include(Post.KEY_USER);
        postQuery.setLimit(20);
        postQuery.addDescendingOrder(Post.DATE);
        postQuery.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnProfileFragmentInteractionListener {
        void onProfileFragmentInteraction(Uri uri);
    }

}
