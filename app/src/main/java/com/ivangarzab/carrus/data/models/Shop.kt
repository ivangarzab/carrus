package com.ivangarzab.carrus.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by Ivan Garza Bermea.
 */
const val VERSION_SHOP: Int = 1
@Parcelize
data class Shop(
    val version: Int = VERSION_SHOP,
    val name: String,
    val phoneNumber: String,
    val address: Address
): Parcelable, Comparable<Shop> {
    override fun compareTo(other: Shop): Int = compareValuesBy(this, other,
        { it.name },
        { it.phoneNumber },
        { it.address }
    )

    override fun toString(): String {
        return "Shop(" +
                "\nname='$name'" +
                "\nphoneNumber='$phoneNumber'" +
                "\naddress=$address" +
                "\n)"
    }

    companion object {
        val empty: Shop = Shop(
            name = "Empty Shop",
            phoneNumber = "",
            address = Address.empty
        )
    }
}

@Parcelize
data class Address(
    val street: String,
    val unit: String? = null,
    val city: String,
    val state: String,
    val postalCode: String
) : Parcelable, Comparable<Address> {
    fun getShortString(): String = "$street, $city $state"

    override fun compareTo(other: Address): Int = compareValuesBy(this, other,
        { it.street },
        { it.unit },
        { it.city },
        { it.state },
        { it.postalCode }
    )

    override fun toString(): String {
        return "ShopAddress(" +
                "\nstreet='$street'" +
                "\nunit=$unit" +
                "\ncity='$city'" +
                "\nstate='$state'" +
                "\npostalCode='$postalCode'" +
                "\n)"
    }

    companion object {
        val empty: Address = Address(
            street = "Empty street",
            city = "",
            state = "",
            postalCode = ""
        )
    }
}
