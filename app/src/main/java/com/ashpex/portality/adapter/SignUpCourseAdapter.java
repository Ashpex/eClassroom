package com.ashpex.portality.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ashpex.portality.R;

import java.util.List;

public class SignUpCourseAdapter extends RecyclerView.Adapter<SignUpCourseAdapter.SignUpCourseViewHolder>{
    private final List<String> mlist;
    public SignUpCourseAdapter(List<String> list) {
        mlist = list;
    }
    @NonNull
    @Override
    public SignUpCourseAdapter.SignUpCourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new SignUpCourseViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_course_sign_up, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SignUpCourseViewHolder holder, int position) {
        holder.bindData(mlist.get(position));
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public static class SignUpCourseViewHolder extends RecyclerView.ViewHolder{

        private final TextView nameCourse_item;
        private final TextView nameTeacher_item;
        private final ImageButton btnRegister;
        public SignUpCourseViewHolder(@NonNull View itemView) {
            super(itemView);
            nameCourse_item = itemView.findViewById(R.id.nameCourse_item);
            nameTeacher_item = itemView.findViewById(R.id.nameTeacher_item);
            btnRegister = itemView.findViewById(R.id.btnRegister);
        }
        public void bindData(String pos) {
            nameCourse_item.setText("Giáo dục công dân");
            nameTeacher_item.setText("Giảng viên: Lee Quan");
            btnRegister.setBackgroundResource(R.drawable.ic_unregistered);
        }
    }
}
