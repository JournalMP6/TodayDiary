package com.mptsix.todaydiary.view.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.viewbinding.ViewBinding
import com.mptsix.todaydiary.viewmodel.LockViewModel
import com.mptsix.todaydiary.viewmodel.ProfileViewModel
import java.lang.RuntimeException
import java.net.ConnectException
import java.net.SocketTimeoutException

abstract class SuperActivity<T: ViewBinding>: AppCompatActivity(){
    private var _binding: T? = null
    protected val binding: T get() = _binding!!
    private val lockViewModel: LockViewModel by viewModels()

    companion object{
        var isLock:Boolean ?= null
    }

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
        initObserve()
    }

    fun initObserve(){
        lockViewModel.auxPwRegisterSucceeds.observe(this){
            isLock = it
        }
    }

    override fun onRestart() {
        Log.d(this::class.java.simpleName, "Restart Called")
        super.onRestart()

        if(isLock!=null && isLock == true){
            val intentStr = intent.getStringExtra("IN")
            intent.removeExtra("IN")
            if(intentStr == null){
                val intent = Intent(this, LockActivity::class.java)
                startActivity(intent)
            }
        }
    }

    fun showDialog(context: Context, title:String, message: String){
        val builder: AlertDialog.Builder? = context.let{
            AlertDialog.Builder(it)
        }
        builder?.setMessage(message)
            ?.setTitle(title)
            ?.setPositiveButton("확인"){
                    _, _ ->
            }

        val dialog: AlertDialog?= builder?.create()
        dialog?.show()
    }
    fun _onFailure(context:Context, t:Throwable):Unit{
        when(t){
            is ConnectException, is SocketTimeoutException -> showDialog(context, "Server Error", "서버 상태가 불안정합니다. \n잠시 후에 다시 시도해주세요.")
            is RuntimeException -> {
                showDialog(context, "접속이 끊어졌습니다.", "로그인 페이지로 이동합니다.")
                // Go back login activity?
            }
            else -> Toast.makeText(context, "알 수 없는 에러가 발생했습니다. 메시지: ${t.message}", Toast.LENGTH_SHORT).show()

        }
    }
}