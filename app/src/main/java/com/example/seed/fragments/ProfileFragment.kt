package com.example.seed.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.seed.R
import com.example.seed.adapter.TimelineAdapter
import com.example.seed.adapter.UserPostAdapter
import com.example.seed.databinding.FragmentProfileBinding
import com.example.seed.databinding.FragmentTimelineBinding
import com.example.seed.viewmodel.PostViewModel
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
    // TODO: Rename and change types of parameters
    private lateinit var binding: FragmentProfileBinding
    private lateinit var adapter: UserPostAdapter
    private lateinit var postViewModel: PostViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProfileBinding.inflate(
            inflater, container, false
        )

        postViewModel = ViewModelProvider(this)[PostViewModel::class.java]
        adapter = UserPostAdapter(
            this,
            FirebaseFirestore.getInstance().collection("posts")
        )

//        val queryByUser = FirebaseFirestore().g
        adapter.setQuery()
        // TODO: Filter query by userid (adapter.setView())

        binding.recyclerPost.adapter = adapter

        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun likePost(postId: String){
        // TODO: replace "01" with actual userId
        postViewModel.likePostByUser(postId, "01");
    }
}