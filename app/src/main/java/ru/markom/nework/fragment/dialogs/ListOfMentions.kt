@file:OptIn(ExperimentalCoroutinesApi::class)

package ru.markom.nework.fragment.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.markom.nework.R
import ru.markom.nework.adapter.AdapterUsersIdCallback
import ru.markom.nework.adapter.UsersListAdapter
import ru.markom.nework.databinding.FaragmenListOfUsersBinding
import ru.markom.nework.fragment.DisplayingImagesFragment.Companion.textArg
import ru.markom.nework.viewmodel.PostViewModel

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ListOfMentions : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FaragmenListOfUsersBinding.inflate(inflater, container, false)

        val postViewModel: PostViewModel by activityViewModels()


        val adapter = UsersListAdapter(object : AdapterUsersIdCallback {
            override fun goToPageUser(id: Int) {
                val idAuthor = id.toString()
                findNavController().navigate(R.id.userJobFragment,Bundle().apply { textArg = idAuthor })
            }
        })
        binding.list.adapter = adapter

        postViewModel.dataState.observe(viewLifecycleOwner) { state ->
            if (state.loading){
                Snackbar.make(binding.root, R.string.problem_loading, Snackbar.LENGTH_SHORT).show()
            }
        }

        postViewModel.dataUserMentions.observe(viewLifecycleOwner) {
            val newUser = adapter.itemCount < it.size
            adapter.submitList(it) {
                if (newUser) {
                    binding.list.smoothScrollToPosition(0)
                }
            }
        }
        binding.enter.visibility = View.GONE
        return binding.root
    }

}