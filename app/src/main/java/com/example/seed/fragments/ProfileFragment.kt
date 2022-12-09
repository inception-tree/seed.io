package com.example.seed.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.seed.adapter.UserPostAdapter
import com.example.seed.databinding.FragmentProfileBinding
import com.example.seed.viewmodel.PostViewModel
import com.example.seed.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    companion object {
        private const val NOT_LOGGED_IN_USER_ID = "01"
        private const val TAG = "ProfileFragment Tag"
    }

    private lateinit var binding: FragmentProfileBinding
    private lateinit var adapter: UserPostAdapter
    private lateinit var postViewModel: PostViewModel
    private lateinit var userViewModel: UserViewModel
    private var userId : String = NOT_LOGGED_IN_USER_ID
    private val postCollection = FirebaseFirestore.getInstance().collection("posts")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProfileBinding.inflate(
            inflater, container, false
        )
        Log.d(TAG, "on create view")

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        postViewModel = ViewModelProvider(this)[PostViewModel::class.java]
        adapter = UserPostAdapter(
            this,
            FirebaseFirestore.getInstance().collection("posts")
        )
        binding.recyclerPost.adapter = adapter

        if (FirebaseAuth.getInstance().currentUser != null) {
            var user = FirebaseAuth.getInstance().currentUser!!
            userId = user.uid
            Log.d(TAG, userId)
        }
        displayUserPosts()
        UserViewModel.getUserInfo(userId, ::displayUserInfo, ::displayUserNotFound)
        return binding.root
    }

    private fun displayUserInfo(document: DocumentSnapshot) {
        binding.tvUsername.text = document.getString("username")
        binding.tvBio.text = document.getString("bio")
        displayProfileImage(document)
    }

    private fun displayUserNotFound(userId: String) {
        binding.tvUsername.text = "User not found"
        binding.tvBio.text = "User not found"
    }


    private fun displayProfileImage(document: DocumentSnapshot) {
        val profileImgURL = document.getString("imgURL")
        if (profileImgURL.toString() != "") {
            Glide.with(this@ProfileFragment)
                .load(
                    (profileImgURL.toString())
                )
                .into(binding.ivProfile)
        }
    }

    private fun displayUserPosts() {
        // TODO: Fix title
        val queryUserPosts = postCollection.whereEqualTo("authorid", userId)
        adapter.setQuery(queryUserPosts)
    }
//
    fun likePost(postId: String){
        // TODO: replace "01" with actual userId
        postViewModel.likePostByUser(postId, userId);
    }
}