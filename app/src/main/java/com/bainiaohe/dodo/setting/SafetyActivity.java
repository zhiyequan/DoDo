package com.bainiaohe.dodo.setting;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bainiaohe.dodo.R;

public class SafetyActivity extends PreferenceActivity implements OnPreferenceChangeListener,
		OnPreferenceClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.set_head);
		addPreferencesFromResource(R.xml.safety);
        ((Button) findViewById(R.id.itembackset)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SafetyActivity.this.finish();
            }
        });
        ((TextView) findViewById(R.id.settitletext)).setText("安全");

		//pageCapListPref = (ListPreference)findPreference("listNum");
		//pageCapListPref.setOnPreferenceChangeListener(this);
	}
	
    @Override  
    public boolean onPreferenceChange(Preference preference, Object newValue) {  
        // TODO Auto-generated method stub
        return true;  
    }  
    
    @Override  
    public boolean onPreferenceClick(Preference preference) {  
        // TODO Auto-generated method stub   
        //�ж����ĸ�Preference�������  
        if(preference.getKey().equals("restore"))
        {   
    		Dialog alertDialog = new AlertDialog.Builder(SafetyActivity.this, AlertDialog.THEME_HOLO_LIGHT).
                    setTitle("����"). 
                    setMessage("ȷ��Ҫ�ָ�Ĭ��������?"). 
                    //setIcon(R.drawable.logo).
                    setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(SafetyActivity.this.getApplicationContext());
                            sp.edit().putString("listNum", "50").commit();
                            sp.edit().putString("listDist", "1000").commit();
                            sp.edit().putBoolean("switch1", true).commit();
                            sp.edit().putBoolean("switch2", false).commit();
                            sp.edit().putBoolean("switch3", true).commit();
                            sp.edit().putBoolean("switch4", false).commit();
                            finish();
                        }
                    }). 
                    setNegativeButton("ȡ��", new DialogInterface.OnClickListener() { 
                         
                        @Override 
                        public void onClick(DialogInterface dialog, int which) { 
                            dialog.dismiss();  
                        } 
                    }). 
                    create(); 
            alertDialog.show();
        }  
        return true;  
    }
    
    @Override    
    protected void onDestroy() {
        super.onDestroy();  
    }  
}
