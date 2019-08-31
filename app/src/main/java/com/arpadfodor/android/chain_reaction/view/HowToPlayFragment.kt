package com.arpadfodor.android.chain_reaction.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arpadfodor.android.chain_reaction.R
import com.arpadfodor.android.chain_reaction.view.subclass.BaseFragment

class HowToPlayFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_how_to_play, container, false)
        return view
    }

}