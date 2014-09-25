package ca.ualberta.adai1_todolist;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import android.content.Context;
import com.google.gson.Gson;

public class TodoListSave {
	private static final String TODOLIST = "todoList.sav";
	private static final String ARCHLIST = "archList.sav";
	
	public static void saveInFile(Context context, TodoList list, int category) {
		String fileName = null;
		if (category == 0)
			fileName = TODOLIST;
		else
			fileName = ARCHLIST;
		try {
			FileOutputStream fos = context.openFileOutput(fileName, 0);
			Gson gson = new Gson();
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			gson.toJson(list, osw);
			osw.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
