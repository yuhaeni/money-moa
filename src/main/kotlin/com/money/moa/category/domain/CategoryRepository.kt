package com.money.moa.category.domain

import org.springframework.data.repository.CrudRepository

interface CategoryRepository: CrudRepository<Category, Long> {
    override fun findAll(): List<Category>
}