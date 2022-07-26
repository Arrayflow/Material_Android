package com.example.materialtest

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.materialtest.databinding.ActivitySecondBinding
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import javax.net.ssl.ManagerFactoryParameters
import kotlin.concurrent.thread

class SecondActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySecondBinding
    val fruits = mutableListOf(Fruit("Apple", R.drawable.apple),
        Fruit("Banana", R.drawable.banana),
        Fruit("Orange", R.drawable.orange),
        Fruit("Watermelon", R.drawable.watermelon),
        Fruit("Pear", R.drawable.pear),
        Fruit("Grape", R.drawable.grape),
        Fruit("Pineapple", R.drawable.pineapple),
        Fruit("Strawberry", R.drawable.strawberry),
        Fruit("Cherry", R.drawable.cherry),
        Fruit("Mango", R.drawable.mango))

    val fruitList = ArrayList<Fruit>()

    private fun initFruits() {
        fruitList.clear()
        repeat(20) {
            val index = (0 until fruits.size).random()
            fruitList.add(fruits[index])
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setSupportActionBar(binding.toolbarSecond)

        initFruits()
        val layoutManager = GridLayoutManager(this, 2)
        binding.recycleView.layoutManager = layoutManager
        val adapter = FruitAdapter(this, fruitList)
        binding.recycleView.adapter = adapter

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_menu)
        }
        //下拉刷新逻辑
        binding.swipeRefresh.setColorSchemeResources(androidx.appcompat.R.color.primary_material_dark)
        binding.swipeRefresh.setOnRefreshListener {
            refreshFruits(adapter)
        }

        //设置navCall默认选中
        binding.navView.setCheckedItem(R.id.navCall)
        //设置Navigation中的具体点击事件
        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navCall -> {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) !=
                            PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), 1)
                    }
                    val intent = Intent(Intent.ACTION_CALL)
                    intent.data = Uri.parse("tel:112")
                    startActivity(intent)
                }
                //任意就退出drawerLayout
                else -> binding.drawerLayout.closeDrawers()
            }
            true
        }
        //设定浮动按钮
        binding.fab.setOnClickListener { view -> //这里要传入一个view，以给Lambda表达式一个正确的操作对象，同时也可以用it代替
            //Snackbar代替Toast，不同的是具有一个动作的最后交互
            //Snackbar中接收的view是fab这个按钮，告诉了Snackbar是基于fab这个view触发的，fab又是CoordinatorLayout的子控件，因此snackbar弹出的窗口也能被监听到
//            Snackbar.make(view, "Are you done all the things?", Snackbar.LENGTH_SHORT).setAction("Yes") {
//                Toast.makeText(this, "Done.", Toast.LENGTH_SHORT).show()
//            }.show()
            view.showSnackbar("Are you done all the things", "Yes") {
                "Done".showToast(this)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> binding.drawerLayout.openDrawer(GravityCompat.START)
        }
        return true
    }
    private fun refreshFruits(adapter: FruitAdapter) {
        thread {
            Thread.sleep(1000)
            runOnUiThread {
                initFruits()
                adapter.notifyDataSetChanged()
                binding.swipeRefresh.isRefreshing = false
            }
        }
    }

}