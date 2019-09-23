package com.arpadfodor.android.chain_reaction.presenter

import com.google.firebase.auth.FirebaseUser

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

    /**
     * Shows the Google Play login status
     *
     * @param    user   A nullable FirebaseUser object to show its data - if null, user is not signed in
     */
    fun showLoginStatus(user: FirebaseUser?)

}