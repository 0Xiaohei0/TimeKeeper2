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
import android.widget.Toast
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

    lateinit var timerText: Chronometer
    lateinit var startBT: Button
    lateinit var pauseBT: Button
    lateinit var continueBT: Button
    lateinit var resetBT: Button
    var timeWhenStopped: Long = 0
    var timeToSave: Long = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        timerText = view.findViewById(R.id.timerText)
        startBT = view.findViewById(R.id.startBT)
        pauseBT = view.findViewById(R.id.pauseBT)
        continueBT = view.findViewById(R.id.continueBT)
        resetBT = view.findViewById(R.id.resetBT)

        if (savedInstanceState == null || !savedInstanceState.getBoolean("timerStarted")) {//if timer has not started
            startBT.visibility = View.VISIBLE
            resetBT.visibility = View.INVISIBLE
            continueBT.visibility = View.INVISIBLE
            pauseBT.visibility = View.INVISIBLE
        } else {//if timer has started
            startBT.visibility = savedInstanceState.getInt("startBT_visibility")
            resetBT.visibility = savedInstanceState.getInt("resetBT_visibility")
            continueBT.visibility = savedInstanceState.getInt("continueBT_visibility")
            pauseBT.visibility = savedInstanceState.getInt("pauseBT_visibility")
            timeWhenStopped = savedInstanceState.getLong("timeWhenStopped")
            timerText.base = SystemClock.elapsedRealtime() + timeWhenStopped
            if(pauseBT.visibility == View.VISIBLE){//if the timer was running
                continueTimer()//continue timer
            }
        }
//timer running
        //pause timer
        //save button states
        //restore button states
        //continue timer


        //timer not running
            ////save button states
            //restore button states



        startBT.setOnClickListener {

            timerText.base = SystemClock.elapsedRealtime()
            timerText.start()
            startBT.visibility = View.INVISIBLE
            pauseBT.visibility = View.VISIBLE
        }
        pauseBT.setOnClickListener {
            pauseTimer()
        }
        continueBT.setOnClickListener {
            continueTimer()
        }
        resetBT.setOnClickListener {
            timerText.stop()
            saveTimeData()
            timerText.base = SystemClock.elapsedRealtime()
            timeWhenStopped = 0
            resetBT.visibility = View.INVISIBLE
            startBT.visibility = View.VISIBLE
            continueBT.visibility = View.INVISIBLE
        }

    }

    private fun continueTimer() {
        timerText.base = SystemClock.elapsedRealtime() + timeWhenStopped
        timerText.start()
        startBT.visibility = View.INVISIBLE
        pauseBT.visibility = View.VISIBLE
        continueBT.visibility = View.INVISIBLE
        resetBT.visibility = View.INVISIBLE
    }

    private fun pauseTimer() {
        timeWhenStopped = timerText.base - SystemClock.elapsedRealtime()
        timerText.stop()
        startBT.visibility = View.INVISIBLE
        pauseBT.visibility = View.INVISIBLE
        continueBT.visibility = View.VISIBLE
        resetBT.visibility = View.VISIBLE
    }

    private fun saveTimeData() {
        timeToSave = -(timeWhenStopped) / 1000
        if(timeToSave<1){
            Toast.makeText(context, R.string.time_too_short, Toast.LENGTH_LONG).show()
        }else {
            val currentTime = LocalDateTime.now()
            var formatterDate = DateTimeFormatter
                .ofPattern("yyyy-MM-dd")
            var formatterTimeAdd = DateTimeFormatter.ofPattern("HH:mm:ss")

            var timeToSaveString = secToTime(timeToSave.toInt())
            var dateToSave = formatterDate.format(currentTime)
            var addTimeToSave = formatterTimeAdd.format(currentTime)

            val DBOpenHelper = DatabaseHelper(context = requireContext())
            var data =
                timeDataStructure(dateToSave, addTimeToSave, timeToSaveString, timeToSave.toInt())
            DBOpenHelper.insertData(data)
            Toast.makeText(context, R.string.time_saved, Toast.LENGTH_LONG).show()

            DBOpenHelper.close()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (startBT.visibility == View.VISIBLE) {//if the timer has started
            outState.putBoolean("timerStarted", false)
        }else{
            outState.putBoolean("timerStarted", true)
        }
            outState.putInt("startBT_visibility", startBT.visibility)
            outState.putInt("resetBT_visibility", resetBT.visibility)
            outState.putInt("continueBT_visibility", continueBT.visibility)
            outState.putInt("pauseBT_visibility", pauseBT.visibility)
        if(pauseBT.visibility == View.VISIBLE) {//if the timer is running
            pauseTimer()//save the current time
        }
        outState.putLong("timeWhenStopped", timeWhenStopped)
    }

    private fun secToTime(sec: Int): String {
        val seconds = sec % 60
        var minutes = sec / 60
        if (minutes >= 60) {
            val hours = minutes / 60
            minutes %= 60
            if (hours >= 24) {
                val days = hours / 24
                return String.format("%d" +"%02d:%02d:%02d", days, hours % 24, minutes, seconds)
            }
            return String.format("%02d:%02d:%02d", hours, minutes, seconds)
        }
        return String.format("00:%02d:%02d", minutes, seconds)
    }
}

