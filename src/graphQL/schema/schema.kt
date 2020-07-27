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
            ${getStockPriceSchema()}
            
            type Query {
                me: User
                login(user:UserLoginInput!): User
                companiesConnection(skip:Int, limit:Int):CompaniesConnection
                getPrices(ticker:String!):[StockPrice]!

            }
    
            type Mutation {
                register(user: UserRegisterInput!): User!
                updateName(name: String!): User
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
                .stockPriceQueryResolvers()
        }
        .type("Mutation") { builder ->
            builder
                .userMutationResolvers()
                .stockPriceMutationResolvers()
        }
        .build()
    return SchemaGenerator()
        .makeExecutableSchema(schemaDef, runtimeWiring)
}
