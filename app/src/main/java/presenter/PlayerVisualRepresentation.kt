package presenter

import android.graphics.Color

/**
 * Player Id and Color connection
 */
object PlayerVisualRepresentation {

    /**
     * Returns the Color of the given Player
     *
     * @param       Id      Id of the Player
     * @return      Int     Color Id of the Player
     */
    fun getColorById(Id: Int): Int {

        return when (Id) {
            1 -> Color.RED
            2 -> Color.GREEN
            3 -> Color.BLUE
            4 -> Color.YELLOW
            5 -> Color.rgb(255, 165, 0)
            6 -> Color.MAGENTA
            7 -> Color.rgb(210, 180, 140)
            8 -> Color.LTGRAY
            else -> 0
        }

    }

    /**
     * Returns the Drawable resource image Id based on the input parameters
     *
     * @param       color       Color Id of the Player
     * @param       number      Number of dots of Player
     * @return      Int         Drawable resource Id of the image
     */
    fun getDotsImageIdByColorAndNumber(color: Int, number: Int): Int {

        val drawableId: Int

        when (color) {
            8 -> drawableId = when (number) {
                1 -> hu.bme.aut.android.chainreaction.R.drawable.grey_dot1
                2 -> hu.bme.aut.android.chainreaction.R.drawable.grey_dot2
                3 -> hu.bme.aut.android.chainreaction.R.drawable.grey_dot3
                else -> { // Note the block
                    hu.bme.aut.android.chainreaction.R.drawable.nothing
                }

            }
            7 -> drawableId = when (number) {
                1 -> hu.bme.aut.android.chainreaction.R.drawable.brown_dot1
                2 -> hu.bme.aut.android.chainreaction.R.drawable.brown_dot2
                3 -> hu.bme.aut.android.chainreaction.R.drawable.brown_dot3
                else -> { // Note the block
                    hu.bme.aut.android.chainreaction.R.drawable.nothing
                }

            }
            6 -> drawableId = when (number) {
                1 -> hu.bme.aut.android.chainreaction.R.drawable.pink_dot1
                2 -> hu.bme.aut.android.chainreaction.R.drawable.pink_dot2
                3 -> hu.bme.aut.android.chainreaction.R.drawable.pink_dot3
                else -> { // Note the block
                    hu.bme.aut.android.chainreaction.R.drawable.nothing
                }

            }
            5 -> drawableId = when (number) {
                1 -> hu.bme.aut.android.chainreaction.R.drawable.orange_dot1
                2 -> hu.bme.aut.android.chainreaction.R.drawable.orange_dot2
                3 -> hu.bme.aut.android.chainreaction.R.drawable.orange_dot3
                else -> { // Note the block
                    hu.bme.aut.android.chainreaction.R.drawable.nothing
                }

            }
            4 -> drawableId = when (number) {
                1 -> hu.bme.aut.android.chainreaction.R.drawable.yellow_dot1
                2 -> hu.bme.aut.android.chainreaction.R.drawable.yellow_dot2
                3 -> hu.bme.aut.android.chainreaction.R.drawable.yellow_dot3
                else -> { // Note the block
                    hu.bme.aut.android.chainreaction.R.drawable.nothing
                }

            }
            3 -> drawableId = when (number) {
                1 -> hu.bme.aut.android.chainreaction.R.drawable.blue_dot1
                2 -> hu.bme.aut.android.chainreaction.R.drawable.blue_dot2
                3 -> hu.bme.aut.android.chainreaction.R.drawable.blue_dot3
                else -> { // Note the block
                    hu.bme.aut.android.chainreaction.R.drawable.nothing
                }

            }
            2 -> drawableId = when (number) {
                1 -> hu.bme.aut.android.chainreaction.R.drawable.green_dot1
                2 -> hu.bme.aut.android.chainreaction.R.drawable.green_dot2
                3 -> hu.bme.aut.android.chainreaction.R.drawable.green_dot3
                else -> { // Note the block
                    hu.bme.aut.android.chainreaction.R.drawable.nothing
                }

            }
            1 -> drawableId = when (number) {
                1 -> hu.bme.aut.android.chainreaction.R.drawable.red_dot1
                2 -> hu.bme.aut.android.chainreaction.R.drawable.red_dot2
                3 -> hu.bme.aut.android.chainreaction.R.drawable.red_dot3
                else -> { // Note the block
                    hu.bme.aut.android.chainreaction.R.drawable.nothing
                }
            }
            else -> drawableId = hu.bme.aut.android.chainreaction.R.drawable.nothing
        }

        return drawableId

    }

}
