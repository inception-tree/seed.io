package com.example.seed.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.seed.R
import com.example.seed.data.Post
import com.example.seed.databinding.FragmentNewPostBinding
import com.example.seed.viewmodel.PostViewModel
import com.google.firebase.auth.FirebaseAuth

class NewPostFragment : Fragment() {

    private lateinit var binding : FragmentNewPostBinding
    private lateinit var viewModel : PostViewModel
    private var userId : String = "01"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewPostBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[PostViewModel::class.java]

        var categoryAdapter = ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.category_array,
            android.R.layout.simple_spinner_item
        )
        categoryAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        if (FirebaseAuth.getInstance().currentUser != null) {
            var user = FirebaseAuth.getInstance().currentUser!!
            userId = user.uid
        }
        binding.spinnerCategory.adapter = categoryAdapter

        binding.btnCreatePost.setOnClickListener {
            if (validInput()) {
                val newPost = Post(authorid = userId, title = binding.etTitle.text.toString(), body = binding.etContents.text.toString(), tag = binding.spinnerCategory.selectedItemPosition)
                viewModel.createPost(newPost)
                it.findNavController().popBackStack()
            }
        }


        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NewPostFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    private fun validInput() : Boolean {
        return binding.etContents.text.isNotEmpty() && binding.etTitle.text.isNotEmpty()
    }
}