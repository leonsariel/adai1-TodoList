package ca.ualberta.adai1_todolist;

import java.io.Serializable;

public class TodoItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3867211010134267995L;

	String item;
	boolean archived = false;
	boolean checked = false;
	boolean selected = false;
	public TodoItem(String item) {
		this.item = item;
	}

	public String getItem() {
		return item;
	}
	public void setSelect(Boolean isSelect){
		selected = isSelect;
	}
	public boolean ifSelected(){
		return selected;
	}
	public void setCheck(Boolean isChecked) {
		checked = isChecked;
	}

	public boolean ifChecked() {
		return checked;
	}

	public void setArchive(Boolean isArchived) {
		archived = isArchived;
	}

	public boolean ifArchived() {
		return archived;
	}

	@Override
	public String toString() {
		return item;
	}

}
