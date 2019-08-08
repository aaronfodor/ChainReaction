package presenter

import android.graphics.Color
import hu.bme.aut.android.chain_reaction.R

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
     * @param       color                   Color Id of the Player
     * @param       number                  Number of dots of Player
     * @param       isCloseToExplosion      Is the particle close to the explosion
     * @param       gifEnabled              True means gif is needed, false means standing image is required
     * @return      Int                     Drawable resource Id of the image
     */
    fun getDotsImageIdByColorAndNumber(color: Int, number: Int, isCloseToExplosion: Boolean, gifEnabled: Boolean): Int {

        return if(gifEnabled){
            getDotsGifImageId(color, number, isCloseToExplosion)
        } else{
            getDotsStandingImageId(color, number)
        }

    }

    /**
     * Returns the Drawable resource standing image Id based on the input parameters
     *
     * @param       color       Color Id of the Player
     * @param       number      Number of dots of Player
     * @return      Int         Drawable resource Id of the image
     */
    private fun getDotsStandingImageId(color: Int, number: Int): Int {

        val drawableId: Int

        when (color) {
            8 -> drawableId = when (number) {
                1 -> R.drawable.grey_dot1stand
                2 -> R.drawable.grey_dot2stand
                3 -> R.drawable.grey_dot3stand
                else -> { // Note the block
                    R.drawable.nothing
                }

            }
            7 -> drawableId = when (number) {
                1 -> R.drawable.brown_dot1stand
                2 -> R.drawable.brown_dot2stand
                3 -> R.drawable.brown_dot3stand
                else -> { // Note the block
                    R.drawable.nothing
                }

            }
            6 -> drawableId = when (number) {
                1 -> R.drawable.pink_dot1stand
                2 -> R.drawable.pink_dot2stand
                3 -> R.drawable.pink_dot3stand
                else -> { // Note the block
                    R.drawable.nothing
                }

            }
            5 -> drawableId = when (number) {
                1 -> R.drawable.orange_dot1stand
                2 -> R.drawable.orange_dot2stand
                3 -> R.drawable.orange_dot3stand
                else -> { // Note the block
                    R.drawable.nothing
                }

            }
            4 -> drawableId = when (number) {
                1 -> R.drawable.yellow_dot1stand
                2 -> R.drawable.yellow_dot2stand
                3 -> R.drawable.yellow_dot3stand
                else -> { // Note the block
                    R.drawable.nothing
                }

            }
            3 -> drawableId = when (number) {
                1 -> R.drawable.blue_dot1stand
                2 -> R.drawable.blue_dot2stand
                3 -> R.drawable.blue_dot3stand
                else -> { // Note the block
                    R.drawable.nothing
                }

            }
            2 -> drawableId = when (number) {
                1 -> R.drawable.green_dot1stand
                2 -> R.drawable.green_dot2stand
                3 -> R.drawable.green_dot3stand
                else -> { // Note the block
                    R.drawable.nothing
                }

            }
            1 -> drawableId = when (number) {
                1 -> R.drawable.red_dot1stand
                2 -> R.drawable.red_dot2stand
                3 -> R.drawable.red_dot3stand
                else -> { // Note the block
                    R.drawable.nothing
                }
            }
            else -> drawableId = R.drawable.nothing
        }

        return drawableId

    }

    /**
     * Returns the Drawable resource gif image Id based on the input parameters
     *
     * @param       color                   Color Id of the Player
     * @param       number                  Number of dots of Player
     * @param       isCloseToExplosion      Is the particle close to the explosion
     * @return      Int                     Drawable resource Id of the image
     */
    private fun getDotsGifImageId(color: Int, number: Int, isCloseToExplosion: Boolean): Int {

        val drawableId: Int

        if(!isCloseToExplosion){
            drawableId = getDotsStandingImageId(color, number)
            return drawableId
        }

        when (color) {
            8 -> drawableId = when (number) {
                1 -> R.drawable.grey_dot1gif
                2 -> R.drawable.grey_dot2gif
                3 -> R.drawable.grey_dot3gif
                else -> { // Note the block
                    R.drawable.nothing
                }

            }
            7 -> drawableId = when (number) {
                1 -> R.drawable.brown_dot1gif
                2 -> R.drawable.brown_dot2gif
                3 -> R.drawable.brown_dot3gif
                else -> { // Note the block
                    R.drawable.nothing
                }

            }
            6 -> drawableId = when (number) {
                1 -> R.drawable.pink_dot1gif
                2 -> R.drawable.pink_dot2gif
                3 -> R.drawable.pink_dot3gif
                else -> { // Note the block
                    R.drawable.nothing
                }

            }
            5 -> drawableId = when (number) {
                1 -> R.drawable.orange_dot1gif
                2 -> R.drawable.orange_dot2gif
                3 -> R.drawable.orange_dot3gif
                else -> { // Note the block
                    R.drawable.nothing
                }

            }
            4 -> drawableId = when (number) {
                1 -> R.drawable.yellow_dot1gif
                2 -> R.drawable.yellow_dot2gif
                3 -> R.drawable.yellow_dot3gif
                else -> { // Note the block
                    R.drawable.nothing
                }

            }
            3 -> drawableId = when (number) {
                1 -> R.drawable.blue_dot1gif
                2 -> R.drawable.blue_dot2gif
                3 -> R.drawable.blue_dot3gif
                else -> { // Note the block
                    R.drawable.nothing
                }

            }
            2 -> drawableId = when (number) {
                1 -> R.drawable.green_dot1gif
                2 -> R.drawable.green_dot2gif
                3 -> R.drawable.green_dot3gif
                else -> { // Note the block
                    R.drawable.nothing
                }

            }
            1 -> drawableId = when (number) {
                1 -> R.drawable.red_dot1gif
                2 -> R.drawable.red_dot2gif
                3 -> R.drawable.red_dot3gif
                else -> { // Note the block
                    R.drawable.nothing
                }
            }
            else -> drawableId = R.drawable.nothing
        }

        return drawableId

    }

}
