package com.sds.study.recordapp.record;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sds.study.recordapp.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by student on 2016-11-17.
 */

public class ListFragment extends Fragment implements AdapterView.OnItemClickListener{
    ListView listView;
    ArrayAdapter<String> adapter;
    ArrayList<String> list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, null);
        listView = (ListView)view.findViewById(R.id.listView);
        list = getFiles();
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        return view;
    }

    public ArrayList getFiles(){
        File dir = new File(Environment.getExternalStorageDirectory(),"iot_record");
        String[] files = dir.list();
        ArrayList<String> result = new ArrayList<>();
        for(int i=0;i<files.length;i++){
            result.add(files[i]);
        }
        return result;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int index, long id) {
        TextView txt = (TextView)view;
        String filename = txt.getText().toString();
        Toast.makeText(getContext(),"파일명 = "+filename,Toast.LENGTH_SHORT).show();

        FileListActivity fileListActivity = (FileListActivity)getContext();
        fileListActivity.viewPager.setCurrentItem(1);
    }
}
