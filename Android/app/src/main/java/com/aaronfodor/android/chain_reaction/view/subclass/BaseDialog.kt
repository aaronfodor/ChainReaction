package com.aaronfodor.android.chain_reaction.view.subclass

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import com.aaronfodor.android.chain_reaction.R
import com.aaronfodor.android.chain_reaction.model.AudioService
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView

/**
 * Dialog class of the app
 *
 * @param    context            Context of the parent where the dialog is shown
 * @param    title              Title of the dialog
 * @param    description        Description of the dialog
 * @param    image              Image shown on the dialog
 */
class BaseDialog(context: Context, title: String, description: String, image: Drawable) : AlertDialog(context) {

    /**
     * Positive and negative Buttons of the dialog
     */
    var buttonPositive: MainButton? = null
    var buttonNegative: BaseButton? = null

    init {

        this.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        this.window?.attributes?.windowAnimations = R.style.MenuDialogAnimation

        val inflater = getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.dialog_base, null)
        setView(view)

        val imageViewIcon = view.findViewById<ImageView>(R.id.ivMenuDialog)
        imageViewIcon.setImageDrawable(image)

        val textViewTitle = view.findViewById<TextView>(R.id.tvMenuDialogTitle)
        textViewTitle.text = title

        val textViewDescription = view.findViewById<TextView>(R.id.tvMenuDialogDescription)
        textViewDescription.text = description

        buttonPositive = view.findViewById<MainButton?>(R.id.btnPositiveMenuDialog)
        buttonPositive?.setOnClickListener {
            AudioService.soundPositiveButtonClick()
            this.dismiss()
        }

        buttonNegative = view.findViewById<BaseButton?>(R.id.btnNegativeMenuDialog)
        buttonNegative?.setOnClickListener {
            AudioService.soundNegativeButtonClick()
            this.dismiss()
        }

    }

    /**
     * Sets the positive Button on click listener
     *
     * @param    func        Lambda to execute when the positive Button is pressed
     */
    fun setPositiveButton(func: () -> Unit){
        buttonPositive?.setOnClickListener {
            AudioService.soundPositiveButtonClick()
            this.dismiss()
            func()
        }
    }

    /**
     * Sets the negative Button on click listener
     *
     * @param    func        Lambda to execute when the negative Button is pressed
     */
    fun setNegativeButton(func: () -> Unit){
        buttonNegative?.setOnClickListener {
            AudioService.soundNegativeButtonClick()
            this.dismiss()
            func()
        }
    }

    /**
     * Show the dialog - play its Buttons' animations
     */
    override fun show() {
        super.show()
        buttonPositive?.startAppearingAnimation()
        buttonNegative?.startAppearingAnimation()
    }

}