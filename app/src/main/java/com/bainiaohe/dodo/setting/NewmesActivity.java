package com.bainiaohe.dodo.setting;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.bainiaohe.dodo.R;

public class NewmesActivity extends PreferenceActivity implements OnPreferenceChangeListener,
		OnPreferenceClickListener {
    private SharedPreferences sp;
    private CheckBoxPreference callnewmes;
    private PreferenceGroup mespg, vspg;
    private Preference detailmes, isring, isshake;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.set_head);
		addPreferencesFromResource(R.xml.newmes);
        sp = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        ((Button) findViewById(R.id.itembackset)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                NewmesActivity.this.finish();
            }
        });
        ((TextView) findViewById(R.id.settitletext)).setText("新消息提醒");
        callnewmes = (CheckBoxPreference) findPreference("callnewmes");
        callnewmes.setOnPreferenceChangeListener(this);
        mespg = (PreferenceGroup) findPreference("mespg");
        vspg = (PreferenceGroup) findPreference("vspg");
        detailmes = (Preference) findPreference("detailmes");
        isring = (Preference) findPreference("isring");
        isshake = (Preference) findPreference("isshake");

		//pageCapListPref = (ListPreference)findPreference("listNum");
		//pageCapListPref.setOnPreferenceChangeListener(this);
	}
	
    @Override  
    public boolean onPreferenceChange(Preference preference, Object newValue) {  
        // TODO Auto-generated method stub
        if (preference.getKey().equals("callnewmes") && callnewmes.isChecked()) {
            mespg.removePreference(detailmes);
            vspg.removeAll();
        }
        else {
            mespg.addPreference(detailmes);
            vspg.addPreference(isring);
            vspg.addPreference(isshake);
        }
        return true;  
    }  
    
    @Override  
    public boolean onPreferenceClick(Preference preference) {  
        // TODO Auto-generated method stub   
        /*//�ж����ĸ�Preference�������
        if(preference.getKey().equals("restore"))
        {   
    		Dialog alertDialog = new AlertDialog.Builder(NewmesActivity.this, AlertDialog.THEME_HOLO_LIGHT).
                    setTitle("����"). 
                    setMessage("ȷ��Ҫ�ָ�Ĭ��������?"). 
                    //setIcon(R.drawable.logo).
                    setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(NewmesActivity.this.getApplicationContext());
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
        }  */
        return true;  
    }
    
    @Override    
    protected void onDestroy() {
        super.onDestroy();  
    }  
}
