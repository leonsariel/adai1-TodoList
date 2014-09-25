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
	// declare name for save files
	private static final String TODOLIST = "todoList.sav";
	private static final String ARCHLIST = "archList.sav";
	// declare spinner, listview, edittext and buttons
	private Spinner selectCategory = null;
	private ListView todoListView = null;
	private EditText addTodoText = null;
	private Button addTodoButton = null;
	private Button clearButton = null;
	private Button showSumBotton = null;
	// todo_list contains todo items that added
	public static TodoList todo_list = null;
	// arch_list contains archived items
	public static TodoList arch_list = null;
	// choose_email_list contains choosed items
	private ArrayList<TodoItem> choose_email_list = null;
	// todo list adapter
	private TodoListAdapter todo_adapter = null;
	// archived list adapter
	private TodoListAdapter arch_adapter = null;
	// spinner adapter
	private ArrayAdapter spin_adapter = null;
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

		// save the select items in the choose_email_list
		todoListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int size;
				size = currentList().size();
				choose_email_list = new ArrayList<TodoItem>();
				SparseBooleanArray selectId = todoListView
						.getCheckedItemPositions();
				for (int i = 0; i < size; i++)
					if (selectId.get(i))
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
		choose_email_list = new ArrayList<TodoItem>();
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

	// when no text is entered, show the text:No text entered
	public void noTextEntered() {
		Toast.makeText(this, "No Text Entered", Toast.LENGTH_SHORT).show();
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
	public void emailListProcessor(ArrayList<TodoItem> mailList) {
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
	//return the current list on the listview
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

}
