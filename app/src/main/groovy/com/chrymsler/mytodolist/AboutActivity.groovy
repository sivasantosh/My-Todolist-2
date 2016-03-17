package com.chrymsler.mytodolist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        String content = """
<p>This is a simple todolist app with a folder system. The first page (you see when the app starts) lists the folders. Tap on the folder to see the todos under it.</p>
<h2>How to use the app?</h2>
<h3>Folders Page</h3>
<p>1. Tap + to create new folder.<br>
2. Long tap folder to rename it.<br>
3. Drag up/down to reposition.<br>
4. Swipe left/right to delete folder.<br>
5. Tap folder to view it's todos.<br></p>
<h3>Todos Page</h3>
<p>1. Tap + to create new todo.<br>
2. Tap todo to edit it.<br>
3. Drag up/down to reposition.<br>
4. Swipe left/right to delete it.<br>
5. Tap icon to change todo's priority.</p><br><br>
<p>Developed by<br/><b>Santosh Kumar S</b> <a href="http://santosh-online.blogspot.in/">(my blog)</a></p>"""

        TextView t = (TextView) findViewById(R.id.textView3)
        t.setText(Html.fromHtml(content))
        t.setMovementMethod(LinkMovementMethod.instance)
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
