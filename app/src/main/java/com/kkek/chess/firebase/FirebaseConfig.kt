package com.kkek.chess.firebase

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FirebaseConfig {
    val database = Firebase.database.reference
    val listener = object : ChildEventListener{
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            println(snapshot.children)
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            println(snapshot.children)
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
            println(snapshot.children)
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            println(snapshot.children)
        }

        override fun onCancelled(error: DatabaseError) {
            println(error)
        }

    }

    fun write(key: String, value: String) {
        database.child(key).setValue(value)
        database.push()
    }


}