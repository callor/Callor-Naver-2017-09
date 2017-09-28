package com.callor.navermovie

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.TextView

import com.callor.navermovie.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var mainBinding: ActivityMainBinding? = null

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> return@OnNavigationItemSelectedListener true

        // 영화검색 메뉴를 클릭했을때
            R.id.navigation_movie -> {
                val movieIntent = Intent(this@MainActivity, MovieActivity::class.java)
                startActivity(movieIntent)
                return@OnNavigationItemSelectedListener true
            }

            R.id.navigation_notifications -> return@OnNavigationItemSelectedListener true
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainBinding!!.navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }
}
