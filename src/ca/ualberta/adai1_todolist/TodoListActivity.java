package ca.ualberta.adai1_todolist;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

import ca.ualberta.adai1_todolist.R;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
	private static final String TODOLIST = "todoList.sav";
	private static final String ARCHLIST = "archList.sav";
	private Spinner selectCategory;
	private ListView todoListView;
	private EditText addTodoText;
	private Button addTodoButton;
	private Button clearButton;
	private Button showSumBotton;
	public static TodoList todo_list;
	public static TodoList arch_list;
	public static ArrayList<TodoItem> choose_email_list;
	private TodoListAdapter todo_adapter;
	private TodoListAdapter arch_adapter;

	private ArrayAdapter spin_adapter;

	private static long categoryID;
	private static final int ITEM_DELETE = 1;
	private static final int ITEM_ARCHIVE = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_todo_list);

		todoListView = (ListView) findViewById(R.id.todoListView);
		selectCategory = (Spinner) findViewById(R.id.selectCategory);
		addTodoText = (EditText) findViewById(R.id.addTodoText);
		addTodoButton = (Button) findViewById(R.id.addTodoButton);
		clearButton = (Button) findViewById(R.id.clearButton);
		showSumBotton = (Button) findViewById(R.id.showSummaryBotton);

		spin_adapter = ArrayAdapter.createFromResource(this, R.array.list_type,
				android.R.layout.simple_spinner_dropdown_item);
		selectCategory.setAdapter(spin_adapter);

		// change select category
		selectCategory.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// enable to change view between todo list and the archived list
				categoryID = position;
				changeList();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				selectCategory.setSelection(0);
			}
		});

		// show a text about the sum of check status
		showSumBotton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(TodoListActivity.this,
						SummaryActivity.class);
				startActivity(intent);
			}
		});
		// clear the text that entered
		clearButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				addTodoText.setText("");
			}
		});
		// add the text that entered to the todo list
		addTodoButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				TodoItem newItem = new TodoItem(addTodoText.getText()
						.toString());
				if (newItem.getItem().trim().length() != 0) {
					todo_list.addItem(newItem);
					addTodoText.setText("");
					update();
				} else {
					noTextEntered();
				}

			}
		});

		// this part is for email!!
		todoListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int size;
				size = currentList().size();
				choose_email_list = new ArrayList<TodoItem>();
				SparseBooleanArray checkedIds = todoListView
						.getCheckedItemPositions();
				for (int i = 0; i < size; i++)
					if (checkedIds.get(i))
						choose_email_list.add(currentList().get(i));

			}
		});

		// on long click show the menu for delete/archive the item
		todoListView
				.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
					public void onCreateContextMenu(ContextMenu menu, View v,
							ContextMenuInfo menuInfo) {
						menu.add(0, ITEM_DELETE, 0, "Delete");
						if (categoryID == 0) {
							menu.add(0, ITEM_ARCHIVE, 0, "Archive");
						} else {
							menu.add(0, ITEM_ARCHIVE, 0, "Unarchive");
						}
					}
				});
	}

	// From lonelyTwitter
	@Override
	protected void onStart() {
		super.onStart();
		todo_list = loadFromFile(TODOLIST);
		arch_list = loadFromFile(ARCHLIST);
		if (todo_list == null)
			todo_list = new TodoList();
		if (arch_list == null)
			arch_list = new TodoList();
		todo_adapter = new TodoListAdapter(this, todo_list.getList(),
				todo_list, 0);
		arch_adapter = new TodoListAdapter(this, arch_list.getList(),
				arch_list, 1);
	}

	// http://www.oschina.net/code/snippet_1014520_27327 2014-9-23
	protected void changeList() {
		if (categoryID == 0) {
			addTodoButton.setEnabled(true);
			addTodoText.setEnabled(true);
			clearButton.setEnabled(true);
			todo_adapter = new TodoListAdapter(this, todo_list.getList(),
					todo_list, 0);
			todoListView.setAdapter(todo_adapter);
		}
		if (categoryID == 1) {
			addTodoButton.setEnabled(false);
			addTodoText.setEnabled(false);
			clearButton.setEnabled(false);
			arch_adapter = new TodoListAdapter(this, arch_list.getList(),
					arch_list, 1);
			todoListView.setAdapter(arch_adapter);
		}
	}

	// http://www.oschina.net/code/snippet_1014520_27327 2014-9-23
	// handle changes on list while delete/archive items
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		int selectedPosition = info.position;

		switch (item.getItemId()) {
		case ITEM_DELETE:
			// delete
			currentList().removeItem(selectedPosition);
			update();
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
			break;

		default:
			break;
		}

		return super.onContextItemSelected(item);

	}

	// show text!
	public void noTextEntered() {
		Toast.makeText(this, "No Text Entered", Toast.LENGTH_SHORT).show();
	}

	public void emailSelectItems(MenuItem menu) {
		emailListProcessor(choose_email_list);
	}

	public void emailCurrentList(MenuItem menu) {
		emailListProcessor(currentList().getArrayList());
	}

	public void emailAllItems(MenuItem menu) {
		ArrayList<TodoItem> allItems = new ArrayList<TodoItem>();
		allItems.addAll(todo_list.getArrayList());
		allItems.addAll(arch_list.getArrayList());
		emailListProcessor(allItems);
	}

	// http://stackoverflow.com/questions/2197741/how-can-i-send-emails-from-my-android-application
	// 2014-9-24
	public void emailListProcessor(ArrayList<TodoItem> mailList) {
		StringBuffer mailBody = new StringBuffer();
		for (int i = 0; i < mailList.size(); i++)
			mailBody.append(mailList.get(i).getItem() + "\n");
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("message/rfc822");
		i.putExtra(Intent.EXTRA_EMAIL, new String[] { "recipient@example.com" });
		i.putExtra(Intent.EXTRA_SUBJECT, "Select Todo List Items");
		i.putExtra(Intent.EXTRA_TEXT, mailBody.toString());
		try {
			startActivity(Intent.createChooser(i, "Send mail..."));
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(TodoListActivity.this,
					"There are no email clients installed.", Toast.LENGTH_SHORT)
					.show();
		}
	}

	public TodoList currentList() {
		if (categoryID == 0)
			return todo_list;
		else
			return arch_list;
	}

	// change the adapter while click on different category
	// update both todo list and arch list at the same time
	public void update() {
		todo_adapter.notifyDataSetChanged();
		arch_adapter.notifyDataSetChanged();
		TodoListSave.saveInFile(this, todo_list, 0);
		TodoListSave.saveInFile(this, arch_list, 1);
	}

	private TodoList loadFromFile(String fileName) {
		TodoList list = null;
		try {
			FileInputStream fis = openFileInput(fileName);
			BufferedReader in = new BufferedReader(new InputStreamReader(fis));
			Gson gson = new Gson();
			// Following line from
			// https://sites.google.com/site/gson/gson-user-guide 2014-09-23
			Type listType = new TypeToken<TodoList>() {
			}.getType();
			list = gson.fromJson(in, listType);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_todo_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		return super.onOptionsItemSelected(item);
	}
}
