package com.greentree.telegram.queue.service

import com.greentree.telegram.queue.model.Position
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class MainServiceTest {
    @Test
    fun test1(){
        Assertions.assertEquals(MainService.findFirstFreeNumber(listOf()), 1)
        Assertions.assertEquals(MainService.findFirstFreeNumber(listOf(0, 2).toPositions()), 1)
        Assertions.assertEquals(MainService.findFirstFreeNumber(listOf(0, 1).toPositions()), 2)
        Assertions.assertEquals(MainService.findFirstFreeNumber(listOf(0, 0).toPositions()), 1)
        Assertions.assertEquals(MainService.findFirstFreeNumber(listOf(1, 2).toPositions()), 3)
        Assertions.assertEquals(MainService.findFirstFreeNumber(listOf(3, 4).toPositions()), 1)
    }
}

private fun List<Int>.toPositions() = map { Position().also { pos -> pos.number = it } }
