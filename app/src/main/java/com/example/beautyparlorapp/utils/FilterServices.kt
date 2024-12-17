package com.example.beautyparlorapp.utils
import android.widget.Filter
import com.example.beautyparlorapp.adapter.SearchAdapter
import com.example.beautyparlorapp.data.ServiceModel
import java.util.Locale

class FilterServices(
    private val adapter: SearchAdapter,
    private val filterService:ArrayList<ServiceModel>
): Filter() {
    override fun performFiltering(searchingText: CharSequence?): FilterResults {

        val filterResults=FilterResults()

        if (!searchingText.isNullOrEmpty()){

            val query=searchingText.toString().trim().uppercase(Locale.getDefault()).split(" ")  //ye query ko lowercase me karega and also ye query ko split krega
            val filterServiceList=ArrayList<ServiceModel>()

            for (service in filterService){
                if (query.any {search->
                    service.serviceName?.uppercase(Locale.getDefault())?.contains(search)==true
                            ||service.servicePrice?.uppercase(Locale.getDefault())?.contains(search)==true
                            ||service.serviceCategory?.uppercase(Locale.getDefault())?.contains(search)==true
                    }){
                    filterServiceList.add(service)
                }
            }
            filterResults.apply {
                count=filterServiceList.size
                values=filterServiceList
            }

        }else{
            filterResults.apply {
                count=filterService.size
                values=filterService
            }
        }

        return filterResults
    }

    override fun publishResults(p0: CharSequence?, results: FilterResults?) {
        adapter.differ.submitList(results?.values as ArrayList<ServiceModel>)
    }
}