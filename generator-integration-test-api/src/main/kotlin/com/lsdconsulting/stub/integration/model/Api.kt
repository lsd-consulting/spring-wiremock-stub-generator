package com.lsdconsulting.stub.integration.model

import com.fasterxml.jackson.annotation.JsonProperty

data class GreetingResponse(
    @JsonProperty("name")
    val name: String
)

data class GreetingRequest(
    @JsonProperty("name")
    val name: String
)
