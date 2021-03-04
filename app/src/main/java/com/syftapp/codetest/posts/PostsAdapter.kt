package com.syftapp.codetest.posts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.syftapp.codetest.R
import com.syftapp.codetest.data.model.domain.Post
import kotlinx.android.synthetic.main.view_post_list_item.view.*

//Changed to use a ListAdapter, giving the diffUtill the responsibility to calculate
// which views need recreating or data reset.

class PostsAdapter(
    private val onItemClick: (post: Post) -> Unit,
    private val onPostDelete: (post: Post) -> Unit
) : ListAdapter<Post, PostViewHolder>(diffUtil) {

    var lastList = listOf<Post>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.view_post_list_item, parent, false)

        return PostViewHolder(view, onItemClick, onPostDelete)
    }

    override fun onCurrentListChanged(
        previousList: MutableList<Post>,
        currentList: MutableList<Post>
    ) {
        super.onCurrentListChanged(previousList, currentList)
        lastList = previousList
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(currentList[position], !lastList.contains(currentList[position]))
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
                return true
            }

            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem.title == newItem.title &&
                        oldItem.body == newItem.body
            }
        }
    }
}

class PostViewHolder(
    private val view: View,
    val onItemClick: (post: Post) -> Unit,
    val onPostDelete: (post: Post) -> Unit
) : RecyclerView.ViewHolder(view) {

    fun bind(item: Post, newItem: Boolean) {
        //added the position to indicate that different items are displayed
        view.postTitle.text = "${item.id}.  ${item.title}"
        view.bodyPreview.text = item.body
        view.icDelete.setOnClickListener { onPostDelete(item) }
        view.setOnClickListener { onItemClick(item) }
        if (newItem) {
            itemView.animation =
                AnimationUtils.loadAnimation(itemView.context, R.anim.fade_in_alpha)
        }
    }
}