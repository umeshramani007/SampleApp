package com.example.eydstest.presentation.ui.fragment

import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eydstest.data.model.GIFObject
import com.example.eydstest.databinding.FragmentSearchGifBinding
import com.example.eydstest.presentation.ui.adapter.SearchGifAdapter
import com.example.eydstest.presentation.viewmodel.SearchGifViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.util.*

/***
 * Display the Trending GIF and allow user to search GIFs based on entered search query
 */
@AndroidEntryPoint
class SearchGifFragment : Fragment() {

    private lateinit var mBinding: FragmentSearchGifBinding
    private val mViewModel: SearchGifViewModel by viewModels()
    lateinit var mAdapter: SearchGifAdapter
    private var isLoading: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentSearchGifBinding.inflate(inflater)
        addListeners()
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.recylerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            mAdapter = SearchGifAdapter(requireContext()) { gifObject ->
                mViewModel.toggleFavorite(gifObject)
            }
            adapter = mAdapter
        }


        mViewModel.searchList.observe(viewLifecycleOwner, {
            displayListUI(it)
        })

        mViewModel.trendingGifList.observe(viewLifecycleOwner, {
            displayListUI(it)
        })

        mViewModel.error.observe(viewLifecycleOwner, {
            showError(it)
        })

        showProgress(true)
        loadData(true)
    }

    /***
     * Manage the UI on screen as per the list size
     */
    private fun displayListUI(list: List<GIFObject>) {

        mAdapter.setData(list)

        showProgress(false)

        showNoData(list.isEmpty())

        isLoading = false
    }

    private fun addListeners() {
        mBinding.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                showProgress(true)
                loadData(false)
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                query?.let {
                    if (it.length == 0) {
                        displayListUI((mViewModel.trendingGifList.value) ?: ArrayList())
                    }
                }
                return false
            }
        })

        mBinding.recylerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (!isLoading && mAdapter.itemCount > 0) {
                    if (linearLayoutManager != null &&
                        linearLayoutManager.findLastCompletelyVisibleItemPosition() == mAdapter.itemCount - 1
                    ) {

                        //bottom of list!
                        mAdapter.addLoadingView()
                        recyclerView.scrollToPosition(mAdapter.itemCount - 1)
                        loadData(mBinding.searchView.query.toString().isEmpty())
                        isLoading = true
                    }
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
    }

    /***
     * Fetch the new data (trending or search)
     */
    private fun loadData(loadTrending: Boolean) {
        if (loadTrending)
            mViewModel.fetchTrendingGif()
        else
            mViewModel.searchGif(mBinding.searchView.query.toString())

    }

    /***
     * Show loading progress bar while fetching the data fro the first time and
     * on every new search query
     */
    private fun showProgress(loading: Boolean) {
        if (loading) {
            showNoData(false)
            mBinding.progressBar.visibility = View.VISIBLE
            mBinding.recylerView.visibility = View.GONE
        } else {
            mBinding.progressBar.visibility = View.GONE
            mBinding.recylerView.visibility = View.VISIBLE
        }
    }

    /***
     * Show/hide no data view if list is empty
     */
    private fun showNoData(show: Boolean) {
        if (show)
            mBinding.txtNoData.visibility = View.VISIBLE
        else
            mBinding.txtNoData.visibility = View.GONE
    }

    /***
     * show error message on screen if any
     */
    private fun showError(errMessage: String) {
        mBinding.recylerView.visibility = View.GONE
        mBinding.progressBar.visibility = View.GONE
        mBinding.txtNoData.visibility = View.VISIBLE
        mBinding.txtNoData.text = errMessage
    }
}