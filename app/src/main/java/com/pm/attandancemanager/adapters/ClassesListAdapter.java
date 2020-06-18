package com.pm.attandancemanager.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pm.attandancemanager.R;
import com.pm.attandancemanager.activities.AddStudentsActivity;
import com.pm.attandancemanager.database.DatabaseHandler;
import com.pm.attandancemanager.database.RefreshDatabaseListener;
import com.pm.attandancemanager.models.Classes;
import com.pm.attandancemanager.utils.AndroidVersionUtility;
import com.pm.attandancemanager.utils.Constants;

import java.util.List;


public class ClassesListAdapter extends RecyclerView.Adapter<ClassesListAdapter.ViewHolder> {

    private List<Classes> studentsList;
    private Activity mContext;
    private DatabaseHandler db;
    private TextView mTextAlert;
    private static RefreshDatabaseListener mListener;


    public ClassesListAdapter(Activity context, List<Classes> studentsList,TextView mTextAlert, RefreshDatabaseListener mListener) {
        this.mContext = context;
        this.studentsList = studentsList;
        this.mTextAlert  = mTextAlert;
        this.mListener = mListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_classes, parent, false);
         db = new DatabaseHandler(mContext);
        return new ClassesListAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.mTvSubjectName.setText("" + studentsList.get(position).getSubjectName());
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Creating the instance of PopupMenu
                final PopupMenu popup = new PopupMenu(mContext, holder.mImageView);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.classes_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId()==R.id.action_edit)
                        {
                            addNewSubjectDialog(studentsList.get(position).getSubjectName());
                        }
                        else {

                            db.deleteClass(studentsList.get(position).getSubjectName());
                            studentsList.remove(position);
                            notifyDataSetChanged();
                            if(studentsList.size()==0)
                            mTextAlert.setVisibility(View.VISIBLE);

                            //addNewSubjectDialog(studentsList.get(position).getSubjectName());
                        }

                        return true;
                    }
                });

                popup.show(); //showing popup menu
            }



        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AddStudentsActivity.class);
                intent.putExtra(Constants.SUBJECT_NAME,studentsList.get(position).getSubjectName());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTvSubjectName,mTvTotalLectures;
        private  ImageView mImageView;

        ViewHolder(View itemView) {
            super(itemView);

            mTvSubjectName = (TextView) itemView.findViewById(R.id.tv_subject_name);
            mTvTotalLectures = (TextView) itemView.findViewById(R.id.tv_total_lecture);
            mImageView = (ImageView) itemView.findViewById(R.id.menu_bar);
        }

    }
    public void addNewSubjectDialog(final String subjectName) {
        final Dialog dialog;
        if (AndroidVersionUtility.isLollipopOrGreater()) {
            dialog = new Dialog(mContext, android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            dialog = new Dialog(mContext);
        }

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setContentView(R.layout.custom_dialog_add_students);
        final TextView dialogTitle = (TextView) dialog.findViewById(R.id.dialog_title);
        dialogTitle.setText(mContext.getString(R.string.add_new_class));
        dialog.setCancelable(true);

        final EditText edtName = (EditText) dialog.findViewById(R.id.et_dialog_name);
        final EditText edtRollNumber = (EditText) dialog.findViewById(R.id.et_dialog_roll_number);
        edtRollNumber.setVisibility(View.GONE);
        final EditText edtSection = (EditText) dialog.findViewById(R.id.et_dialog_section);
        edtSection.setVisibility(View.VISIBLE);
        if(subjectName.contains("-")) {
            String[] subject = subjectName.split("-");
            edtName.setText(subject[0]);
            edtSection.setText(subject[1]);

        }
        else
        {
            edtName.setText(subjectName);
        }

        final TextView txtSave = (TextView) dialog.findViewById(R.id.tv_save);
        final TextView txtCancel = (TextView) dialog.findViewById(R.id.tv_cancel);

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        txtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString();
                String section = edtSection.getText().toString();
                if (!section.isEmpty())
                    name = name + "-" + section;
                db.updateClass(subjectName,name);
                updateList(db.getAllClassList());
               // mListener.dataRefreshed();
                dialog.cancel();
            }
        });
        dialog.show();
    }
    public void updateList (List<Classes> items) {
        if (items != null && items.size() > 0) {
            studentsList.clear();
            studentsList.addAll(items);
            notifyDataSetChanged();
        }
    }

}
