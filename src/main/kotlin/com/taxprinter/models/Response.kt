package com.taxprinter.models

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by george on 03/07/16.
 */

class Response<T> (
        @JsonProperty val message: String,
        @JsonProperty val response: T,
        @JsonProperty val status: String
)