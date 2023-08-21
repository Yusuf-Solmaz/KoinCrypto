package com.yusuf.koincryptoapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.yusuf.koincryptoapp.R
import com.yusuf.koincryptoapp.databinding.FragmentCryptoListBinding
import com.yusuf.koincryptoapp.model.Crypto
import com.yusuf.koincryptoapp.service.CryptoAPI
import com.yusuf.koincryptoapp.util.API.BASE_URL
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CryptoListFragment : Fragment() , RecyclerViewAdapter.Listener{

    private var _binding: FragmentCryptoListBinding? = null

    private val binding get() = _binding!!

    private var cryptoList: ArrayList<Crypto>? = null
    private var job: Job? = null

    private var  recyclerViewAdapter: RecyclerViewAdapter? = null

    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("Error: ${throwable.localizedMessage}" )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCryptoListBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.layoutManager = layoutManager

        loadData()
    }

    private fun loadData(){

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CryptoAPI::class.java)

        job = CoroutineScope(Dispatchers.IO +exceptionHandler).launch {
            val response = retrofit.getCryptos()

            withContext(Dispatchers.Main){
                if (response.isSuccessful){
                    response.body()?.let {
                            cryptoList = ArrayList(it)
                        cryptoList?.let {
                            recyclerViewAdapter = RecyclerViewAdapter(it,this@CryptoListFragment)
                            binding.recyclerView.adapter = recyclerViewAdapter
                        }

                        }


                    }
                }
            }
        }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        job?.cancel()
    }

    override fun onItemClick(cryptoModel: Crypto) {
        Toast.makeText(requireContext(),"Clicked on: ${cryptoModel.currency}",Toast.LENGTH_LONG).show()
    }
}
