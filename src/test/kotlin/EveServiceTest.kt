package org.example.mirai.plugin

import rocks.ditto.miral.eve_plugin.service.EveService

fun main() {
    testFetchOrder()
}

fun testFetchOrder(){
    println(EveService.fetchJita("ra"))
    println(EveService.fetchJita("raven"))
}