package com.example.android.my_trip

import com.transferwise.sequencelayout.SequenceAdapter
import com.transferwise.sequencelayout.SequenceStep

/**
 * Created by David Rosas on 10/11/2018.
 */
class MyAdapter(private val items: List<MyItem>) : SequenceAdapter<MyAdapter.MyItem>() {

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): MyItem {
        return items[position]
    }

    override fun bindView(sequenceStep: SequenceStep, item: MyItem) {
        with(sequenceStep) {
            setActive(item.isActive)
            setAnchor(item.formattedDate)
            setAnchorTextAppearance(2)
            setTitle(item.title)
            setTitleTextAppearance(2)
            setSubtitle(item.subtitle)
            setSubtitleTextAppearance(1)
        }
    }

    data class MyItem(var isActive: Boolean,
                      var formattedDate: String,
                      var title: String,
                      var subtitle: String)
}