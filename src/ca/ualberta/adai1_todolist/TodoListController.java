package ca.ualberta.adai1_todolist;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class TodoListController {
	private static final String TODOLIST = "todoList.sav";
	private static final String ARCHLIST = "archList.sav";

	public static TodoList loadFromFile(Context context, String fileName) {
		TodoList list = null;
		try {
			FileInputStream fis = context.openFileInput(fileName);
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
		if(list == null)
			return list = new TodoList();
		return list;
	}

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
