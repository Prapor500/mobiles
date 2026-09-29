package ru.mtuci.drivenext.ui

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ru.mtuci.drivenext.R
import ru.mtuci.drivenext.databinding.FragmentOnboardingBinding
import ru.mtuci.drivenext.presentation.MainViewModel

class OnboardingFragment : Fragment(R.layout.fragment_onboarding) {
    private val viewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentOnboardingBinding? = null
    private val binding get() = _binding!!

    private val pageCount = 3
    private var currentPage = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentOnboardingBinding.bind(view)

        binding.viewPager.adapter = OnboardingAdapter(this)
        buildIndicators()
        updateControls(0)

        binding.skipButton.setOnClickListener {
            viewModel.completeOnboarding()
        }

        binding.actionButton.setOnClickListener {
            if (currentPage == pageCount - 1) {
                viewModel.completeOnboarding()
            } else {
                binding.viewPager.setCurrentItem(currentPage + 1, true)
            }
        }

        binding.viewPager.registerOnPageChangeCallback(
            object : androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    currentPage = position
                    updateControls(position)
                }
            }
        )
    }

    private fun buildIndicators() {
        binding.indicatorContainer.removeAllViews()
        repeat(pageCount) { position ->
            val indicator = View(requireContext())
            val params = LinearLayout.LayoutParams(
                if (position == 0) dp(28) else dp(12),
                dp(5)
            )
            params.marginEnd = dp(5)
            indicator.layoutParams = params
            indicator.setBackgroundResource(
                if (position == 0) R.drawable.indicator_active else R.drawable.indicator_inactive
            )
            binding.indicatorContainer.addView(indicator)
        }
    }

    private fun updateControls(position: Int) {
        for (i in 0 until binding.indicatorContainer.childCount) {
            val indicator = binding.indicatorContainer.getChildAt(i)
            val params = indicator.layoutParams as LinearLayout.LayoutParams
            params.width = if (i == position) dp(28) else dp(12)
            params.height = dp(5)
            indicator.layoutParams = params
            indicator.setBackgroundResource(
                if (i == position) R.drawable.indicator_active else R.drawable.indicator_inactive
            )
        }

        binding.actionButton.text = if (position == pageCount - 1) {
            getString(R.string.start)
        } else {
            getString(R.string.next)
        }
    }

    private fun dp(value: Int): Int =
        (value * resources.displayMetrics.density).toInt()

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
