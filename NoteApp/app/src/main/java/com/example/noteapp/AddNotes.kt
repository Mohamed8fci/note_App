package com.example.noteapp

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.insert
import android.view.View
import android.widget.Toast
import androidx.room.Insert
import kotlinx.android.synthetic.main.activity_add_notes.*
import java.lang.Exception

class AddNotes : AppCompatActivity() {
    var id = 0
    val dbTable = "Notes"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notes)

        try {
            var bundle: Bundle? = intent.extras
            id = bundle!!.getInt("ID", 0)
            if (id != 0) {
                etTitle.setText(bundle?.getString("name"))
                etTitle.setText(bundle?.getString("des"))
            }
        } catch (ex: Exception){}

    }

    fun buAdd(view: View) {
        var dbManager = DbManager(this)

        var values = ContentValues()
        values.put("Title", etTitle.text.toString())
        values.put("Description", etDes.text.toString())

        if(id==0) {
            val ID = dbManager.Insert(values)
            if (ID > 0) {
                Toast.makeText(this, " note is added", Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this, " cannot add note ", Toast.LENGTH_LONG).show()
            }
        }else{
            var selectionArs= arrayOf(id.toString())
            val ID = dbManager.Update(values,"ID=?",selectionArs)
            if (ID > 0) {
                Toast.makeText(this, " note is added", Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this, " cannot add note ", Toast.LENGTH_LONG).show()
            }
        }

    }
}