package com.example.eliteworks

import AttendanceDecorator

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


import androidx.fragment.app.Fragment
import com.example.eliteworks.databinding.FragmentAttendanceBinding
import com.prolificinteractive.materialcalendarview.MaterialCalendarView



class AttendanceFragment : BaseFragment() {
    private var _binding: FragmentAttendanceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAttendanceBinding.inflate(inflater, container, false)

        // Initialize MaterialCalendarView from the binding
        val calendarView: MaterialCalendarView = binding.calendarView
        

        // Dummy attendance data
        val attendanceData = mapOf(
            "2023-08-01" to "present",
            "2023-08-02" to "absent",
            "2023-08-03" to "half-leave",
            // Add more dates here
        )

        // Decorators for each attendance type
        val absentDecorator = AttendanceDecorator("absent",R.color.calendar_leave, attendanceData,requireContext())
        val halfLeaveDecorator = AttendanceDecorator("half-leave", R.color.calendar_half_leave, attendanceData,requireContext())

        calendarView.addDecorators(absentDecorator, halfLeaveDecorator)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

