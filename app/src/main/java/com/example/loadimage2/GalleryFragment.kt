package com.example.loadimage2

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout


class GalleryFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeLayoutGallery:SwipeRefreshLayout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val galleryAdapter = GalleryAdapter()
        recyclerView = view.findViewById(R.id.recyclerView)
        swipeLayoutGallery= view.findViewById(R.id.swipeLayoutGallery)
        recyclerView.apply {
            adapter = galleryAdapter
            layoutManager = GridLayoutManager(requireContext(),2)
        }
        val galleryViewModel = ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory(requireActivity().application))[GalleryViewModel::class.java]
        galleryViewModel.photoListLive.observe(viewLifecycleOwner) {
            galleryAdapter.submitList(it)
            swipeLayoutGallery.isRefreshing = false
        }
        galleryViewModel.photoListLive.value?:galleryViewModel.fetchData()

        swipeLayoutGallery.setOnRefreshListener {
            galleryViewModel.fetchData()
        }

        val menuHost : MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu,menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.swipeindicator -> {
                        swipeLayoutGallery.isRefreshing = true
                        Handler(Looper.getMainLooper()).postDelayed({ galleryViewModel.fetchData() }, 1000)

                        true
                    }
                    else ->false
                }
            }
        },viewLifecycleOwner,Lifecycle.State.RESUMED)
    }
}