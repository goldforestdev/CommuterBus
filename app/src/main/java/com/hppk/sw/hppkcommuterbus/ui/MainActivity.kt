package com.hppk.sw.hppkcommuterbus.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.*
import com.hppk.sw.hppkcommuterbus.R
import com.hppk.sw.hppkcommuterbus.data.model.BusLine
import com.google.firebase.database.DataSnapshot




class MainActivity : AppCompatActivity() {
    private var busLineDBData : MutableList<BusLine> = mutableListOf()
    private var postListener: ValueEventListener? = null
    private lateinit var postReference: DatabaseReference

    val busLineData: MutableList<BusLine>
        get() = busLineDBData

    companion object {

        private val TAG = this::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        postReference = FirebaseDatabase.getInstance().reference.child("bus-line")

        findViewById<BottomNavigationView>(R.id.bottomNavView).setupWithNavController(
            findNavController(R.id.navHostFragment)
        )
    }

    override fun onStart() {
        super.onStart()

        val postListener = object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {

                Toast.makeText(baseContext, "Failed to load post.",
                    Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.e(TAG,"[Commuter]dataSnapshot : $dataSnapshot")

                for (postSnapshot in dataSnapshot.children) {
                    val busLine = postSnapshot.getValue(BusLine::class.java)
                    busLine?.let {
                        Log.e(TAG,"[Commuter]busLine ID : ${it.nameKr}")
                        busLineDBData.add(busLine)
                    }
                }
            }
        }

        postReference.addValueEventListener(postListener)
        this.postListener = postListener
    }


    override fun onStop() {
        super.onStop()

        postListener?.let {
            postReference.removeEventListener(it)
        }
    }
}
