package com.king.holymary;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Arvindo on 22-02-2017.
 * Company KinG
 * email at support@towardtheinfinity.com
 */

public class Notify extends Fragment implements View.OnClickListener{

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.notify_tab, container, false);
        ImageView new_updates = (ImageView) view.findViewById(R.id.new_updates);
        ImageView examination_cell = (ImageView) view.findViewById(R.id.examination_cell);
        ImageView magazine = (ImageView) view.findViewById(R.id.magazine);
        ImageView sports = (ImageView) view.findViewById(R.id.sports);
        ImageView library = (ImageView) view.findViewById(R.id.library);
        ImageView scholarship = (ImageView) view.findViewById(R.id.scholarship);
        ImageView training = (ImageView) view.findViewById(R.id.training);
        ImageView higher_education = (ImageView) view.findViewById(R.id.higher_education);
        ImageView accounts = (ImageView) view.findViewById(R.id.accounts);
        ImageView placements = (ImageView) view.findViewById(R.id.placements);

        new_updates.setOnClickListener(this);
        examination_cell.setOnClickListener(this);
        magazine.setOnClickListener(this);
        sports.setOnClickListener(this);
        library.setOnClickListener(this);
        scholarship.setOnClickListener(this);
        training.setOnClickListener(this);
        higher_education.setOnClickListener(this);
        accounts.setOnClickListener(this);
        placements.setOnClickListener(this);

        return  view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.new_updates:    //latest_updates
                startActivity(new Intent(getActivity(), NoticeActivity_1.class)
                        .putExtra("dataType", "1"));
                break;

            case R.id.examination_cell:    //examination_cell
                startActivity(new Intent(getActivity(), NoticeActivity_1.class)
                        .putExtra("dataType", "2"));
                break;

            case R.id.magazine:    //magazine
                startActivity(new Intent(getActivity(), NoticeActivity_1.class)
                        .putExtra("dataType", "3"));
                break;

            case R.id.sports:    //sports
                startActivity(new Intent(getActivity(), NoticeActivity_1.class)
                        .putExtra("dataType", "4"));
                break;

            case R.id.library:    //library
                startActivity(new Intent(getActivity(), NoticeActivity_1.class)
                        .putExtra("dataType", "5"));
                break;

            case R.id.scholarship:    //scholarship
                startActivity(new Intent(getActivity(), NoticeActivity_1.class)
                        .putExtra("dataType", "6"));
                break;

            case R.id.training:    //training
                startActivity(new Intent(getActivity(), NoticeActivity_1.class)
                        .putExtra("training", "7"));
                break;

            case R.id.higher_education:    //higher_edu_cell
                startActivity(new Intent(getActivity(), NoticeActivity_1.class)
                        .putExtra("dataType", "8"));
                break;

            case R.id.accounts:    //accounts
                startActivity(new Intent(getActivity(), NoticeActivity_1.class)
                        .putExtra("dataType", "9"));
                break;

            case R.id.placements:   //placements
                startActivity(new Intent(getActivity(), NoticeActivity_1.class)
                        .putExtra("dataType", "10"));
                break;
        }
    }
}