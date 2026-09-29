package ru.mtuci.drivenext.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import ru.mtuci.drivenext.R
import ru.mtuci.drivenext.databinding.FragmentOnboardingPageBinding

class OnboardingPageFragment : Fragment(R.layout.fragment_onboarding_page) {
    private var _binding: FragmentOnboardingPageBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentOnboardingPageBinding.bind(view)

        val imageName = requireArguments().getString(ARG_IMAGE)!!
        val title = requireArguments().getString(ARG_TITLE)!!
        val description = requireArguments().getString(ARG_DESCRIPTION)!!

        val imageId = resources.getIdentifier(imageName, "drawable", requireContext().packageName)
        binding.pageImage.setImageResource(imageId)
        binding.pageTitle.text = title
        binding.pageDescription.text = description
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        private const val ARG_IMAGE = "image"
        private const val ARG_TITLE = "title"
        private const val ARG_DESCRIPTION = "description"

        fun newInstance(page: OnboardingPage) = OnboardingPageFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_IMAGE, page.imageName)
                putString(ARG_TITLE, page.title)
                putString(ARG_DESCRIPTION, page.description)
            }
        }
    }
}
