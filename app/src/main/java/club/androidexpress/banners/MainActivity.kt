package club.androidexpress.banners

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlin.math.abs

class MainActivity : AppCompatActivity() {
   private lateinit var viewPager: ViewPager2
   private lateinit var tabLayout: TabLayout
   private lateinit var  banners: Array<String>
   private lateinit var adapter: BannerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        supportFragmentManager.beginTransaction()
//            .add(R.id.container, BannerFragment.getInstance(0))
//            .commit()

        viewPager = findViewById(R.id.view_pager)
        tabLayout = findViewById(R.id.tab_layout)

        banners = resources.getStringArray(R.array.banners)
        adapter = BannerAdapter(this, banners.size)

        viewPager.adapter = adapter

        viewPager.registerOnPageChangeCallback(viewPagerCallback)

        //region orientação
        // viewPager.orientation = ViewPager2.ORIENTATION_VERTICAL
        //endregion

        // TabLayout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = banners[position].split(".")[0]
        }.attach()

        //region RTL Suporte
//        viewPager.layoutDirection = ViewPager2.LAYOUT_DIRECTION_RTL
//        tabLayout.layoutDirection = View.LAYOUT_DIRECTION_RTL
        //endregion

        //region Animação
        viewPager.apply {
            offscreenPageLimit = 3
            clipToPadding = false
            clipChildren = false
        }

        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer { page, position ->
            val ref: Float = 1 - abs(position)
            page.scaleY = (0.85f + ref * 0.15f)
        }
        compositePageTransformer.addTransformer(MarginPageTransformer(40))

        viewPager.setPageTransformer(compositePageTransformer)
        //endregion
    }



    override fun onDestroy() {
        super.onDestroy()
        viewPager.unregisterOnPageChangeCallback(viewPagerCallback)
    }

    private val handler = Handler()
    private val runnable = Runnable {
        viewPager.currentItem = viewPager.currentItem + 1
    }

    private val viewPagerCallback get() = object: ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            if ((banners.size - 2) == position) {
                banners += resources.getStringArray(R.array.banners)
                adapter.itemsCount = banners.size
                adapter.notifyDataSetChanged()
            }
            handler.removeCallbacks(runnable)
            handler.postDelayed(runnable, 1500)
        }
    }

   inner class BannerAdapter(activity: AppCompatActivity, var itemsCount: Int): FragmentStateAdapter(activity) {
        override fun getItemCount(): Int = itemsCount

        override fun createFragment(position: Int): Fragment {
            val filename: String = banners[position]
            return BannerFragment.getInstance(filename)
        }
    }
}