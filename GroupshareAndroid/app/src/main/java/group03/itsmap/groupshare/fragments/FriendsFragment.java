package group03.itsmap.groupshare.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.AccessToken;

import org.lucasr.twowayview.TwoWayView;

import java.util.ArrayList;

import group03.itsmap.groupshare.R;
import group03.itsmap.groupshare.adapters.FriendsListAdapter;
import group03.itsmap.groupshare.models.Friend;
import group03.itsmap.groupshare.utils.FacebookUtil;

public class FriendsFragment extends Fragment {
    private static final String FRIENDS_LIST_KEY = "group03.itsmap.groupshare.fragments.FriendsFragment.FriendsList";

    private ArrayList<Friend> friendsList;
    private FriendsListAdapter friendsListAdapter;

    public FriendsFragment() {
    }

    public static FriendsFragment createFriendsFragment(ArrayList<Friend> friends) {
        FriendsFragment fragment = new FriendsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(FRIENDS_LIST_KEY, friends);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        friendsList = getArguments().getParcelableArrayList(FriendsFragment.FRIENDS_LIST_KEY);
        friendsListAdapter = new FriendsListAdapter(getActivity(), R.layout.friends_list_item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        TwoWayView friendsListView = (TwoWayView) view.findViewById(R.id.fragment_friends_list);

        friendsListView.setAdapter(friendsListAdapter);

        refreshAdapter();
        return view;
    }

    private void refreshAdapter() {
        if (friendsListAdapter == null) return;
        friendsListAdapter.clear();
        friendsListAdapter.add(new Friend(FacebookUtil.UserInfo.getId(), FacebookUtil.UserInfo.getName(), FacebookUtil.UserInfo.getPictureUrl()));
        friendsListAdapter.addAll(friendsList);
        friendsListAdapter.notifyDataSetChanged();
    }

    public void updateFriends(ArrayList<Friend> friends) {
        if (friends == null) return;
        friendsList = friends;
        refreshAdapter();
    }
}
