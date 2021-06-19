package com.mptsix.todaydiary.view.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.mptsix.todaydiary.R
import com.mptsix.todaydiary.data.internal.DiaryWriteMode
import com.mptsix.todaydiary.databinding.ActivityMainBinding
import com.mptsix.todaydiary.transition.DisplayTransition
import com.mptsix.todaydiary.view.fragment.*
import com.mptsix.todaydiary.viewmodel.JournalViewModel

class MainActivity : SuperActivity<ActivityMainBinding>(){
    private val journalViewModel: JournalViewModel by viewModels()

    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
    override fun initView() {
        initObserver()

        // Start with Main Fragment
        setupBottomNavigationView()
        commitFragment(MainFragment())
    }

    private fun initObserver() {
        journalViewModel.displayTransition.observe(this) {
            when (it.displayTransition) {
                DisplayTransition.REQUEST_DIARY -> {
                    val bundle: Bundle = Bundle().apply {putLong("timeStamp", it.data as Long)}
                    val diaryFragment: DiaryFragment = DiaryFragment().apply {
                        arguments = bundle
                    }
                    commitFragment(diaryFragment, true)
                }

                DisplayTransition.REQUEST_EDIT -> {
                    val diaryMode: DiaryWriteMode = it.data as DiaryWriteMode
                    val bundle: Bundle = Bundle().apply {
                        putLong("timeStamp", diaryMode.timeStamp)
                        putBoolean("modeType", diaryMode.modeType)
                    }
                    val editDiaryFragment: EditDiaryFragment = EditDiaryFragment().apply {
                        arguments = bundle
                    }
                    commitFragment(editDiaryFragment, true)
                }

                DisplayTransition.REQUEST_USERINFO->{
                    commitFragment(UserInfoFragment(), true)
                }

                DisplayTransition.REQUEST_SEARCH->{
                    commitFragment(UserSearchFragment(), true)
                }
            }
        }
    }

    private fun setupBottomNavigationView() {
        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.mainPage -> {
                    commitFragment(MainFragment())
                    true
                }
                R.id.searchPage -> {
                    commitFragment(UserSearchFragment(), false)
                    true
                }
                R.id.userInfoPage -> {
                    commitFragment(UserInfoFragment(), false)
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    private fun commitFragment(targetFragment: Fragment, replace: Boolean = false) {
        supportFragmentManager.beginTransaction().apply {
            //if (replace) addToBackStack(null)
            replace(R.id.eachPageView, targetFragment)
            if(targetFragment != MainFragment()){
                Log.d("checkBack", supportFragmentManager.backStackEntryCount.toString())
                if(replace){
                    addToBackStack(null)
                }
            }
            commit()
        }
    }

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount  > 0){
            supportFragmentManager.popBackStack()
            return
        }else{
            moveTaskToBack(true);
            finishAndRemoveTask()
        }
        super.onBackPressed()
    }
}