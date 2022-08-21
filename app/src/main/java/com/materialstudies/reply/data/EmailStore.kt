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
import androidx.lifecycle.Transformations
import com.materialstudies.reply.R
import com.materialstudies.reply.ui.home.Mailbox

/**
 * A static data store of [Email]s.
 */
object EmailStore {

    private val allEmails = mutableListOf(
        Email(
            0L,
            AccountStore.getContactAccountById(9L),
            listOf(AccountStore.getDefaultUserAccount()),
            "Stealing",
            """
                My computer was stolen.

                I left my M1 Macbook Air at H26 during Computer Science class. If you find it, please leave it at Dorm 427.

                Thank you so much!
            """.trimIndent(),
            isStarred = false
        ),
        Email(
            1L,
            AccountStore.getContactAccountById(6L),
            listOf(AccountStore.getDefaultUserAccount()),
            "Fight on 3rd Floor",
            """
                There was a big fight at the 3rd floor dorms last night.

                There is still blood.
            """.trimIndent()
        ),
        Email(
            2L,
            AccountStore.getContactAccountById(5L),
            listOf(AccountStore.getDefaultUserAccount()),
            "Fountain Destroyed.",
            "2nd floor dorms.",
            listOf(
                EmailAttachment(R.drawable.toilet, "Bridge in Paris"),
            )
        ),
        Email(
            3L,
            AccountStore.getContactAccountById(8L),
            listOf(AccountStore.getDefaultUserAccount()),
            "High school reunion?",
            """
                Hi friends,

                I was at the grocery store on Sunday night.. when I ran into Genie Williams! I almost didn't recognize her afer 20 years!

                Anyway, it turns out she is on the organizing committee for the high school reunion this fall. I don't know if you were planning on going or not, but she could definitely use our help in trying to track down lots of missing alums. If you can make it, we're doing a little phone-tree party at her place next Saturday, hoping that if we can find one person, thee more will...
            """.trimIndent(),
            mailbox = Mailbox.SENT
        ),
        Email(
            5L,
            AccountStore.getContactAccountById(8L),
            listOf(AccountStore.getDefaultUserAccount()),
            "High School Reunions",
            "When does preparation for end-of-the-year events begin? It seems to be starting later than previous years."
        ),
            Email(
                    4L,
                    AccountStore.getContactAccountById(11L),
                    listOf(
                            AccountStore.getDefaultUserAccount(),
                            AccountStore.getContactAccountById(8L),
                            AccountStore.getContactAccountById(5L)
                    ),
                    "Brazil trip",
                    """
                What happened to the all-school field trip to Brazil? I want to go the Brazil! We have already gave the school thousands of dollars. Either give us refunds or fulfill your end!
            """.trimIndent(),
                    isStarred = true,
                    mailbox = Mailbox.SENT
            ),
        Email(
            6L,
            AccountStore.getContactAccountById(10L),
            listOf(AccountStore.getDefaultUserAccount()),
            "Recipe to try",
            "Raspberry Pie: We should make this pie recipe tonight! The filling is " +
                "very quick to put together.",
            mailbox = Mailbox.SENT, isImportant = true
        ),
        Email(
            7L,
            AccountStore.getContactAccountById(9L),
            listOf(AccountStore.getDefaultUserAccount()),
            "Delivered",
            "Your shoes should be waiting for you at home!",
                mailbox = Mailbox.SENT
        ),
        Email(
          8L,
          AccountStore.getContactAccountById(10L),
          listOf(AccountStore.getDefaultUserAccount()),
          "Stinky bathroom.",
          """
              The boys bathroom is abysmal.
          """.trimIndent(),
          mailbox = Mailbox.TRASH,
                isStarred = true
        ),
            Email(
                    12L,
                    AccountStore.getContactAccountById(12L),
                    listOf(AccountStore.getDefaultUserAccount()),
                    "The \"Mod\" Problem",
                    """
              150 Tufts students forced to live in complete isolation in trailers with roach infestation.
          """.trimIndent(),
                    mailbox = Mailbox.TRASH,
                    isStarred = true
            )


    )

    private val _emails: MutableLiveData<List<Email>> = MutableLiveData()

    private val inbox: LiveData<List<Email>>
        get() = Transformations.map(_emails) { emails ->
            emails.filter { it.mailbox == Mailbox.INBOX }
        }

    private val starred: LiveData<List<Email>>
        get() = Transformations.map(_emails) { emails ->
            emails.filter { it.isStarred }
        }

    private val sent: LiveData<List<Email>>
        get() = Transformations.map(_emails) { emails ->
            emails.filter { it.mailbox == Mailbox.SENT }
        }

    private val trash: LiveData<List<Email>>
        get() = Transformations.map(_emails) { emails ->
            emails.filter { it.mailbox == Mailbox.TRASH }
        }

    private val spam: LiveData<List<Email>>
        get() = Transformations.map(_emails) { emails ->
            emails.filter { it.mailbox == Mailbox.SPAM }
        }

    private val drafts: LiveData<List<Email>>
        get() = Transformations.map(_emails) { emails ->
            emails.filter { it.mailbox == Mailbox.DRAFTS }
        }

    init {
        _emails.value = allEmails
    }

    fun getEmails(mailbox: Mailbox): LiveData<List<Email>> {
        return when (mailbox) {
            Mailbox.INBOX -> inbox
            Mailbox.STARRED -> starred
            Mailbox.SENT -> sent
            Mailbox.TRASH -> trash
            Mailbox.SPAM -> spam
            Mailbox.DRAFTS -> drafts
        }
    }

    /**
     * Get an [Email] with the given [id].
     */
    fun get(id: Long): Email? {
        return allEmails.firstOrNull { it.id == id }
    }

    /**
     * Create a new, blank [Email].
     */
    fun create(): Email {
        return Email(
            System.nanoTime(), // Unique ID generation.
            AccountStore.getDefaultUserAccount()
        )
    }

    /**
     * Create a new [Email] that is a reply to the email with the given [replyToId].
     */
    fun createReplyTo(replyToId: Long): Email {
        val replyTo = get(replyToId) ?: return create()
        return Email(
            id = System.nanoTime(),
            sender = replyTo.recipients.firstOrNull() ?: AccountStore.getDefaultUserAccount(),
            recipients = listOf(replyTo.sender) + replyTo.recipients,
            subject = replyTo.subject,
            isStarred = replyTo.isStarred,
            isImportant = replyTo.isImportant
        )
    }

    /**
     * Delete the [Email] with the given [id].
     */
    fun delete(id: Long) {
        update(id) { mailbox = Mailbox.TRASH }
    }

    /**
     * Update the [Email] with the given [id] by applying all mutations from [with].
     */
    fun update(id: Long, with: Email.() -> Unit) {
        allEmails.find { it.id == id }?.let {
            it.with()
            _emails.value = allEmails
        }
    }

    /**
     * Get a list of [EmailFolder]s by which [Email]s can be categorized.
     */
    fun getAllFolders() = listOf(
        "Receipts",
        "Pine Elementary",
        "Taxes",
        "Vacation",
        "Mortgage",
        "Grocery coupons"
    )
}

