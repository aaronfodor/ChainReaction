package view.subclass

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import hu.bme.aut.android.chain_reaction.R
import presenter.AudioPresenter
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class BaseDialog(context: Context?, title: String, description: String, image: Drawable) : AlertDialog(context) {

    var buttonPositive: Button? = null
    var buttonNegative: Button? = null

    init {

        this.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        this.window?.attributes?.windowAnimations = R.style.MenuDialogAnimation

        val inflater = getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.dialog_menu, null)
        setView(view)

        val imageViewIcon = view.findViewById<ImageView>(R.id.ivMenuDialog)
        imageViewIcon.setImageDrawable(image)

        val textViewTitle = view.findViewById<TextView>(R.id.tvMenuDialogTitle)
        textViewTitle.text = title

        val textViewDescription = view.findViewById<TextView>(R.id.tvMenuDialogDescription)
        textViewDescription.text = description

        buttonPositive = view.findViewById<Button?>(R.id.btnPositiveMenuDialog)
        buttonPositive?.setOnClickListener {
            AudioPresenter.soundPositiveButtonClick()
            this.dismiss()
        }

        buttonNegative = view.findViewById<Button?>(R.id.btnNegativeMenuDialog)
        buttonNegative?.setOnClickListener {
            AudioPresenter.soundNegativeButtonClick()
            this.dismiss()
        }

    }

    fun setPositiveButton(func: () -> Unit){
        buttonPositive?.setOnClickListener {
            AudioPresenter.soundPositiveButtonClick()
            this.dismiss()
            func()
        }
    }

    fun setNegativeButton(func: () -> Unit){
        buttonNegative?.setOnClickListener {
            AudioPresenter.soundNegativeButtonClick()
            this.dismiss()
            func()
        }
    }

}