package com.muhammetguler.mytakingnoteapp

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_notes.*
import java.lang.Exception

class AddNotesActivity : AppCompatActivity() {
    var ID = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notes)

        try {
            var bundle:Bundle?=intent.extras
            ID=bundle!!.getInt("ID", 0)
            if(ID!=0) {
                editTextTitle.setText(bundle.getString("NAME"))
                editTextDescription.setText((bundle.getString("DESCRP")))
            }
        } catch (e:Exception) {
            e.printStackTrace()
        }
    }

    fun buttonSave(view: View) {
        val db = Database(this)
        var values = ContentValues()
        if(editTextTitle.text.length>0) {
            values.put("Title", editTextTitle.text.toString())
            values.put("Description", editTextDescription.text.toString())
        }
        else {
            editTextTitle.setError("Please add a text")
            editTextTitle.requestFocus()
            return
        }

        if(ID==0) {
            val id = db.store(values)
            if (id>0) {
                Toast.makeText(this,"Note is added", Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this, "Note could not be added", Toast.LENGTH_LONG).show()
            }

        } else {
            val id = db.updateNote(ID, editTextTitle.text.toString(), editTextDescription.text.toString())
            if(id>0) {
                Toast.makeText(this,"Note is added", Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this, "Note could not be added", Toast.LENGTH_LONG).show()
            }
        }
    }
}