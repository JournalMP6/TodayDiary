package com.mptsix.todaydiary.view.activity

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import java.lang.RuntimeException
import java.net.ConnectException
import java.net.SocketTimeoutException

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