package com.taxprinter.models

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by george on 03/07/16.
 */

class Response<out T> (
        @JsonProperty("message") val message: String,
        @JsonProperty("response") val response: T,
        @JsonProperty("status") val status: String
)