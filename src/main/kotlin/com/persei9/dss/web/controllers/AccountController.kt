package com.persei9.dss.web.controllers

import com.persei9.dss.model.Account
import com.persei9.dss.repository.IAccountRepository
import com.persei9.dss.web.requests.AccountRequest
import org.bson.types.ObjectId
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/accounts")
class AccountController(
    private val accountRepository: IAccountRepository
) {
    @GetMapping
    fun getAllAccounts(): ResponseEntity<List<Account>> {
        val accounts = accountRepository.findAll()

        return ResponseEntity.ok(accounts)
    }

    @GetMapping("/{id}")
    fun getAccount(@PathVariable("id") id: String): ResponseEntity<Account> {
        val account = accountRepository.findOneById(ObjectId(id))

        return ResponseEntity.ok(account)
    }

    @PostMapping
    fun createAccount(@RequestBody request: AccountRequest): ResponseEntity<Account> {
        val account = accountRepository.save(Account(
            firstName = request.firstName,
            lastName = request.lastName,
            email = request.email
        ))

        return ResponseEntity(account, HttpStatus.CREATED)
    }
}



