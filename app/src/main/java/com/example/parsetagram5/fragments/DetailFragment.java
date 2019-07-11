package com.example.parsetagram5.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.parsetagram5.Post;
import com.example.parsetagram5.R;
import com.parse.ParseFile;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailFragment.OnDetailFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment {
    public ImageView ivComment;
    public ImageView ivImage;
    public ImageView ivLike;
    public ImageView ivUsericon;
    public TextView tvUsername;
    public TextView tvDate;
    public TextView tvCaption;
    public TextView tvLikes;
    public TextView tvComments;
    public RecyclerView rvComments;
    Context context;
    Post post;

    private OnDetailFragmentInteractionListener mListener;

    public DetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailFragment newInstance(String param1, String param2) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = container.getContext();
        ivComment = container.findViewById(R.id.ivComment);
        ivImage = container.findViewById(R.id.ivImage);
        ivLike = container.findViewById(R.id.ivLike);
        ivUsericon = container.findViewById(R.id.ivUsericon);
        tvUsername = container.findViewById(R.id.tvUsername);
        tvCaption = container.findViewById(R.id.tvCaption);
        tvDate = container.findViewById(R.id.tvDate);
        tvLikes = container.findViewById(R.id.tvLikes);
        tvComments = container.findViewById(R.id.tvComments);
        rvComments = container.findViewById(R.id.rvComments);
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.OnDetailFragmentInteraction(uri);
        }
    }

    public void setPost(Post post) {
        this.post = post;
        tvUsername.setText(post.getUser().getUsername());
        tvCaption.setText(post.getDescription());
        tvDate.setText(post.getDate());
        Glide.with(context).load(post.getImage().getUrl()).into(ivImage);
        if (post.getUser().get("profilePicture") != null) {
            Glide.with(context).load(((ParseFile) (post.getUser().get("profilePicture"))).getUrl()).into(ivUsericon);
        }


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDetailFragmentInteractionListener) {
            mListener = (OnDetailFragmentInteractionListener) context;
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
    public interface OnDetailFragmentInteractionListener {
        void OnDetailFragmentInteraction(Uri uri);
    }
}
