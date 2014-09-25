package ca.ualberta.adai1_todolist;


public class TodoItem {
	
	String item;
	boolean archived = false;//initial archive status
	boolean checked = false;//initial check status
	
	public TodoItem(String item) {
		this.item = item;
	}

	public String getItem() {
		return item;
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
