package com.example.eydstest.presentation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eydstest.data.model.GIFObject
import com.example.eydstest.databinding.FragmentFavoriteGifBinding
import com.example.eydstest.databinding.FragmentSearchGifBinding
import com.example.eydstest.presentation.ui.adapter.FavoriteGifAdapter
import com.example.eydstest.presentation.ui.adapter.SearchGifAdapter
import com.example.eydstest.presentation.viewmodel.FavoriteGifViewModel
import com.example.eydstest.presentation.viewmodel.SearchGifViewModel
import dagger.hilt.android.AndroidEntryPoint

/***
 * Display the list of Favorite Gifs in Grid
 */
@AndroidEntryPoint
class FavoriteGifFragment : Fragment() {

    lateinit var mBinding: FragmentFavoriteGifBinding
    private val mViewModel: FavoriteGifViewModel by viewModels()
    lateinit var mAdapter: FavoriteGifAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentFavoriteGifBinding.inflate(inflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.recylerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            mAdapter = FavoriteGifAdapter(requireContext()) { gifObject ->
                mViewModel.removeFromFavorite(gifObject)
                mAdapter.notifyDataSetChanged()
            }
            adapter = mAdapter
        }

        mViewModel.favoriteGifList.observe(viewLifecycleOwner, {
            displayListUI(it)
        })

    }

    private fun displayListUI(list: List<GIFObject>) {
        mAdapter.setData(list)

        showProgress(false)
        showNoData(list.isEmpty())
    }

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

    private fun showNoData(show: Boolean) {
        if (show)
            mBinding.txtNoData.visibility = View.VISIBLE
        else
            mBinding.txtNoData.visibility = View.GONE
    }
}