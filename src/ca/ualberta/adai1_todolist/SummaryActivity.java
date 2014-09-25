package ca.ualberta.adai1_todolist;

import ca.ualberta.adai1_todolist.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SummaryActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_summary);
		TextView showNumber = (TextView) findViewById(R.id.showSummaryView);
		Button backToMain = (Button) findViewById(R.id.backToMain);
		// calculate and show the summary at the summary activity
		int todo_size = TodoListActivity.todo_list.size();
		int arch_size = TodoListActivity.arch_list.size();
		int todo_check = TodoListActivity.todo_list.checkedCount();
		int arch_check = TodoListActivity.arch_list.checkedCount();
		int todo_uncheck = todo_size - todo_check;
		int arch_uncheck = arch_size - arch_check;
		int all_size = todo_size + arch_size;
		int all_checked = todo_check + arch_check;
		int all_unchecked = all_size - all_checked;

		String summary = new String("All Items:" + all_size
				+ "\n->All Checked:" + all_checked + "\n->All Unchecked:"
				+ all_unchecked + "\n\nUnarchived Items:" + todo_size
				+ "\n->Checked Unarchived:" + todo_check
				+ "\n->Unchecked Unarchived:" + todo_uncheck
				+ "\n\nArchived Items:" + arch_size + " \n->Checked Archived:"
				+ arch_check + "\n->Unchecked Archived:" + arch_uncheck + "");
		showNumber.setText(summary);
		//return to the main activity
		backToMain.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(SummaryActivity.this,
						TodoListActivity.class);
				startActivity(intent);
			}
		});

	}
}
