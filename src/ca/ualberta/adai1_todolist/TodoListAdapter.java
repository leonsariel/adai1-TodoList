package ca.ualberta.adai1_todolist;

import java.util.List;

import ca.ualberta.adai1_todolist.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class TodoListAdapter extends ArrayAdapter<TodoItem> {

	Context context;
	List<TodoItem> list;
	TodoList theList = new TodoList();
	int category;

	public TodoListAdapter(Context context, List<TodoItem> list,
			TodoList theList, int category) {
		super(context, R.layout.todo_list_item, list);
		this.context = context;
		this.list = list;
		this.theList = theList;
		this.category = category;
	}

	// http://blog.csdn.net/notice520/article/details/7266896 2014-9-12
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.todo_list_item, null);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.itemText = (TextView) convertView
				.findViewById(R.id.todoTextView);
		holder.checkBox = (CheckBox) convertView
				.findViewById(R.id.itemCheckBox);
		convertView.setTag(holder);
		TodoItem item = list.get(position);
		holder.itemText.setText(item.getItem());
		holder.checkBox.setChecked(item.ifChecked());
		holder.checkBox.setOnCheckedChangeListener(new TodoCheckListener(
				position));
		return convertView;
	}

	// change the check boolean status for the item
	private class TodoCheckListener implements OnCheckedChangeListener {

		int position;

		public TodoCheckListener(int position) {
			this.position = position;
		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			list.get(this.position).setCheck(isChecked);
			//save the check status when the check box is clicked
			TodoListController.saveInFile(context, theList, category);
		}
	}

}
