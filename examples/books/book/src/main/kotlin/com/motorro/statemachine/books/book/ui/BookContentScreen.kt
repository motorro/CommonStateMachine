/*
 * Copyright 2026 Nikolai Kotchetkov.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.motorro.statemachine.books.book.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.motorro.statemachine.books.book.R
import com.motorro.statemachine.books.book.api.BookGesture
import com.motorro.statemachine.books.book.api.BookUiState
import com.motorro.statemachine.books.domain.entity.Book
import kotlinx.collections.immutable.persistentListOf

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun BookContentScreen(state: BookUiState.Content, onGesture: (BookGesture) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = state.book.title) },
                navigationIcon = {
                    IconButton(onClick = { onGesture(BookGesture.Back) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.content_back)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onGesture(BookGesture.Delete) }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(id = R.string.content_delete_item)
                        )
                    }
                }
            )
        }
    ) { padding ->
        if (state.showDeleteConfirmation) {
            DeleteConfirmationDialog(onGesture)
        }
        BookContent(
            padding = padding,
            book = state.book
        )
    }
}

@Composable
private fun BookContent(padding: PaddingValues, book: Book) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .padding(padding)
            .verticalScroll(scrollState)
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = book.title,
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = book.authors.joinToString(),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = book.content,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun DeleteConfirmationDialog(onGesture: (BookGesture) -> Unit) {
    AlertDialog(
        onDismissRequest = { onGesture(BookGesture.Cancel) },
        title = { Text(text = stringResource(id = R.string.item_delete_confirmation_title)) },
        text = { Text(text = stringResource(id = R.string.item_delete_confirmation_message)) },
        confirmButton = {
            TextButton(onClick = { onGesture(BookGesture.Confirm) }) {
                Text(text = stringResource(id = R.string.item_delete_confirmation_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = { onGesture(BookGesture.Cancel) }) {
                Text(text = stringResource(id = R.string.item_delete_confirmation_cancel))
            }
        }
    )
}

@Preview
@Composable
private fun BookContentScreenPreview() {
    BookContentScreen(
        state = BookUiState.Content(
            book = Book(1, "Title", persistentListOf("Author"), "Content"),
            showDeleteConfirmation = false
        ),
        onGesture = {}
    )
}

@Preview
@Composable
private fun BookContentScreenWithConfirmationPreview() {
    BookContentScreen(
        state = BookUiState.Content(
            book = Book(1, "Title", persistentListOf("Author"), "Content"),
            showDeleteConfirmation = true
        ),
        onGesture = {}
    )
}
