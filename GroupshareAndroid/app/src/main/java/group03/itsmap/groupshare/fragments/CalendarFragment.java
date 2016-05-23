package group03.itsmap.groupshare.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageButton;

import group03.itsmap.groupshare.R;
import group03.itsmap.groupshare.activities.CalendarActivity;
import group03.itsmap.groupshare.utils.IntentKey;

public class CalendarFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        CalendarView calendarView = (CalendarView) view.findViewById(R.id.fragment_calender_view);

        ImageButton calendarFragmentButton = (ImageButton) view.findViewById(R.id.calender_fragment_button);
        calendarFragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startCalendarActivityIntent = new Intent(getActivity(), CalendarActivity.class);
                startActivity(startCalendarActivityIntent);
            }
        });

        return view;
    }

}
