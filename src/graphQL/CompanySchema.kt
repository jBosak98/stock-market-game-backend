package com.ktor.stock.market.game.jbosak.graphQL

import com.ktor.stock.market.game.jbosak.model.ConnectionArguments
import com.ktor.stock.market.game.jbosak.repository.CompanyRepository.companiesSize
import com.ktor.stock.market.game.jbosak.repository.CompanyRepository.findCompanies
import com.ktor.stock.market.game.jbosak.service.getCompanies
import com.ktor.stock.market.game.jbosak.utils.convertToObject
import graphql.schema.idl.TypeRuntimeWiring

fun getCompanySchema() =
    """
    type Company {
        id:String!
        ticker:String
        name:String
        lei:String
        cik:Int
        externalId:String!
    }
    
    type CompaniesConnection {
        totalCount:Int
        companies: [Company]!
    }
    """

fun TypeRuntimeWiring.Builder.companyQueryResolvers() =
    this.dataFetcher("companiesConnection"){ env ->
        val (skip, limit) = convertToObject(env.arguments, ConnectionArguments::class.java)!!
        val companies = getCompanies(skip,limit?: 10)

        object {
            val totalCount = companiesSize()
            val companies = companies
        }
    }

