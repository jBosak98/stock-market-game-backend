package com.ktor.stock.market.game.jbosak.graphQL.schema

import graphql.schema.GraphQLSchema
import graphql.schema.idl.RuntimeWiring.newRuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser


fun getSchema(): GraphQLSchema {
    val schemaDef = SchemaParser().parse(
        """
            ${getUserSchema()}
            ${getCompanySchema()}
            ${getQuoteSchema()}
            ${getShareShema()}
            ${getTransactionSchema()}
            ${getCandleSchema()}
            
            type Query {
                me: User
                login(user:UserLoginInput!): User
                companiesConnection(skip:Int, limit:Int): CompaniesConnection
                getQuote(ticker:String!): Quote
                getCompany(ticker:String!): Company
                getTransactions: [Transaction!]!
                getCandles(ticker:String!, from:String!, to:String, resolution:String!):[Candle!]!

            }
    
            type Mutation {
                register(user: UserRegisterInput!): User
                buyShare(ticker: String!, amount: Int!, price: Float): User
                sellShare(ticker: String!, amount: Int!, price: Float): User
            }
            schema {
                query: Query
                mutation: Mutation
            }
        """
    )
    val runtimeWiring = newRuntimeWiring()
        .type("Query") { builder ->
            builder
                .userQueryResolvers()
                .companyQueryResolvers()
                .quoteQueryResolvers()
                .transactionQueryResolvers()
                .candleQueryResolvers()
        }
        .type("Mutation") { builder ->
            builder
                .userMutationResolvers()
                .shareMutationResolvers()
        }
        .build()
    return SchemaGenerator()
        .makeExecutableSchema(schemaDef, runtimeWiring)
}
