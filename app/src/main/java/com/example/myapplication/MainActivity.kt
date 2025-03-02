package com.example.myapplication

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var searchQuery: EditText
    private lateinit var tagQuery: EditText
    private lateinit var saveButton: Button
    private lateinit var taggedList: ListView
    private lateinit var clearTagsButton: Button

    private val tags = mutableListOf("AndroidFP", "Deitel", "Google", "iPhoneFP", "JavaFP", "JavaHTP")
    private lateinit var adapter: TagAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchQuery = findViewById(R.id.searchQuery)
        tagQuery = findViewById(R.id.tagQuery)
        saveButton = findViewById(R.id.saveButton)
        taggedList = findViewById(R.id.taggedList)
        clearTagsButton = findViewById(R.id.clearTagsButton)

        // Set up custom adapter
        adapter = TagAdapter(tags)
        taggedList.adapter = adapter

        saveButton.setOnClickListener {
            val newTag = tagQuery.text.toString().trim()
            if (newTag.isNotEmpty() && !tags.contains(newTag)) {
                tags.add(newTag)
                adapter.notifyDataSetChanged()
                tagQuery.text.clear()
            } else {
                Toast.makeText(this, "Tag already exists or empty!", Toast.LENGTH_SHORT).show()
            }
        }

        clearTagsButton.setOnClickListener {
            tags.clear()
            adapter.notifyDataSetChanged()
        }
    }

    // Custom Adapter for ListView
    inner class TagAdapter(private val tagList: MutableList<String>) : BaseAdapter() {

        override fun getCount(): Int = tagList.size
        override fun getItem(position: Int): Any = tagList[position]
        override fun getItemId(position: Int): Long = position.toLong()

        override fun getView(position: Int, convertView: android.view.View?, parent: android.view.ViewGroup): android.view.View {
            val view = layoutInflater.inflate(R.layout.list_item_tag, parent, false)
            val tagText = view.findViewById<TextView>(R.id.tagText)
            val editButton = view.findViewById<Button>(R.id.editButton)

            tagText.text = tagList[position]

            editButton.setOnClickListener {
                showEditDialog(position)
            }

            return view
        }
    }

    // Function to show Edit Dialog
    private fun showEditDialog(position: Int) {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Edit Tag")

        val input = EditText(this)
        input.setText(tags[position])
        dialog.setView(input)

        dialog.setPositiveButton("Save") { _, _ ->
            val newTag = input.text.toString().trim()
            if (newTag.isNotEmpty()) {
                tags[position] = newTag
                adapter.notifyDataSetChanged()
            }
        }
        dialog.setNegativeButton("Cancel", null)
        dialog.show()
    }
}
