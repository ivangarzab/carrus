package com.ivangarzab.carrus.util.extensions

import android.view.View
import com.ivangarzab.carrus.databinding.ModalMessageBinding

/**
 * Created by Ivan Garza Bermea.
 */
fun ModalMessageBinding.bind(
    message: String,
    onCloseClickListener: View.OnClickListener? = null
) {
    this.message = message
    this.setCloseButtonClickListener {view ->
        //TODO: Create dissolve animation
        onCloseClickListener?.onClick(view)
    }

}