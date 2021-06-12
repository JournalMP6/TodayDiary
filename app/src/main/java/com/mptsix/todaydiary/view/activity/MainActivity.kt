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
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity @Inject constructor() : SuperActivity<ActivityMainBinding>(){
    private val journalViewModel: JournalViewModel by viewModels()

    @Inject
    lateinit var mainFragment: MainFragment

    @Inject
    lateinit var userSearchFragment: UserSearchFragment

    @Inject
    lateinit var userInfoFragment: UserInfoFragment

    @Inject
    lateinit var diaryFragment: DiaryFragment

    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
    override fun initView() {
        initObserver()

        // Start with Main Fragment
        setupBottomNavigationView()
        commitFragment(mainFragment)
    }

    private fun initObserver() {
        journalViewModel.displayTransition.observe(this) {
            when (it.displayTransition) {
                DisplayTransition.REQUEST_DIARY -> {
                    diaryFragment.journalTimeStamp = it.data as Long
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
                    commitFragment(userInfoFragment, true)
                }

                DisplayTransition.REQUEST_SEARCH->{
                    commitFragment(userSearchFragment, true)
                }
            }
        }
    }

    private fun setupBottomNavigationView() {
        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.mainPage -> {
                    commitFragment(mainFragment)
                    true
                }
                R.id.searchPage -> {
                    commitFragment(userSearchFragment, false)
                    true
                }
                R.id.userInfoPage -> {
                    commitFragment(userInfoFragment, false)
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