package com.flyingh.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.flyingh.adapter.GroupAdapter;
import com.flyingh.adapter.GroupAdapter.Transformer;
import com.flyingh.vo.Person;

public class MainActivity extends Activity {

	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		listView = (ListView) findViewById(R.id.listView);
		List<Person> list = Arrays.asList(new Person("a", 18, 98), new Person("b", 19, 88), new Person("c", 18, 98), new Person("d", 17, 88),
				new Person("e", 20, 75), new Person("f", 18, 99), new Person("g", 18, 98), new Person("h", 19, 88));
		Transformer<Person, Integer> transformer = new Transformer<Person, Integer>() {

			@Override
			public Integer transform(Person p) {
				return p.getAge();
			}

		};
		listView.setAdapter(new GroupAdapter<Integer, List<Person>, Person>(this, transformer, new ArrayList<>(list)) {

			@Override
			public View newKeyView(Context context, Integer item, ViewGroup parent) {
				TextView textView = new TextView(MainActivity.this);
				return textView;
			}

			@Override
			public void bindKeyView(View view, Context context, Integer item) {
				TextView textView = (TextView) view;
				textView.setText(String.valueOf(item));
			}

			@Override
			public View newValueView(Context context, Person item, ViewGroup parent) {
				return View.inflate(context, R.layout.person_item, null);
			}

			@Override
			public void bindValueView(View view, Context context, Person item) {
				Person person = item;
				TextView nameTextView = (TextView) view.findViewById(R.id.nameTextView);
				TextView scoreTextView = (TextView) view.findViewById(R.id.scoreTextView);
				nameTextView.setText(person.getName());
				scoreTextView.setText(String.valueOf(person.getScore()));
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
