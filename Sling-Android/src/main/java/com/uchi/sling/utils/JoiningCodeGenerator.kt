/*
 * Copyright (c) 2023 Ashish Yadav <mailtoashish693@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.uchi.sling.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.uchi.sling.utils.auth.CODE
import com.uchi.sling.utils.auth.FB_CODES
import com.uchi.sling.utils.auth.FB_UID
import java.util.*
import kotlinx.coroutines.tasks.await

class JoiningCodeGenerator {
    suspend fun generateCode(): String {
        val db = FirebaseFirestore.getInstance()
        val codesRef = db.collection(FB_CODES)

        // Check if a document already exists for the user
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid.toString()
        val query = codesRef.whereEqualTo(FB_UID, uid).limit(1)
        val result = query.get().await()
        val documentExists = !result.isEmpty

        if (documentExists) {
            // Document already exists, return the code from the document
            val code = result.documents[0].getString(CODE)
            return code ?: throw Exception("Code not found in document")
        } else {
            // Document does not exist, generate a new code and create a new document
            val characters = arrayOf(
                "A",
                "B",
                "C",
                "D",
                "E",
                "F",
                "G",
                "H",
                "I",
                "J",
                "K",
                "L",
                "M",
                "N",
                "O",
                "P",
                "Q",
                "R",
                "S",
                "T",
                "U",
                "V",
                "W",
                "X",
                "Y",
                "Z",
                "0",
                "1",
                "2",
                "3",
                "4",
                "5",
                "6",
                "7",
                "8",
                "9"
            )
            val codeLength = 8
            var code = ""
            val random = Random()
            for (i in 0 until codeLength) {
                val index = random.nextInt(characters.size)
                code += characters[index]
            }

            // Check if the generated code already exists in the database, generate a new code if necessary
            var codeExists = true
            while (codeExists) {
                val query_ = codesRef.whereEqualTo(CODE, code).limit(1)
                val result_ = query_.get().await()
                codeExists = !result_.isEmpty
                if (codeExists) {
                    code = generateCode()
                }
            }

            // Create a new document with the user ID and the generated code
            val newDocument = hashMapOf(
                FB_UID to uid, CODE to code
            )
            codesRef.add(newDocument).await()
            return code
        }
    }
}
