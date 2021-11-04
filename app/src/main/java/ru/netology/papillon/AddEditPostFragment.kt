package ru.netology.papillon

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.papillon.databinding.FragmentAddEditPostBinding
import ru.netology.papillon.utils.AndroidUtils
import ru.netology.papillon.utils.AndroidUtils.hideKeyboard
import ru.netology.papillon.utils.AndroidUtils.showKeyboard
import ru.netology.papillon.utils.StringArg
import ru.netology.papillon.viewmodel.PostViewModel

class AddEditPostFragment : Fragment() {

    companion object {
        var Bundle.textDataContent: String? by StringArg
    }

    private val postViewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment)

    private var fragmentBinding: FragmentAddEditPostBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_edit_post_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save -> {
                fragmentBinding?.let {
                    postViewModel.changeContent(it.etEdit.text.toString())
                    postViewModel.savePost()
                    hideKeyboard(requireView())
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAddEditPostBinding.inflate(inflater, container, false)

        binding.etEdit.requestFocus()
        showKeyboard(binding.root)

        arguments?.textDataContent?.let { binding.etEdit.setText(it) }

//        binding.ok.setOnClickListener {
//            postViewModel.changeContent(binding.etEditContent.text.toString())
//            postViewModel.savePost()
//            hideKeyboard(requireView())
//        }

        postViewModel.postCreated.observe(viewLifecycleOwner) {
            postViewModel.loadPosts()
            findNavController().navigateUp()
        }

        return binding.root
    }
}