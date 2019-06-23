package presenter

interface IMainView {

    /**
     * Notifies the user about AI loading result
     *
     * @param    isLoaded   True if loaded, false otherwise
     */
    fun loadedAI(isLoaded: Boolean)

    /**
     * Notifies the user that AI component is not loaded
     */
    fun notLoadedAI()

}