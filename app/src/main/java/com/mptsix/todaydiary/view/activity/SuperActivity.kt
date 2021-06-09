package com.mptsix.todaydiary.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class SuperActivity<T: ViewBinding>: AppCompatActivity(){
    private var _binding: T? = null
    protected val binding: T get() = _binding!!

    /**
     * getViewBinding: Get ViewBinding object
     * Called on onCreate
     */
    abstract fun getViewBinding(): T

    /**
     * initView(): Initiate View on onViewCreated
     * Called when onCreate called.
     */
    abstract fun initView()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = getViewBinding()
        setContentView(binding.root)
        initView()
    }
}