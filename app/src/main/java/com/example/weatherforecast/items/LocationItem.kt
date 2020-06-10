package com.example.weatherforecast.items

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.weatherforecast.R
import com.example.weatherforecast.models.LocationModel
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import com.mikepenz.materialdrawer.util.ifNotNull

open class LocationItem : AbstractItem<LocationItem.ViewHolder>() {
    var locationModel: LocationModel? = null

    /** defines the type defining this item. must be unique. preferably an id */
    override val type: Int
        get() = R.id.fastadapter_location_item_id

    /** defines the layout which will be used for this item in the list */
    override val layoutRes: Int
        get() = R.layout.cell_location_item

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    class ViewHolder(view: View) : FastAdapter.ViewHolder<LocationItem>(view) {
        var name: TextView = view.findViewById(R.id.cell_location_name)
        var check: ImageView = view.findViewById(R.id.cell_location_check)

        override fun bindView(item: LocationItem, payloads: MutableList<Any>) {
            val model = item.locationModel
            model.ifNotNull {
                name.text = it.locationObject.city
                check.visibility = if(it.isSelected) View.VISIBLE else View.INVISIBLE

            }
        }

        override fun unbindView(item: LocationItem) {
            name.text = null
            check.visibility = View.INVISIBLE
        }

    }
}