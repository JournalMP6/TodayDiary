package com.mptsix.todaydiary.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

// T is ViewBinding
abstract class SuperFragment<T: ViewBinding>: Fragment() {
    private var _binding: T? = null
    protected val binding: T get() = _binding!!

    /**
     * getViewBinding: Get ViewBinding object
     * Called on onCreateView
     */
    abstract fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): T

    /**
     * initView(): Initiate View on onViewCreated
     * Called when onViewCreated called.
     */
    abstract fun initView()

    /**
     * From Below, it notes about Fragment Lifecycle Function
     */

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}