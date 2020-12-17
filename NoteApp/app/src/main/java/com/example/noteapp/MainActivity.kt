package com.example.noteapp

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.icu.text.CaseMap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.view.View.inflate
import android.widget.BaseAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.core.content.res.ColorStateListInflaterCompat.inflate
import androidx.core.content.res.ComplexColorCompat.inflate
import androidx.core.graphics.drawable.DrawableCompat.inflate
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.ticket.view.*

class MainActivity : AppCompatActivity() {
    var listNote = ArrayList<note>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Toast.makeText(this,"on Create",Toast.LENGTH_LONG).show()

        // listNote.add(note(1,"meet prefosor","i will be a good man in future ,that a succsefull man and have a lot of many ,by my god, and goods of parent , realtion good with people"))
        // listNote.add(note(2,"meet doctor","i will be a good man in future ,that a succsefull man and have a lot of many ,by my god, and goods of parent , realtion good with people"))
        // listNote.add(note(3,"meet developer","i will be a good man in future ,that a succsefull man and have a lot of many ,by my god, and goods of parent , realtion good with people"))
      LoadQuery("%")
    }

    override fun onResume() {
        super.onResume()
        Toast.makeText(this,"on Resume",Toast.LENGTH_LONG).show()
        LoadQuery("%")
    }

    override fun onStart() {
        Toast.makeText(this,"on Start",Toast.LENGTH_LONG).show()
        super.onStart()
    }

    override fun onStop() {
        Toast.makeText(this,"on Stop",Toast.LENGTH_LONG).show()
        super.onStop()
    }

    override fun onPause() {
        Toast.makeText(this,"on Pause",Toast.LENGTH_LONG).show()
        super.onPause()
    }

    override fun onDestroy() {
        Toast.makeText(this,"on Destroy",Toast.LENGTH_LONG).show()
        super.onDestroy()
    }

    override fun onRestart() {
        Toast.makeText(this,"on Restart",Toast.LENGTH_LONG).show()
        super.onRestart()
    }

    fun LoadQuery(title:String){

        var dbManager=DbManager(this)
        val projections= arrayOf("ID","Title","Description")
        val selectionArgs= arrayOf(title)
        val cursor=dbManager.Query(projections,"Title like ?",selectionArgs,"Title")
        listNote.clear()
        if(cursor.moveToFirst()){

            do{
                val ID=cursor.getInt(cursor.getColumnIndex("ID"))
                val Title=cursor.getString(cursor.getColumnIndex("Title"))
                val Description=cursor.getString(cursor.getColumnIndex("Description"))

                listNote.add(note(ID,Title,Description))

            }while (cursor.moveToNext())
        }

        var myNotesAdapter= MyNotesAdapter( this,listNote)
        lvNotes.adapter=myNotesAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        val sv = menu!!.findItem(R.id.app_bar_search).actionView as SearchView
        val sm = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        sv.setSearchableInfo(sm.getSearchableInfo(componentName))
        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(applicationContext,query,Toast.LENGTH_LONG).show()
                LoadQuery("%"+ query +"%")

                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item!=null) {
            when (item.itemId) {
                R.id.addNote -> {
                   //go to add page
                    var intent = Intent(this,AddNotes::class.java)
                    startActivity(intent)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    inner class MyNotesAdapter:BaseAdapter{
        var listnoteadapter = ArrayList<note>()
        var context:Context
        constructor(context: Context,listnoteadapter:ArrayList<note>):super(){
            this.listnoteadapter=listnoteadapter
            this.context=context
        }

        override fun getCount(): Int {
            return listnoteadapter.size
        }

        override fun getItem(p0: Int): Any {
            return listnoteadapter[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            var myView = layoutInflater.inflate(R.layout.ticket,null)
            var myNote = listnoteadapter[p0]
            myView.tvTitle.text = myNote.name
            myView.tvDes.text = myNote.des
            myView.ivDelete.setOnClickListener(View.OnClickListener {
                var dbManager=DbManager(this.context)
                val selectionArgs= arrayOf(myNote.Id.toString())
                dbManager.Delete("ID=?",selectionArgs)
                LoadQuery("%")
            })
            myView.ivEdit.setOnClickListener(View.OnClickListener {
                GoToUpdate(myNote)
            })
            return myView
        }
    }
    fun GoToUpdate(Note:note){
        var intent = Intent(this,AddNotes::class.java)
        intent.putExtra("ID",Note.Id)
        intent.putExtra("name",Note.name)
        intent.putExtra("Des",Note.des)
        startActivity(intent)
    }
}