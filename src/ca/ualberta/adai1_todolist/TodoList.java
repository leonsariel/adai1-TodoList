package ca.ualberta.adai1_todolist;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TodoList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5369600595466689740L;
	ArrayList<TodoItem> todoList;

	public TodoList() {
		todoList = new ArrayList<TodoItem>();
	}

	public List<TodoItem> getList() {
		return todoList;
	}
	public ArrayList<TodoItem> getArrayList(){
		
		return todoList;
	}
	public Collection<TodoItem> getItems() {
		return todoList;
	}

	public void addItem(TodoItem newItem) {
		this.todoList.add(newItem);
	}

	public void removeItem(TodoItem item) {
		todoList.remove(item);
	}

	public void removeItem(int position) {
		todoList.remove(position);
	}

	public boolean contains(TodoItem item) {
		return todoList.contains(item);
	}

	public int size() {
		return todoList.size();
	}

	public int checkedCount() {
		int c_count = 0;
		for (int i = 0; i < todoList.size(); i++) {
			TodoItem item = todoList.get(i);
			if (item.ifChecked())
				c_count++;
		}
		return c_count;
	}

	public void set(int position, String item) {
		TodoItem todoItem = new TodoItem(item);
		todoList.set(position, todoItem);
	}

	public TodoItem get(int position) {
		return todoList.get(position);
	}
}
