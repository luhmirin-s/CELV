package com.github.siga111.customELV.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.github.siga111.customELV.app.widget.CustomELV;
import com.github.siga111.customELV.app.widget.CustomELVAdapter;
import com.github.siga111.customELV.app.widget.TopLevelGroup;

import java.util.ArrayList;
import java.util.List;



public class MainActivity extends Activity {

    CustomELV mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (CustomELV) findViewById(R.id.main_list_view);
        mListView.setOnListItemClickListener(new CustomELV.OnListItemClickedListener() {
            @Override
            public void onListItemClicked(Object listItem) {
                Toast.makeText(getApplicationContext(), ((Data) listItem).toString(), Toast.LENGTH_SHORT).show();
            }
        });
        mListView.init(createStructure(), new CustomELVAdapter(this));

    }

    private List<TopLevelGroup> createStructure() {
        List<Data> data = MockData.getMockData();

        List<TopLevelGroup> result = new ArrayList<TopLevelGroup>();
        
        result.add(new TopLevelGroup(data.get(0)));
        result.add(new TopLevelGroup(data.get(1)));
        result.add(new TopLevelGroup(data.get(2)));
        result.add(new TopLevelGroup(data.get(3)));
        
        result.get(0).add(data.get(4));
        result.get(0).add(data.get(5));
        result.get(2).add(data.get(6));
        result.get(2).add(data.get(7));
        result.get(3).add(data.get(8));

        return result;
    }

    public void selectEight(View v) {
        mListView.setSelectedItem(2, 1);
    }

    public void selectFour(View v) {
        mListView.setSelectedItem(3, CustomELV.NO_CHILD_SELECTED);
    }
}
