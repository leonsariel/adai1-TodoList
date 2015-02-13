package ca.ualberta.adai1_todolist;

import java.util.ArrayList;
import java.util.List;

public class TodoList {

	ArrayList<TodoItem> todoList;

	public TodoList() {
		todoList = new ArrayList<TodoItem>();
	}

	// return a List
	public List<TodoItem> getList() {
		return todoList;
	}

	// return an ArrayList
	public ArrayList<TodoItem> getArrayList() {

		return todoList;
	}

	public void addItem(TodoItem newItem) {
		this.todoList.add(newItem);
	}
	
	public void removeItem(int position) {
		todoList.remove(position);
	}
	
	public int size() {
		return todoList.size();
	}

	// count the checked items in the list
	public int checkedCount() {
		int c_count = 0;
		for (int i = 0; i < todoList.size(); i++) {
			TodoItem item = todoList.get(i);
			if (item.ifChecked())
				c_count++;
		}
		return c_count;
	}
	//get the item at current position
	public TodoItem get(int position) {
		return todoList.get(position);
	}
}
