package com.example.timekeeper2

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class DataFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_data, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.data_page_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if(id == R.id.edit_data){
            view?.let { Navigation.findNavController(it).navigate((R.id.NavigateToBrowseDataFragment)) }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //find components
        val totalTimeTodayDisplayed : TextView = view.findViewById(R.id.TotalTimeTodayDisplayed)
        val totalTimeDisplayed : TextView = view.findViewById(R.id.TotalTimeDisplayed)
        val totalTimeThisWeekDisplayed : TextView = view.findViewById(R.id.totalTimeThisWeekDisplayed)

        val DBOpenHelper = DatabaseHelper(context = requireContext())
        var dataList = DBOpenHelper.readData()

        //get current date
        val currentTime = LocalDateTime.now()
        var formatter = DateTimeFormatter
            .ofPattern("yyyy-MM-dd")
        var currentDate = formatter.format(currentTime)
        //get date from past week
        val dateFromPastWeek = ArrayList<String>()
        for(i in 0 until 7){
            dateFromPastWeek.add(getCalculatedDate("yyyy-MM-dd", -i))
        }

        //calculate current time today
        var totalTimeToday : Int =0
        var totalTime : Int =0
        for (i in 0 until dataList.size){
            totalTime+= dataList[i].time
            if(currentDate== dataList[i].date)
            totalTimeToday+= dataList[i].time
        }

        //calculate time this week (this is a horrible approach >_<)
        var totalTimeThisWeek : Int =0
        for (i in 0 until dataList.size){
            for (j in 0 until dateFromPastWeek.size) {
                if (dateFromPastWeek[j] == dataList[i].date)
                    totalTimeThisWeek += dataList[i].time
            }
        }

        //set text
        var totalTimeTodayString : String = secToTime(totalTimeToday)
        var totalTimeString : String = secToTime(totalTime)
        var totalTimeThisWeekString : String = secToTime(totalTimeThisWeek)

        totalTimeTodayDisplayed.text = totalTimeTodayString
        totalTimeDisplayed.text = totalTimeString
        totalTimeThisWeekDisplayed.text = totalTimeThisWeekString
    }

    @SuppressLint("SimpleDateFormat")
    fun getCalculatedDate(dateFormat: String?, days: Int): String {
        val cal = Calendar.getInstance()
        val s = SimpleDateFormat(dateFormat)
        cal.add(Calendar.DAY_OF_YEAR, days)
        return s.format(Date(cal.timeInMillis))
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
