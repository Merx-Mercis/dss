package com.persei9.dss.repository

import com.persei9.dss.model.Account
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface IAccountRepository : MongoRepository<Account, String> {
    fun findOneById(id: ObjectId): Account
}