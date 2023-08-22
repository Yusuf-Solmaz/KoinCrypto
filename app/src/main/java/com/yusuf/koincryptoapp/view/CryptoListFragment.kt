package com.yusuf.koincryptoapp.view

import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.yusuf.koincryptoapp.databinding.FragmentCryptoListBinding
import com.yusuf.koincryptoapp.model.Crypto
import com.yusuf.koincryptoapp.viewmodel.CryptoViewModel


class CryptoListFragment : Fragment() , RecyclerViewAdapter.Listener{

    private var _binding: FragmentCryptoListBinding? = null

    private val binding get() = _binding!!

    private var cryptoAdapter = RecyclerViewAdapter(arrayListOf(),this)
    private lateinit var viewModel: CryptoViewModel

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

        viewModel = ViewModelProvider(this).get(CryptoViewModel::class.java)

        observeLiveData()

        viewModel.getDataFromAPI()
    }

    private fun observeLiveData(){
        viewModel.cryptoList.observe(viewLifecycleOwner, Observer {cryptos ->
            cryptos?.let{
                binding.recyclerView.visibility=View.VISIBLE
                cryptoAdapter = RecyclerViewAdapter(ArrayList(cryptos.data ?: arrayListOf()),this@CryptoListFragment)
                binding.recyclerView.adapter=cryptoAdapter
            }
        })

        viewModel.cryptoError.observe(viewLifecycleOwner, Observer { error->

            error?.let {
                if (it.data == true){
                    binding.cryptoErrorText.visibility=View.VISIBLE
                    binding.recyclerView.visibility=View.GONE
                }
                else{
                    binding.cryptoErrorText.visibility=View.GONE
                }
            }

        })

        viewModel.cryptoLoading.observe(viewLifecycleOwner, Observer { loading ->
            loading?.let {
                if (it.data == true){
                    binding.cryptoProgressBar.visibility=View.VISIBLE
                    binding.recyclerView.visibility=View.GONE
                    binding.cryptoErrorText.visibility=View.GONE
                }
                else{
                    binding.cryptoProgressBar.visibility=View.GONE
                }
            }
        })
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(cryptoModel: Crypto) {
        Toast.makeText(requireContext(),"Clicked on: ${cryptoModel.currency}",Toast.LENGTH_LONG).show()
    }
}
