/*
  * This file is part of HyperCeiler.

  * HyperCeiler is free software: you can redistribute it and/or modify
  * it under the terms of the GNU Affero General Public License as
  * published by the Free Software Foundation, either version 3 of the
  * License.

  * This program is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  * GNU Affero General Public License for more details.

  * You should have received a copy of the GNU Affero General Public License
  * along with this program.  If not, see <https://www.gnu.org/licenses/>.

  * Copyright (C) 2023-2025 HyperCeiler Contributions
*/
package com.sevtinge.hyperceiler.hook.module.hook.securitycenter

import android.annotation.SuppressLint
import android.widget.TextView
import com.sevtinge.hyperceiler.hook.module.base.BaseHook
import io.github.kyuubiran.ezxhelper.core.finder.MethodFinder.`-Static`.methodFinder
import io.github.kyuubiran.ezxhelper.core.util.ClassUtil.loadClass
import io.github.kyuubiran.ezxhelper.xposed.dsl.HookFactory.`-Static`.createHook

class RemoveOpenAppConfirmationPopup : BaseHook() {
    @SuppressLint("DiscouragedApi")
    override fun init() {
        loadClass("android.widget.TextView").methodFinder()
            .filterByName("setText")
            .filterByParamTypes {
                it[0] == CharSequence::class.java
            }.first().createHook {
                after {
                    val textView = it.thisObject as TextView
                    if (it.args.isNotEmpty() && it.args[0]?.toString().equals(
                            textView.context.resources.getString(
                                textView.context.resources.getIdentifier(
                                    "button_text_accept",
                                    "string",
                                    textView.context.packageName
                                )
                            )
                        )
                    ) {
                        textView.performClick()
                    }
                }
            }
    }
}
