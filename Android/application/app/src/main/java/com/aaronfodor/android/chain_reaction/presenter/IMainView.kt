package com.aaronfodor.android.chain_reaction.presenter

import com.google.firebase.auth.FirebaseUser

interface IMainView {

    /**
     * Shows the Google Play login status
     *
     * @param    user   A nullable FirebaseUser object to show its data - if null, user is not signed in
     */
    fun showLoginStatus(user: FirebaseUser?)

}