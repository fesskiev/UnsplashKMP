@file:OptIn(ExperimentalMaterial3Api::class)

package com.unsplash.android.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.unsplash.android.theme.AppTheme

@Composable
fun SearchBar(
    modifier: Modifier,
    suggestions: List<String>,
    query: String,
    onQueryChange: (String) -> Unit
) {
    var active by rememberSaveable { mutableStateOf(false) }
    DockedSearchBar(
        modifier = modifier
            .padding(top = 8.dp),
        query = query,
        onQueryChange = onQueryChange,
        onSearch = { active = false },
        active = active && suggestions.isNotEmpty(),
        onActiveChange = { active = it },
        placeholder = { Text("Search photos") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        trailingIcon = {
            Icon(
                modifier = Modifier.clickable {
                    active = !active
                },
                imageVector = Icons.Default.MoreVert,
                contentDescription = null
            )
        },
    ) {
        repeat(suggestions.size) { index ->
            val suggestion = suggestions[index]
            ListItem(
                headlineContent = { Text(suggestion) },
                leadingContent = { Icon(Icons.Filled.Star, contentDescription = null) },
                modifier = Modifier
                    .clickable {
                        onQueryChange(suggestion)
                        active = false
                    }
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }
    }
}

@Preview
@Composable
fun SearchBarPreview() {
    AppTheme {
        var query by remember { mutableStateOf("") }
        SearchBar(
            modifier = Modifier,
            suggestions = listOf("kyiv", "lviv"),
            query = query,
            onQueryChange = {
                query = it
            }
        )
    }
}