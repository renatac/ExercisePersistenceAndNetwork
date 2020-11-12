package com.example.persistenceapp.model

//Classe do mapeamento entre um retorno Json e uma class kotlin, ignorei os campos que eu não iria usar
data class City(
    val id: Long,
    val name: String,
    //@SerializadName("coisa_x")
    val coisaX: String

)