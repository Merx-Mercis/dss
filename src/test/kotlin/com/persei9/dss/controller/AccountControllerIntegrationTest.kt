package com.persei9.dss.controller

import com.persei9.dss.model.Account
import com.persei9.dss.repository.IAccountRepository
import com.persei9.dss.web.requests.AccountRequest
import org.bson.types.ObjectId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AccountControllerIntegrationTest @Autowired constructor(
    private val accountRepository: IAccountRepository,
    private val restTemplate: TestRestTemplate
) {
    private val defaultAccountId = ObjectId.get()

    @LocalServerPort
    protected var port: Int = 0

    @BeforeEach
    fun setUp() {
        accountRepository.deleteAll()
    }

    private fun getRootUrl(): String? = "http://localhost:$port/accounts"

    private fun saveOneAccount() = accountRepository.save(Account(
        defaultAccountId,
        "Mike",
        "Wazowski",
        "mike@wazowski"
    ))

    private fun prepareAccountRequest() = AccountRequest(
        "defaultFirstName",
        "defaultLastName",
        "defaultEmail"
    )

    @Test
    fun `should return all accounts`() {
        saveOneAccount()

        val response = restTemplate.getForEntity(getRootUrl(), List::class.java)

        assertEquals(200, response.statusCode.value())
        assertNotNull(response.body)
        assertEquals(1, response.body?.size)
    }

    @Test
    fun `should return single account by id`() {
        saveOneAccount()

        val response = restTemplate.getForEntity(getRootUrl() + "/$defaultAccountId", Account::class.java)

        assertEquals(200, response.statusCode.value())
        assertNotNull(response.body)
        // assertEquals(defaultAccountId, response.body?.id)
    }

    @Test
    fun `should create new account`() {
        val accountRequest = prepareAccountRequest()

        val response = restTemplate.postForEntity(getRootUrl(), accountRequest, Account::class.java)

        assertEquals(201, response.statusCode.value())
        assertNotNull(response.body)
        assertNotNull(response.body?.id)
        assertEquals(accountRequest.firstName, response.body?.firstName)
        assertEquals(accountRequest.lastName, response.body?.lastName)
        assertEquals(accountRequest.email, response.body?.email)
    }
}