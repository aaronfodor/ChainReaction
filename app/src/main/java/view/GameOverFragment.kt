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
            val playersNumber = bundle.getInt("playersNumber")
            val tvGameOver = view.findViewById<TextView>(hu.bme.aut.android.chainreaction.R.id.tvGameOver)
            var playerText = getString(R.string.player_data_info)

            for (i in 1..playersNumber) {
                val key = (i-1).toString()
                val newText = bundle.getString(key).toString()
                playerText += "\n\n" + newText
            }

            tvGameOver.text = playerText

        }

        return view

    }

}