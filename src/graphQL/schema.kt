package com.ktor.stock.market.game.jbosak.graphQL

import graphql.schema.GraphQLSchema
import graphql.schema.idl.RuntimeWiring.newRuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser


fun getSchema(): GraphQLSchema {
    val schemaDef = SchemaParser().parse(
        """
            ${getUserSchema()}
            type Query {
                me: User
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
        }
        .type("Mutation") { builder ->
            builder
                .userMutationResolvers()
        }
        .build()
    return SchemaGenerator()
        .makeExecutableSchema(schemaDef, runtimeWiring)
}
