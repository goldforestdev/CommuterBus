package com.hppk.sw.hppkcommuterbus.data.repository.source.remote

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.hppk.sw.hppkcommuterbus.data.model.BusLine
import com.hppk.sw.hppkcommuterbus.data.model.BusLineNotFoundExceptions
import com.hppk.sw.hppkcommuterbus.data.model.BusLineSaveFailedExceptions
import com.hppk.sw.hppkcommuterbus.data.repository.source.BusLineDataSource
import io.reactivex.Completable
import io.reactivex.Single

class FirebaseBusLineDao(
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
) : BusLineDataSource {

    override fun getBusLines(): Single<List<BusLine>> = Single.create<List<BusLine>> { emitter ->
        database.reference
            .child("bus-line")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(err: DatabaseError) {
                    emitter.onError(err.toException())
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    dataSnapshot.children.mapNotNull { child ->
                        child.getValue(BusLine::class.java)
                    }.let { busLines ->
                        emitter.onSuccess(busLines)
                    }
                }

            })
    }

    override fun getBusLine(id: String): Single<BusLine> = Single.create<BusLine> { emitter ->
        database.reference
            .child("bus-line")
            .child(id)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(err: DatabaseError) {
                    emitter.onError(err.toException())
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val busLine = dataSnapshot.getValue(BusLine::class.java)
                    if (busLine == null) {
                        emitter.onError(BusLineNotFoundExceptions("BusLine [$id] is not exist"))
                    } else {
                        emitter.onSuccess(busLine)
                    }
                }
            })
    }

    override fun save(busLine: BusLine): Completable = Completable.create { emitter ->
        database.reference
            .child("bus-line")
            .child(busLine.id)
            .setValue(busLine)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    emitter.onComplete()
                } else {
                    emitter.onError(BusLineSaveFailedExceptions("Saving BusLine [${busLine.id}] is failed"))
                }
            }
    }

}