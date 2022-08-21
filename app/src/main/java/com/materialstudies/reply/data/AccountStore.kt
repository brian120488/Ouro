/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.materialstudies.reply.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.materialstudies.reply.R

/**
 * An static data store of [Account]s. This includes both [Account]s owned by the current user and
 * all [Account]s of the current user's contacts.
 */
object AccountStore {

    private val allUserAccounts = mutableListOf(
        Account(
            1L,
            0L,
            "",
            "Hansen",
            "Academic Dishonesty",
            "Academic Dishonesty",
            R.drawable.avatar_10,
            true
        ),
        Account(
            2L,
            0L,
            "",
            "H",
            "Facility Issue",
            "Facility Issue",
            R.drawable.avatar_10
        ),
        Account(
            3L,
            0L,
            "",
            "Hansen",
            "Bullying",
            "Bullying",
            R.drawable.avatar_10
        )
    )

    private val allUserContactAccounts = listOf(
        Account(
            4L,
            1L,
            "Tracy",
            "Alvarez",
            "tracealvie@gmail.com",
            "tracealvie@gravity.com",
            R.drawable.avatar_10
        ),
        Account(
            5L,
            2L,
            "Not reviewed yet.",
            "",
            "atrabucco222@gmail.com",
            "atrabucco222@work.com",
            R.drawable.avatar_10
        ),
        Account(
            6L,
            3L,
            "Reviewed.",
            "",
            "aliconnors@gmail.com",
            "aliconnors@android.com",
            R.drawable.avatar_10
        ),
        Account(
            7L,
            4L,
            "#4",
            "",
            "albertowilliams124@gmail.com",
            "albertowilliams124@chromeos.com",
            R.drawable.avatar_10
        ),
        Account(
            8L,
            5L,
            "Reviewed.",
            "",
            "alen13@gmail.com",
            "alen13@mountainview.gov",
            R.drawable.avatar_10
        ),
        Account(
            9L,
            6L,
            "Currently being reviewed.",
            "",
            "express@google.com",
            "express@gmail.com",
            R.drawable.avatar_10
        ),
        Account(
            10L,
            7L,
            "#2",
            "",
            "sandraadams@gmail.com",
            "sandraadams@textera.com",
            R.drawable.ic_baseline_account_circle_24
        ),
        Account(
            11L,
            8L,
            "#1",
            "",
            "trevorhandsen@gmail.com",
            "trevorhandsen@express.com",
            R.drawable.ic_baseline_account_circle_24
        ),
        Account(
            12L,
            9L,
            "#3",
            "",
            "sholt@gmail.com",
            "sholt@art.com",
            R.drawable.ic_baseline_account_circle_24
        ),
        Account(
            13L,
            10L,
            "#2",
            "",
            "fhawkank@gmail.com",
            "fhawkank@thisisme.com",
            R.drawable.ic_baseline_account_circle_24
        )
    )

    private val _userAccounts: MutableLiveData<List<Account>> = MutableLiveData()
    val userAccounts: LiveData<List<Account>>
        get() = _userAccounts

    init {
        postUpdateUserAccountsList()
    }

    /**
     * Get the current user's default account.
     */
    fun getDefaultUserAccount() = allUserAccounts.first()

    /**
     * Get all [Account]s owned by the current user.
     */
    fun getAllUserAccounts() = allUserAccounts

    /**
     * Whether or not the given [Account.id] uid is an account owned by the current user.
     */
    fun isUserAccount(uid: Long): Boolean = allUserAccounts.any { it.uid == uid }

    fun setCurrentUserAccount(accountId: Long): Boolean {
        var updated = false
        allUserAccounts.forEachIndexed { index, account ->
            val shouldCheck = account.id == accountId
            if (account.isCurrentAccount != shouldCheck) {
                allUserAccounts[index] = account.copy(isCurrentAccount = shouldCheck)
                updated = true
            }
        }
        if (updated) postUpdateUserAccountsList()
        return updated
    }

    private fun postUpdateUserAccountsList() {
        val newList = allUserAccounts.toList()
        _userAccounts.value = newList
    }

    /**
     * Get the contact of the current user with the given [accountId].
     */
    fun getContactAccountById(accountId: Long): Account {
        return allUserContactAccounts.firstOrNull { it.id == accountId } ?: allUserContactAccounts.first()
    }
}