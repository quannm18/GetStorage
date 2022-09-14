package com.example.myapplication

import android.Manifest
import android.app.Activity
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapter.BigFileItemAdapter
import com.example.myapplication.viewmodel.BigFileViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val rcvMain: RecyclerView by lazy { findViewById<RecyclerView>(R.id.rcvMain) }
    private lateinit var mViewModel: BigFileViewModel
    private var bigFileItemAdapter: BigFileItemAdapter = BigFileItemAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel: BigFileViewModel by viewModels()

        mViewModel = viewModel

        lifecycleScope.launch(IO) {
            mViewModel.listImage.collectLatest {
                bigFileItemAdapter.submitData(it)
            }
        }

        rcvMain.apply {
            adapter = bigFileItemAdapter
            addItemDecoration(
                DividerItemDecoration(
                    this@MainActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
        findViewById<Button>(R.id.button).setOnClickListener {
            requestReadAndWriteStorage()
        }
    }
    fun requestReadAndWriteStorage(){
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ), 999
        )
    }
}