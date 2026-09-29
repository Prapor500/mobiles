package ru.mtuci.drivenext.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import ru.mtuci.drivenext.R
import ru.mtuci.drivenext.databinding.FragmentNoConnectionBinding
import ru.mtuci.drivenext.presentation.MainViewModel

class NoConnectionFragment : Fragment(R.layout.fragment_no_connection) {
    private val viewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentNoConnectionBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNoConnectionBinding.bind(view)

        binding.retryButton.setOnClickListener {
            viewModel.retryConnection()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
                viewModel.retrying.collect { retrying ->
                binding.retryButton.isEnabled = !retrying
                binding.retryButton.text = if (retrying) "" else getString(R.string.retry)
                binding.retryProgress.visibility = if (retrying) View.VISIBLE else View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
