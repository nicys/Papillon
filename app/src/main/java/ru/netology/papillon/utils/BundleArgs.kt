package ru.netology.papillon.utils

import android.os.Bundle
import ru.netology.papillon.dto.Job
import ru.netology.papillon.dto.Post
import ru.netology.papillon.dto.User
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object PostArg: ReadWriteProperty<Bundle, Post?> {
    override fun setValue(thisRef: Bundle, property: KProperty<*>, value: Post?) {
        thisRef.putParcelable(property.name, value)
    }

    override fun getValue(thisRef: Bundle, property: KProperty<*>): Post? {
        return thisRef.getParcelable(property.name)
    }
}

object JobArg: ReadWriteProperty<Bundle, Job?> {
    override fun setValue(thisRef: Bundle, property: KProperty<*>, value: Job?) {
        thisRef.putParcelable(property.name, value)
    }

    override fun getValue(thisRef: Bundle, property: KProperty<*>): Job? {
        return thisRef.getParcelable(property.name)
    }
}

object UserArg: ReadWriteProperty<Bundle, User?> {
    override fun setValue(thisRef: Bundle, property: KProperty<*>, value: User?) {
        thisRef.putParcelable(property.name, value)
    }

    override fun getValue(thisRef: Bundle, property: KProperty<*>): User? {
        return thisRef.getParcelable(property.name)
    }
}

object StringArg: ReadWriteProperty<Bundle, String?> {
    override fun setValue(thisRef: Bundle, property: KProperty<*>, value: String?) {
        thisRef.putString(property.name, value)
    }

    override fun getValue(thisRef: Bundle, property: KProperty<*>): String? {
        return thisRef.getString(property.name)
    }
}

