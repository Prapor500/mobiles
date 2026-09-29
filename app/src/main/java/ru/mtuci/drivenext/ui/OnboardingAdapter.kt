package ru.mtuci.drivenext.ui

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class OnboardingAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private val pages = listOf(
        OnboardingPage("onboarding_rent", "Аренда автомобилей", "Открой для себя удобный и доступный способ передвижения."),
        OnboardingPage("onboarding_safe", "Безопасно и удобно", "Арендуй автомобиль и наслаждайся его удобством."),
        OnboardingPage("onboarding_offers", "Лучшие предложения", "Выбирай понравившееся среди сотен доступных автомобилей.")
    )

    override fun getItemCount(): Int = pages.size

    override fun createFragment(position: Int): Fragment =
        OnboardingPageFragment.newInstance(pages[position])
}

data class OnboardingPage(
    val imageName: String,
    val title: String,
    val description: String
)
