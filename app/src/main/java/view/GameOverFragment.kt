package view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import hu.bme.aut.android.chainreaction.R

class GameOverFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_gameover, container, false)

        val bundle = this.arguments
        if(bundle != null){
            val avgWaiting = bundle.getInt("avgWaitingTime")
            var tvGameOver = view.findViewById<TextView>(hu.bme.aut.android.chainreaction.R.id.tvGameOver)
            tvGameOver.text = getString(R.string.response_time, avgWaiting)
        }

        return view

    }

}