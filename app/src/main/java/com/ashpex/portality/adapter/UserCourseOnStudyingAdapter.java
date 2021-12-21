package com.ashpex.portality.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ashpex.portality.R;
import com.ashpex.portality.model.UserCourseOnStudying;

import java.util.List;

public class UserCourseOnStudyingAdapter extends RecyclerView.Adapter<UserCourseOnStudyingAdapter.UserCourseViewHolder>{
    private List<UserCourseOnStudying> mlist;

    public void notifyData() {
        notifyDataSetChanged();
    }
    public void setList(List<UserCourseOnStudying> list) {
        mlist = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public UserCourseOnStudyingAdapter.UserCourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new UserCourseOnStudyingAdapter.UserCourseViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_course_on_studying, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserCourseOnStudyingAdapter.UserCourseViewHolder holder, int position) {
        holder.bindData(mlist.get(position));
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public static class UserCourseViewHolder extends RecyclerView.ViewHolder{

        private final TextView nameCourse;
        private final TextView timeCourse;
        private final TextView nameTeacher;
        public UserCourseViewHolder(@NonNull View itemView) {
            super(itemView);
            nameCourse = itemView.findViewById(R.id.nameCourse);
            timeCourse = itemView.findViewById(R.id.timeCourse);
            nameTeacher = itemView.findViewById(R.id.nameTeacher);
        }
        public void bindData(UserCourseOnStudying pos) {
            nameCourse.setText(pos.getCourse_name());
            timeCourse.setText("Thời gian: "+ pos.getTime_start().substring(0,5)+ " - " + pos.getTime_end().substring(0,5));
            nameTeacher.setText("Giáo viên: " + pos.getTeacher_name());
        }
    }
}