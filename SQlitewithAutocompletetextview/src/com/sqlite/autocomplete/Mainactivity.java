package com.sqlite.autocomplete;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class Mainactivity extends Activity
{
	private SQLiteAdapter db;
	AutoCompleteTextView at;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        db = new SQLiteAdapter(getBaseContext());
        
        at = (AutoCompleteTextView)findViewById(R.id.autotext);
        at.setOnEditorActionListener(new OnEditorActionListener(){
			@Override 
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) { 
				if(arg1 == EditorInfo.IME_ACTION_SEND) {
					String s = at.getText().toString();
		    		if (isSearchNewToDb(s)) {
						db.openToWrite();
						db.insert(s);
						db.close();
						updateAdapterFromDB();
		    		}
					at.setText("");
				}
				return false; 
			} 
	
		});
        
        at.setThreshold(1);
        
        dbInit();
    }
    
	private void dbInit() {
        db.openToRead();
        String[] getcontent = db.getAllContents();
        db.close();
        
        if (getcontent.length == 0) dbPutInitialValues();
        
        updateAdapterFromDB();
	}
	
    private boolean isSearchNewToDb(String searchString) {
    	db.openToRead();
        String[] content = db.getAllContents();
        List<String> contentList = Arrays.asList(content);
        if (contentList.contains(searchString)) {
        	return false;
        } else {
        	return true;
        }
    }

	private void dbPutInitialValues() {
		String[] icao = {
				"alpha",
				"bravo",
				"charlie",
				"delta",
				"echo",
				"foxtrot",
				"golf",
				"hotel",
				"india",
				"juliett",
				"kilo",
				"lima",
				"mike",
				"november",
				"oscar",
				"papa",
				"quebec",
				"romeo",
				"sierra",
				"tango",
				"uniform",
				"victor",
				"whiskey",
				"x-ray",
				"yankee",
				"zulu"
				};
		
		db.openToWrite();
        for (String s : icao) {
        	db.insert(s);
        }
        db.close();
	}
    
	private void updateAdapterFromDB() {
        db.openToRead();
        String[] getcontent = db.getAllContents();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, getcontent);
        at.setAdapter(adapter);
        db.close();
	}
}