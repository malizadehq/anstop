/***************************************************************************
 *   Copyright (C) 2009 by mj   										   *
 *   fakeacc.mj@gmail.com  												   *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 *   This program is distributed in the hope that it will be useful,       *
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *   GNU General Public License for more details.                          *
 *                                                                         *
 *   You should have received a copy of the GNU General Public License     *
 *   along with this program; if not, write to the                         *
 *   Free Software Foundation, Inc.,                                       *
 *   59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.             *
 ***************************************************************************/

package An.stop;


import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;


public class loadActivity extends ListActivity {
	
	private anstopDbAdapter dbHelper;
	String body;
	
	private static final int MENU_DELETE = 12;
	private static final int DELETE_ITEM = 13;
	private static final int MENU_EXPORT = 14;
	private static final int EXPORT_ITEM = 15;
	//private static final int MENU_EXPORT_ALL = 16;
	private static final int EXPORT_ALL_ITEM = 17;
	

	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.load);
		dbHelper = new anstopDbAdapter(this);
		dbHelper.open();
		fillData();
		registerForContextMenu(getListView());
			
		
	}
	
	private void fillData() {
		Cursor c = dbHelper.fetchAll();
        startManagingCursor(c);
        
        String[] from = new String[]{anstopDbAdapter.KEY_TITLE};
        int[] to = new int[]{R.id.text1};
        
        
        SimpleCursorAdapter times = 
        	    new SimpleCursorAdapter(this, R.layout.times_row, c, from, to);
        setListAdapter(times);
	}
	
	 @Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
	    menu.add(MENU_DELETE, DELETE_ITEM, 0, R.string.delete);
	    menu.add(MENU_EXPORT, EXPORT_ITEM, 0, R.string.export);
		
	 }
	
	@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(this, showTimesActivity.class);
        i.putExtra(anstopDbAdapter.KEY_ROWID, id);
        startActivity(i);
    }
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

		switch(item.getItemId()) {
    	case DELETE_ITEM:
	        dbHelper.delete(info.id);
	        fillData();
	        return true;
	        
    	case EXPORT_ITEM:
    		
    		Toast toast;
    		exportHelper exHlp = new exportHelper(this);
	        
			
			if(exHlp.write(info.id))
				toast = Toast.makeText(this, R.string.export_succes, Toast.LENGTH_SHORT);
			else
				toast = Toast.makeText(this, R.string.export_fail, Toast.LENGTH_SHORT);
    		
			toast.show();
    		
    		return true;
		}
		return super.onContextItemSelected(item);
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	//menu.add(MENU_EXPORT_ALL, EXPORT_ALL_ITEM, 0, R.string.export_all).setIcon(android.R.drawable.ic_menu_share);
    	
        return true;
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case EXPORT_ALL_ITEM:
			Cursor c = dbHelper.fetchAll();
			
			Toast toast;
			
			exportHelper exHlp = new exportHelper(this);
			if(exHlp.write(c))
				toast = Toast.makeText(this, R.string.export_succes, Toast.LENGTH_SHORT);
			else
				toast = Toast.makeText(this, R.string.export_fail, Toast.LENGTH_SHORT);
			
			toast.show();
			
			return true;
		}
		
		return false;
	}

}