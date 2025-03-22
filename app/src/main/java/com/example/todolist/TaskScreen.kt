package com.example.todolist

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.todolist.dataclass.Task
import com.example.todolist.repo.TaskViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(viewModel: TaskViewModel) {

    val tasks by viewModel.allTasks.observeAsState(emptyList())
    var showAddDialog by remember { mutableStateOf(false) }
    var editingTask by remember { mutableStateOf<Task?>(null) }
    val context = LocalContext.current

    val firebaseAnalytics = remember { Firebase.analytics }



    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            TopAppBar(title = { Text(text = "Todos") },
                actions = {
                    IconButton(onClick = {
                        FirebaseCrashlytics.getInstance().log("User triggered a crash")
                        viewModel.triggerDatabaseErr()
                        throw RuntimeException("This is a test crash")

                    }) {
                        Icon(Icons.Default.Warning, contentDescription = "Trigger Crash")
                    }
                })

            LazyColumn(Modifier.weight(1f)) {
                items(tasks) { task ->
                    TaskCard(task, viewModel, onEdit = { editingTask = it }) {message->
                        showToast(context, message)
                    }
                }
            }
        }



        FloatingActionButton(
            onClick = { showAddDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Task")
        }
    }

    if (showAddDialog) {
        TaskDialog(
            title = "Add Task",
            initialText = "",
            onDismiss = { showAddDialog = false },
            onConfirm = { title ->
                if (title.isNotBlank()) {
                    viewModel.insert(Task(title = title))
                    showAddDialog = false
                    showToast(context, "Task added successfully!")
                    trackFirebaseEvent(firebaseAnalytics, "Task_Added", title)


                }
            }
        )
    }

    if (editingTask != null) {
        TaskDialog(
            title = "Edit Task",
            initialText = editingTask!!.title,
            onDismiss = { editingTask = null },
            onConfirm = { newTitle ->
                if (newTitle.isNotBlank()) {
                    viewModel.update(editingTask!!.copy(title = newTitle))
                    editingTask = null
                    showToast(context, "Task updated successfully!")
                    trackFirebaseEvent(firebaseAnalytics, "Task_Edited", newTitle)


                }
            }
        )
    }
}

@Composable
fun TaskCard(task: Task, viewModel: TaskViewModel, onEdit: (Task) -> Unit,showMessage: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { isChecked ->
                    viewModel.update(task.copy(isCompleted = isChecked))
                    showMessage("Task marked as ${if (isChecked) "completed" else "incomplete"}")

                }
            )
            Text(
                text = task.title,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { onEdit(task)
            }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit Task")
            }
            IconButton(onClick = { viewModel.delete(task)
                    showMessage("Task deleted successfully!")
            }
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Task")
            }
        }
    }
}

@Composable
fun TaskDialog(title: String, initialText: String, onDismiss: () -> Unit, onConfirm: (String) -> Unit) {
    var taskText by remember { mutableStateOf(initialText) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            OutlinedTextField(
                value = taskText,
                onValueChange = { taskText = it },
                label = { Text("Task Title") }
            )
        },
        confirmButton = {
            Button(onClick = { onConfirm(taskText) }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )

}
fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
fun trackFirebaseEvent(firebaseAnalytics: FirebaseAnalytics, eventName: String, eventDetail: String) {
    val bundle = Bundle().apply {
        putString("Task_Detail", eventDetail)
    }
    firebaseAnalytics.logEvent(eventName, bundle)
}