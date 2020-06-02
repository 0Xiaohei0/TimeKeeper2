package com.example.timekeeper2

import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Chronometer
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class TimerFragment : Fragment() {

    override fun onCreateView(


        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val timerText : Chronometer = view.findViewById(R.id.timerText)
        val startBT : Button = view.findViewById(R.id.startBT)
        val pauseBT : Button = view.findViewById(R.id.pauseBT)
        val continueBT : Button = view.findViewById(R.id.continueBT)
        val resetBT : Button = view.findViewById(R.id.resetBT)



        pauseBT.visibility = View.INVISIBLE
        continueBT.visibility = View.INVISIBLE
        resetBT.visibility = View.INVISIBLE

        var timeWhenStopped : Long = 0
        var timeToSave : Long


        startBT.setOnClickListener{ view->
            timerText.base = SystemClock.elapsedRealtime()
            timerText.start()
            startBT.visibility = View.INVISIBLE
            pauseBT.visibility = View.VISIBLE
        }
        pauseBT.setOnClickListener{ view->
            timeWhenStopped = timerText.base - SystemClock.elapsedRealtime()
            timerText.stop()
            pauseBT.visibility = View.INVISIBLE
            continueBT.visibility = View.VISIBLE
            resetBT.visibility = View.VISIBLE
        }
        continueBT.setOnClickListener{ view->
            timerText.base = SystemClock.elapsedRealtime() + timeWhenStopped
            timerText.start()
            pauseBT.visibility = View.VISIBLE
            continueBT.visibility = View.INVISIBLE
            resetBT.visibility = View.INVISIBLE
        }
        resetBT.setOnClickListener{ view->

            val currentTime = LocalDateTime.now()
            var formatter = DateTimeFormatter
                .ofPattern("yyyy-MM-dd")

            timeToSave=-(timeWhenStopped)/1000
            var dateToSave = formatter.format(currentTime)

            val DBOpenHelper = DatabaseHelper(context = requireContext())
            var data = timeDataStructure(dateToSave, timeToSave.toInt())
            DBOpenHelper.insertData(data)
           // Toast.makeText(this, timeToSave.toString(), Toast.LENGTH_LONG).show()

            timerText.base = SystemClock.elapsedRealtime()
            timeWhenStopped= 0
            resetBT.visibility = View.INVISIBLE
            startBT.visibility = View.VISIBLE
            continueBT.visibility = View.INVISIBLE
        }
    }





}
