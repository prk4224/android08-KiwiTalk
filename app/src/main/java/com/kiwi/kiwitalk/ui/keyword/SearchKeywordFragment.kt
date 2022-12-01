package com.kiwi.kiwitalk.ui.keyword

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.kiwi.kiwitalk.R
import com.kiwi.kiwitalk.databinding.FragmentSearchKeywordBinding
import com.kiwi.kiwitalk.ui.keyword.recyclerview.KeywordCategoryAdapter
import com.kiwi.kiwitalk.ui.keyword.recyclerview.SelectedKeywordAdapter
import dagger.hilt.android.AndroidEntryPoint

/**
 * TODO 완료 버튼이랑 화면 전환
 */

@AndroidEntryPoint
class SearchKeywordFragment : Fragment() {

    private val searchKeywordViewModel: SearchKeywordViewModel by activityViewModels()
    private var _binding: FragmentSearchKeywordBinding? = null
    private val binding get() = _binding!!
    private lateinit var keywordCategoryAdapter: KeywordCategoryAdapter
    private lateinit var selectedKeywordAdapter: SelectedKeywordAdapter
    private val keywordClickListener: (View) -> Unit = { chip ->
        Log.d("KEYWORD_CLICK", "clicked!: ")
        if (selectedKeywordAdapter.itemCount<5 || !(chip as Chip).isChecked){
            searchKeywordViewModel.setSelectedKeywords((chip as Chip).text.toString())
        } else {
            chip.isChecked = false
            Snackbar.make(binding.root,"최대 5개 까지 선택 가능합니다",Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchKeywordBinding.inflate( inflater,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = searchKeywordViewModel

        initToolbar()
        setAdapter()
        setObserve()
        setListener()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchKeywordViewModel.getAllKeywords()
        searchKeywordViewModel.saveBeforeKeywords()
    }

    private fun setObserve(){
        searchKeywordViewModel.allKeywords.observe(viewLifecycleOwner){ list ->
            keywordCategoryAdapter.keywordCategoryList = list
            keywordCategoryAdapter.notifyDataSetChanged()
        }

        searchKeywordViewModel.selectedKeyword.observe(viewLifecycleOwner){ list ->
            selectedKeywordAdapter.submitList(list)
        }
    }

    private fun setAdapter() {
        keywordCategoryAdapter =
            KeywordCategoryAdapter(
                searchKeywordViewModel.allKeywords.value,
                keywordClickListener,
                searchKeywordViewModel.selectedKeyword.value
            )
        binding.rvSearchKeywordKeywordCategoryList.adapter = keywordCategoryAdapter

        selectedKeywordAdapter = SelectedKeywordAdapter()
        binding.rvSearchKeywordSelectedKeywordList.adapter = selectedKeywordAdapter
    }

    private fun initToolbar(){
        binding.tbSearchKeywordTitle.inflateMenu(R.menu.menu_search_keyword_toolbar)
    }

    private fun setListener(){
        with(binding.tbSearchKeywordTitle){
            setNavigationOnClickListener {
                try {
                    val navController = this@SearchKeywordFragment.findNavController()
                    searchKeywordViewModel.SaveSelectedKeywordOrNot(false)
                    navController.popBackStack()
                } catch (e: Exception){
                    Log.d("NAV_CONTROLLER", "setNavigationOnClickListener: $e")
                }
            }

            setOnMenuItemClickListener {
                Log.d("NAV_CONTROLLER", "setOnMenuItemClickListener TRY: ")
                when(it.itemId){
                    R.id.item_select_keyword -> {
                        try {
                            val navController = this@SearchKeywordFragment.findNavController()
                            searchKeywordViewModel.SaveSelectedKeywordOrNot(true)
                            navController.popBackStack()
                        } catch (e: Exception) {
                            Log.d("NAV_CONTROLLER", "setOnMenuItemClickListener ERROR: $e")
                        }
                    }
                }
                return@setOnMenuItemClickListener true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}