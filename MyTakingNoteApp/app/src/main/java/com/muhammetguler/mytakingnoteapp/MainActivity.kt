package com.muhammetguler.mytakingnoteapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_splash.view.*
import kotlinx.android.synthetic.main.row_list.view.*

class MainActivity : AppCompatActivity() {
    var listNotes=ArrayList<MyNotes>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonAddNote.setOnClickListener{
            val intent = Intent(this,AddNotesActivity::class.java)
            startActivity(intent)
        }
    }

    fun loadData() {
        var DB = Database(this)
        listNotes.clear()
        val crsr = DB.query()
        listNotes = crsr
        var myAdapter = Adapter(this, listNotes)
        listViewNotes.adapter = myAdapter
    }


    override fun onResume() {
        super.onResume()
        loadData()
        var db = Database(this)
        if(db.checkSize()>0) {
            noNotes.visibility = View.GONE
            listViewNotes.visibility = View.VISIBLE
        } else {
            noNotes.visibility = View.VISIBLE
            listViewNotes.visibility = View.GONE
        }
    }


    inner class Adapter : BaseAdapter {


        var listNotes = ArrayList<MyNotes>()
        var context : Context? =null
        constructor(context: Context, listNotes:ArrayList<MyNotes>):super() {
            this.listNotes = listNotes
            this.context = context
        }
        override fun getCount(): Int {
            return listNotes.size
        }

        override fun getItem(position: Int): Any {
            return listNotes[position]

        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var view = layoutInflater.inflate(R.layout.row_list,null)
            var myNote = listNotes[position]
            view.tVTittle.text = myNote.nameOfNote
            view.tVDescription.text = myNote.descrpofNote
            view.imageViewDelete.setOnClickListener {
                var db = Database(context)
                db.deleteNote(myNote.idOfNote!!)
                loadData()
                if(db.checkSize()>0) {
                    noNotes.visibility=View.GONE
                    listViewNotes.visibility = View.VISIBLE
                }
                else {
                    noNotes.visibility = View.VISIBLE
                    listViewNotes.visibility = View.GONE
                }
            }
            view.imageViewEdit.setOnClickListener {
                goUpdate(myNote)

            }
            return view
        }
    }
    fun goUpdate(note:MyNotes) {
        var intent = Intent(this, AddNotesActivity::class.java )
        intent.putExtra("ID", note.idOfNote)
        intent.putExtra("NAME", note.nameOfNote)
        intent.putExtra("DESCRP", note.descrpofNote)
        startActivity(intent)


    }
}