package com.ashpex.portality.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ashpex.portality.ActionForumInterface;
import com.ashpex.portality.CourseActivity;
import com.ashpex.portality.R;
import com.ashpex.portality.SearchResultActivity;
import com.ashpex.portality.SignUpActivity;
import com.ashpex.portality.adapter.UserCourseOnStudyingAdapter;
import com.ashpex.portality.api.ApiService;
import com.ashpex.portality.model.Course;
import com.ashpex.portality.model.UserCourseOnStudying;
import com.ashpex.portality.fragment.CalendarFragment;
import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForumFragment extends Fragment {
    private LinearLayout layout_course_forum;
    private TextView txtNameUser_forum;
    private LinearLayout layout_Schedule_forum;
    private LinearLayout layout_task_forum;
    private LinearLayout layout_fee_forum;
    private RecyclerView ryc_forum;
    private View view;
    private String userName;
    private String token;
    private Integer userId;
    private UserCourseOnStudyingAdapter userCourseAdapter;
    private TextView txtForum;
    private ActionForumInterface actionForumInterface;
    private EditText searchText;
    private ImageView searchingIcon;
    private int page;

    public void setActionForumInterface(ActionForumInterface actionForumInterface) {
        this.actionForumInterface = actionForumInterface;
    }

    NavigationView navigationView;
    private int type;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_forum, container, false);
        navigationView = LayoutInflater.from(getContext()).inflate(R.layout.main_screen, null)
                .findViewById(R.id.nav_view_main);
        mappingControls();
        getData();
        addEvents();


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        txtNameUser_forum.setText(userName);
    }

    private void getData() {
        SharedPreferences sharedPref = view.getContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        userName = sharedPref.getString("user_name", "null");
        token = sharedPref.getString("token", "null");
        userId = sharedPref.getInt("user_id", 0);
        type = sharedPref.getInt("user_type", 0);
        String temp = "Lịch học hôm nay";
        if(type == 1) {
            temp = "Lịch dạy hôm nay";
        }
        txtForum.setText(temp);

        ApiService.apiService.getUserCourse(userId, token).enqueue(new Callback<List<UserCourseOnStudying>>() {
            @Override
            public void onResponse(Call<List<UserCourseOnStudying>> call, Response<List<UserCourseOnStudying>> response) {
                if(response.code() == 200) {
                    List<UserCourseOnStudying> mlist = response.body();
                    FilterAsyncTask filterAsyncTask = new FilterAsyncTask();
                    filterAsyncTask.setMlist(mlist);
                    filterAsyncTask.execute();
                }
            }

            @Override
            public void onFailure(Call<List<UserCourseOnStudying>> call, Throwable t) {
                Log.d("alo", "Cant connect");
            }
        });
    }

    private void filterList(List<UserCourseOnStudying> mlist) {
        Date currentTime = Calendar.getInstance().getTime();
        int day = currentTime.getDay();
        mlist.removeIf(i -> i.getDay_study() != day);
    }

    private void addEvents() {
        searchingIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(searchText.getText())) {
                    Toast.makeText(getContext(), "Mời nhập đầy đủ dữ liệu", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    dialogDuty();
                }
            }
        });
        layout_course_forum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CourseActivity.class);
                startActivity(intent);
            }
        });


        layout_Schedule_forum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionForumInterface.setChecked(1);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frMain, new CalendarFragment());
                transaction.commit();
            }
        });
    }

    private void dialogDuty() {
        final String[] state = {
                "Khóa học đang trong thời gian đăng ký", "Khóa học đang trong thời gian học",
                "Khóa học đã kết thúc"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Option");
        builder.setItems(state, new DialogInterface.OnClickListener() {@Override
        public void onClick(DialogInterface dialog, int which) {
            page = which;
            dialog.cancel();
            Intent intent = new Intent(getActivity(), SearchResultActivity.class);
            intent.putExtra("text_search", searchText.getText().toString());
            intent.putExtra("page", page);
            startActivity(intent);
        }
        });
        builder.show();
    }

    private void setCheckedSchedule() {
        navigationView.getMenu().findItem(R.id.menuSchedule).setChecked(true);
    }

    private void mappingControls() {
        layout_course_forum = view.findViewById(R.id.layout_course_forum);
        txtNameUser_forum = view.findViewById(R.id.txtNameUser_forum);
        layout_Schedule_forum = view.findViewById(R.id.layout_Schedule_forum);
        layout_task_forum = view.findViewById(R.id.layout_task_forum);
        layout_fee_forum = view.findViewById(R.id.layout_fee_forum);
        ryc_forum = view.findViewById(R.id.ryc_forum);
        txtForum = view.findViewById(R.id.txtForum);
        userCourseAdapter = new UserCourseOnStudyingAdapter();
        searchText = view.findViewById(R.id.searchText);
        searchingIcon = view.findViewById(R.id.searchingIcon);
    }

    class FilterAsyncTask extends AsyncTask<Void, Void, Void> {
        List<UserCourseOnStudying> mlist;

        public void setMlist(List<UserCourseOnStudying> mlist) {
            this.mlist = mlist;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            filterList(mlist);
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            if(mlist.size() !=0) {
                userCourseAdapter.setList(mlist);
                ryc_forum.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                ryc_forum.setAdapter(userCourseAdapter);
            }
            else
                Toast.makeText(view.getContext(), "Hôm nay lịch trống", Toast.LENGTH_SHORT).show();
        }
    }
}