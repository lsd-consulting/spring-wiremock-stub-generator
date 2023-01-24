package com.lsdconsulting.stub.integration.model

import com.fasterxml.jackson.annotation.JsonProperty

data class Greeting(
    @JsonProperty("name")
    val name: String
)