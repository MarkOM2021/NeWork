package ru.markom.nework.fragment.wall

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import ru.markom.nework.R
import ru.markom.nework.adapter.myWall.posts.MyPostsAdapter
import ru.markom.nework.adapter.myWall.posts.MyWallOnInteractionListener
import ru.markom.nework.adapter.posts.PagingLoadStateAdapter
import ru.markom.nework.databinding.FragmentMyWallPostBinding
import ru.markom.nework.dto.PostResponse
import ru.markom.nework.fragment.FeedFragment.Companion.intArg
import ru.markom.nework.viewmodel.AuthViewModel
import ru.markom.nework.viewmodel.MyWallPostViewModel

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MyPostFragment : Fragment() {
    private val viewModel: MyWallPostViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()

    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMyWallPostBinding.inflate(inflater, container, false)

        val adapter = MyPostsAdapter(object : MyWallOnInteractionListener {
            override fun onLike(post: PostResponse) {
                if (authViewModel.authenticated) {
                    if (!post.likedByMe) viewModel.likeById(post.id) else viewModel.disLikeById(post.id)
                } else {
                    Snackbar.make(binding.root, R.string.To_continue, Snackbar.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_feedFragment_to_authenticationFragment)
                }
            }

            override fun onEdit(post: PostResponse) {
                findNavController().navigate(
                    R.id.newPostFragment,
                    Bundle().apply { intArg = post.id })
            }

            override fun onRemove(post: PostResponse) {
                viewModel.removeById(post.id)
            }

            override fun onShare(post: PostResponse) {
                if (authViewModel.authenticated) {
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, post.content)
                        type = "text/plain"
                    }

                    val shareIntent =
                        Intent.createChooser(intent, getString(R.string.chooser_share_post))
                    startActivity(shareIntent)
                } else {
                    Snackbar.make(binding.root, R.string.To_continue, Snackbar.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_feedFragment_to_authenticationFragment)
                }
            }

            override fun loadingTheListOfMentioned(post: PostResponse) {
                if (authViewModel.authenticated) {
                    if (post.mentionIds.isEmpty()) {
                        Snackbar.make(binding.root, R.string.mention_anyone, Snackbar.LENGTH_SHORT)
                            .show()
                    } else {
                        viewModel.loadUsersMentions(post.mentionIds)
                        findNavController().navigate(R.id.listOfMentions,Bundle().apply { intArg = 1 })
                    }
                } else {
                    Snackbar.make(binding.root, R.string.To_continue, Snackbar.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_feedFragment_to_authenticationFragment)
                }
            }

        })

        binding.list.adapter = adapter.withLoadStateHeaderAndFooter(
            header = PagingLoadStateAdapter(object : PagingLoadStateAdapter.OnInteractionListener {
                override fun onRetry() {
                    adapter.retry()
                }
            }),
            footer = PagingLoadStateAdapter(object : PagingLoadStateAdapter.OnInteractionListener {
                override fun onRetry() {
                    adapter.retry()
                }
            }),
        )

        authViewModel.data.observeForever {
            binding.nameUser.text = it.nameUser
            Glide.with(this@MyPostFragment)
                .load(it.avatarUser)
                .error(R.drawable.ic_avatar_loading_error_48)
                .placeholder(R.drawable.ic_baseline_cruelty_free_48)
                .timeout(10_000)
                .circleCrop()
                .into(binding.avatar)
        }

        viewModel.dataState.observe(viewLifecycleOwner) { state ->
            binding.progress.isVisible = state.loading
            if (state.loading) {
                Snackbar.make(binding.root, R.string.problem_loading, Snackbar.LENGTH_SHORT).show()
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.data.collectLatest(adapter::submitData)
        }

        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow.collectLatest {
                binding.swiperefresh.isRefreshing = it.refresh is LoadState.Loading
            }
        }

        binding.swiperefresh.setOnRefreshListener {
            adapter.refresh()
        }

        binding.menu.setOnClickListener {
            findNavController().navigate(R.id.myJobFragment2)
        }

        binding.home.setOnClickListener {
            viewModel.removeAll()
            findNavController().navigate(R.id.feedFragment)
        }

        return binding.root
    }
}