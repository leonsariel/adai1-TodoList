name:               Anni Dai
ONE Card number:    1264320
Unix id:            adai1
lecture section:    A1
instructor's name:  Abram Hindle
lab section:        D01
=========================================================
This TodoList Application will start by click the app "adai1-TodoList" in the Launcher.
=========================================================
After start the application, the main activity has three button(show summary, add, clear),
one spinner, one gray listview area, one edit text field and the right corner has a main
menu(three dots).
If you want to add a todo item, just type the context in the edit text field and click the
add button on the right. If you want to clear the edit text field click the clear button.
After you click the add button, the item will show on the scrollable listview area with a 
checkbox on the right.
If you want to check an item, just click the checkbox. If you want to uncheck the checked box
again.
If you want to delete or archive/unarchive an item, long click the item and a menu will pop up
with two options. If the item is unarchive, then the options will be Delete and Archive. If the
item is archived, then the options will be Delete and Unarchive.
Once you click on the delete option, the item will be deleted.
If you click on the Archive option,the item will be archived and move to the Archive List, and
will no longer show in the TODO List.
To see the archived list, you can click on the spinner and choose archive list option. Once the
archive list category is been chose, the listview area will switch to show the archived list.
You may not add item to the archive list by enter text and click add button, to add new item you
need to switch to the TODO List by using the spinner.
When click the menu button(three dots on the right corner) you will see three options: 
Email Select Items, Email Current List and Email All Items.
In order to use email function, you need to make sure you have App that is capable for email and
your email account has already setup.
If you want to email some select items, you should first click the items(not on the check box)
to select the items, the selected items will be highlight. Then click menu button and choose
Email Select Items, then the email app will be call and the chosen items will be in the text body
with their status(->checked:true/false ->archived:true/false).
If you want to email TODO List/Archive List, you need to be sure the spinner shows:
TODO List/Archive List, then click the main menu and choose Email Current List. Then the email app 
will be call and the TODO List will be in the text body.
If you want to email all the items(both TODO List and Archive List), choose Email All Items from the
menu. And the list will be shown with each item's status on the text body in the email app that is
called.
If you want to see the summary, press the Show Summary button on the main activity, and the activity
will switch to the summary activity.
========================================================
The summary activity has a text view and a return button.
The text view will show the count of items of both Todo List and Archive List, and each with the count of
checked/unchecked items from the List.
========================================================
If you exit the App, it will save everything you did, and will load when the App start.
========================================================
License
========================================================

 TODO List: record/delete TODO items with checkbox, archive function and can be email
 Copyright (C) 2014  Anni Dai adai1@ualberta.ca

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.