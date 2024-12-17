package com.example.beautyparlorapp.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.beautyparlorapp.R
import com.example.beautyparlorapp.adapter.SearchAdapter
import com.example.beautyparlorapp.adapter.ServiceAdapter
import com.example.beautyparlorapp.data.ServiceModel
import com.example.beautyparlorapp.databinding.FragmentSearchBinding
import com.example.beautyparlorapp.utils.Constant
import com.google.firebase.firestore.FirebaseFirestore


class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchList:ArrayList<ServiceModel>
    private val adapter by lazy {
        SearchAdapter(onProductClick = ::onCategoryClick, context = requireContext(), list = searchList)
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRvService()
        fetchServiceList()
        setUpSearchLayout()
    }

    private fun setRvService() {
        searchList=ArrayList()
        binding.searchRv.adapter=adapter
        binding.searchRv.layoutManager=LinearLayoutManager(requireContext())
        binding.searchRv.setHasFixedSize(true)
    }

    private fun fetchServiceList() {

        val db=FirebaseFirestore.getInstance().collection("Parlor Data Info")

        db.addSnapshotListener { snapshot, error ->

            if (snapshot!=null){
                val dataList= mutableListOf<ServiceModel>()

                for (document in snapshot.documents){
                    val data=document.toObject(ServiceModel::class.java)
                    dataList.add(data!!)
                }
                adapter.differ.submitList(dataList)
                adapter.originalList=dataList as ArrayList<ServiceModel>
            }

        }
    }
    private fun setUpSearchLayout() {
        binding.etSearch.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val query=char.toString().trim()  //char ko str me convert krdega query ke liye
                adapter.filter.filter(query)
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun onCategoryClick(serviceModel: ServiceModel) {
        val bundle=Bundle().apply {
            putString("serviceName",serviceModel.serviceName)
        }
         findNavController().navigate(R.id.action_searchFragment_to_detailServiceFragment,bundle,Constant.slideRightLeftNavOptions)
    }

}