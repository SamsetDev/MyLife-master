package com.sam.mylife.core.model

open class AppUser {

    var name: String = "defaultvalue"
        get() = field                     // getter
        set(value) { field = value }      // setter

    var id: String = ""
        get() = field
        set(value) { field = value }

    var email: String = ""
        get() = field
        set(value) { field = value }

    var photo: String = ""
        get() = field
        set(value) { field = value }

    var token: String = ""
        get() = field
        set(value) { field = value }
}