package com.example.timekeeper2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_browse_data.*

/**
 * A simple [Fragment] subclass.
 */
class browseDataFragment : Fragment() , TimeRecyclerAdapter.OnTimeItemClickedListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_browse_data, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val DBOpenHelper = DatabaseHelper(context = requireContext())
        var dataList = DBOpenHelper.readData()

        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        TimeDataRecyclerView.layoutManager = linearLayoutManager
        TimeDataRecyclerView.adapter = context?.let { TimeRecyclerAdapter(it, dataList, this) }
    }

    override fun onItemClick(timeData: timeDataStructure) {
        Toast.makeText(context,timeData.time.toString(),Toast.LENGTH_SHORT).show()
    }

}
