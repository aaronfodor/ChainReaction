package com.aaronfodor.android.chain_reaction.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aaronfodor.android.chain_reaction.BuildConfig
import com.aaronfodor.android.chain_reaction.R
import com.aaronfodor.android.chain_reaction.view.subclass.BaseFragment
import kotlinx.android.synthetic.main.fragment_about.view.*


class AboutFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_about, container, false)
        view.textViewAbout.text = getString(R.string.about, BuildConfig.VERSION_NAME)

        view.buttonMoreFromDeveloper.text = getString(R.string.more_from_developer)
        view.buttonMoreFromDeveloper.setOnClickListener {
            val defaultBrowser = Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_APP_BROWSER)
            val uri = Uri.parse(getString(R.string.storePage))
            defaultBrowser.data = uri
            startActivity(defaultBrowser)
        }
        return view
    }

}