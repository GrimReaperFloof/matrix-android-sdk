/*
 * Copyright 2019 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.matrix.androidsdk.rest.model

import com.google.gson.annotations.SerializedName

/**
 * https://matrix.org/docs/spec/client_server/r0.4.0.html#server-discovery
 * <pre>
 * {
 *     "m.homeserver": {
 *         "base_url": "https://matrix.org"
 *     },
 *     "m.identity_server": {
 *         "base_url": "https://vector.im"
 *     }
 *     "m.integrations": {
 *          "managers": [
 *              {
 *                  "api_url": "https://integrations.example.org",
 *                  "ui_url": "https://integrations.example.org/ui"
 *              },
 *              {
 *                  "api_url": "https://bots.example.org"
 *              }
 *          ]
 *    }
 *     "im.vector.riot.jitsi": {
 *         "preferredDomain": "https://jitsi.riot.im/"
 *     }
 * }
 * </pre>
 */
class WellKnown {

    @JvmField
    @SerializedName("m.homeserver")
    var homeServer: WellKnownBaseConfig? = null

    @JvmField
    @SerializedName("m.identity_server")
    var identityServer: WellKnownBaseConfig? = null


    @JvmField
    @SerializedName("m.integrations")
    var integrations: Map<String, *>? = null

    /**
     * Returns the list of integration managers proposed
     */
    fun getIntegrationManagers(): List<WellKnownManagerConfig> {
        val managers = ArrayList<WellKnownManagerConfig>()
        integrations?.get("managers")?.let {
            (it as? ArrayList<*>)?.let { configs ->
                configs.forEach { config ->
                    (config as? Map<*, *>)?.let { map ->
                        val apiUrl = map["api_url"] as? String
                        val uiUrl = map["ui_url"] as? String ?: apiUrl
                        if (apiUrl != null
                                && apiUrl.startsWith("https://")
                                && uiUrl!!.startsWith("https://")) {
                            managers.add(WellKnownManagerConfig(
                                    apiUrl = apiUrl,
                                    uiUrl = uiUrl
                            ))
                        }
                    }
                }
            }
        }
        return managers
    }

    @JvmField
    @SerializedName("im.vector.riot.jitsi")
    var jitsiServer: WellKnownPreferredConfig? = null
}
