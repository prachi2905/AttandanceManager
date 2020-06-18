package com.pm.attandancemanager.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pm.attandancemanager.R;
import com.pm.attandancemanager.activities.StudentReportActivity;
import com.pm.attandancemanager.database.DatabaseHandler;
import com.pm.attandancemanager.models.Students;
import com.pm.attandancemanager.utils.Constants;
import com.pm.attandancemanager.utils.DialogUtils;

import java.util.List;


/**
 * Created by ghanshyamnayma on 12/07/17 on 23/5/17.
 */

public class StudentsListAdapter extends RecyclerView.Adapter<StudentsListAdapter.ViewHolder> {

    private List<Students> studentsList;
    private Activity mContext;
    private DatabaseHandler db;

    public StudentsListAdapter(Activity context, List<Students> studentsList, boolean isAddStudent) {
        this.mContext = context;
        this.studentsList = studentsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_attendance, parent, false);
        return new StudentsListAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.mTvSubjectName.setText("" + studentsList.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, StudentReportActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.STUDENT_NAME, studentsList.get(position));
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                deleteConfirmDialog(position);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTvSubjectName, mTvTotalLectures;

        ViewHolder(View itemView) {
            super(itemView);

            mTvSubjectName = (TextView) itemView.findViewById(R.id.tv_subject_name);
            mTvTotalLectures = (TextView) itemView.findViewById(R.id.tv_total_lecture);
        }


    }

    // showing custom dialog on delete
    private void deleteConfirmDialog(final int position) {
        //fetch the user information to show in alert
        final AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setTitle(mContext.getString(R.string.app_name));
        dialog.setMessage("Do you really want to delete it?");
        dialog.setCancelable(true);
        //positive button
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               //TODO detele functionality here
                DialogUtils.appToast(mContext,"Deleted successfully");
                db = new DatabaseHandler(mContext);
                if(position == 1)
                db.deleteStudent(studentsList.get(position).getId());
                else
                    db.deleteStudent(studentsList.get(position-1).getId());
                studentsList.remove(position);
                notifyDataSetChanged();
                dialog.cancel();

            }
        });
        //negative button
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

}
