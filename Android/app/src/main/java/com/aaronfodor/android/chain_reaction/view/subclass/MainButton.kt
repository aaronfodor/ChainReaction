package com.aaronfodor.android.chain_reaction.view.subclass

import android.content.Context
import android.util.AttributeSet
import com.aaronfodor.android.chain_reaction.R

/**
 * Main Button of the app
 */
class MainButton : BaseButton{

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        this.setTextColor(resources.getColor(R.color.colorMainButtonText))
    }

}