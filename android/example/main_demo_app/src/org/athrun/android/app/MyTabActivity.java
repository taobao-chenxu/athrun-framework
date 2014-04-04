package org.athrun.android.app;

import android.app.TabActivity;  
import android.content.Intent;  
import android.os.Bundle;  
import android.widget.TabHost;  
import android.widget.TabHost.TabSpec;  
  
public class MyTabActivity extends TabActivity {  
    private TabHost m_tabHost;  
    /** Called when the activity is first created. */  
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.tabs_layout);  
          
        //getTabHost返回的TabHost用于装载tabs  
        m_tabHost = getTabHost();  
          
        //add tabs,这里用于添加具体的Tabs,并用Tab触发相应的Activity  
        addOneTab();  
        addTwoTab();  
        addThreeTab();  
        m_tabHost.setCurrentTab(0);
        
    }  
      
    public void addOneTab(){  
        Intent intent = new Intent();  
        intent.setClass(MyTabActivity.this, TabOneActivity.class);  
          
        TabSpec spec = m_tabHost.newTabSpec("One");  
        spec.setIndicator(getString(R.string.first_tab), null);  
        spec.setContent(intent);          
        m_tabHost.addTab(spec);  
    }  
      
    public void addTwoTab(){  
        Intent intent = new Intent();  
        intent.setClass(MyTabActivity.this, TabTwoActivity.class);  
          
        TabSpec spec = m_tabHost.newTabSpec("Two");  
        spec.setIndicator(getString(R.string.second_tab), null);  
        spec.setContent(intent);          
        m_tabHost.addTab(spec);  
    }  
    public void addThreeTab(){  
        Intent intent = new Intent();  
        intent.setClass(MyTabActivity.this, TabThreeActivity.class);  
          
        TabSpec spec = m_tabHost.newTabSpec("Three");  
        spec.setIndicator(getString(R.string.third_tab), null);  
        spec.setContent(intent);          
        m_tabHost.addTab(spec);  
    }  
    
}  
