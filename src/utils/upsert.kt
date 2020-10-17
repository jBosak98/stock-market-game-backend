package com.ktor.stock.market.game.jbosak.utils

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Index
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.transactions.TransactionManager

inline fun <T : Table> T.upsert(
    conflictIndex: Index,
    body: T.(InsertStatement<Number>) -> Unit
) = upsertStatement<Number>(this, conflictIndex).apply {
    body(this)
    execute(TransactionManager.current())
}



fun Table.indexR(customIndexName: String? = null, isUnique: Boolean = false, vararg columns: Column<*>): Index {
    val index = Index(columns.toList(), isUnique, customIndexName)
    indices.add(index)
    return index
}

fun Table.uniqueIndexR(customIndexName: String? = null, vararg columns: Column<*>): Index =
    indexR(customIndexName, true, *columns)

fun <Key : Any>upsertStatement(table: Table, conflictIndex: Index)
        = object: InsertStatement<Key>(table, false) {
    override fun prepareSQL(transaction: Transaction) = buildString {
        append(super.prepareSQL(transaction))

        val filteredKeys = values.keys.filter { it !in conflictIndex.columns }
        val dialect = transaction.db.vendor
        if (dialect == "postgresql") {
            append(" ON CONFLICT ON CONSTRAINT ")
            append(conflictIndex.indexName)
            append(" DO UPDATE SET ")

            filteredKeys
                .joinTo(this) { "${transaction.identity(it)}=EXCLUDED.${transaction.identity(it)}" }
        } else {
            append(" ON DUPLICATE KEY UPDATE ")
            filteredKeys
                .joinTo(this) { "${transaction.identity(it)}=VALUES(${transaction.identity(it)})" }

        }
    }
}


