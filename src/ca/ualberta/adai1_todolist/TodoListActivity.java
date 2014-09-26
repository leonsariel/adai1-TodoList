package ca.ualberta.adai1_todolist;

import java.util.ArrayList;
import ca.ualberta.adai1_todolist.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import android.widget.Toast;

public class TodoListActivity extends Activity {
	// declare name for save files
	public static final String TODOLISTFILE = "todoList.sav";
	public static final String ARCHLISTFILE = "archList.sav";
	// todo_list contains todo items that added
	public static TodoList todo_list;
	// arch_list contains archived items
	public static TodoList arch_list;
	// declare spinner, listview, edittext and buttons
	private Spinner selectCategory;
	private ListView todoListView;
	private EditText addTodoText;
	private Button addTodoButton;
	private Button clearButton;
	private Button showSumBotton;
	// choose_email_list contains chose items
	private ArrayList<TodoItem> choose_email_list;
	// todo list adapter
	private TodoListAdapter todo_adapter;
	// archived list adapter
	private TodoListAdapter arch_adapter;
	// spinner adapter
	private ArrayAdapter spin_adapter;
	private String enteredText;
	// categoryID is for the
	private static long categoryID;
	private static final int ITEM_DELETE = 1;
	private static final int ITEM_ARCHIVE = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_todo_list);

		selectCategory = (Spinner) findViewById(R.id.selectCategory);
		todoListView = (ListView) findViewById(R.id.todoListView);
		addTodoText = (EditText) findViewById(R.id.addTodoText);
		addTodoButton = (Button) findViewById(R.id.addTodoButton);
		clearButton = (Button) findViewById(R.id.clearButton);
		showSumBotton = (Button) findViewById(R.id.showSummaryBotton);
		spin_adapter = ArrayAdapter.createFromResource(this, R.array.list_type,
				android.R.layout.simple_spinner_dropdown_item);
		selectCategory.setAdapter(spin_adapter);
		// change select category
		selectCategory.setOnItemSelectedListener(new change_category_click());
		// show a text about the sum of check status
		showSumBotton.setOnClickListener(new show_sum_click());
		// clear the text that entered
		clearButton.setOnClickListener(new clear_click());
		// add the text that entered to the todo list
		addTodoButton.setOnClickListener(new add_click());

		// on click on item save the select items in the choose_email_list
		todoListView.setOnItemClickListener(new choose_item_click());

		// on long click show the menu for delete/archive the item
		todoListView.setOnCreateContextMenuListener(new menu_long_click());
	}

	// https://github.com/Annieday/lonelyTwitter/blob/master/src/ca/ualberta/cs/lonelytwitter/LonelyTwitterActivity.java
	// 2014-9-23
	@Override
	protected void onStart() {
		super.onStart();
		todo_list = TodoListControl.loadFromFile(this, TODOLISTFILE);
		arch_list = TodoListControl.loadFromFile(this, ARCHLISTFILE);
		choose_email_list = new ArrayList<TodoItem>();
		todo_adapter = new TodoListAdapter(this, todo_list.getList(),
				todo_list, 0);
		arch_adapter = new TodoListAdapter(this, arch_list.getList(),
				arch_list, 1);
	}

	@Override
	protected void onPause() {
		super.onPause();
		enteredText = addTodoText.getText().toString();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		addTodoText.setText(enteredText);
	}

	@Override
	protected void onResume() {
		super.onResume();
		addTodoText.setText(enteredText);
		if (categoryID == 0)
			todoListView.setAdapter(todo_adapter);
		else
			todoListView.setAdapter(arch_adapter);
	}

	private class change_category_click implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// enable to change view between todo list and the archived list
			categoryID = position;
			if (categoryID == 0) {
				addTodoButton.setEnabled(true);
				addTodoText.setEnabled(true);
				clearButton.setEnabled(true);
				choose_email_list = new ArrayList<TodoItem>();
				todoListView.setAdapter(todo_adapter);
			}
			if (categoryID == 1) {
				addTodoButton.setEnabled(false);
				addTodoText.setEnabled(false);
				clearButton.setEnabled(false);
				choose_email_list = new ArrayList<TodoItem>();
				todoListView.setAdapter(arch_adapter);
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			selectCategory.setSelection(0);
		}
	}

	private class show_sum_click implements OnClickListener {
		public void onClick(View v) {
			Intent intent = new Intent(TodoListActivity.this,
					SummaryActivity.class);
			startActivity(intent);
		}
	}

	private class clear_click implements OnClickListener {
		public void onClick(View v) {
			addTodoText.setText("");
		}
	}

	private class add_click implements OnClickListener {
		public void onClick(View v) {
			TodoItem newItem = new TodoItem(addTodoText.getText().toString());
			if (newItem.getItem().trim().length() != 0) {
				todo_list.addItem(newItem);
				addTodoText.setText("");
				update();
			} else {
				noTextEntered();
			}

		}
	}

	private class choose_item_click implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			updateChooseList();
		}
	}

	private class menu_long_click implements OnCreateContextMenuListener {
		public void onCreateContextMenu(ContextMenu menu, View v,
				ContextMenuInfo menuInfo) {
			menu.add(0, ITEM_DELETE, 0, "Delete");
			if (categoryID == 0) {
				menu.add(0, ITEM_ARCHIVE, 0, "Archive");
			} else {
				menu.add(0, ITEM_ARCHIVE, 0, "Unarchive");
			}
		}
	}
	// change the adapter while click on different category
	// update both todo list and arch list at the same time
	private void update() {
		todo_adapter.notifyDataSetChanged();
		arch_adapter.notifyDataSetChanged();
		TodoListControl.saveInFile(this, todo_list, 0);
		TodoListControl.saveInFile(this, arch_list, 1);
	}

	// http://developer.android.com/reference/android/util/SparseBooleanArray.html
	// 2014-9-24
	// get all select item and save in the choose_mail_list
	private void updateChooseList() {
		int size;
		size = currentList().size();
		choose_email_list = new ArrayList<TodoItem>();
		SparseBooleanArray selectId = todoListView.getCheckedItemPositions();
		for (int i = 0; i < size; i++)
			if (selectId.get(i))
				choose_email_list.add(currentList().get(i));
	}

	// when no text is entered, show the text:No text entered
	private void noTextEntered() {
		Toast.makeText(this, "No Text Entered", Toast.LENGTH_SHORT).show();
	}
	// http://www.oschina.net/code/snippet_1014520_27327 2014-9-23
	// handle changes on list while delete/archive items
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		int selectedPosition = info.position;

		switch (item.getItemId()) {
		case ITEM_DELETE:
			// delete
			currentList().removeItem(selectedPosition);
			update();
			updateChooseList();
			break;

		case ITEM_ARCHIVE:
			// archive/unarchive
			if (categoryID == 0) {
				TodoItem newItem = todo_list.get(selectedPosition);
				newItem.setArchive(true);
				arch_list.addItem(newItem);
				todo_list.removeItem(selectedPosition);
			} else {
				TodoItem newItem = arch_list.get(selectedPosition);
				newItem.setArchive(false);
				todo_list.addItem(newItem);
				arch_list.removeItem(selectedPosition);
			}
			update();
			updateChooseList();
			break;

		default:
			break;
		}
		return super.onContextItemSelected(item);
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_todo_list, menu);
		return true;
	}

	// email select items
	public void emailSelectItems(MenuItem menu) {
		if (choose_email_list.size() != 0)
			emailListProcessor(choose_email_list);
		else
			Toast.makeText(this, "No item select", Toast.LENGTH_SHORT).show();
	}

	// email current list
	public void emailCurrentList(MenuItem menu) {
		if (currentList().size() != 0)
			emailListProcessor(currentList().getArrayList());
		else
			Toast.makeText(this, "Current list is empty", Toast.LENGTH_SHORT)
					.show();
	}

	// email all items in both todo and archive list
	public void emailAllItems(MenuItem menu) {
		ArrayList<TodoItem> allItems = new ArrayList<TodoItem>();
		allItems.addAll(todo_list.getArrayList());
		allItems.addAll(arch_list.getArrayList());
		if (allItems.size() != 0)
			emailListProcessor(allItems);
		else
			Toast.makeText(this, "There is no Todo items.", Toast.LENGTH_SHORT)
					.show();
	}

	// http://stackoverflow.com/questions/2197741/how-can-i-send-emails-from-my-android-application
	// 2014-9-24
	private void emailListProcessor(ArrayList<TodoItem> mailList) {
		StringBuffer mailBody = new StringBuffer();
		for (int i = 0; i < mailList.size(); i++)
			mailBody.append(mailList.get(i).getItem() + "\n->Checked: "
					+ mailList.get(i).ifChecked() + "\n->Archived: "
					+ mailList.get(i).ifArchived() + "\n");
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("message/rfc822");
		i.putExtra(Intent.EXTRA_TEXT, mailBody.toString());
		try {
			startActivity(Intent.createChooser(i, "Send mail..."));
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(TodoListActivity.this,
					"There are no email clients installed.", Toast.LENGTH_SHORT)
					.show();
		}
	}

	// return the current list on the listview
	private TodoList currentList() {
		if (categoryID == 0)
			return todo_list;
		else
			return arch_list;
	}

}
