package com.lsdconsulting.stub.integration.controller.multiple

import com.lsdconsulting.stub.integration.model.GreetingResponse
import io.lsdconsulting.stub.annotation.GenerateWireMockStub
import org.apache.commons.lang3.RandomStringUtils.secure
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation.RequestMethod.PUT
import org.springframework.web.bind.annotation.RestController

@GenerateWireMockStub
@RestController
class SimplePostPutRestController {

    @RequestMapping(path = ["/multipleController/postPutResource"], method = [POST, PUT])
    fun postPutResourceWitNoRequestBody() = GreetingResponse(name = secure().nextAlphabetic(10))
}
